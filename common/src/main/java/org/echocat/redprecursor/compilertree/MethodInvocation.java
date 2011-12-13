package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.ArgumentsEnabledNode;
import org.echocat.redprecursor.compilertree.base.ExpressionWithEnclosingExpression;
import org.echocat.redprecursor.compilertree.base.UsingTypeParameterEnabledNode;

public interface MethodInvocation extends ExpressionWithEnclosingExpression, UsingTypeParameterEnabledNode, ArgumentsEnabledNode {}
