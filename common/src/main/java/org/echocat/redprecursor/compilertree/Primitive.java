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

import java.lang.reflect.Field;

public enum Primitive {
    BYTE(Byte.class),
    SHORT(Short.class),
    INT(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    CHAR(Character.class),
    STRING(String.class),
    NULL(null);

    private static Class<?> tryGetPrimitiveTypeFor(Class<?> type) {
        Class<?> primitiveType;
        try {
            final Field field = type.getDeclaredField("TYPE");
            primitiveType = (Class<?>) field.get(null);
        } catch (NoSuchFieldException ignored) {
            primitiveType = null;
        } catch (Exception e) {
            throw new RuntimeException("Could not access the field TYPE of " + type.getName() + ".", e);
        }
        return primitiveType;
    }

    public static Primitive getTypeFor(Object value) {
        Primitive result = null;
        for (Primitive primitive : values()) {
            if (primitive.isValueOfType(value)) {
                result = primitive;
                break;
            }
        }
        return result;
    }

    private final Class<?> _type;
    private final Class<?> _primitiveType;

    Primitive(Class<?> type) {
        _type = type;
        _primitiveType = type != null ? tryGetPrimitiveTypeFor(type) : null;
    }

    public boolean isValueOfType(Object value) {
        return (this == NULL && value == null) || (_type != null && _type.isInstance(value)) || (_primitiveType != null && _primitiveType.isInstance(value));
    }
}
