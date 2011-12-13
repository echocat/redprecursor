package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Node;

import java.util.List;

public interface Modifiers extends Node {

    public List<Modifier> getModifier();

    public void setModifiers(List<Modifier> flags);

    public List<? extends Annotation> getAnnotations();

    public void setAnnotations(List<Annotation> annotations);
}
