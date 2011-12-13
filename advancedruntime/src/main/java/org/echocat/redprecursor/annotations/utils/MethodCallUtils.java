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
import org.echocat.redprecursor.annotations.MethodCall.Parameter;
import org.echocat.redprecursor.annotations.MethodCall.Parameters;
import org.echocat.redprecursor.annotations.utils.query.BooleanExpressionQuery;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class MethodCallUtils {

    @Nonnull
    public static <T> MethodCall<T> toMethodCall(@Nonnull final Class<T> thisType, @Nullable final T thisInstance, @Nonnull final String methodName, @Nullable Object... parameterNamesAndValues) {
        requireNonNull("thisType", thisType);
        requireNonNull("methodName", methodName);
        final Parameters parameters = toParameters(parameterNamesAndValues);
        return new MethodCall<T>() {
            @Nonnull public BooleanExpressionQuery is() { return new BooleanExpressionQuery(); }
            @Nonnull public BooleanExpressionQuery has() { return is(); }
            @Nonnull public BooleanExpressionQuery does() { return is(); }
            @Nonnull @Override public Class<T> getType() { return thisType; }
            @Override public T getThis() { return thisInstance; }
            @Nonnull @Override public String getMethodName() { return methodName; }
            @Nonnull @Override public Parameters getParameters() { return parameters; }
        };
    }

    @Nonnull
    public static Parameters toParameters(@Nullable Object... parameterNamesAndValues) {
        final Map<String, Object> parameterNameToValue = new LinkedHashMap<String, Object>();
        if (parameterNamesAndValues != null) {
            String parameterName = null;
            boolean lastWasParameterName = false;
            for (Object parameterNameOrValue : parameterNamesAndValues) {
                if (lastWasParameterName) {
                    lastWasParameterName = false;
                    parameterNameToValue.put(parameterName, parameterNameOrValue);
                } else {
                    lastWasParameterName = true;
                    if (!(parameterNameOrValue instanceof String)) {
                        throw new IllegalArgumentException("Got '" + parameterNameOrValue + "' but a string as parameter name was expected.");
                    }
                    // noinspection UnusedAssignment
                    parameterName = (String) parameterNameOrValue;
                }
            }
            if (lastWasParameterName) {
                throw new IllegalArgumentException(Arrays.toString(parameterNamesAndValues) + " does not contain a parameter value for parameter name '" + parameterName + "'.");
            }
        }
        return toParameters(parameterNameToValue);
    }

    @Nonnull
    public static Parameters toParameters(@Nonnull final Map<String, Object> parameterNameToValue) {
        return new ParametersImpl(parameterNameToValue);
    }

    protected static class ParametersImpl implements Parameters {

        private final Map<String, Object> _parameterNameToValue;

        protected ParametersImpl(@Nonnull Map<String, Object> parameterNameToValue) {
            requireNonNull("parameterNameToValue", parameterNameToValue);
            _parameterNameToValue = parameterNameToValue;
        }

        @Nonnull @Override
        public Parameter getParameter(@Nonnull final String name) throws NoSuchElementException {
            requireNonNull("name", name);
            if (!hasParameter(name)) {
                throw new NoSuchElementException("Could not find a parameter with name: " + name);
            }
            final Object value = _parameterNameToValue.get(name);
            return new ParameterImpl(name, value);
        }

        @Override
        public boolean hasParameter(@Nonnull String name) {
            requireNonNull("name", name);
            return _parameterNameToValue.containsKey(name);
        }

        @Override
        public Iterator<Parameter> iterator() {
            final Iterator<Entry<String, Object>> i = _parameterNameToValue.entrySet().iterator();
            return new Iterator<Parameter>() {

                @Override
                public Parameter next() {
                    final Entry<String, Object> entry = i.next();
                    return new ParameterImpl(entry.getKey(), entry.getValue());
                }

                @Override public boolean hasNext() { return i.hasNext(); }

                @Override public void remove() { throw new UnsupportedOperationException(); }
            };
        }
    }

    protected static class ParameterImpl implements Parameter {

        private final String _name;
        private final Object _value;

        protected ParameterImpl(@Nonnull String name, @Nullable Object value) {
            requireNonNull("name", name);
            _name = name;
            _value = value;
        }

        @Nonnull @Override
        public String getName() {
            return _name;
        }

        @Override
        public Object getValue() {
            return _value;
        }
    }

    private MethodCallUtils() {}
}
