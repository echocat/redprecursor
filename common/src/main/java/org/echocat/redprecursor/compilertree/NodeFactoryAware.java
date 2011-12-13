package org.echocat.redprecursor.compilertree;

import javax.annotation.Nonnull;

public interface NodeFactoryAware {

    public void setNodeFactory(@Nonnull NodeFactory nodeFactory);

}
