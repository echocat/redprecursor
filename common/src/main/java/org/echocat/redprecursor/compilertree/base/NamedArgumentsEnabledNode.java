package org.echocat.redprecursor.compilertree.base;

import java.util.Map;

public interface NamedArgumentsEnabledNode extends Node {

    public Map<String, ? extends Expression> getArguments();

    public void setArguments(Map<String, ? extends Expression> arguments);
}
