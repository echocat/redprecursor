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

package org.echocat.redprecursor.compilertree;

import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

public interface CompilerTreeConnector {

    public boolean canHandle(@Nonnull ProcessingEnvironment processingEnvironment);

    public void init(@Nonnull ProcessingEnvironment processingEnvironment);

    @Nonnull
    public Iterable<? extends CompilationUnit> toCompilationUnits(@Nonnull RoundEnvironment roundEnvironment);

    @Nonnull
    public NodeFactory getNodeFactoryFor(@Nonnull CompilationUnit compilationUnit);

    @Nonnull
    public Logger getLogger(@Nonnull CompilationUnit compilationUnit);
}
