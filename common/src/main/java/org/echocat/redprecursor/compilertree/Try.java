package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.BodyStatement;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

public interface Try extends BodyStatement<Statement> {

    public List<? extends Catch> getCatches();

    public void setCatches(List<Catch> catches);

    public List<? extends Statement> getFinalizer();

    public void setFinalizer(List<Statement> finalizer);
}

