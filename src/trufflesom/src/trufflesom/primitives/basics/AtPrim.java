package trufflesom.primitives.basics;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;

import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.vm.SymbolTable;
import trufflesom.vm.constants.Nil;
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

  @Specialization(guards = "receiver.isBooleanType()")
  public static final boolean doBooleanSArray(final SArray receiver, final long idx) {
    return receiver.getBooleanStorage()[(int) idx - 1];
  }

  // FIXME: Need to do error send if index is out of bounds

  @Specialization(guards = "receiver.isEmptyType()")
  public static final Object doEmptySVector(final SVector receiver, final long idx) {
    return Nil.nilObject;
  }

  @Specialization(guards = "receiver.isObjectType()")
  public static final Object doObjectSVector(final SVector receiver, final long idx) {
    return receiver.getObjectStorage()[(int) idx - 1];
  }

  @Specialization(guards = "receiver.isLongType()")
  public static final long doLongSVector(final SVector receiver, final long idx) {
    return receiver.getLongStorage()[(int) idx - 1];
  }

  @Specialization(guards = "receiver.isDoubleType()")
  public static final double doDoubleSVector(final SVector receiver, final long idx) {
    return receiver.getDoubleStorage()[(int) idx - 1];
  }

  @Specialization(guards = "receiver.isBooleanType()")
  public static final boolean doBooleanSVector(final SVector receiver, final long idx) {
    return receiver.getBooleanStorage()[(int) idx - 1];
  }

}
