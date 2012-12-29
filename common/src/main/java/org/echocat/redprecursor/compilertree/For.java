/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat RedPrecursor, Copyright (c) 2011-2012 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

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
