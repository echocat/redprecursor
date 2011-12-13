package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Node;

import java.util.List;

public interface Erroneous extends Expression {

    public List<? extends Node> getErrors();

    public void setError(List<Node> errors);
}
