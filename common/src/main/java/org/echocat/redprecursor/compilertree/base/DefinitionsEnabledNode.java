package org.echocat.redprecursor.compilertree.base;

import java.util.List;

public interface DefinitionsEnabledNode extends Node {

    public List<? extends Declaration> getDeclarations();

    public void setDeclarations(List<Declaration> declarations);
}
