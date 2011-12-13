package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIf;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import org.echocat.redprecursor.compilertree.If;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Statement;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunIf implements If, SunStatement {

    private final JCIf _jc;
    private final SunNodeConverter _converter;

    public SunIf(JCIf jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunStatement getThenBody() {
        return _converter.jcToNode(_jc.thenpart, SunStatement.class);
    }

    @Override
    public void setThenBody(Statement thenBody) {
        _jc.thenpart = _converter.nodeToJc(thenBody, SunStatement.class, JCStatement.class);
    }

    @Override
    public SunStatement getElseBody() {
        return _converter.jcToNode(_jc.elsepart, SunStatement.class);
    }

    @Override
    public void setElseBody(Statement elseBody) {
        _jc.elsepart = _converter.nodeToJc(elseBody, SunStatement.class, JCStatement.class);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.cond, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        requireNonNull("expression", expression);
        _jc.cond = _converter.nodeToJc(expression, SunExpression.class, JCExpression.class);
    }

    @Override
    public JCIf getJc() {
        return _jc;
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
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getThenBody(), getElseBody(), getExpression());
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
