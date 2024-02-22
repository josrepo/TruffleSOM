package trufflesom.vmobjects;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import trufflesom.interpreter.objectstorage.FieldAccessorNode;
import trufflesom.interpreter.objectstorage.ObjectLayout;
import trufflesom.interpreter.objectstorage.StorageLocation;
import trufflesom.vm.Classes;
import trufflesom.vm.Universe;
import trufflesom.vm.constants.Nil;

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

  public SVector(final SClass clazz, final long length) {
    super(clazz, setSVectorObjectLayout(clazz));
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

  private static class FirstStorageLocation extends StorageLocation implements StorageLocation.LongStorageLocation {

    protected FirstStorageLocation(final long fieldIndex) {
      super(fieldIndex);
    }

    @Override
    public long readLong(final SObject obj) {
      return ((SVector) obj).first;
    }

    @Override
    public void writeLong(final SObject obj, final long value) {
      ((SVector) obj).first = (int) value;
    }

    @Override
    public long increment(final SObject obj) {
      ((SVector) obj).incrementFirstIndex();
      return ((SVector) obj).first;
    }

    @Override
    public boolean isSet(final SObject obj) {
      return true;
    }

    @Override
    public Object read(final SObject obj) {
      return readLong(obj);
    }

    @Override
    public void write(final SObject obj, final Object value) {
      assert value != null;
      if (value instanceof Long) {
        writeLong(obj, (long) value);
      } else {
        assert value != Nil.nilObject;
        CompilerDirectives.transferToInterpreterAndInvalidate();
        obj.setUninitializedField(fieldIndex, value);
      }
    }

    @Override
    public FieldAccessorNode.AbstractReadFieldNode getReadNode(final int idx, final ObjectLayout layout, final FieldAccessorNode.AbstractReadFieldNode next) {
      CompilerAsserts.neverPartOfCompilation("StorageLocation");
      return new FieldAccessorNode.ReadLongFieldNode(idx, layout, next);
    }

    @Override
    public FieldAccessorNode.AbstractWriteFieldNode getWriteNode(final int idx, final ObjectLayout layout, final FieldAccessorNode.AbstractWriteFieldNode next) {
      CompilerAsserts.neverPartOfCompilation("StorageLocation");
      return new FieldAccessorNode.WriteLongFieldNode(idx, layout, next);
    }

    @Override
    public void debugPrint(final SObject obj) {
      Universe.println("FirstStorageLocation: fieldIdx=" + this.fieldIndex);
    }
  }

  private static class LastStorageLocation extends StorageLocation implements StorageLocation.LongStorageLocation {

    protected LastStorageLocation(final long fieldIndex) {
      super(fieldIndex);
    }

    @Override
    public long readLong(final SObject obj) {
      return ((SVector) obj).last;
    }

    @Override
    public void writeLong(final SObject obj, final long value) {
      ((SVector) obj).last = (int) value;
    }

    @Override
    public long increment(final SObject obj) {
      ((SVector) obj).incrementFirstIndex();
      return ((SVector) obj).last;
    }

    @Override
    public boolean isSet(final SObject obj) {
      return true;
    }

    @Override
    public Object read(final SObject obj) {
      return readLong(obj);
    }

    @Override
    public void write(final SObject obj, final Object value) {
      assert value != null;
      if (value instanceof Long) {
        writeLong(obj, (long) value);
      } else {
        assert value != Nil.nilObject;
        CompilerDirectives.transferToInterpreterAndInvalidate();
        obj.setUninitializedField(fieldIndex, value);
      }
    }

    @Override
    public FieldAccessorNode.AbstractReadFieldNode getReadNode(final int idx, final ObjectLayout layout, final FieldAccessorNode.AbstractReadFieldNode next) {
      CompilerAsserts.neverPartOfCompilation("StorageLocation");
      return new FieldAccessorNode.ReadLongFieldNode(idx, layout, next);
    }

    @Override
    public FieldAccessorNode.AbstractWriteFieldNode getWriteNode(final int idx, final ObjectLayout layout, final FieldAccessorNode.AbstractWriteFieldNode next) {
      CompilerAsserts.neverPartOfCompilation("StorageLocation");
      return new FieldAccessorNode.WriteLongFieldNode(idx, layout, next);
    }

    @Override
    public void debugPrint(final SObject obj) {
      Universe.println("LastStorageLocation: fieldIdx=" + this.fieldIndex);
    }
  }

  private static class StorageStorageLocation extends StorageLocation.AbstractObjectStorageLocation {

    public StorageStorageLocation(final int fieldIndex) {
      super(fieldIndex);
    }

    @Override
    public boolean isSet(final SObject obj) {
      return true;
    }

    @Override
    public Object read(final SObject obj) {
      final SVector vec = (SVector) obj;

      if (vec.isEmptyType()) {
        return new SArray(vec.getEmptyStorage());
      } else if (vec.isDoubleType()) {
        return new SArray(vec.getDoubleStorage());
      } else if (vec.isLongType()) {
        return new SArray(vec.getLongStorage());
      } else if (vec.isObjectType()) {
        return new SArray(vec.getObjectStorage());
      }

      return Nil.nilObject;
    }

    @Override
    public void write(final SObject obj, final Object value) {
      // FIXME: Don't really want people writing here
    }

    @Override
    public void debugPrint(final SObject obj) {
      Universe.println("StorageStorageLocation: fieldIdx=" + this.fieldIndex);
    }
  }

  public static ObjectLayout setSVectorObjectLayout(final SClass clazz) {
    final ObjectLayout currentLayout = clazz.getLayoutForInstances();
    if (currentLayout.hasPatchedStorageLocation()) {
      return clazz.getLayoutForInstances();
    }

    return createSVectorObjectLayout(clazz);
  }

  @CompilerDirectives.TruffleBoundary
  private static ObjectLayout createSVectorObjectLayout(final SClass clazz) {
    final Class<?>[] knownFieldTypes = new Class<?>[3];
    knownFieldTypes[0] = Long.class;
    knownFieldTypes[1] = Long.class;
    knownFieldTypes[2] = Object.class;

    final StorageLocation[] storageLocations = new StorageLocation[3];
    storageLocations[0] = new FirstStorageLocation(0);
    storageLocations[1] = new LastStorageLocation(1);
    storageLocations[2] = new StorageStorageLocation(0);

    final ObjectLayout layout = new ObjectLayout(knownFieldTypes, storageLocations, 2, 1, clazz);
    clazz.setLayoutForInstances(layout);
    return layout;
  }

}
