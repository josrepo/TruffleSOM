package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.ExpressionNode;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.primitives.basics.EqualsEqualsPrimFactory;
import trufflesom.vm.SymbolTable;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "remove:", selector = "remove:", receiverType = SVector.class, inParser = false)
public abstract class RemoveObjectPrim extends BinaryMsgExprNode {

  @Child private ExpressionNode equalsEquals;

  protected static final boolean valueIsNil(final Object value) {
    return value == Nil.nilObject;
  }

  protected static final boolean valueIsNotNil(final Object value) {
    return value != Nil.nilObject;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Node> T initialize(final long coord) {
    super.initialize(coord);
    equalsEquals = EqualsEqualsPrimFactory.create(null, null);
    equalsEquals.initialize(coord);
    return (T) this;
  }

  @Specialization(guards = {"receiver.isEmptyType()", "valueIsNotNil(value)"})
  @SuppressWarnings("unused")
  public final boolean doEmptySVector(final VirtualFrame frame, final SVector receiver, final Object value) {
    return false;
  }

  @Specialization(guards = {"receiver.isEmptyType()", "valueIsNil(value)"})
  @SuppressWarnings("unused")
  public final boolean doEmptySVectorWithNil(final VirtualFrame frame, final SVector receiver, final Object value) {
    receiver.setLastIndex(1);
    receiver.resetFirstIndex();

    return true;
  }

  @Specialization(guards = "receiver.isObjectType()")
  public final boolean doObjectSVector(final VirtualFrame frame, final SVector receiver, final Object value) {
    final Object[] storage = receiver.getObjectStorage();
    final Object[] newStorage = new Object[storage.length];
    int last = receiver.getLastIndex() - 1;
    int newLast = 1;
    boolean found = false;

    for (int i = receiver.getFirstIndex() - 1; i < last; i++) {
      if ((boolean) this.equalsEquals.doPreEvaluated(frame, new Object[]{storage[i], value})) {
        found = true;
      } else {
        newStorage[i] = storage[i];
        newLast++;
      }
    }

    receiver.setStorage(newStorage);
    receiver.setLastIndex(newLast);
    receiver.resetFirstIndex();

    return found;
  }

  @Specialization(guards = "receiver.isLongType()")
  @SuppressWarnings("unused")
  public final boolean doLongSVector(final VirtualFrame frame, final SVector receiver, final long value) {
    final long[] storage = receiver.getLongStorage();
    final long[] newStorage = new long[storage.length];
    int last = receiver.getLastIndex() - 1;
    int newLast = 1;
    boolean found = false;

    for (int i = receiver.getFirstIndex() - 1; i < last; i++) {
      if (storage[i] == value) {
        found = true;
      } else {
        newStorage[i] = storage[i];
        newLast++;
      }
    }

    receiver.setStorage(newStorage);
    receiver.setLastIndex(newLast);
    receiver.resetFirstIndex();

    return found;
  }

  @Specialization(guards = "receiver.isDoubleType()")
  @SuppressWarnings("unused")
  public final boolean doDoubleSVector(final VirtualFrame frame, final SVector receiver, final double value) {
    final double[] storage = receiver.getDoubleStorage();
    final double[] newStorage = new double[storage.length];
    int last = receiver.getLastIndex() - 1;
    int newLast = 1;
    boolean found = false;

    for (int i = receiver.getFirstIndex() - 1; i < last; i++) {
      if (storage[i] == value) {
        found = true;
      } else {
        newStorage[i] = storage[i];
        newLast++;
      }
    }

    receiver.setStorage(newStorage);
    receiver.setLastIndex(newLast);
    receiver.resetFirstIndex();

    return found;
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("remove:");
  }

}
