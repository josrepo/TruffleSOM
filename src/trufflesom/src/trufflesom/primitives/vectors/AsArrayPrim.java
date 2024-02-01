package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vmobjects.SArray;
import trufflesom.vmobjects.SVector;

import java.util.Arrays;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "asArray", selector = "asArray", receiverType = SVector.class, inParser = false)
public abstract class AsArrayPrim extends UnaryExpressionNode {

  @Specialization(guards = "receiver.isEmptyType()")
  @SuppressWarnings("unused")
  public static final SArray doEmptySVector(final SVector receiver) {
    return SArray.create(0);
  }

  @Specialization(guards = "receiver.isObjectType()")
  public static final SArray doObjectSVector(final SVector receiver) {
    return SArray.create(Arrays.copyOfRange(receiver.getObjectStorage(),
        receiver.getFirstIndex() - 1,
        receiver.getLastIndex() - 1));
  }

  @Specialization(guards = "receiver.isLongType()")
  public static final SArray doLongSVector(final SVector receiver) {
    return SArray.create(Arrays.copyOfRange(receiver.getLongStorage(),
        receiver.getFirstIndex() - 1,
        receiver.getLastIndex() - 1));
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public static final SArray doDoubleSVector(final SVector receiver) {
    return SArray.create(Arrays.copyOfRange(receiver.getDoubleStorage(),
        receiver.getFirstIndex() - 1,
        receiver.getLastIndex() - 1));
  }

}
