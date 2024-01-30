package trufflesom.primitives.collections;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;

import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.Invokable;
import trufflesom.interpreter.nodes.nary.BinaryMsgExprNode;
import trufflesom.primitives.basics.BlockPrims.ValueOnePrim;
import trufflesom.primitives.basics.BlockPrimsFactory.ValueOnePrimFactory;
import trufflesom.primitives.basics.LengthPrim;
import trufflesom.primitives.basics.LengthPrimFactory;
import trufflesom.vm.SymbolTable;
import trufflesom.vmobjects.SArray;
import trufflesom.vmobjects.SBlock;
import trufflesom.vmobjects.SSymbol;
import trufflesom.vmobjects.SVector;


@GenerateNodeFactory
@Primitive(className = "Array", primitive = "doIndexes:")
@Primitive(className = "Vector", primitive = "doIndexes:")
@Primitive(selector = "doIndexes:", receiverType = {SArray.class, SVector.class}, disabled = true)
public abstract class DoIndexesPrim extends BinaryMsgExprNode {
  @Child private ValueOnePrim block;
  @Child private LengthPrim   length;

  @Override
  public SSymbol getSelector() {
    return SymbolTable.symbolFor("doIndexes:");
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Node> T initialize(final long coord) {
    super.initialize(coord);
    block = ValueOnePrimFactory.create(null, null);
    length = LengthPrimFactory.create(null);
    return (T) this;
  }

  @Specialization
  public final SArray doArray(final VirtualFrame frame,
      final SArray receiver, final SBlock b) {
    int l = (int) this.length.executeEvaluated(frame, receiver);
    loopArray(frame, b, l);
    return receiver;
  }

  @Specialization
  public final SVector doSVector(final VirtualFrame frame,
      final SVector receiver, final SBlock b) {
    int l = receiver.getSize() + 1;
    try {
      if (1 < l) {
        this.block.executeEvaluated(frame, b, (long) 1);
      }
      for (long i = 2; i < l; i++) {
        this.block.executeEvaluated(frame, b, i);
      }
    } finally {
      if (CompilerDirectives.inInterpreter()) {
        reportLoopCount(l);
      }
    }
    return receiver;
  }

  private void loopArray(final VirtualFrame frame, final SBlock b, final int l) {
    try {
      assert SArray.FIRST_IDX == 0;
      if (SArray.FIRST_IDX < l) {
        this.block.executeEvaluated(
            // +1 because it is going to the Smalltalk level
            frame, b, (long) SArray.FIRST_IDX + 1);
      }
      for (long i = 1; i < l; i++) {
        this.block.executeEvaluated(
            frame, b, i + 1); // +1 because it is going to the Smalltalk level
      }
    } finally {
      if (CompilerDirectives.inInterpreter()) {
        reportLoopCount(l);
      }
    }
  }

  protected final void reportLoopCount(final long count) {
    assert count >= 0;
    CompilerAsserts.neverPartOfCompilation("reportLoopCount");
    Node current = getParent();
    while (current != null && !(current instanceof RootNode)) {
      current = current.getParent();
    }
    if (current != null) {
      ((Invokable) current).propagateLoopCountThroughoutLexicalScope(count);
    }
  }

}
