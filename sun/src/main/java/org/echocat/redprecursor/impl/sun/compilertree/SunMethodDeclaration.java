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

import com.sun.tools.javac.tree.JCTree.*;
import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunMethodDeclaration implements MethodDeclaration, SunDeclaration {

    private final JCMethodDecl _jc;
    private final SunNodeConverter _converter;

    public SunMethodDeclaration(JCMethodDecl jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunIdentifier getResultType() {
        return _converter.jcToNode(_jc.restype, SunIdentifier.class);
    }

    @Override
    public void setResultType(Expression type) {
        _jc.restype = _converter.nodeToJc(requireNonNull("type", type), SunKey.class, JCExpression.class);
    }

    @Override
    public List<? extends SunIdentifier> getThrows() {
        return _converter.jcsToNodes(_jc.thrown, SunIdentifier.class);
    }

    @Override
    public void setThrows(List<Identifier> thrownTypes) {
        _jc.thrown = _converter.nodesToJcs(thrownTypes, SunIdentifier.class, JCExpression.class);
    }

    @Override
    public SunExpression getDefaultValue() {
        return _converter.jcToNode(_jc.defaultValue, SunExpression.class);
    }

    @Override
    public void setDefaultValue(Expression expression) {
        _jc.defaultValue = _converter.nodeToJc(expression, SunExpression.class, JCExpression.class);
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
    public SunModifiers getModifiers() {
        return _converter.jcToNode(_jc.mods, SunModifiers.class);
    }

    @Override
    public void setModifiers(Modifiers modifiers) {
        _jc.mods = _converter.nodeToJc(requireNonNull("modifiers", modifiers), SunModifiers.class, JCModifiers.class);
    }

    @Override
    public String getName() {
        return _converter.nameToString(_jc.name);
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
    public List<? extends SunVariableDeclaration> getParameterDeclarations() {
        return _converter.jcsToNodes(_jc.params, SunVariableDeclaration.class);
    }

    @Override
    public void setParameterDeclarations(List<VariableDeclaration> parameterDeclarations) {
        _jc.params = _converter.nodesToJcs(parameterDeclarations, SunVariableDeclaration.class, JCVariableDecl.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getResultType(), getDefaultValue(), getModifiers()).append(getThrows(), getBody(), getTypeParameters(), getParameterDeclarations());
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
    public JCMethodDecl getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
