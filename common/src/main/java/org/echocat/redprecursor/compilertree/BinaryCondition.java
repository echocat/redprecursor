package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;

public interface BinaryCondition extends Expression {

    public Expression getLeft();

    public void setLeft(Expression left);

    public Operator getOperator();

    public void setOperator(Operator operator);

    public Expression getRight();

    public void setRight(Expression right);

}
