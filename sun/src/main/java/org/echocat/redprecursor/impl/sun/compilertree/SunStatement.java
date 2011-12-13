package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCStatement;
import org.echocat.redprecursor.compilertree.base.Statement;

public interface SunStatement extends Statement, SunNode {

    @Override
    public JCStatement getJc();
}
