/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is echocat redprecursor.
 *
 * The Initial Developer of the Original Code is Gregor Noczinski.
 * Portions created by the Initial Developer are Copyright (C) 2012
 * the Initial Developer. All Rights Reserved.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.redprecursor.handler;

import org.echocat.redprecursor.compilertree.ClassDeclaration;
import org.echocat.redprecursor.compilertree.CompilationUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.System.arraycopy;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonEmpty;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class InClassStatement {

    private final CompilationUnit _compilationUnit;
    private final ClassDeclaration[] _declarations;

    public InClassStatement(@Nonnull CompilationUnit compilationUnit, @Nonnull ClassDeclaration... declarations) {
        _compilationUnit = requireNonNull("compilationUnit", compilationUnit);
        _declarations = requireNonEmpty("declarations", declarations);
    }

    @Nonnull
    public CompilationUnit getCompilationUnit() {
        return _compilationUnit;
    }

    @Nonnull
    public ClassDeclaration[] getDeclarations() {
        return _declarations;
    }

    @Nonnull
    public ClassDeclaration getTopClass() {
        return _declarations[0];
    }

    @Nonnull
    public ClassDeclaration getCurrentClass() {
        return _declarations[_declarations.length - 1];
    }

    @Nonnull
    public ClassDeclaration[] getDeclarationsWith(@Nonnull ClassDeclaration next) {
        return getDeclarationsWith(_declarations, next);
    }

    @Nonnull
    public static ClassDeclaration[] getDeclarationsWith(@Nullable ClassDeclaration[] base, @Nonnull ClassDeclaration next) {
        final ClassDeclaration[] declarations = new ClassDeclaration[base != null ? base.length + 1 : 1];
        if (base != null) {
            arraycopy(base, 0, declarations, 0, base.length);
        }
        declarations[declarations.length - 1] = next;
        return declarations;
    }
}
