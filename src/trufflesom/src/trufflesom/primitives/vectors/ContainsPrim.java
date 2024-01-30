package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.primitives.basics.BlockPrimsFactory;
import trufflesom.primitives.basics.EqualsPrim;
import trufflesom.primitives.basics.EqualsPrimFactory;
import trufflesom.primitives.basics.LengthPrimFactory;
import trufflesom.vm.SymbolTable;
import trufflesom.vm.constants.Nil;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "contains:", selector = "contains:", receiverType = SVector.class, inParser = false)
public abstract class ContainsPrim extends BinaryMsgExprNode {

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
  public final boolean doEmptySVector(final VirtualFrame frame, final SVector receiver, final Object value) {
    return false;
  }

  @Specialization(guards = {"receiver.isEmptyType()", "valueIsNil(value)"})
  @SuppressWarnings("unused")
  public final boolean doEmptySVectorWithNil(final VirtualFrame frame, final SVector receiver, final Object value) {
    return true;
  }

  @Specialization(guards = "receiver.isObjectType()")
  public final boolean doObjectSVector(final VirtualFrame frame, final SVector receiver, final Object value) {
    final Object[] storage = receiver.getObjectStorage();
    int last = receiver.getLastIndex() - 1;

    for (int i = receiver.getFirstIndex() - 1; i < last; i++) {
      if (this.equals.executeEvaluated(frame, storage[i], value).equals(true)) {
        return true;
      }
    }

    return false;
  }

  @Specialization(guards = "receiver.isLongType()")
  public final boolean doLongSVector(final VirtualFrame frame, final SVector receiver, final long value) {
    final long[] storage = receiver.getLongStorage();
    int last = receiver.getLastIndex() - 1;

    for (int i = receiver.getFirstIndex() - 1; i < last; i++) {
      if (storage[i] == value) {
        return true;
      }
    }

    return false;
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public final boolean doDoubleSVector(final VirtualFrame frame, final SVector receiver, final double value) {
    final double[] storage = receiver.getDoubleStorage();
    int last = receiver.getLastIndex() - 1;

    for (int i = receiver.getFirstIndex() - 1; i < last; i++) {
      if (storage[i] == value) {
        return true;
      }
    }

    return false;
  }

  @Specialization(guards = "receiver.isBooleanType()")
  public final boolean doBooleanSVector(final VirtualFrame frame, final SVector receiver, final boolean value) {
    final boolean[] storage = receiver.getBooleanStorage();
    int last = receiver.getLastIndex() - 1;

    for (int i = receiver.getFirstIndex() - 1; i < last; i++) {
      if (storage[i] == value) {
        return true;
      }
    }

    return false;
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("contains:");
  }

}
