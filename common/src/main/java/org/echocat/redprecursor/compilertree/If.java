package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.ExpressionStatement;
import org.echocat.redprecursor.compilertree.base.Statement;

public interface If extends ExpressionStatement {

    public Statement getThenBody();

    public void setThenBody(Statement thenBody);

    public Statement getElseBody();

    public void setElseBody(Statement elseBody);
}
