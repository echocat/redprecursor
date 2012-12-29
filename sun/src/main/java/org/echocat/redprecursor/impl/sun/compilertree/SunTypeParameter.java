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

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.TypeParameter;
import org.echocat.redprecursor.compilertree.base.Expression;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunTypeParameter implements TypeParameter, SunNode {

    private final JCTypeParameter _jc;
    private final SunNodeConverter _converter;

    public SunTypeParameter(JCTypeParameter jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public String getName() {
        return _converter.nameToString(_jc.name);
    }

    @Override
    public void setName(String name) {
        _jc.name = _converter.stringToName(name);
    }

    @Override
    public List<? extends SunExpression> getBounds() {
        return _converter.jcsToNodes(_jc.bounds, SunExpression.class);
    }

    @Override
    public void setBounds(List<Expression> bounds) {
        _jc.bounds = _converter.nodesToJcs(bounds, SunExpression.class, JCExpression.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getBounds());
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
    public JCTypeParameter getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
