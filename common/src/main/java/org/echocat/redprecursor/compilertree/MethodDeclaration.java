package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.*;

import java.util.List;

public interface MethodDeclaration extends Declaration, BodyNode<Statement>, NameEnabledNode, ModifiersEnabledNode, TypeParameterEnabledNode {

    public Identifier getResultType();

    public void setResultType(Expression type);

    public List<? extends Identifier> getThrows();

    public void setThrows(List<Identifier> thrownTypes);

    public Expression getDefaultValue();

    public void setDefaultValue(Expression expression);

    public List<? extends VariableDeclaration> getParameterDeclarations();

    public void setParameterDeclarations(List<VariableDeclaration> parameterDeclarations);

}
