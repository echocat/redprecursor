package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCSynchronized;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Synchronized;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunSynchronized implements Synchronized, SunStatement {

    private final JCSynchronized _jc;
    private final SunNodeConverter _converter;

    public SunSynchronized(JCSynchronized jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunStatement> getBody() {
        return _converter.jcBlockToStatements(_jc.body);
    }

    @Override
    public void setBody(List<Statement> body) {
        _jc.body = _converter.statementsToJcBlock(body, JCBlock.class);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.lock, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.lock = _converter.nodeToJc(requireNonNull("expression", expression), SunExpression.class, JCExpression.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return IterableBuilder.<SunNode>toIterable(getExpression()).append(getBody());
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
    public JCSynchronized getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
