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

package org.echocat.redprecursor.annotations.utils;

import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import static java.util.Collections.synchronizedMap;

/**
 * This {@link PropertyAccessor} could access public, protected, package local and private properties and fields of an object.
 */
public class AccessAlsoProtectedMembersReflectivePropertyAccessor extends ReflectivePropertyAccessor {

    private static final Map<Class<?>, Map<String, PropertyDescriptor>> TYPE_TO_PROPERTY_DESCRIPTORS = synchronizedMap(new WeakHashMap<Class<?>, Map<String, PropertyDescriptor>>());

    @Override
    protected Method findGetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
        Method result = null;
        final PropertyDescriptor propertyDescriptor = findPropertyDescriptorFor(clazz, propertyName);
        if (propertyDescriptor != null) {
            result = propertyDescriptor.getReadMethod();
        }
        if (result == null) {
            Class<?> current = clazz;
            final String getterName = "get" + StringUtils.capitalize(propertyName);
            while (result == null && !Object.class.equals(current)) {
                try {
                    final Method potentialMethod = current.getDeclaredMethod(getterName);
                    if (!mustBeStatic || Modifier.isStatic(potentialMethod.getModifiers())) {
                        if (!potentialMethod.isAccessible()) {
                            potentialMethod.setAccessible(true);
                        }
                        result = potentialMethod;
                    }
                } catch (NoSuchMethodException ignored) {}
                current = current.getSuperclass();
            }
        }
        return result;
    }

    @Override
    protected Method findSetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
        Method result = null;
        final PropertyDescriptor propertyDescriptor = findPropertyDescriptorFor(clazz, propertyName);
        if (propertyDescriptor != null) {
            result = propertyDescriptor.getWriteMethod();
        }
        if (result == null) {
            Class<?> current = clazz;
            final String setterName = "set" + StringUtils.capitalize(propertyName);
            while (result == null && !Object.class.equals(current)) {
                final Method[] potentialMethods = current.getDeclaredMethods();
                for (Method potentialMethod : potentialMethods) {
                    if (setterName.equals(potentialMethod.getName())) {
                        if (potentialMethod.getParameterTypes().length == 1) {
                            if (!mustBeStatic || Modifier.isStatic(potentialMethod.getModifiers())) {
                                if (!potentialMethod.isAccessible()) {
                                    potentialMethod.setAccessible(true);
                                }
                                result = potentialMethod;
                            }
                        }
                    }
                }
                current = current.getSuperclass();
            }
        }
        return result;
    }

    @Override
    protected Field findField(String name, Class<?> clazz, boolean mustBeStatic) {
        Field result = null;
        Class<?> current = clazz;
        while (result == null && !Object.class.equals(current)) {
            try {
                final Field potentialField = current.getDeclaredField(name);
                if (!mustBeStatic || Modifier.isStatic(potentialField.getModifiers())) {
                    if (!potentialField.isAccessible()) {
                        potentialField.setAccessible(true);
                    }
                    result = potentialField;
                }
            } catch (NoSuchFieldException ignored) {}
            current = current.getSuperclass();
        }
        return result;
    }

    @Nullable
    private PropertyDescriptor findPropertyDescriptorFor(@Nonnull Class<?> clazz, @Nonnull String propertyName) {
        final Map<String, PropertyDescriptor> propertyNameToDescriptor = getPropertyNameToDescriptorFor(clazz);
        return propertyNameToDescriptor.get(propertyName);
    }

    @Nonnull
    private Map<String, PropertyDescriptor> getPropertyNameToDescriptorFor(@Nonnull Class<?> clazz) {
        Map<String, PropertyDescriptor> propertyNameToDescriptor = TYPE_TO_PROPERTY_DESCRIPTORS.get(clazz);
        if (propertyNameToDescriptor == null) {
            propertyNameToDescriptor = new HashMap<String, PropertyDescriptor>();
            final BeanInfo beanInfo;
            try {
                beanInfo = Introspector.getBeanInfo(clazz);
            } catch (IntrospectionException e) {
                throw new RuntimeException("Could not load beanInfo of " + clazz.getName() + ".", e);
            }
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                propertyNameToDescriptor.put(propertyDescriptor.getName(), propertyDescriptor);
            }
            TYPE_TO_PROPERTY_DESCRIPTORS.put(clazz, propertyNameToDescriptor);
        }
        return propertyNameToDescriptor;
    }
}
