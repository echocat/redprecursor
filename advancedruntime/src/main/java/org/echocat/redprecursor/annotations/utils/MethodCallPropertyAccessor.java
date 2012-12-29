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

import org.echocat.redprecursor.annotations.MethodCall;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class MethodCallPropertyAccessor implements PropertyAccessor {

    public static final String THIS_KEYWORD = "this";

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class[]{ MethodCall.class };
    }

    @Override
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
        return target instanceof MethodCall && (THIS_KEYWORD.equals(name) || ((MethodCall)target).getParameters().hasParameter(name));
    }

    @Override
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        final Object value;
        if (target instanceof MethodCall) {
            final MethodCall<?> methodCall = (MethodCall<?>) target;
            if (THIS_KEYWORD.equals(name)) {
                final Object thisInstance = methodCall.getThis();
                value = thisInstance != null ? thisInstance : methodCall.getType();
            } else {
                value = methodCall.getParameters().getParameter(name).getValue();
            }
        } else {
            throw new AccessException(target + " is no instance of " + MethodCall.class.getName() + ".");
        }
        return new TypedValue(value);
    }

    @Override
    public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
        return false;
    }

    @Override
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
        throw new AccessException("Could not modify an instance of " + MethodCall.class.getName());
    }
}
