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

import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Primitive;
import org.echocat.redprecursor.compilertree.PrimitiveType;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunPrimitiveType implements PrimitiveType, SunIdentifier {

    private final JCPrimitiveTypeTree _jc;
    private final SunNodeConverter _converter;

    public SunPrimitiveType(JCPrimitiveTypeTree jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable();
    }

    @Override
    public Primitive getPrimitive() {
        return SunPrimitiveUtil.codeToPrimitive(_jc.typetag);
    }

    @Override
    public JCPrimitiveTypeTree getJc() {
        return _jc;
    }

    @Override
    public Position getPosition() {
        return _converter.jcPositionToPosition(_jc.pos);
    }

    @Override
    public void setPosition(Position position) {
        _jc.pos = _converter.positionToJcPosition(position);
    }

    @Override
    public String getStringRepresentation() {
        return getPrimitive().toString();
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
