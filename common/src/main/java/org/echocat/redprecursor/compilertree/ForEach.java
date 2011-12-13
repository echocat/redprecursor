package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.ExpressionStatement;
import org.echocat.redprecursor.compilertree.base.LoopStatement;

public interface ForEach extends LoopStatement, ExpressionStatement {

    public VariableDeclaration getParameterDeclaration();

    public void setParameterDeclaration(VariableDeclaration parameterDeclaration);
}
