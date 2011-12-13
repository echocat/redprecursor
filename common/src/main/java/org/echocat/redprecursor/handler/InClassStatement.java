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

import javax.annotation.Nonnull;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class InClassStatement {

    private final CompilationUnit _compilationUnit;
    private final ClassDeclaration _topClass;
    private final ClassDeclaration _currentClass;

    public InClassStatement(@Nonnull CompilationUnit compilationUnit, @Nonnull ClassDeclaration topClass, @Nonnull ClassDeclaration currentClass) {
        _compilationUnit = requireNonNull("compilationUnit", compilationUnit);
        _topClass = requireNonNull("topClass", topClass);
        _currentClass = requireNonNull("currentClass", currentClass);
    }

    @Nonnull
    public CompilationUnit getCompilationUnit() {
        return _compilationUnit;
    }

    @Nonnull
    public ClassDeclaration getTopClass() {
        return _topClass;
    }

    @Nonnull
    public ClassDeclaration getCurrentClass() {
        return _currentClass;
    }
}
