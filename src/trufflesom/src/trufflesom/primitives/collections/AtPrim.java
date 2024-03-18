package trufflesom.primitives.collections;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.HostCompilerDirectives.InliningCutoff;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;

import com.oracle.truffle.api.frame.VirtualFrame;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.GenericMessageSendNode;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.vm.SymbolTable;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SAbstractObject;
import trufflesom.vmobjects.SArray;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Array", primitive = "at:")
@Primitive(className = "Vector", primitive = "at:")
@Primitive(selector = "at:", receiverType = {SArray.class, SVector.class}, inParser = false)
public abstract class AtPrim extends BinaryMsgExprNode {
  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("at:");
  }

  @Specialization(guards = "receiver.isEmptyType()")
  public static final Object doEmptySArray(final SArray receiver, final long idx) {
    assert idx > 0;
    assert idx <= receiver.getEmptyStorage();
    return Nil.nilObject;
  }

  @Specialization(guards = "receiver.isPartiallyEmptyType()")
  public static final Object doPartiallyEmptySArray(final SArray receiver, final long idx) {
    return receiver.getPartiallyEmptyStorage().get(idx - 1);
  }

  @Specialization(guards = "receiver.isObjectType()")
  public static final Object doObjectSArray(final SArray receiver, final long idx) {
    return receiver.getObjectStorage()[(int) idx - 1];
  }

  @Specialization(guards = "receiver.isLongType()")
  public static final long doLongSArray(final SArray receiver, final long idx) {
    return receiver.getLongStorage()[(int) idx - 1];
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public static final double doDoubleSArray(final SArray receiver, final long idx) {
    return receiver.getDoubleStorage()[(int) idx - 1];
  }

  @Specialization(guards = "receiver.isEmptyType()")
  public final Object doEmptySVector(final VirtualFrame frame, final SVector receiver, final long idx) {
    final int storeIdx = (int) idx + receiver.getFirstIndex() - 1;
    if (indexValid(receiver, storeIdx)) {
      return Nil.nilObject;
    } else {
      return doInvalidIndexError(receiver, storeIdx);
    }
  }

  @Specialization(guards = "receiver.isObjectType()")
  public final Object doObjectSVector(final VirtualFrame frame, final SVector receiver, final long idx) {
    final int storeIdx = (int) idx + receiver.getFirstIndex() - 1;
    if (indexValid(receiver, storeIdx)) {
      return receiver.getObjectStorage()[storeIdx - 1];
    } else {
      return doInvalidIndexError(receiver, storeIdx);
    }
  }

  @Specialization(guards = "receiver.isLongType()")
  public final Object doLongSVector(final VirtualFrame frame, final SVector receiver, final long idx) {
    final int storeIdx = (int) idx + receiver.getFirstIndex() - 1;
    if (indexValid(receiver, storeIdx)) {
      return receiver.getLongStorage()[storeIdx - 1];
    } else {
      return doInvalidIndexError(receiver, storeIdx);
    }
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public final Object doDoubleSVector(final VirtualFrame frame, final SVector receiver, final long idx) {
    final int storeIdx = (int) idx + receiver.getFirstIndex() - 1;
    if (indexValid(receiver, storeIdx)) {
      return receiver.getDoubleStorage()[storeIdx - 1];
    } else {
      return doInvalidIndexError(receiver, storeIdx);
    }
  }

  private static boolean indexValid(final SVector vector, final int index) {
    return vector.getFirstIndex() <= index && index < vector.getLastIndex();
  }

  @TruffleBoundary
  @InliningCutoff
  private Object doInvalidIndexError(final SVector receiver, final int index) {
    return SAbstractObject.sendError(receiver,
        "Vector[" + receiver.getFirstIndex() + ".." + receiver.getLastIndex() + "]: Index " + index + " out of bounds");
  }
}
