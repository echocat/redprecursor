package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Throw;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunThrow implements Throw, SunStatement {

    private final JCThrow _jc;
    private final SunNodeConverter _converter;

    public SunThrow(JCThrow jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.expr, SunExpression.class);
    }

    @Override
    public void setExpression(Expression value) {
        _jc.expr = _converter.nodeToJc(requireNonNull("value", value), SunExpression.class, JCExpression.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getExpression());
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
    public JCThrow getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
