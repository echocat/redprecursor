package org.echocat.redprecursor.compilertree.base;

import java.util.List;

public interface UsingTypeParameterEnabledNode extends Node {

    public List<? extends Expression> getTypeParameters();

    public void setTypeParameters(List<Expression> typeParameters);
}
