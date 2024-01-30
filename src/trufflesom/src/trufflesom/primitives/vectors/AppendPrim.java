package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.vm.SymbolTable;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

import java.util.Arrays;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "append:", selector = "append:", inParser = false, receiverType = SVector.class)
public abstract class AppendPrim extends BinaryMsgExprNode {

  protected static final boolean valueIsNil(final Object value) {
    return value == Nil.nilObject;
  }

  protected static final boolean valueIsNotNil(final Object value) {
    return value != Nil.nilObject;
  }

  protected static final boolean valueIsNotLong(final Object value) {
    return !(value instanceof Long);
  }

  protected static final boolean valueIsNotDouble(final Object value) {
    return !(value instanceof Double);
  }

  protected static final boolean valueNotLongDouble(final Object value) {
    return !(value instanceof Long) && !(value instanceof Double);
  }

  @Specialization(guards = "receiver.isEmptyType()")
  public static final SVector doEmptySVector(final SVector receiver, final long value) {
    int capacity = receiver.getEmptyStorage();

    if (receiver.getLastIndex() > capacity) {
      capacity *= 2;
    }

    long[] newStorage = new long[capacity];
    Arrays.fill(newStorage, SVector.EMPTY_LONG_SLOT);
    receiver.setStorage(newStorage);

    newStorage[receiver.getLastIndex() - 1] = value;
    receiver.incrementLastIndex();

    return receiver;
  }

  @Specialization(guards = "receiver.isEmptyType()")
  public static final SVector doEmptySVector(final SVector receiver, final double value) {
    int capacity = receiver.getEmptyStorage();

    if (receiver.getLastIndex() > capacity) {
      capacity *= 2;
    }

    double[] newStorage = new double[capacity];
    Arrays.fill(newStorage, SVector.EMPTY_DOUBLE_SLOT);
    receiver.setStorage(newStorage);

    newStorage[receiver.getLastIndex() - 1] = value;
    receiver.incrementLastIndex();

    return receiver;
  }

  @Specialization(guards = {"receiver.isEmptyType()", "valueIsNotNil(value)", "valueNotLongDouble(value)"})
  public static final SVector doEmptySVector(final SVector receiver, final Object value) {
    int capacity = receiver.getEmptyStorage();

    if (receiver.getLastIndex() > capacity) {
      capacity *= 2;
    }

    Object[] newStorage = new Object[capacity];
    receiver.setStorage(newStorage);

    newStorage[receiver.getLastIndex() - 1] = value;
    receiver.incrementLastIndex();

    return receiver;
  }

  @Specialization(guards = {"receiver.isEmptyType()", "valueIsNil(value)"})
  @SuppressWarnings("unused")
  public static final SVector doEmptySVectorWithNil(final SVector receiver, final Object value) {
    final int capacity = receiver.getEmptyStorage();

    if (receiver.getLastIndex() > capacity) {
      receiver.setStorage(capacity * 2);
    }

    receiver.incrementLastIndex();

    return receiver;
  }

  @Specialization(guards = "receiver.isObjectType()")
  public static final SVector doObjectSVector(final SVector receiver, final Object value) {
    Object[] storage = receiver.getObjectStorage();

    if (receiver.getLastIndex() > storage.length) {
      final Object[] newStorage = new Object[storage.length * 2];
      System.arraycopy(storage, 0, newStorage, 0, storage.length);
      storage = newStorage;
      receiver.setStorage(newStorage);
    }

    storage[receiver.getLastIndex() - 1] = value;
    receiver.incrementLastIndex();

    return receiver;
  }

  @Specialization(guards = "receiver.isLongType()")
  public static final SVector doLongSVector(final SVector receiver, final long value) {
    long[] storage = receiver.getLongStorage();

    if (receiver.getLastIndex() > storage.length) {
      final long[] newStorage = new long[storage.length * 2];
      System.arraycopy(storage, 0, newStorage, 0, storage.length);
      storage = newStorage;
      receiver.setStorage(newStorage);
    }

    storage[receiver.getLastIndex() - 1] = value;
    receiver.incrementLastIndex();

    return receiver;
  }

  @Specialization(guards = {"receiver.isLongType()", "valueIsNotLong(value)"})
  public static final SVector doLongSVector(final SVector receiver, final Object value) {
    long[] storage = receiver.getLongStorage();
    Object[] newStorage;

    if (receiver.getLastIndex() > storage.length) {
      newStorage = new Object[storage.length * 2];
    } else {
      newStorage = new Object[storage.length];
    }

    for (int i = 0; i < storage.length; i++) {
      newStorage[i] = storage[i] == SVector.EMPTY_LONG_SLOT ? null : storage[i];
    }

    receiver.setStorage(newStorage);
    newStorage[receiver.getLastIndex() - 1] = value;
    receiver.incrementLastIndex();

    return receiver;
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public static final SVector doDoubleSVector(final SVector receiver, final double value) {
    double[] storage = receiver.getDoubleStorage();

    if (receiver.getLastIndex() > storage.length) {
      final double[] newStorage = new double[storage.length * 2];
      System.arraycopy(storage, 0, newStorage, 0, storage.length);
      storage = newStorage;
      receiver.setStorage(newStorage);
    }

    storage[receiver.getLastIndex() - 1] = value;
    receiver.incrementLastIndex();

    return receiver;
  }

  @Specialization(guards = {"receiver.isDoubleType()", "valueIsNotDouble(value)"})
  public static final SVector doDoubleSVector(final SVector receiver, final Object value) {
    double[] storage = receiver.getDoubleStorage();
    Object[] newStorage;

    if (receiver.getLastIndex() > storage.length) {
      newStorage = new Object[storage.length * 2];
    } else {
      newStorage = new Object[storage.length];
    }

    for (int i = 0; i < storage.length; i++) {
      newStorage[i] = storage[i] == SVector.EMPTY_DOUBLE_SLOT ? null : storage[i];
    }

    receiver.setStorage(newStorage);
    newStorage[receiver.getLastIndex() - 1] = value;
    receiver.incrementLastIndex();

    return receiver;
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("append:");
  }

}
