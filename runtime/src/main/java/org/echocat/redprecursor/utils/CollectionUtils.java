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
