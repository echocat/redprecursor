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

package org.echocat.redprecursor.annotations;

import org.echocat.redprecursor.meta.AnnotationAndMeta;
import org.echocat.redprecursor.meta.AnnotationMeta;
import org.echocat.redprecursor.meta.AnnotationMetaFactoryFactory;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Map.Entry;

import static org.echocat.redprecursor.utils.CollectionUtils.asMap;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class AnnotationProxier {

    @Nonnull
    public static <T extends Annotation> T proxyAnnotation(@Nonnull Class<T> annotationType, Object... keyAndValues) {
        requireNonNull("annotationType", annotationType);
        final Map<String, Object> keyToValues = asMap(keyAndValues);
        checkProperties(annotationType, keyToValues);
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(annotationType.getClassLoader(), new Class[]{ annotationType }, new InvocationHandler() { @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final String key = method.getName();
            return keyToValues.get(key);
        }});
    }

    @Nonnull
    public static <T extends Annotation, V> AnnotationAndMeta<T, V> createAnnotationAndMeta(@Nonnull Class<T> annotationType, Object... keyAndValues) {
        requireNonNull("annotationType", annotationType);
        final T annotation = proxyAnnotation(annotationType, keyAndValues);
        final AnnotationMeta<T, V> meta = AnnotationMetaFactoryFactory.getDefaultInstance().getFor(annotation);
        return new AnnotationAndMeta<T, V>(annotation, meta);
    }

    @Nonnull
    private static <T extends Annotation> void checkProperties(@Nonnull Class<T> annotationType, @Nonnull Map<String, Object> keyToValues) {
        requireNonNull("annotationType", annotationType);
        requireNonNull("keyToValues", keyToValues);
        for (Entry<String, Object> keyAndValue : keyToValues.entrySet()) {
            final Method method;
            final String key = keyAndValue.getKey();
            try {
                method = annotationType.getMethod(key);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Could not find a property with name: " + key, e);
            }
            final Object value = keyAndValue.getValue();
            final Class<?> returnType = method.getReturnType();
            if (!returnType.isInstance(value)) {
                throw new IllegalArgumentException("The value of key '" + key + "' is not of expected type: " + returnType.getName());
            }
        }
    }

    private AnnotationProxier() {}
}
