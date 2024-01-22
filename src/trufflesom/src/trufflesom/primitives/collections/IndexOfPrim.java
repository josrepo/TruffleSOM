package trufflesom.primitives.collections;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.vm.SymbolTable;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "indexOf:", selector = "indexOf:", receiverType = SVector.class, inParser = false)
public abstract class IndexOfPrim extends BinaryMsgExprNode {

  @Specialization(guards = "receiver.isObjectType()")
  public static final long doObjectSVector(final SVector receiver, final Object value) {
    if (false /* need to handle strings differently??? */) {
      // need to test how som handles index of with string. test with vector:
      // [1, "two", "three", "two"]
      // assume it does string equality so indexOf("two") would return the first one

      // string stuff
      return -1;
    } else {
      final Object[] storage = receiver.getObjectStorage();
      for (int i = receiver.getFirstIndex() - 1; i < receiver.getLastIndex(); i++) {
        if (storage[i] == value) {
          return i - receiver.getFirstIndex() + 2;
        }
      }
    }

    return -1;
  }

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("indexOf:");
  }

}
