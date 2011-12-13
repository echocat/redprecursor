package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.CreateInstances;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.TypeEnabledNode;

import java.util.List;

public interface NewArray extends CreateInstances, TypeEnabledNode {

    public List<? extends Expression> getElements();

    public void setElements(List<Expression> elements);

    public List<? extends Expression> getDimensions();

    public void setDimensions(List<Expression> dimensions);

}
