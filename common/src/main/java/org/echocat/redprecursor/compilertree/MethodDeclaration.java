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

import org.echocat.redprecursor.compilertree.base.*;

import java.util.List;

public interface MethodDeclaration extends Declaration, BodyNode<Statement>, NameEnabledNode, ModifiersEnabledNode, TypeParameterEnabledNode {

    public Identifier getResultType();

    public void setResultType(Expression type);

    public List<? extends Identifier> getThrows();

    public void setThrows(List<Identifier> thrownTypes);

    public Expression getDefaultValue();

    public void setDefaultValue(Expression expression);

    public List<? extends VariableDeclaration> getParameterDeclarations();

    public void setParameterDeclarations(List<VariableDeclaration> parameterDeclarations);

}
