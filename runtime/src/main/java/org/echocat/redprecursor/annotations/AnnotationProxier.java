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
