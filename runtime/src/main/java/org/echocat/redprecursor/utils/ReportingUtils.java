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

import org.echocat.redprecursor.annotations.VisibleInStackTraces;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.annotation.ElementType.*;
import static java.util.Arrays.asList;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

@VisibleInStackTraces(false)
public class ReportingUtils {

    public static void throwMessageFor(@Nonnull Annotation annotation, @Nonnull String withDefaultMessageSuffix, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable Object value) throws RuntimeException {
        throwMessageFor(elementType == PARAMETER ? IllegalArgumentException.class : IllegalStateException.class, annotation, withDefaultMessageSuffix, elementType, elementName, value);
    }

    public static <T extends Throwable> void throwMessageFor(@Nonnull Class<T> ofType, @Nonnull Annotation forAnnotation, @Nonnull String withDefaultMessageSuffix, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable Object value) throws T {
        requireNonNull("defaultMessage", ofType);
        requireNonNull("forAnnotation", forAnnotation);
        requireNonNull("withDefaultMessageSuffix", withDefaultMessageSuffix);
        requireNonNull("elementType", elementType);
        requireNonNull("elementName", elementName);
        String message = tryGetMessageOnViolationFrom(forAnnotation);
        if (message == null || message.trim().isEmpty()) {
            message = (elementType == METHOD ? "The return value of " + elementName + "(..)" : elementName) + withDefaultMessageSuffix;
        }
        throwMessage(ofType, message, null, elementName, value);
    }

    public static <T extends Throwable> void throwMessage(@Nonnull Class<T> ofType, @Nonnull String message, @Nullable Throwable cause, @Nullable Object... arguments) throws T {
        requireNonNull("message", message);
        final String formattedMessage = arguments != null && arguments.length > 0 ? MessageFormat.format(message, arguments) : message;
        throw createThrowable(requireNonNull("ofType", ofType), formattedMessage, cause);
    }

    @Nonnull
    protected static StackTraceElement[] optimizedStackTrace(@Nonnull StackTraceElement[] original) {
        final List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>(asList(requireNonNull("original", original)));
        final Iterator<StackTraceElement> i = stackTraceElements.iterator();
        final ClassLoader classLoader = ReportingUtils.class.getClassLoader();
        while (i.hasNext()) {
            final StackTraceElement stackTraceElement = i.next();
            final String typeName = stackTraceElement.getClassName();
            try {
                final Class<?> type = classLoader.loadClass(typeName);
                final VisibleInStackTraces visibleInStackTraces = type.getAnnotation(VisibleInStackTraces.class);
                if (visibleInStackTraces != null && !visibleInStackTraces.value()) {
                    i.remove();
                }
            } catch (ClassNotFoundException ignored) {}
        }
        return stackTraceElements.toArray(new StackTraceElement[stackTraceElements.size()]);
    }

    @Nullable
    private static String tryGetMessageOnViolationFrom(@Nonnull Annotation annotation) {
        String result;
        try {
            final Method messageOnViolationMethod = annotation.getClass().getMethod("messageOnViolation");
            if (String.class.equals(messageOnViolationMethod.getReturnType())) {
                result = (String) messageOnViolationMethod.invoke(annotation);
            } else {
                result = null;
            }
        } catch (NoSuchMethodException ignored) {
            result = null;
        } catch (Exception e) {
            throw new RuntimeException("Could not get messageOnViolation property from " + annotation + ".", e);
        }
        return result;
    }

    @Nonnull
    private static <T extends Throwable> T createThrowable(@Nonnull Class<T> type, @Nonnull String message, @Nullable Throwable cause) {
        requireNonNull("type", type);
        requireNonNull("message", message);
        final Constructor<T> constructor = getConstructorFor(type, cause != null ? Throwable.class : null);
        final T t;
        try {
            // noinspection ThrowableResultOfMethodCallIgnored
            t = cause != null ? constructor.newInstance(message, cause) : constructor.newInstance(message);
        } catch (Exception e) {
            throw new Error("Could not create a new instance of " + type.getName() + " with " + constructor + ".", e);
        }
        t.setStackTrace(optimizedStackTrace(new Exception().getStackTrace()));
        return t;
    }

    @Nonnull
    private static <T> Constructor<T> getConstructorFor(@Nonnull Class<T> type, @Nullable Class<? extends Throwable> causeType) {
        requireNonNull("type", type);
        final Constructor<T> constructor;
        try {
            constructor = causeType != null ? type.getConstructor(String.class, causeType) : type.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            final VerifyError error = new VerifyError("The throwMessage method was called with an invalid type to throw: " + type.getName());
            error.initCause(e);
            throw error;
        }
        return constructor;
    }

    private ReportingUtils() {}
}
