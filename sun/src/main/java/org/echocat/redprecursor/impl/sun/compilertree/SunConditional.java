package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import org.echocat.redprecursor.compilertree.Conditional;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunConditional implements Conditional, SunExpression {

    private final JCConditional _jc;
    private final SunNodeConverter _converter;

    public SunConditional(JCConditional jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getIfTrue() {
        return _converter.jcToNode(_jc.truepart, SunExpression.class);
    }

    @Override
    public void setIfTrue(Expression ifTrue) {
        _jc.truepart = _converter.nodeToJc(ifTrue, SunExpression.class, JCExpression.class);
    }

    @Override
    public SunExpression getIfFalse() {
        return _converter.jcToNode(_jc.falsepart, SunExpression.class);
    }

    @Override
    public void setIfFalse(Expression ifFalse) {
        _jc.falsepart = _converter.nodeToJc(ifFalse, SunExpression.class, JCExpression.class);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.cond, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.cond = _converter.nodeToJc(expression, SunExpression.class, JCExpression.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getIfTrue(), getIfFalse(), getExpression());
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
    public JCConditional getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}