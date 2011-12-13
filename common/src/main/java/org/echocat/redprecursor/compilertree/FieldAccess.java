package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;

public interface FieldAccess extends Identifier {

    public String getFieldName();

    public void setFieldName(String fieldName);

    public Expression getContaining();

    public void setContaining(Expression containing);

}
