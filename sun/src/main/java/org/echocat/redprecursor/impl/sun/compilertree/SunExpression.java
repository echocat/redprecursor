package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import org.echocat.redprecursor.compilertree.base.Expression;

public interface SunExpression extends Expression, SunNode {

    @Override
    JCExpression getJc();
}
