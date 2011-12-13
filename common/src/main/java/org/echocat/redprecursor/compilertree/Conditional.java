package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.ExpressionWithEnclosingExpression;

public interface Conditional extends ExpressionWithEnclosingExpression {

    public Expression getIfTrue();

    public void setIfTrue(Expression ifTrue);

    public Expression getIfFalse();

    public void setIfFalse(Expression ifFalse);

}
