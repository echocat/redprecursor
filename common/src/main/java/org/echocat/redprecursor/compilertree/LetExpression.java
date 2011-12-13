package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Node;

import java.util.List;

public interface LetExpression extends Expression {

    public List<? extends VariableDeclaration> getVariableDeclarations();

    public void setVariableDeclarations(List<VariableDeclaration> variableDeclarations);

    public Node getExpression();

    public void setExpression(Node expression);

}
