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
import com.sun.tools.javac.tree.JCTree.JCUnary;
import org.echocat.redprecursor.compilertree.Operator;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Unary;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunUnary implements Unary, SunExpression {

    private JCUnary _jc;
    private final SunNodeConverter _converter;

    public SunUnary(JCUnary jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public Operator getOperator() {
        return SunOperatorUtil.codeToOperator(_jc.getTag());
    }

    @Override
    public void setOperator(Operator operator) {
        final int pos = _jc.pos;
        _jc = _converter.getTreeMaker().Unary(SunOperatorUtil.operatorToCode(operator), _jc.arg);
        _jc.pos = pos;
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.arg, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.arg = _converter.nodeToJc(requireNonNull("expression", expression), SunExpression.class, JCExpression.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getExpression());
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
    public JCUnary getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
