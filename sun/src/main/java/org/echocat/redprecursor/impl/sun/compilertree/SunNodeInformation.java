package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree;

public abstract class SunNodeInformation<S extends SunNode, J extends JCTree> {

    private final Class<S> _nodeType;
    private final Class<J> _jcType;

    protected SunNodeInformation(Class<S> nodeType, Class<J> jcType) {
        _nodeType = nodeType;
        _jcType = jcType;
    }

    public abstract S newInstance(J jcType, SunNodeConverter converter);

    public Class<S> getNodeType() {
        return _nodeType;
    }

    public Class<J> getJcType() {
        return _jcType;
    }
}
