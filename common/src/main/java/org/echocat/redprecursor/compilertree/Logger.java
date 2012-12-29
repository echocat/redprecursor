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

import org.echocat.redprecursor.compilertree.base.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public interface Logger {

    @Nonnull
    public Locale getLocale();

    public void info(@Nonnull Node node, @Nonnull String message, @Nullable Throwable t);

    public void info(@Nonnull Node node, @Nonnull String message);

    public void info(@Nonnull Node node, @Nonnull Throwable t);

    public void warn(@Nonnull Node node, @Nonnull String message, @Nullable Throwable t);

    public void warn(@Nonnull Node node, @Nonnull String message);

    public void warn(@Nonnull Node node, @Nonnull Throwable t);

    public void error(@Nonnull Node node, @Nonnull String message, @Nullable Throwable t);

    public void error(@Nonnull Node node, @Nonnull String message);

    public void error(@Nonnull Node node, @Nonnull Throwable t);
}
