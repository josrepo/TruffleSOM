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
@Primitive(className = "Vector", primitive = "remove", selector = "remove", receiverType = SVector.class, inParser = false)
public abstract class RemoveLastPrim extends UnaryExpressionNode {

  @Specialization(guards = "receiver.isEmptyType()")
  public final Object doEmptySVector(final VirtualFrame frame, final SVector receiver) {
    if (receiver.getSize() > 0) {
      receiver.decrementLastIndex();
      return Nil.nilObject;
    } else {
      return makeGenericSend(SymbolTable.symbolFor("error:"))
          .doPreEvaluated(frame, new Object[] {receiver, "Vector: Attempting to remove the last element from an empty Vector"});
    }
  }

  @Specialization(guards = "receiver.isObjectType()")
  public final Object doObjectSVector(final VirtualFrame frame, final SVector receiver) {
    if (receiver.getSize() > 0) {
      receiver.decrementLastIndex();
      final Object[] storage = receiver.getObjectStorage();
      int last = receiver.getLastIndex() - 1;
      final Object value = storage[last];
      storage[last] = Nil.nilObject;
      return value == null ? Nil.nilObject : value;
    } else {
      return makeGenericSend(SymbolTable.symbolFor("error:"))
          .doPreEvaluated(frame, new Object[] {receiver, "Vector: Attempting to remove the last element from an empty Vector"});
    }
  }

  @Specialization(guards = "receiver.isLongType()")
  public final Object doLongSVector(final VirtualFrame frame, final SVector receiver) {
    if (receiver.getSize() > 0) {
      receiver.decrementLastIndex();
      final long[] storage = receiver.getLongStorage();
      int last = receiver.getLastIndex() - 1;
      final long value = storage[last];
      storage[last] = SVector.EMPTY_LONG_SLOT;
      return value == SVector.EMPTY_LONG_SLOT ? Nil.nilObject : value;
    } else {
      return makeGenericSend(SymbolTable.symbolFor("error:"))
          .doPreEvaluated(frame, new Object[] {receiver, "Vector: Attempting to remove the last element from an empty Vector"});
    }
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public final Object doDoubleSVector(final VirtualFrame frame, final SVector receiver) {
    if (receiver.getSize() > 0) {
      receiver.decrementLastIndex();
      final double[] storage = receiver.getDoubleStorage();
      int last = receiver.getLastIndex() - 1;
      final double value = storage[last];
      storage[last] = SVector.EMPTY_DOUBLE_SLOT;
      return value == SVector.EMPTY_DOUBLE_SLOT ? Nil.nilObject : value;
    } else {
      return makeGenericSend(SymbolTable.symbolFor("error:"))
          .doPreEvaluated(frame, new Object[] {receiver, "Vector: Attempting to remove the last element from an empty Vector"});
    }
  }

}
