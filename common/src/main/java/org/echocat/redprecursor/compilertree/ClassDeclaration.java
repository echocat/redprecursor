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

import javax.annotation.Nullable;
import java.util.List;

public interface ClassDeclaration extends Statement, Declaration, NameEnabledNode, ModifiersEnabledNode, DefinitionsEnabledNode, TypeParameterEnabledNode {

    public Identifier getExtends();

    public void setExtends(Identifier extendingType);

    public List<? extends Identifier> getImplements();

    public void setImplements(List<Identifier> implementingTypes);

    @Nullable
    public String getFullName();

}
