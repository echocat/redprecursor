package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.BodyNode;
import org.echocat.redprecursor.compilertree.base.NameEnabledNode;
import org.echocat.redprecursor.compilertree.base.Statement;

public interface Labelled extends NameEnabledNode, BodyNode<Statement> {}
