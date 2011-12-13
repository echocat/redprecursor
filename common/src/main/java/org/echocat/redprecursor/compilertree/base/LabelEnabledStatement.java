package org.echocat.redprecursor.compilertree.base;

public interface LabelEnabledStatement extends Statement {

    public String getLabel();

    public void setLabel(String label);
}
