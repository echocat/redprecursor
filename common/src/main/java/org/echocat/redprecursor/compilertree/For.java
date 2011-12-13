package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.ExpressionStatement;
import org.echocat.redprecursor.compilertree.base.LoopStatement;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

public interface For extends LoopStatement, ExpressionStatement {

    public List<? extends Statement> getInitiateStatementsStatements();

    public void setInitiateStatements(List<Statement> initiateStatements);

    public List<? extends ExpressionStatement> getStepStatements();

    public void setStepStatements(List<ExpressionStatement> stepStatements);

}
