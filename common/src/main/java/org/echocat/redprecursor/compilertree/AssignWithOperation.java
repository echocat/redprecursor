package org.echocat.redprecursor.compilertree;

public interface AssignWithOperation extends Assign {

    public Operator getOperator();

    public void setOperator(Operator operator);

}
