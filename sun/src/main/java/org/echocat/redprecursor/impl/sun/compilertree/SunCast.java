package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import org.echocat.redprecursor.compilertree.Cast;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunCast implements Cast, SunExpression {

    private final JCTypeCast _jc;
    private final SunNodeConverter _converter;

    public SunCast(JCTypeCast jc, SunNodeConverter converter) {
        _jc = requireNonNull("jcTypeCast", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.expr, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.expr = castNonNullParameterTo("expression", expression, SunExpression.class).getJc();
    }

    @Override
    public SunIdentifier getType() {
        return _converter.jcToNode(_jc.clazz, SunIdentifier.class);
    }

    @Override
    public void setType(Identifier type) {
        _jc.clazz = castNonNullParameterTo("type", type, SunIdentifier.class).getJc();
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getExpression(), getType());
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
    public JCTypeCast getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
