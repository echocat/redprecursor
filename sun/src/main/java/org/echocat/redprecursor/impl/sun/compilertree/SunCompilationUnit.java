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
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import org.echocat.redprecursor.compilertree.Annotation;
import org.echocat.redprecursor.compilertree.CompilationUnit;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Declaration;

import javax.tools.JavaFileObject;
import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunCompilationUnit implements CompilationUnit, SunNode {

    private final JCCompilationUnit _jc;
    private final SunNodeConverter _converter;

    public SunCompilationUnit(JCCompilationUnit jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunAnnotation> getPackageAnnotations() {
        return _converter.jcsToNodes(_jc.packageAnnotations, SunAnnotation.class);
    }

    @Override
    public void setPackageAnnotations(List<Annotation> packageAnnotations) {
        _jc.packageAnnotations = _converter.nodesToJcs(packageAnnotations, SunAnnotation.class, JCAnnotation.class);
    }

    @Override
    public JavaFileObject getSourceFile() {
        return _jc.sourcefile;
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
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getDeclarations(), getPackageAnnotations());
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
    public JCCompilationUnit getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
