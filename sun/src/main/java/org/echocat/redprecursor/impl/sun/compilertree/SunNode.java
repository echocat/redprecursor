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
import org.echocat.redprecursor.compilertree.base.Node;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface SunNode extends Node {

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes();

    public JCTree getJc();


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.PACKAGE })
    public static @interface Implementations {
        public Class<? extends SunNode>[] value();
    }
}
