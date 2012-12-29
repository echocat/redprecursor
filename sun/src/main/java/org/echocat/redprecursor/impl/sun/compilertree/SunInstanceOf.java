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
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCInstanceOf;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.InstanceOf;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunInstanceOf implements InstanceOf, SunExpression {

    private final JCInstanceOf _jc;
    private final SunNodeConverter _converter;

    public SunInstanceOf(JCInstanceOf jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.expr, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        requireNonNull("expression", expression);
        _jc.expr = _converter.nodeToJc(expression, SunExpression.class, JCExpression.class);
    }

    @Override
    public SunIdentifier getType() {
        return _converter.jcToNode(_jc.clazz, SunIdentifier.class);
    }

    @Override
    public void setType(Identifier type) {
        requireNonNull("type", type);
        _jc.clazz = _converter.nodeToJc(type, SunIdentifier.class, JCTree.class);
    }

    @Override
    public JCInstanceOf getJc() {
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
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getExpression(), getType());
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
