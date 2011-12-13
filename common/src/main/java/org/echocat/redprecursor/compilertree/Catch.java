package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.BodyNode;
import org.echocat.redprecursor.compilertree.base.ParameterDeclarationEnabledNode;
import org.echocat.redprecursor.compilertree.base.Statement;

public interface Catch extends BodyNode<Statement>, ParameterDeclarationEnabledNode {}
