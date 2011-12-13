package org.echocat.redprecursor.compilertree.base;

import java.util.List;

public interface ArgumentsEnabledNode extends Node {

    public List<? extends Expression> getArguments();

    public void setArguments(List<Expression> arguments);
}
