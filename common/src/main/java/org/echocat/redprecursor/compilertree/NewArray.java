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
