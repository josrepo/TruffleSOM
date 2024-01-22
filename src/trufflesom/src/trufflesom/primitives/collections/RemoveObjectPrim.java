package trufflesom.primitives.collections;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.vm.SymbolTable;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "remove:", selector = "remove:", receiverType = SVector.class, inParser = false)
public abstract class RemoveObjectPrim extends BinaryMsgExprNode {

  @Specialization(guards = "receiver.isObjectType()")
  public static final boolean doObjectSVector(final SVector receiver, final Object value) {
    final Object[] storage = receiver.getObjectStorage();
    final Object[] newStorage = new Object[storage.length];
    int newLast = 1;
    boolean found = false;

    int lastElement = receiver.getLastIndex() - 2;
    for (int i = receiver.getFirstIndex() - 1; i < lastElement; i++) {
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
