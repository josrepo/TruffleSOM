package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.primitives.basics.EqualsPrim;
import trufflesom.primitives.basics.EqualsPrimFactory;
import trufflesom.vm.SymbolTable;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "indexOf:", selector = "indexOf:", receiverType = SVector.class, inParser = false)
public abstract class IndexOfPrim extends BinaryMsgExprNode {

  @Child private EqualsPrim equals;

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
    equals = EqualsPrimFactory.create(null, null);
    return (T) this;
  }

  @Specialization(guards = {"receiver.isEmptyType()", "valueIsNotNil(value)"})
  @SuppressWarnings("unused")
  public final long doEmptySVector(final VirtualFrame frame, final SVector receiver, final Object value) {
    return -1;
  }

  @Specialization(guards = {"receiver.isEmptyType()", "valueIsNil(value)"})
  @SuppressWarnings("unused")
  public final long doEmptySVectorWithNil(final VirtualFrame frame, final SVector receiver, final Object value) {
    return 1;
  }

  @Specialization(guards = "receiver.isObjectType()")
  public final long doObjectSVector(final VirtualFrame frame, final SVector receiver, final Object value) {
    final Object[] storage = receiver.getObjectStorage();
    int first = receiver.getFirstIndex();
    int last = receiver.getLastIndex() - 1;

    for (int i = first - 1; i < last; i++) {
      if (this.equals.executeEvaluated(frame, storage[i], value).equals(true)) {
        return i - first + 2;
      }
    }

    return -1;
  }

  @Specialization(guards = "receiver.isLongType()")
  @SuppressWarnings("unused")
  public final long doLongSVector(final VirtualFrame frame, final SVector receiver, final long value) {
    final long[] storage = receiver.getLongStorage();
    int first = receiver.getFirstIndex();
    int last = receiver.getLastIndex() - 1;

    for (int i = first - 1; i < last; i++) {
      if (storage[i] == value) {
        return i - first + 2;
      }
    }

    return -1;
  }

  @Specialization(guards = "receiver.isDoubleType()")
  @SuppressWarnings("unused")
  public final long doDoubleSVector(final VirtualFrame frame, final SVector receiver, final double value) {
    final double[] storage = receiver.getDoubleStorage();
    int first = receiver.getFirstIndex();
    int last = receiver.getLastIndex() - 1;

    for (int i = first - 1; i < last; i++) {
      if (storage[i] == value) {
        return i - first + 2;
      }
    }

    return -1;
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("indexOf:");
  }

}
