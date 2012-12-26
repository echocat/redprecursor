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

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.TypeApply;
import org.echocat.redprecursor.compilertree.base.Expression;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunTypeApply implements TypeApply, SunIdentifier {

    private final JCTypeApply _jc;
    private final SunNodeConverter _converter;

    public SunTypeApply(JCTypeApply jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public String getStringRepresentation() {
        if (!(_jc.clazz instanceof JCIdent)) {
            throw new IllegalArgumentException("Only an _jc.class of type " + JCIdent.class.getName() + " is supported but got " + _jc.clazz + ".");
        }
        final JCIdent jc = (JCIdent) _jc.clazz;
        final Symbol symbol = jc.sym != null ? jc.sym : _converter.nameToType(jc.name);
        return symbol != null ? symbol.toString() : _converter.nameToString(jc.name);
    }

    @Override
    public SunExpression getRaw() {
        return _converter.jcToNode(_jc.clazz, SunExpression.class);
    }

    public void setRaw(Expression expression) {
        _jc.clazz = _converter.nodeToJc(requireNonNull("expression", expression), SunExpression.class, JCExpression.class);
    }

    @Override
    public List<? extends SunExpression> getTypeParameters() {
        return _converter.jcsToNodes(_jc.arguments, SunExpression.class);
    }

    @Override
    public void setTypeParameters(List<Expression> typeParameters) {
        _jc.arguments = _converter.nodesToJcs(typeParameters, SunExpression.class, JCExpression.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        // noinspection unchecked
        return toIterable(getRaw()).append(getTypeParameters());
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
    public JCTypeApply getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
