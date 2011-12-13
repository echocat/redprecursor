package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.LetExpr;
import org.echocat.redprecursor.compilertree.LetExpression;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.VariableDeclaration;
import org.echocat.redprecursor.compilertree.base.Node;

import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunLetExpression implements LetExpression, SunExpression {

    private final LetExpr _jc;
    private final SunNodeConverter _converter;

    public SunLetExpression(LetExpr jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunVariableDeclaration> getVariableDeclarations() {
        return _converter.jcsToNodes(_jc.defs, SunVariableDeclaration.class);
    }

    @Override
    public void setVariableDeclarations(List<VariableDeclaration> variableDeclarations) {
        _jc.defs = _converter.nodesToJcs(requireNonNull("variableDeclarations", variableDeclarations), SunVariableDeclaration.class, JCVariableDecl.class);
    }

    @Override
    public SunNode getExpression() {
        return _converter.jcToNode(_jc.expr, SunNode.class);
    }

    @Override
    public void setExpression(Node expression) {
        _jc.expr = _converter.nodeToJc(requireNonNull("expression", expression), SunNode.class, JCTree.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return IterableBuilder.<SunNode>toIterable(getVariableDeclarations()).append(getExpression());
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
    public LetExpr getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
