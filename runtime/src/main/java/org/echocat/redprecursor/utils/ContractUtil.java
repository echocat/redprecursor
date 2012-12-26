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
