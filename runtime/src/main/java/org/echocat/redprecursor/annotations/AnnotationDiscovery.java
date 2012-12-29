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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.Boolean.TRUE;
import static java.lang.System.getProperty;
import static java.lang.reflect.Modifier.isStatic;
import static org.echocat.redprecursor.annotations.DefaultAnnotationCreator.createFor;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class AnnotationDiscovery {

    public static final String CREATE_FALLBACK_ANNOTATIONS_IF_NOT_FOUND_NAME = AnnotationDiscovery.class.getPackage().getName() + ".createFallbackAnnotationsIfNotFound";

    @Nonnull
    public static <T extends Annotation> T discoverMethodAnnotation(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nonnull String methodName, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        requireNonNull("methodName", methodName);
        final Method method = discoverMethod(at, methodName, methodParameterTypes);
        T annotation = method.getAnnotation(annotationOfType);
        if (annotation == null) {
            annotation = returnFallbackIfPossible(annotationOfType, "Could not find an annotation of type " + annotationOfType.getName() + " at " + at.getName() + "." + methodName + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
        }
        return annotation;
    }

    @Nonnull
    public static <T extends Annotation> T discoverConstructorAnnotation(@Nonnull Class<T> annotationOfType, @Nonnull Class<?> at, @Nullable Class<?>... methodParameterTypes) {
        requireNonNull("annotationOfType", annotationOfType);
        requireNonNull("at", at);
        final Constructor<?> constructor = discoverConstructor(at, methodParameterTypes);
        T annotation = constructor.getAnnotation(annotationOfType);
        if (annotation == null) {
            annotation = returnFallbackIfPossible(annotationOfType, "Could not find an annotation of type " + annotationOfType.getName() + " at " + at.getName() + "." + at.getName() + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
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
        T annotation = findMatchingAnnotation(annotationOfType, allMethodParameterAnnotations[methodParameterIndex]);
        if (annotation == null) {
            annotation = returnFallbackIfPossible(annotationOfType, "Could not find an annotation of type " + annotationOfType.getName() + " at " + at.getName() + "." + methodName + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
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
        T annotation = findMatchingAnnotation(annotationOfType, allMethodParameterAnnotations[methodParameterIndex]);
        if (annotation == null) {
            annotation = returnFallbackIfPossible(annotationOfType, "Could not find an annotation of type " + annotationOfType.getName() + " at " + at.getName() + "." + at.getName() + "(" + (methodParameterTypes != null ? Arrays.toString(methodParameterTypes) : "[]") + ").");
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
        final Class<?> enclosingClass = at.getEnclosingClass();
        Class<?>[] targetParameterTypes = parameterTypes != null ? parameterTypes : new Class[0];
        if (enclosingClass != null) {
            final int modifiers = at.getModifiers();
            if (!isStatic(modifiers)) {
                final Class<?>[] oldParametersTypes = targetParameterTypes;
                targetParameterTypes = new Class[oldParametersTypes.length + 1];
                targetParameterTypes[0] = enclosingClass;
                System.arraycopy(oldParametersTypes, 0, targetParameterTypes, 1, oldParametersTypes.length);
            }
        }
        if (at.isEnum()) {
            final Class<?>[] oldParametersTypes = targetParameterTypes;
            targetParameterTypes = new Class[oldParametersTypes.length + 2];
            targetParameterTypes[0] = String.class;
            targetParameterTypes[1] = int.class;
            System.arraycopy(oldParametersTypes, 0, targetParameterTypes, 2, oldParametersTypes.length);
        }
        try {
            constructor = at.getDeclaredConstructor(targetParameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Could not find " + at.getName() + "." + at.getName() + "(" + (parameterTypes != null ? Arrays.toString(parameterTypes) : "[]") + ").", e);
        }
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        return constructor;
    }

    protected static boolean isReturnFallbackIfNotFound() {
        return TRUE.toString().equalsIgnoreCase(getProperty(CREATE_FALLBACK_ANNOTATIONS_IF_NOT_FOUND_NAME, TRUE.toString()));
    }

    @Nonnull
    protected static <T extends Annotation> T returnFallbackIfPossible(@Nonnull Class<T> annotationType, @Nonnull String errorMessage) throws IllegalArgumentException {
        if (isReturnFallbackIfNotFound()) {
            //noinspection UseOfSystemOutOrSystemErr
            System.err.println(
                "**********************************\n"
                + errorMessage
                + " - Try create dummy annotation instance."
                + "\n**********************************");
            return createFor(annotationType);
        } else {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private AnnotationDiscovery() {}
}
