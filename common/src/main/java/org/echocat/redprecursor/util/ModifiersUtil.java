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

package org.echocat.redprecursor.util;

import org.echocat.redprecursor.compilertree.Modifier;
import org.echocat.redprecursor.compilertree.Modifiers;
import org.echocat.redprecursor.compilertree.base.ModifiersEnabledNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class ModifiersUtil {

    public static boolean hasModifier(@Nullable ModifiersEnabledNode node, @Nonnull Modifier modifier) {
        requireNonNull("modifier", modifier);
        final boolean result;
        final Modifiers modifiers = node != null ? node.getModifiers() : null;
        if (modifiers != null) {
            final List<Modifier> allModifier = modifiers.getModifier();
            result = allModifier != null && allModifier.contains(modifier);
        } else {
            result = false;
        }
        return result;
    }

    private ModifiersUtil() {}

}
