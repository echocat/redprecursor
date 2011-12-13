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
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
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

    public MethodStatement(@Nonnull CompilationUnit compilationUnit, @Nonnull ClassDeclaration topClass, @Nonnull ClassDeclaration currentClass, @Nonnull MethodDeclaration method) {
        super(compilationUnit, topClass, currentClass);
        _method = requireNonNull("method", method);
    }

    @Nonnull
    public MethodDeclaration getMethod() {
        return _method;
    }
}
