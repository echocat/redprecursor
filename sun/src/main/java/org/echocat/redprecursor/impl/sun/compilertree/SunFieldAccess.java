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
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import org.echocat.redprecursor.compilertree.FieldAccess;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunFieldAccess implements FieldAccess, SunIdentifier {

    private final JCFieldAccess _jc;
    private final SunNodeConverter _converter;

    public SunFieldAccess(JCFieldAccess jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public String getFieldName() {
        return _converter.nameToString(_jc.name);
    }

    @Override
    public void setFieldName(String fieldName) {
        _jc.name = _converter.stringToName(requireNonNull("fieldName", fieldName));
    }

    @Override
    public SunExpression getContaining() {
        return _converter.jcToNode(_jc.selected, SunExpression.class);
    }

    @Override
    public void setContaining(Expression containing) {
        _jc.selected = _converter.nodeToJc(requireNonNull("containing", containing), SunExpression.class, JCExpression.class);
    }

    @Override
    public JCFieldAccess getJc() {
        return _jc;
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getContaining());
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
        return _jc.toString();
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
