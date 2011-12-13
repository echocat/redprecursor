package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.ExpressionWithEnclosingExpression;

public interface Unary extends ExpressionWithEnclosingExpression {

    public Operator getOperator();

    public void setOperator(Operator operator);
}
