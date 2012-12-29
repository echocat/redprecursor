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

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import org.echocat.redprecursor.compilertree.Annotation;
import org.echocat.redprecursor.compilertree.Modifier;
import org.echocat.redprecursor.compilertree.Modifiers;
import org.echocat.redprecursor.compilertree.Position;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunModifiers implements Modifiers, SunNode {

    private final JCModifiers _jc;
    private final SunNodeConverter _converter;

    public SunModifiers(JCModifiers jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<Modifier> getModifier() {
        return SunModifierUtil.getModifiers(_jc.flags);
    }

    @Override
    public void setModifiers(List<Modifier> modifiers) {
        _jc.flags = SunModifierUtil.getValue(requireNonNull("modifiers", modifiers));
    }

    @Override
    public List<? extends SunAnnotation> getAnnotations() {
        return _converter.jcsToNodes(_jc.annotations, SunAnnotation.class);
    }

    @Override
    public void setAnnotations(List<Annotation> annotations) {
        _jc.annotations = _converter.nodesToJcs(annotations, SunAnnotation.class, JCAnnotation.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getAnnotations());
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
    public JCModifiers getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
