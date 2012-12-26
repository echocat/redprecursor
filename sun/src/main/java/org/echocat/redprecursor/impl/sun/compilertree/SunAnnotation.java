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
