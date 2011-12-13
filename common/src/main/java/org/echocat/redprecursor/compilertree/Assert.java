package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.ExpressionStatement;

public interface Assert extends ExpressionStatement {

    public Expression getDetail();

    public void setDetail(Expression expression);

}
