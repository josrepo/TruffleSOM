package trufflesom.primitives.collections;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "remove", selector = "remove", receiverType = SVector.class, inParser = false)
public abstract class RemoveLastPrim extends UnaryExpressionNode {

  // FIXME: do error send if empty vector

  @Specialization(guards = "receiver.isObjectType()")
  public static final Object doObjectSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      receiver.decrementLastIndex();
      final Object[] storage = receiver.getObjectStorage();
      final Object value = storage[receiver.getLastIndex() - 1];
      storage[receiver.getLastIndex() - 1] = Nil.nilObject;
      return value == null ? Nil.nilObject : value;
    } else {
      return null;
    }
  }

}
