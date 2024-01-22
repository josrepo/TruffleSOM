package trufflesom.primitives.collections;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.vm.SymbolTable;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "contains:", selector = "contains:", receiverType = SVector.class, inParser = false)
public abstract class ContainsPrim extends BinaryMsgExprNode {

  @Specialization(guards = "receiver.isObjectType()")
  public static final boolean doObjectSVector(final SVector receiver, final Object value) {
    if (false /* need to handle strings differently */) {
      // string stuff
      return false;
    } else {
      final Object[] storage = receiver.getObjectStorage();
      for (int i = receiver.getFirstIndex() - 1; i < receiver.getLastIndex(); i++) {
        if (storage[i] == value) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("contains:");
  }

}
