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
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Declaration;

import javax.annotation.Nullable;
import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunClassDeclaration implements ClassDeclaration, SunDeclaration {

    private final JCClassDecl _jc;
    private final SunNodeConverter _converter;

    public SunClassDeclaration(JCClassDecl jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunIdentifier getExtends() {
        return _converter.jcToNode(_jc.extending, SunIdentifier.class);
    }

    @Override
    public void setExtends(Identifier extendingType) {
        final SunIdentifier sunExtendingType = castNonNullParameterTo("extendingType", extendingType, SunIdentifier.class);
        _jc.extending = sunExtendingType.getJc();
    }

    @Override
    public List<? extends SunIdentifier> getImplements() {
        return _converter.jcsToNodes(_jc.implementing, SunIdentifier.class);
    }

    @Override
    public void setImplements(List<Identifier> implementingTypes) {
        _jc.implementing = _converter.nodesToJcs(implementingTypes, SunIdentifier.class, JCExpression.class);
    }

    @Override
    public List<? extends SunDeclaration> getDeclarations() {
        return _converter.jcsToNodes(_jc.defs, SunDeclaration.class);
    }

    @Override
    public void setDeclarations(List<Declaration> declarations) {
        _jc.defs = _converter.nodesToJcs(declarations, SunDeclaration.class, JCTree.class);
    }

    @Override
    public SunModifiers getModifiers() {
        return _converter.jcToNode(_jc.mods, SunModifiers.class);
    }

    @Override
    public void setModifiers(Modifiers modifiers) {
        final SunModifiers sunModifiers = castNonNullParameterTo("modifiers", modifiers, SunModifiers.class);
        _jc.mods = sunModifiers.getJc();
    }

    @Override
    public String getName() {
        return _jc.name.toString();
    }

    @Override
    public void setName(String name) {
        _jc.name = _converter.stringToName(requireNonNull("name", name));
    }

    @Override
    public List<? extends SunTypeParameter> getTypeParameters() {
        return _converter.jcsToNodes(_jc.typarams, SunTypeParameter.class);
    }

    @Override
    public void setTypeParameters(List<TypeParameter> typeParameters) {
        _jc.typarams = _converter.nodesToJcs(typeParameters, SunTypeParameter.class, JCTypeParameter.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getExtends(), getModifiers()).append(getImplements(), getDeclarations(), getTypeParameters());
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
    @Nullable
    public String getFullName() {
        return _jc.sym != null ? _converter.nameToString(_jc.sym.fullname) : null;
    }

    @Override
    public JCClassDecl getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
