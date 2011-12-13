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
