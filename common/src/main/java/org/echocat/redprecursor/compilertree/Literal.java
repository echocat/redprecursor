package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;

public interface Literal extends Expression {

    public Primitive getPrimitive();

    public void setPrimitive(Primitive primitive);

    public Object getValue();

    public void setValue(Object value);

}
