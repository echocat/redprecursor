package org.echocat.redprecursor.compilertree.base;

import org.echocat.redprecursor.compilertree.Position;

public interface Node {

    public Iterable<? extends Node> getAllEnclosedNodes();

    public Position getPosition();

    public void setPosition(Position position);
}
