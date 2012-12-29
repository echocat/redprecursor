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
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.Iterator;
import java.util.ServiceLoader;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class CompilerTreeConnectorFactory {

    private ClassLoader _classLoader;

    public CompilerTreeConnectorFactory() {
        this(null);
    }

    public CompilerTreeConnectorFactory(@Nullable ClassLoader classLoader) {
        _classLoader = classLoader != null ? classLoader : CompilerTreeConnectorFactory.class.getClassLoader();
    }

    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    public void setClassLoader(@Nonnull ClassLoader classLoader) {
        _classLoader = classLoader;
    }

    @Nonnull
    public CompilerTreeConnector getBy(@Nonnull ProcessingEnvironment processingEnvironment) {
        requireNonNull("processingEnvironment", processingEnvironment);
        final Iterator<CompilerTreeConnector> i = ServiceLoader.load(CompilerTreeConnector.class, _classLoader).iterator();
        CompilerTreeConnector result = null;
        while (i.hasNext() && result == null) {
            final CompilerTreeConnector potentialConnector = i.next();
            if (potentialConnector.canHandle(processingEnvironment)) {
                result = potentialConnector;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Could not find any implemenation of '" + CompilerTreeConnector.class.getName() + "' for " + processingEnvironment + ".");
        }
        result.init(processingEnvironment);
        return result;
    }
}
