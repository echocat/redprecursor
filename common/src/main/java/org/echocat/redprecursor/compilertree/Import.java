package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Declaration;
import org.echocat.redprecursor.compilertree.base.Node;

public interface Import extends Declaration {

    public boolean isStatic();

    public void setStatic(boolean isStatic);

    public Node getIdentifier();

    public void setIdentifier(Node node);
}
