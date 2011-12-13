package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import org.echocat.redprecursor.compilertree.MethodInvocation;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunMethodInvocation implements MethodInvocation, SunExpression {

    private final JCMethodInvocation _jc;
    private final SunNodeConverter _converter;

    public SunMethodInvocation(JCMethodInvocation jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunExpression> getArguments() {
        return _converter.jcsToNodes(_jc.args, SunExpression.class);
    }

    @Override
    public void setArguments(List<Expression> arguments) {
        _jc.args = _converter.nodesToJcs(arguments, SunExpression.class, JCExpression.class);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.meth, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.meth = _converter.nodeToJc(requireNonNull("expression", expression), SunExpression.class, JCExpression.class);
    }

    @Override
    public List<? extends SunExpression> getTypeParameters() {
        return _converter.jcsToNodes(_jc.typeargs, SunExpression.class);
    }

    @Override
    public void setTypeParameters(List<Expression> typeParameters) {
        _jc.typeargs = _converter.nodesToJcs(typeParameters, SunExpression.class, JCExpression.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getArguments(), getTypeParameters()).append(getExpression());
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
    public JCMethodInvocation getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
