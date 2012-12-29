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

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static java.lang.reflect.Proxy.newProxyInstance;

public class DefaultAnnotationCreator {

    private static final InvocationHandler HANDLER = new InvocationHandler() { @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object defaultValue;
        try {
            defaultValue = method.getDefaultValue();

        } catch (TypeNotPresentException ignored) {
            defaultValue = null;
        }
        if (defaultValue == null) {
            throw new IllegalStateException("It was not possible to create an working annotation dummy because for " + method.getName() + " was not default value available.");
        }
        return defaultValue;
    }};

    @Nonnull
    public static <A extends Annotation> A createFor(@Nonnull Class<? extends A> type) {
        //noinspection unchecked
        return (A) newProxyInstance(type.getClassLoader(), new Class[]{type}, HANDLER);
    }

    private DefaultAnnotationCreator() {}

}
