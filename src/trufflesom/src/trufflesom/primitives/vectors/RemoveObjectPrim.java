package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.primitives.basics.EqualsEqualsPrim;
import trufflesom.primitives.basics.EqualsEqualsPrimFactory;
import trufflesom.vm.SymbolTable;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "remove:", selector = "remove:", receiverType = SVector.class, inParser = false)
public abstract class RemoveObjectPrim extends BinaryMsgExprNode {

  @Child private EqualsEqualsPrim equalsEquals;

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Node> T initialize(final long coord) {
    super.initialize(coord);
    equalsEquals = EqualsEqualsPrimFactory.create(null, null);
    return (T) this;
  }

  @Specialization(guards = "receiver.isObjectType()")
  public final boolean doObjectSVector(final VirtualFrame frame, final SVector receiver, final Object value) {
    final Object[] storage = receiver.getObjectStorage();
    final Object[] newStorage = new Object[storage.length];
    int last = receiver.getLastIndex() - 1;
    int newLast = 1;
    boolean found = false;

    for (int i = receiver.getFirstIndex() - 1; i < last; i++) {
      if (this.equalsEquals.executeEvaluated(frame, storage[i], value).equals(true)) {
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
