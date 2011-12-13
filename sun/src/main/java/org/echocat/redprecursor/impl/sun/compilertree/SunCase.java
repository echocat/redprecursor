package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCCase;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import org.echocat.redprecursor.compilertree.Case;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunCase implements Case, SunStatement {

    private final JCCase _jc;
    private final SunNodeConverter _converter;

    public SunCase(JCCase jc, SunNodeConverter converter) {
        _jc = requireNonNull("jcCase", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.pat, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.pat = castNonNullParameterTo("expression", expression, SunExpression.class).getJc();
    }

    @Override
    public List<? extends SunStatement> getBody() {
        return _converter.jcsToNodes(_jc.stats, SunStatement.class);
    }

    @Override
    public void setBody(List<Statement> body) {
        _jc.stats = _converter.nodesToJcs(body, SunStatement.class, JCStatement.class);
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
    public JCCase getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
