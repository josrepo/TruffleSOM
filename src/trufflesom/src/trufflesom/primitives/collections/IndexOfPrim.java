package trufflesom.primitives.collections;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.primitives.basics.EqualsPrim;
import trufflesom.primitives.basics.EqualsPrimFactory;
import trufflesom.vm.SymbolTable;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "indexOf:", selector = "indexOf:", receiverType = SVector.class, inParser = false)
public abstract class IndexOfPrim extends BinaryMsgExprNode {

  @Child private EqualsPrim equals;

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Node> T initialize(final long coord) {
    super.initialize(coord);
    equals = EqualsPrimFactory.create(null, null);
    return (T) this;
  }

  @Specialization(guards = "receiver.isObjectType()")
  public final long doObjectSVector(final VirtualFrame frame, final SVector receiver, final Object value) {
      final Object[] storage = receiver.getObjectStorage();
      int first = receiver.getFirstIndex();
      int last = receiver.getLastIndex();

      for (int i = first - 1; i < last; i++) {
        if (this.equals.makeGenericSend(frame, storage[i], value).equals(true)) {
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
