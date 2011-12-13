package org.echocat.redprecursor.compilertree.base;

import org.echocat.redprecursor.compilertree.Identifier;

public interface TypeEnabledNode extends Node {

    public Identifier getType();

    public void setType(Identifier type);
}
