package trufflesom.primitives.vectors;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import trufflesom.bdt.primitives.Primitive;
import trufflesom.interpreter.nodes.nary.UnaryExpressionNode;
import trufflesom.vmobjects.SArray;
import trufflesom.vmobjects.SVector;

import java.util.Arrays;

@GenerateNodeFactory
@Primitive(className = "Vector", primitive = "asArray", selector = "asArray", receiverType = SVector.class, inParser = false)
public abstract class AsArrayPrim extends UnaryExpressionNode {

  @Specialization(guards = "receiver.isObjectType()")
  public static final SArray doSVector(final SVector receiver) {
    return SArray.create(Arrays.copyOfRange(receiver.getObjectStorage(),
        receiver.getFirstIndex() - 1,
        receiver.getLastIndex() - 1));
  }
  
}
