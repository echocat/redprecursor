package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.*;

public interface VariableDeclaration extends Statement, TypeEnabledNode, Declaration, NameEnabledNode, ModifiersEnabledNode {

    public Expression getInitialValue();

    public void setInitialValue(Expression initialValue);
}
