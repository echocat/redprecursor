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

import com.sun.tools.javac.tree.JCTree.JCBinary;
import org.echocat.redprecursor.compilertree.BinaryCondition;
import org.echocat.redprecursor.compilertree.Operator;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunBinaryCondition implements BinaryCondition, SunExpression {

    private JCBinary _jc;
    private final SunNodeConverter _converter;

    public SunBinaryCondition(JCBinary jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getLeft() {
        return _converter.jcToNode(_jc.lhs, SunExpression.class);
    }

    @Override
    public void setLeft(Expression left) {
        _jc.lhs = castNonNullParameterTo("left", left, SunExpression.class).getJc();
    }

    @Override
    public SunExpression getRight() {
        return _converter.jcToNode(_jc.rhs, SunExpression.class);
    }

    @Override
    public void setRight(Expression right) {
        _jc.rhs = castNonNullParameterTo("right", right, SunExpression.class).getJc();
    }

    @Override
    public Operator getOperator() {
        return SunOperatorUtil.codeToOperator(_jc.getTag());
    }

    @Override
    public void setOperator(Operator operation) {
        requireNonNull("operation", operation);
        final int pos = _jc.pos;
        _jc = _converter.getTreeMaker().Binary(SunOperatorUtil.operatorToCode(operation), _jc.lhs, _jc.rhs);
        _jc.pos = pos;
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getLeft(), getRight());
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
    public JCBinary getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
