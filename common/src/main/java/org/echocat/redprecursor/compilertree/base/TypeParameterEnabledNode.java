package org.echocat.redprecursor.compilertree.base;

import org.echocat.redprecursor.compilertree.TypeParameter;

import java.util.List;

public interface TypeParameterEnabledNode extends Node {

    public List<? extends TypeParameter> getTypeParameters();

    public void setTypeParameters(List<TypeParameter> typeParameters);
}
