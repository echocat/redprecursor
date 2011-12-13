package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.ExpressionWithEnclosingExpression;

public interface Assign extends ExpressionWithEnclosingExpression {

    public Expression getAssignTo();

    public void setAssignTo(Expression expression);

}
