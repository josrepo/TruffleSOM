package trufflesom.vmobjects;

import com.oracle.truffle.api.CompilerDirectives;
import trufflesom.vm.Classes;

public class SVector extends SObject {

  private Object storage;

  /**
   * Create an empty vector, using the empty strategy.
   *
   * @param length length
   */
  public SVector(final long length) {
    super(Classes.vectorClass, Classes.vectorClass.getLayoutForInstances());
    storage = (int) length;
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

  public boolean isBooleanType() {
    return storage.getClass() == boolean[].class;
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

  public boolean[] getBooleanStorage() {
    assert isBooleanType();
    return (boolean[]) storage;
  }

}
