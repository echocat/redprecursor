package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.ExpressionWithEnclosingExpression;

public interface ArrayAccess extends ExpressionWithEnclosingExpression, Identifier {

    public Expression getIndex();

    public void setIndex(Expression index);
}
