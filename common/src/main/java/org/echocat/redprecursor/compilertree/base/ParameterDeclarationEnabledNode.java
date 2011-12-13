package org.echocat.redprecursor.compilertree.base;

import org.echocat.redprecursor.compilertree.VariableDeclaration;

public interface ParameterDeclarationEnabledNode extends Node {

    public VariableDeclaration getParameterDeclaration();

    public void setParameterDeclaration(VariableDeclaration parameterDeclaration);
}
