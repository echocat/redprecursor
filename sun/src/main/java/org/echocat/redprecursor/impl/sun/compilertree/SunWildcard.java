/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is echocat redprecursor.
 *
 * The Initial Developer of the Original Code is Gregor Noczinski.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
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
