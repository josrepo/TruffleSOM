package trufflesom.primitives.vectors;

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
import trufflesom.vmobjects.SClass;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@ImportStatic(Classes.class)
@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "initialize:", selector = "initialize:", classSide = true,
    inParser = false, specializer = InitializePrim.IsVectorClass.class)
public abstract class InitializePrim extends BinaryMsgExprNode {

  public static class IsVectorClass extends Specializer<ExpressionNode, SSymbol> {

    public IsVectorClass(final Primitive prim, final NodeFactory<ExpressionNode> fact) {
      super(prim, fact);
    }

    @Override
    public boolean matches(final Object[] args, final ExpressionNode[] argsNodes) {
      return args[0] == Classes.vectorClass;
    }
  }

  @Specialization(guards = "receiver == vectorClass")
  public static final SVector doSClass(@SuppressWarnings("unused") final SClass receiver,
      final long length) {
    return new SVector(length);
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symInitMsg;
  }

}
