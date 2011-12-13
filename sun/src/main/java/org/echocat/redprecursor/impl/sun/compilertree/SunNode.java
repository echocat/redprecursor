package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree;
import org.echocat.redprecursor.compilertree.base.Node;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface SunNode extends Node {

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes();

    public JCTree getJc();


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.PACKAGE })
    public static @interface Implementations {
        public Class<? extends SunNode>[] value();
    }
}
