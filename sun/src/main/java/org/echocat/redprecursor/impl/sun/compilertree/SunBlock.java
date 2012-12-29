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
import com.sun.tools.javac.tree.JCTree.JCStatement;
import org.echocat.redprecursor.compilertree.Block;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunBlock implements Block, SunStatement, SunDeclaration {

    private final JCBlock _jc;
    private final SunNodeConverter _converter;

    public SunBlock(JCBlock jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunStatement> getBody() {
        return _converter.jcsToNodes(_jc.stats, SunStatement.class);
    }

    @Override
    public void setBody(List<Statement> body) {
        _jc.stats = _converter.nodesToJcs(body, SunStatement.class, JCStatement.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        //noinspection unchecked
        return IterableBuilder.<SunNode>toIterable(getBody());
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
    public JCBlock getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
