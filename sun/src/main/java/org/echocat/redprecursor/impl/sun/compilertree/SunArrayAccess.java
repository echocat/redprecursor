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

import com.sun.tools.javac.tree.JCTree.JCArrayAccess;
import org.echocat.redprecursor.compilertree.ArrayAccess;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunArrayAccess implements ArrayAccess, SunIdentifier {

    private final JCArrayAccess _jc;
    private final SunNodeConverter _converter;

    public SunArrayAccess(JCArrayAccess jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.indexed, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.index = castNonNullParameterTo("expression", expression, SunExpression.class).getJc();
    }

    @Override
    public SunExpression getIndex() {
        return _converter.jcToNode(_jc.index, SunExpression.class);
    }

    @Override
    public void setIndex(Expression index) {
        _jc.index = castNonNullParameterTo("index", index, SunExpression.class).getJc();
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getExpression(), getIndex());
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
    public JCArrayAccess getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }

    @Override
    public String getStringRepresentation() {
        return _jc.toString();
    }
}
