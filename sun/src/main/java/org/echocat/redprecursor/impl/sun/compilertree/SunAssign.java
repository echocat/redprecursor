package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCAssign;
import org.echocat.redprecursor.compilertree.Assign;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunAssign implements Assign, SunExpression {

    private final JCAssign _jc;
    private final SunNodeConverter _converter;

    public SunAssign(JCAssign jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.lhs, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.lhs = castNonNullParameterTo("expression", expression, SunExpression.class).getJc();
    }

    @Override
    public SunExpression getAssignTo() {
        return _converter.jcToNode(_jc.rhs, SunExpression.class);
    }

    @Override
    public void setAssignTo(Expression expression) {
        _jc.rhs = castNonNullParameterTo("expression", expression, SunExpression.class).getJc();
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getExpression(), getAssignTo());
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
    public JCAssign getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
