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
import org.echocat.redprecursor.compilertree.MethodDeclaration;
import org.echocat.redprecursor.compilertree.VariableDeclaration;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class MethodParameterStatement extends MethodStatement {

    private final VariableDeclaration _parameter;
    private final int _parameterIndex;

    public MethodParameterStatement(@Nonnull CompilationUnit compilationUnit, @Nonnull ClassDeclaration[] classDeclarations, @Nonnull MethodDeclaration method, @Nonnull VariableDeclaration parameter, @Nonnegative int parameterIndex) {
        super(compilationUnit, method, classDeclarations);
        _parameter = requireNonNull("variableDeclaration", parameter);
        _parameterIndex = parameterIndex;
    }

    public MethodParameterStatement(@Nonnull MethodStatement methodStatement, @Nonnull VariableDeclaration parameter, @Nonnegative int parameterIndex) {
        this(methodStatement.getCompilationUnit(), methodStatement.getDeclarations(), methodStatement.getMethod(), parameter, parameterIndex);
    }

    @Nonnull
    public VariableDeclaration getParameter() {
        return _parameter;
    }

    @Nonnegative
    public int getParameterIndex() {
        return _parameterIndex;
    }
}
