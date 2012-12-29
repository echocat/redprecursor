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

import com.sun.tools.javac.tree.JCTree.JCCase;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCSwitch;
import org.echocat.redprecursor.compilertree.Case;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Switch;
import org.echocat.redprecursor.compilertree.base.Expression;

import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunSwitch implements Switch, SunStatement {

    private final JCSwitch _jc;
    private final SunNodeConverter _converter;

    public SunSwitch(JCSwitch jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.selector, SunExpression.class);
    }

    @Override
    public void setExpression(Expression value) {
        _jc.selector = _converter.nodeToJc(requireNonNull("value", value), SunExpression.class, JCExpression.class);
    }

    @Override
    public List<? extends SunCase> getCases() {
        return _converter.jcsToNodes(_jc.cases, SunCase.class);
    }

    @Override
    public void setCases(List<Case> cases) {
        _jc.cases = _converter.nodesToJcs(cases, SunCase.class, JCCase.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return IterableBuilder.<SunNode>toIterable(getExpression()).append(getCases());
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
    public JCSwitch getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
