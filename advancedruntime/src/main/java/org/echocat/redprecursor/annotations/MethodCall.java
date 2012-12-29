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

package org.echocat.redprecursor.annotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;

public interface MethodCall<T> {

    public static interface Parameter {

        @Nonnull public String getName();

        @Nullable public Object getValue();
    }

    public static interface Parameters extends Iterable<Parameter> {

        @Nonnull public Parameter getParameter(@Nonnull String name) throws NoSuchElementException;

        public boolean hasParameter(@Nonnull String name);
    }

    @Nonnull public Class<T> getType();

    @Nullable public T getThis();

    @Nonnull public String getMethodName();

    @Nonnull public Parameters getParameters();
}
