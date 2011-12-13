package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCLiteral;
import org.echocat.redprecursor.compilertree.Literal;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Primitive;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunLiteral implements Literal, SunExpression {

    private final JCLiteral _jc;
    private final SunNodeConverter _converter;

    public SunLiteral(JCLiteral jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public Primitive getPrimitive() {
        return SunPrimitiveUtil.codeToPrimitive(_jc.typetag);
    }

    @Override
    public void setPrimitive(Primitive primitive) {
        _jc.typetag = SunPrimitiveUtil.primitiveToCode(primitive);
    }

    @Override
    public Object getValue() {
        return _jc.value;
    }

    @Override
    public void setValue(Object value) {
        final Primitive primitive = getPrimitive();
        if (primitive.isValueOfType(value)) {
            throw new IllegalArgumentException("The given value (" + value + ") is not of type: " + primitive);
        }
        _jc.value = value;
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable();
    }

    @Override
    public Position getPosition() {
        return _converter.jcPositionToPosition(_jc.pos);
    }

    @Override
    public void setPosition(Position position) {
        _jc.pos = _converter.positionToJcPosition(position);
    }

    @Override
    public JCLiteral getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
