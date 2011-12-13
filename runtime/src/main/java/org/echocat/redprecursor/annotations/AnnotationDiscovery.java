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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class AnnotationDiscovery {

    @Nonnull
    public static <T extends Annotation> T discoverMethodAnnotation(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nonnull String methodName, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        requireNonNull("methodName", methodName);
        final Method method = discoverMethod(at, methodName, methodParameterTypes);
        final T annotation = method.getAnnotation(annotationOfType);
        if (annotation == null) {
            throw new IllegalArgumentException("Could not find an annotation of type " + annotationOfType.getName() + " at " + at.getName() + "." + methodName + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
        }
        return annotation;
    }

    @Nonnull
    public static <T extends Annotation> T discoverConstructorAnnotation(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        final Constructor<?> constructor = discoverConstructor(at, methodParameterTypes);
        final T annotation = constructor.getAnnotation(annotationOfType);
        if (annotation == null) {
            throw new IllegalArgumentException("Could not find an annotation of type " + annotationOfType.getName() + " at " + at.getName() + "." + at.getName() + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
        }
        return annotation;
    }

    @Nonnull
    public static <T extends Annotation, V> AnnotationAndMeta<T, V> discoverMethodAnnotationAndMeta(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nonnull String methodName, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        requireNonNull("methodName", methodName);
        final T annotation = discoverMethodAnnotation(annotationOfType, at, methodName, methodParameterTypes);
        final AnnotationMeta<T, V> meta = AnnotationMetaFactoryFactory.getDefaultInstance().getFor(annotation);
        return new AnnotationAndMeta<T, V>(annotation, meta);
    }

    @Nonnull
    public static <T extends Annotation, V> AnnotationAndMeta<T, V> discoverConstructorAnnotationAndMeta(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        final T annotation = discoverConstructorAnnotation(annotationOfType, at, methodParameterTypes);
        final AnnotationMeta<T, V> meta = AnnotationMetaFactoryFactory.getDefaultInstance().getFor(annotation);
        return new AnnotationAndMeta<T, V>(annotation, meta);
    }

    @Nonnull
    public static <T extends Annotation> T discoverMethodParameterAnnotation(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nonnull String methodName, @Nonnegative int methodParameterIndex, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        requireNonNull("methodName", methodName);
        final Method method = discoverMethod(at, methodName, methodParameterTypes);
        final Annotation[][] allMethodParameterAnnotations = method.getParameterAnnotations();
        if (methodParameterIndex >= allMethodParameterAnnotations.length) {
            throw new IllegalArgumentException("Could not find parameter #" + methodParameterIndex + " at " + at.getName() + "." + methodName + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
        }
        final T annotation = findMatchingAnnotation(annotationOfType, allMethodParameterAnnotations[methodParameterIndex]);
        if (annotation == null) {
            throw new IllegalArgumentException("Could not find an annotation of type " + annotationOfType.getName() + " at " + at.getName() + "." + methodName + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
        }
        return annotation;
    }

    @Nonnull
    public static <T extends Annotation> T discoverConstructorParameterAnnotation(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nonnegative int methodParameterIndex, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        final Constructor<?> constructor = discoverConstructor(at, methodParameterTypes);
        final Annotation[][] allMethodParameterAnnotations = constructor.getParameterAnnotations();
        if (methodParameterIndex >= allMethodParameterAnnotations.length) {
            throw new IllegalArgumentException("Could not find parameter #" + methodParameterIndex + " at " + at.getName() + "." + at.getName() + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
        }
        final T annotation = findMatchingAnnotation(annotationOfType, allMethodParameterAnnotations[methodParameterIndex]);
        if (annotation == null) {
            throw new IllegalArgumentException("Could not find an annotation of type " + annotationOfType.getName() + " at " + at.getName() + "." + at.getName() + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
        }
        return annotation;
    }

    @Nonnull
    public static <T extends Annotation, V> AnnotationAndMeta<T, V> discoverMethodParameterAnnotationAndMeta(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nonnull String methodName, @Nonnegative int methodParameterIndex, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        requireNonNull("methodName", methodName);
        final T annotation = discoverMethodParameterAnnotation(annotationOfType, at, methodName, methodParameterIndex, methodParameterTypes);
        final AnnotationMeta<T, V> meta = AnnotationMetaFactoryFactory.getDefaultInstance().getFor(annotation);
        return new AnnotationAndMeta<T, V>(annotation, meta);
    }

    @Nonnull
    public static <T extends Annotation, V> AnnotationAndMeta<T, V> discoverConstructorParameterAnnotationAndMeta(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nonnegative int methodParameterIndex, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        final T annotation = discoverConstructorParameterAnnotation(annotationOfType, at, methodParameterIndex, methodParameterTypes);
        final AnnotationMeta<T, V> meta = AnnotationMetaFactoryFactory.getDefaultInstance().getFor(annotation);
        return new AnnotationAndMeta<T, V>(annotation, meta);
    }

    @Nullable
    private static <T extends Annotation> T findMatchingAnnotation(@Nonnull Class<T> expectedAnnotationType, @Nonnull Annotation[] annotations) {
        requireNonNull("expectedAnnotationType", expectedAnnotationType);
        requireNonNull("annotations", annotations);
        T result = null;
        for (Annotation annotation : annotations) {
            if (expectedAnnotationType.isInstance(annotation)) {
                result = expectedAnnotationType.cast(annotation);
                break;
            }
        }
        return result;
    }

    @Nonnull
    private static Method discoverMethod(@Nonnull Class<?> at, @Nonnull String withName, @Nullable Class<?>[] parameterTypes) {
        requireNonNull("at", at);
        requireNonNull("withName", withName);
        final Method method;
        try {
            method = at.getDeclaredMethod(withName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Could not find " + at.getName() + "." + withName + "(" + (parameterTypes != null ? Arrays.toString(parameterTypes) : "[]") + ").", e);
        }
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method;
    }

    @Nonnull
    private static Constructor<?> discoverConstructor(@Nonnull Class<?> at, @Nullable Class<?>[] parameterTypes) {
        requireNonNull("at", at);
        final Constructor<?> constructor;
        try {
            constructor = at.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Could not find " + at.getName() + "." + at.getName() + "(" + (parameterTypes != null ? Arrays.toString(parameterTypes) : "[]") + ").", e);
        }
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        return constructor;
    }

    private AnnotationDiscovery() {}

}
