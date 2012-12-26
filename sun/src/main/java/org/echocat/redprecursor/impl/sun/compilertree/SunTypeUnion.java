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
 * Portions created by the Initial Developer are Copyright (C) 2012
 * the Initial Developer. All Rights Reserved.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.TypeUnion;
import org.echocat.redprecursor.compilertree.base.Expression;

import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunTypeUnion implements SunIdentifier, TypeUnion {

    private final JCTypeUnion _jc;
    private final SunNodeConverter _converter;

    public SunTypeUnion(JCTypeUnion jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public String getStringRepresentation() {
        return _jc.toString();
    }

    @Override
    public List<? extends SunExpression> getValues() {
        return _jc.alternatives != null ? _converter.jcsToNodes(_jc.alternatives, SunExpression.class) : null;
    }

    @Override
    public void setValues(List<Expression> values) {
        _jc.alternatives = values != null ? _converter.nodesToJcs(values, SunExpression.class, JCExpression.class) : null;
    }

    @Override
    public Iterable<? extends SunExpression> getAllEnclosedNodes() {
        // noinspection unchecked
        return IterableBuilder.toIterable(getValues());
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
    public JCTypeUnion getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
