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
import org.echocat.redprecursor.compilertree.MethodDeclaration;

import javax.annotation.Nonnull;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class MethodStatement extends InClassStatement {

    private final MethodDeclaration _method;

    public MethodStatement(@Nonnull CompilationUnit compilationUnit, @Nonnull MethodDeclaration method, @Nonnull ClassDeclaration... declarations) {
        super(compilationUnit, declarations);
        _method = requireNonNull("method", method);
    }

    @Nonnull
    public MethodDeclaration getMethod() {
        return _method;
    }
}
