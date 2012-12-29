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

import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import org.echocat.redprecursor.compilertree.ForEach;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.VariableDeclaration;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Statement;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunForEach implements ForEach, SunStatement {

    private final JCEnhancedForLoop _jc;
    private final SunNodeConverter _converter;

    public SunForEach(JCEnhancedForLoop jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunVariableDeclaration getParameterDeclaration() {
        return _converter.jcToNode(_jc.var, SunVariableDeclaration.class);
    }

    @Override
    public void setParameterDeclaration(VariableDeclaration parameterDeclaration) {
        requireNonNull("parameterDeclaration", parameterDeclaration);
        _jc.var = _converter.nodeToJc(parameterDeclaration, SunVariableDeclaration.class, JCVariableDecl.class);
    }

    @Override
    public SunStatement getBody() {
        return _converter.jcToNode(_jc.body, SunStatement.class);
    }

    @Override
    public void setBody(Statement body) {
        _jc.body = _converter.nodeToJc(body, SunStatement.class, JCStatement.class);
    }

    @Override
    public SunExpression getExpression() {
        return _converter.jcToNode(_jc.expr, SunExpression.class);
    }

    @Override
    public void setExpression(Expression expression) {
        requireNonNull("expression", expression);
        _jc.expr = _converter.nodeToJc(expression, SunExpression.class, JCExpression.class);
    }

    @Override
    public JCEnhancedForLoop getJc() {
        return _jc;
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
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getParameterDeclaration(), getBody(), getExpression());
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
