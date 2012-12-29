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

public interface NewClass extends CreateInstances, TypeEnabledNode, UsingTypeParameterEnabledNode, ArgumentsEnabledNode {

    public Expression getEnclosing();

    public void setEnclosing(Expression enclosing);

    public ClassDeclaration getClassDeclaration();

    public void setClassDeclaration(ClassDeclaration classDeclaration);

}
