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
