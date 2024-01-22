package trufflesom.primitives.collections;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.vm.SymbolTable;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "append:", selector = "append:", inParser = false, receiverType = SVector.class)
public abstract class AppendPrim extends BinaryMsgExprNode {

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

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("append:");
  }

}
