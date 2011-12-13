package org.echocat.redprecursor.compilertree.base;

public interface NodeWithEnclosingExpression extends Node {

    public Expression getExpression();

    public void setExpression(Expression expression);

}
