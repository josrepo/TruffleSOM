package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "first", selector = "first", receiverType = SVector.class, inParser = false)
public abstract class FirstPrim extends UnaryExpressionNode {

  @Specialization(guards = "receiver.isEmptyType")
  @SuppressWarnings("unused")
  public static final Object doEmptySVector(final SVector receiver) {
    return Nil.nilObject;
  }

  @Specialization(guards = "receiver.isObjectType()")
  public static final Object doObjectSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      return receiver.getObjectStorage()[receiver.getFirstIndex() - 1];
    } else {
      return Nil.nilObject;
    }
  }

  @Specialization(guards = "receiver.isLongType()")
  public static final Object doLongSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      return receiver.getLongStorage()[receiver.getFirstIndex() - 1];
    } else {
      return Nil.nilObject;
    }
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public static final Object doDoubleSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      return receiver.getDoubleStorage()[receiver.getFirstIndex() - 1];
    } else {
      return Nil.nilObject;
    }
  }

}
