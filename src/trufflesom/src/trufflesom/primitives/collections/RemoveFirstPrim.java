package trufflesom.primitives.collections;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "removeFirst", selector = "removeFirst", receiverType = SVector.class, inParser = false)
public abstract class RemoveFirstPrim extends UnaryExpressionNode {

  // FIXME: do error send if empty vector

  @Specialization(guards = "receiver.isObjectType()")
  public static final Object doObjectSVector(final SVector receiver) {
    if (receiver.getSize() > 0) {
      final Object[] storage = receiver.getObjectStorage();
      int first = receiver.getFirstIndex() - 1;
      final Object value = storage[first];
      storage[first] = Nil.nilObject;
      receiver.incrementFirstIndex();
      return value == null ? Nil.nilObject : value;
    } else {
      return null;
    }
  }

}
