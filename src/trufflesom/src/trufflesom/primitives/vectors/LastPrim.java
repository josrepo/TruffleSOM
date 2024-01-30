package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "last", selector = "last", receiverType = SVector.class, inParser = false)
public abstract class LastPrim extends UnaryExpressionNode {

  @Specialization(guards = "receiver.isObjectType()")
  public static final Object doObjectSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      return receiver.getObjectStorage()[receiver.getLastIndex() - 2];
    } else {
      return Nil.nilObject;
    }
  }

  @Specialization(guards = "receiver.isLongType()")
  public static final Object doLongSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      return receiver.getLongStorage()[receiver.getLastIndex() - 2];
    } else {
      return Nil.nilObject;
    }
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public static final Object doDoubleSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      return receiver.getDoubleStorage()[receiver.getLastIndex() - 2];
    } else {
      return Nil.nilObject;
    }
  }

  @Specialization(guards = "receiver.isBooleanType()")
  public static final Object doBooleanSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      return receiver.getBooleanStorage()[receiver.getLastIndex() - 2];
    } else {
      return Nil.nilObject;
    }
  }

}
