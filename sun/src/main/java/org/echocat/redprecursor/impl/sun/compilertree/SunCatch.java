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

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import org.echocat.redprecursor.compilertree.Catch;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.VariableDeclaration;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunCatch implements Catch, SunNode {

    private final JCCatch _jc;
    private final SunNodeConverter _converter;

    public SunCatch(JCCatch jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunStatement> getBody() {
        return _converter.jcBlockToStatements(_jc.body);
    }

    @Override
    public void setBody(List<Statement> body) {
        _jc.body = _converter.statementsToJcBlock(body, JCBlock.class);
    }

    @Override
    public SunVariableDeclaration getParameterDeclaration() {
        return _converter.jcToNode(_jc.param, SunVariableDeclaration.class);
    }

    @Override
    public void setParameterDeclaration(VariableDeclaration parameterDeclaration) {
        final SunVariableDeclaration sunVariableDeclaration = castNonNullParameterTo("parameterDeclaration", parameterDeclaration, SunVariableDeclaration.class);
        _jc.param = sunVariableDeclaration.getJc();
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getBody()).append(getParameterDeclaration());
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
    public JCCatch getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
