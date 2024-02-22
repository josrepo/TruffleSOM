package trufflesom.primitives.collections;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.Specialization;

import trufflesom.bdt.primitives.Primitive;
import trufflesom.bdt.primitives.Specializer;
import trufflesom.interpreter.nodes.ExpressionNode;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.vm.Classes;
import trufflesom.vm.SymbolTable;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SArray;
import trufflesom.vmobjects.SClass;
import trufflesom.vmobjects.SObject;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@ImportStatic(Classes.class)
@GenerateNodeFactory
@Primitive(className = "Array", primitive = "new:", classSide = true, specializer = NewPrim.IsArrayClass.class)
@Primitive(className = "Vector", primitive = "new:", classSide = true, specializer = NewPrim.IsVectorClass.class)
@Primitive(selector = "new:", inParser = false)
public abstract class NewPrim extends BinaryMsgExprNode {

  @SuppressWarnings("unused")
  protected static final int LIMIT = 3;

  public static class IsArrayClass extends Specializer<ExpressionNode, SSymbol> {

    public IsArrayClass(final Primitive prim, final NodeFactory<ExpressionNode> fact) {
      super(prim, fact);
    }

    @Override
    public boolean matches(final Object[] args, final ExpressionNode[] argNodes) {
      return args[0] == Classes.arrayClass;
    }
  }

  public static class IsVectorClass extends Specializer<ExpressionNode, SSymbol> {

    public IsVectorClass(final Primitive prim, final NodeFactory<ExpressionNode> fact) {
      super(prim, fact);
    }

    @Override
    public boolean matches(final Object[] args, final ExpressionNode[] argsNodes) {
      return args[0] == Classes.vectorClass;
    }
  }

  @Specialization(guards = "receiver == arrayClass")
  public static final SArray doSClassSArray(@SuppressWarnings("unused") final SClass receiver, final long length) {
    return new SArray(length);
  }

  @Specialization(guards = "receiver == vectorClass")
  public static final SVector doSClassSVector(@SuppressWarnings("unused") final SClass receiver, final long length) {
    return new SVector(length);
  }

  @Specialization(guards = {"receiver == cachedReceiver", "isSubclass"}, limit = "LIMIT")
  public static final SVector doCachedSClassSVectorSubclass(@SuppressWarnings("unused") final SClass receiver,
      final long length, @Cached("isVectorSubclass(receiver)") @SuppressWarnings("unused") final boolean isSubclass,
      @Cached("receiver") final SClass cachedReceiver) {
    return new SVector(cachedReceiver, length);
  }

  @Specialization(replaces = "doCachedSClassSVectorSubclass", guards = "isVectorSubclass(receiver)")
  public static final SVector doUncachedSClassSVectorSubclass(final SClass receiver, final long length) {
    return new SVector(receiver, length);
  }

  @SuppressWarnings("unused")
  @CompilerDirectives.TruffleBoundary
  public static boolean isVectorSubclass(final SClass receiver) {
    final SObject superClass = (receiver).getSuperClass();
    if (superClass == Nil.nilObject) {
      return false;
    }

    return superClass == Classes.vectorClass || isVectorSubclass((SClass) superClass);
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symNewMsg;
  }
}
