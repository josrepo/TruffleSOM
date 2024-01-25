package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "size", selector = "size", receiverType = SVector.class, inParser = false)
public abstract class SizePrim extends UnaryExpressionNode {

  @Specialization
  public static final long doObjectSVector(final SVector receiver) {
    return receiver.getSize();
  }

}
