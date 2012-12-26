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

import com.sun.tools.javac.tree.JCTree.JCAssignOp;
import org.echocat.redprecursor.compilertree.AssignWithOperation;
import org.echocat.redprecursor.compilertree.Operator;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunAssignWithOperation implements AssignWithOperation, SunExpression {

    private JCAssignOp _jc;
    private final SunNodeConverter _converter;

    public SunAssignWithOperation(JCAssignOp jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.lhs, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        _jc.lhs = castNonNullParameterTo("expression", expression, SunExpression.class).getJc();
    }

    @Override
    public SunExpression getAssignTo() {
        return _converter.jcToNode(_jc.rhs, SunExpression.class);
    }

    @Override
    public void setAssignTo(Expression expression) {
        _jc.rhs = castNonNullParameterTo("expression", expression, SunExpression.class).getJc();
    }

    @Override
    public Operator getOperator() {
        return SunOperatorUtil.codeToOperator(_jc.getTag());
    }

    @Override
    public void setOperator(Operator operation) {
        requireNonNull("operation", operation);
        final int pos = _jc.pos;
        _jc = _converter.getTreeMaker().Assignop(SunOperatorUtil.operatorToCode(operation), _jc.lhs, _jc.rhs);
        _jc.pos = pos;
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getExpression(), getAssignTo());
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
    public JCAssignOp getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
