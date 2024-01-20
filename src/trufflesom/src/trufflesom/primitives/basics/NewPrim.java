package trufflesom.primitives.basics;

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
import trufflesom.vmobjects.SArray;
import trufflesom.vmobjects.SClass;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@ImportStatic(Classes.class)
@GenerateNodeFactory
@Primitive(className = "Array", primitive = "new:", classSide = true, specializer = NewPrim.IsArrayClass.class)
@Primitive(className = "Vector", primitive = "new:", classSide = true, specializer = NewPrim.IsVectorClass.class)
@Primitive(selector = "new:", inParser = false)
public abstract class NewPrim extends BinaryMsgExprNode {

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
  public static final SArray doSClassSArray(@SuppressWarnings("unused") final SClass receiver,
      final long length) {
    return new SArray(length);
  }

  @Specialization(guards = "receiver == vectorClass")
  public static final SVector doSClassSVector(@SuppressWarnings("unused") final SClass receiver,
      final long length) {
    return new SVector(length);
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symNewMsg;
  }
}
