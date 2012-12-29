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

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCImport;
import org.echocat.redprecursor.compilertree.Import;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Node;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunImport implements Import, SunDeclaration {

    private final JCImport _jc;
    private final SunNodeConverter _converter;

    public SunImport(JCImport jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public boolean isStatic() {
        return _jc.staticImport;
    }

    @Override
    public void setStatic(boolean isStatic) {
        _jc.staticImport = isStatic;
    }

    @Override
    public SunNode getIdentifier() {
        return _converter.jcToNode(_jc.qualid, SunNode.class);
    }

    @Override
    public void setIdentifier(Node node) {
        _jc.qualid = _converter.nodeToJc(requireNonNull("node", node), SunNode.class, JCTree.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getIdentifier());
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
    public JCImport getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
