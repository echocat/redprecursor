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
