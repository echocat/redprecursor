package org.echocat.redprecursor.compilertree.base;

public interface LoopStatement extends Statement {

    public Statement getBody();

    public void setBody(Statement body);
}
