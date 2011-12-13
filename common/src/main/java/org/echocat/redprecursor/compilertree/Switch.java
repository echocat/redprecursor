package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.ExpressionStatement;

import java.util.List;

public interface Switch extends ExpressionStatement {

    public List<? extends Case> getCases();

    public void setCases(List<Case> cases);

}
