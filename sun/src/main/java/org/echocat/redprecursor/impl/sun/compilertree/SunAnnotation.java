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

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import org.echocat.redprecursor.compilertree.Annotation;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import java.util.Map;

import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunAnnotation implements Annotation, SunExpression {

    private final JCAnnotation _jc;
    private final SunNodeConverter _converter;

    public SunAnnotation(JCAnnotation jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public Map<String, ? extends SunExpression> getArguments() {
        return _converter.jcArgumentExpressionsToExpressionMap(_jc.args);
    }

    @Override
    public void setArguments(Map<String, ? extends Expression> arguments) {
        _jc.args = _converter.expressionMapToJcParameterExpressions(arguments);
    }

    @Override
    public SunIdentifier getType() {
        return _converter.jcToNode(_jc.annotationType, SunIdentifier.class);
    }

    @Override
    public void setType(Identifier type) {
        final SunIdentifier sunType = castNonNullParameterTo("type", type, SunIdentifier.class);
        _jc.annotationType = sunType.getJc();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return IterableBuilder.<SunNode>toIterable(getType()).append(getArguments().values());
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
    public JCAnnotation getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
