package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import org.echocat.redprecursor.compilertree.DoWhile;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Statement;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunDoWhile implements DoWhile, SunStatement {

    private final JCDoWhileLoop _jc;
    private final SunNodeConverter _converter;

    public SunDoWhile(JCDoWhileLoop jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunStatement getBody() {
        return _converter.jcToNode(_jc.body, SunStatement.class);
    }

    @Override
    public void setBody(Statement body) {
        _jc.body = _converter.nodeToJc(body, SunStatement.class, JCStatement.class);
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
        return toIterable(getBody(), getExpression());
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
    public JCDoWhileLoop getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
