package org.echocat.redprecursor.compilertree.base;

import org.echocat.redprecursor.compilertree.Modifiers;

public interface ModifiersEnabledNode extends Node {

    public Modifiers getModifiers();

    public void setModifiers(Modifiers modifiers);
}
