package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.NameEnabledNode;

import java.util.List;

public interface TypeParameter extends NameEnabledNode {

    public List<? extends Expression> getBounds();

    public void setBounds(List<Expression> bounds);
}
