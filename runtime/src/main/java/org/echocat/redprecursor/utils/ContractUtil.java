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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ContractUtil {

    public static <T> T requireNonNull(String parameterName, T value) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " is null");
        }
        return value;
    }

    public static <T extends Collection<?>> T requireNonEmpty(String parameterName, T value) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " is null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException(parameterName + " is empty");
        }
        return value;
    }

    public static <T> T[] requireNonEmpty(String parameterName, T[] value) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " is null");
        }
        if (value.length == 0) {
            throw new IllegalArgumentException(parameterName + " is empty");
        }
        return value;
    }

    public static String requireNonEmpty(String parameterName, String value) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " is null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException(parameterName + " is empty");
        }
        return value;
    }

    public static <T, R extends T> R castNonNullParameterTo(String parameterName, T value, Class<R> requiredType) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " is null");
        }
        if (!requiredType.isInstance(value)) {
            throw new IllegalArgumentException(parameterName + " is not of type " + requiredType.getName() + ".");
        }
        return requiredType.cast(value);
    }

    public static <T, R extends T> R castNullableParameterTo(String parameterName, T value, Class<R> requiredType) {
        final R result;
        if (value != null) {
            if (!requiredType.isInstance(value)) {
                throw new IllegalArgumentException(parameterName + " is not of type " + requiredType.getName() + ".");
            } else {
                result = requiredType.cast(value);
            }
        } else {
            result = null;
        }
        return result;
    }

    public static <KT, KR extends KT, VT, VR extends VT>  Map<KR, VR> castNonNullMapParameterTo(String parameterName, Map<KT, VT> value, Class<KR> requiredKeyType, Class<VR> requiredValueType) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " is null");
        }
        for (Entry<KT, VT> entry : value.entrySet()) {
            final KT entryKey = entry.getKey();
            final VT entryValue = entry.getValue();
            if (entryKey == null) {
                throw new IllegalArgumentException("The key of entry " + entry + " of " + parameterName + " is null.");
            }
            if (entryValue == null) {
                throw new IllegalArgumentException("The value of entry " + entry + " of " + parameterName + " is null.");
            }
            if (!requiredKeyType.isInstance(entryKey)) {
                throw new IllegalArgumentException("The key of entry " + entry + " of " + parameterName + " is not of type " + requiredKeyType.getName() + ".");
            }
            if (!requiredValueType.isInstance(entryValue)) {
                throw new IllegalArgumentException("The value of entry " + entry + " of " + parameterName + " is not of type " + requiredValueType.getName() + ".");
            }
        }
        //noinspection unchecked
        return (Map<KR, VR>) value;
    }

    public static <T, R extends T> List<R> castNonNullListParameterTo(String parameterName, List<T> value, Class<R> requiredType) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " is null");
        }
        for (T entry : value) {
            if (entry == null) {
                throw new IllegalArgumentException("One entry of " + parameterName + " is null.");
            }
            if (!requiredType.isInstance(entry)) {
                throw new IllegalArgumentException("One entry of " + parameterName + " is not of type " + requiredType.getName() + ".");
            }
        }
        //noinspection unchecked
        return (List<R>) value;
    }

    public static <T, R extends T> List<R> castNullableListParameterTo(String parameterName, List<T> value, Class<R> requiredType) {
        if (value != null) {
            for (T entry : value) {
                if (entry == null) {
                    throw new IllegalArgumentException("One entry of " + parameterName + " is null.");
                }
                if (!requiredType.isInstance(entry)) {
                    throw new IllegalArgumentException("One entry of " + parameterName + " is not of type " + requiredType.getName() + ".");
                }
            }
        }
        //noinspection unchecked
        return value != null ? (List<R>) value : Collections.<R>emptyList();
    }

    private ContractUtil() {}
}
