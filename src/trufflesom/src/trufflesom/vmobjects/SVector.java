package trufflesom.vmobjects;

import com.oracle.truffle.api.CompilerDirectives;
import trufflesom.vm.Classes;

public class SVector extends SObject {

  private Object storage;
  private int first, last;

  public static final long EMPTY_LONG_SLOT = Long.MIN_VALUE + 3L;
  public static final double EMPTY_DOUBLE_SLOT = Double.MIN_VALUE + 3.0;

  /**
   * Create an empty vector, using the empty strategy.
   *
   * @param length length
   */
  public SVector(final long length) {
    super(Classes.vectorClass, Classes.vectorClass.getLayoutForInstances());
    storage = (int) length;
    first = last = 1;
  }

  public boolean isEmptyType() {
    return storage.getClass() == Integer.class;
  }

  public boolean isObjectType() {
    return storage.getClass() == Object[].class;
  }

  public boolean isLongType() {
    return storage.getClass() == long[].class;
  }

  public boolean isDoubleType() {
    return storage.getClass() == double[].class;
  }

  public int getEmptyStorage() {
    assert isEmptyType();
    return (int) storage;
  }

  public Object[] getObjectStorage() {
    assert isObjectType();
    return CompilerDirectives.castExact(storage, Object[].class);
  }

  public long[] getLongStorage() {
    assert isLongType();
    return (long[]) storage;
  }

  public double[] getDoubleStorage() {
    assert isDoubleType();
    return (double[]) storage;
  }

  public int getFirstIndex() {
    return first;
  }

  public int getLastIndex() {
    return last;
  }

  public int getSize() {
    return last - first;
  }

  public boolean isEmpty() {
    return last == first;
  }

  public void setStorage(final Object storage) {
    this.storage = storage;
  }

  public void setLastIndex(final int last) {
    this.last = last;
  }

  public void incrementLastIndex() {
    last++;
  }

  public void decrementLastIndex() {
    last--;
  }

  public void resetFirstIndex() {
    first = 1;
  }

  public void incrementFirstIndex() {
    first++;
  }

}
