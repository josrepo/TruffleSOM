package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "capacity", selector = "capacity", receiverType = SVector.class, inParser = false)
public abstract class CapacityPrim extends UnaryExpressionNode {

  @Specialization(guards = "receiver.isEmptyType()")
  public static final long doEmptySVector(final SVector receiver) {
    return receiver.getEmptyStorage();
  }

  @Specialization(guards = "receiver.isObjectType()")
  public static final long doObjectSVector(final SVector receiver) {
    return receiver.getObjectStorage().length;
  }

  @Specialization(guards = "receiver.isLongType()")
  public static final long doLongSVector(final SVector receiver) {
    return receiver.getLongStorage().length;
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public static final long doDoubleSVector(final SVector receiver) {
    return receiver.getDoubleStorage().length;
  }

}
