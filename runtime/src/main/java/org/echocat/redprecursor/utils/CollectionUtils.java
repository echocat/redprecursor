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

package org.echocat.redprecursor.utils;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class CollectionUtils {

    private static final Object VOID = new Object();

    @Nonnull
    public static <K, V> Map<K, V> asMap(Object... keyAndValues) throws IllegalArgumentException {
        final Map<K, V> result = new LinkedHashMap<K, V>();
        Object lastKey = VOID;
        if (keyAndValues != null) {
            for (Object keyOrValue : keyAndValues) {
                //noinspection ObjectEquality
                if (lastKey == VOID) {
                    lastKey = keyOrValue;
                } else{
                    // noinspection unchecked
                    result.put((K)lastKey, (V)keyOrValue);
                    lastKey = VOID;
                }
            }
        }
        //noinspection ObjectEquality
        if (lastKey != VOID) {
            throw new IllegalArgumentException("There is no value for key: " + lastKey);
        }
        return result;
    }

    private CollectionUtils() {}
}
