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
