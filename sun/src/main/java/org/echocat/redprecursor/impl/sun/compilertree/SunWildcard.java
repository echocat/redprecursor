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
import com.sun.tools.javac.tree.JCTree.JCWildcard;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Wildcard;
import org.echocat.redprecursor.compilertree.WildcardKind;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunWildcard implements SunIdentifier, Wildcard {

    private final JCWildcard _jc;
    private final SunNodeConverter _converter;

    public SunWildcard(JCWildcard jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public void setKind(WildcardKind kind) {
        _jc.kind = kind != null ? _converter.wildcardKindToTypeBoundKind(kind) : null;
    }

    @Override
    public WildcardKind getKind() {
        return _jc.kind != null ? _converter.typeBoundKindToWildcardKind(_jc.kind) : null;
    }

    @Override
    public String getStringRepresentation() {
        return _jc.toString();
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.inner, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.inner = expression != null ? _converter.nodeToJc(expression, SunExpression.class, JCExpression.class) : null;
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return IterableBuilder.<SunNode>toIterable(getExpression());
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
    public JCWildcard getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
