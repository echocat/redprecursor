package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import org.echocat.redprecursor.compilertree.For;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.ExpressionStatement;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunFor implements For, SunStatement {

    private final JCForLoop _jc;
    private final SunNodeConverter _converter;

    public SunFor(JCForLoop jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunStatement> getInitiateStatementsStatements() {
        return _converter.jcsToNodes(_jc.init, SunStatement.class);
    }

    @Override
    public void setInitiateStatements(List<Statement> initiateStatements) {
        _jc.init = _converter.nodesToJcs(initiateStatements, SunStatement.class, JCStatement.class);
    }

    @Override
    public List<? extends SunExpressionStatement> getStepStatements() {
        return _converter.jcsToNodes(_jc.step, SunExpressionStatement.class);
    }

    @Override
    public void setStepStatements(List<ExpressionStatement> stepStatements) {
        _jc.step = _converter.nodesToJcs(stepStatements, SunExpressionStatement.class, JCExpressionStatement.class);
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
        return IterableBuilder.<SunNode>toIterable(getInitiateStatementsStatements(), getStepStatements()).append(getBody(), getExpression());
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
    public JCForLoop getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
