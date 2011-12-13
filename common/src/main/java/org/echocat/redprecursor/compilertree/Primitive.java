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
