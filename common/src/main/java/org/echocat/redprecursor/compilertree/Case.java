package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.BodyStatement;
import org.echocat.redprecursor.compilertree.base.ExpressionStatement;
import org.echocat.redprecursor.compilertree.base.Statement;

public interface Case extends ExpressionStatement, BodyStatement<Statement> {}
