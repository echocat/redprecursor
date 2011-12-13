package org.echocat.redprecursor.compilertree.base;

import java.util.List;

public interface BodyNode<T extends Node> extends Node {

    public List<? extends T> getBody();

    public void setBody(List<T> body);
}
