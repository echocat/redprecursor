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

package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree;

public abstract class SunNodeInformation<S extends SunNode, J extends JCTree> {

    private final Class<S> _nodeType;
    private final Class<J> _jcType;

    protected SunNodeInformation(Class<S> nodeType, Class<J> jcType) {
        _nodeType = nodeType;
        _jcType = jcType;
    }

    public abstract S newInstance(J jcType, SunNodeConverter converter);

    public Class<S> getNodeType() {
        return _nodeType;
    }

    public Class<J> getJcType() {
        return _jcType;
    }
}
