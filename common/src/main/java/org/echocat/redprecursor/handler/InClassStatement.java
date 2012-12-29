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
