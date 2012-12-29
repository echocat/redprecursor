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
