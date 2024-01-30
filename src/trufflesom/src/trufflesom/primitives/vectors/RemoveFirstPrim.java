package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vm.SymbolTable;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "removeFirst", selector = "removeFirst", receiverType = SVector.class, inParser = false)
public abstract class RemoveFirstPrim extends UnaryExpressionNode {

  @Specialization(guards = "receiver.isObjectType()")
  public final Object doObjectSVector(final VirtualFrame frame, final SVector receiver) {
    if (receiver.getSize() > 0) {
      final Object[] storage = receiver.getObjectStorage();
      int first = receiver.getFirstIndex() - 1;
      final Object value = storage[first];
      storage[first] = Nil.nilObject;
      receiver.incrementFirstIndex();
      return value == null ? Nil.nilObject : value;
    } else {
      return makeGenericSend(SymbolTable.symbolFor("error:"))
          .doPreEvaluated(frame, new Object[] {receiver, "Vector: Attempting to remove the first element from an empty Vector"});
    }
  }

}
