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

import com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import org.echocat.redprecursor.compilertree.Labelled;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunLabelled implements Labelled, SunStatement {

    private final JCLabeledStatement _jc;
    private final SunNodeConverter _converter;

    public SunLabelled(JCLabeledStatement jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunStatement> getBody() {
        return _converter.jcBlockToStatements(_jc.body);
    }

    @Override
    public void setBody(List<Statement> body) {
        _jc.body = _converter.statementsToJcBlock(body, JCStatement.class);
    }

    @Override
    public String getName() {
        return _converter.nameToString(_jc.label);
    }

    @Override
    public void setName(String name) {
        _jc.label = _converter.stringToName(requireNonNull("name", name));
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getBody());
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
    public JCLabeledStatement getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
