package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "isEmpty", selector = "isEmpty", receiverType = SVector.class, inParser = false)
public abstract class IsEmptyPrim extends UnaryExpressionNode {

  @Specialization
  public static final boolean doSVector(final SVector receiver) {
    return receiver.isEmpty();
  }

}
