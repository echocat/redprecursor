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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;

import javax.annotation.Nonnull;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"AccessingNonPublicFieldOfAnotherObject"})
public class AccessAlsoProtectedMembersReflectivePropertyAccessorUnitTest {

    private static final AccessAlsoProtectedMembersReflectivePropertyAccessor INSTANCE = new AccessAlsoProtectedMembersReflectivePropertyAccessor();
    private static final EvaluationContext CONTEXT = mock(EvaluationContext.class);

    @Test
    public void testFindGetterForProperty() throws Exception {
        final Foo foo = new Foo();
        assertThat(INSTANCE.read(CONTEXT, foo, "STATIC_READ_ONLY"), returnsValue(Foo.STATIC_READ_ONLY));
        assertThat(INSTANCE.read(CONTEXT, foo, "PROTECTED_STATIC_READ_ONLY"), returnsValue(Foo.PROTECTED_STATIC_READ_ONLY));
        assertThat(INSTANCE.read(CONTEXT, foo, "staticReadOnly"), returnsValue(Foo.getStaticReadOnly()));
        assertThat(INSTANCE.read(CONTEXT, foo, "protectedStaticReadOnly"), returnsValue(Foo.getProtectedStaticReadOnly()));
        assertThat(INSTANCE.read(CONTEXT, foo, "c_staticReadWrite"), returnsValue(Foo.c_staticReadWrite));
        assertThat(INSTANCE.read(CONTEXT, foo, "c_protectedStaticReadWrite"), returnsValue(Foo.c_protectedStaticReadWrite));
        assertThat(INSTANCE.read(CONTEXT, foo, "staticReadWrite"), returnsValue(Foo.getStaticReadWrite()));
        assertThat(INSTANCE.read(CONTEXT, foo, "protectedStaticReadWrite"), returnsValue(Foo.getProtectedStaticReadWrite()));
        assertThat(INSTANCE.read(CONTEXT, foo, "_readOnly"), returnsValue(foo._readOnly));
        assertThat(INSTANCE.read(CONTEXT, foo, "_protectedReadOnly"), returnsValue(foo._protectedReadOnly));
        assertThat(INSTANCE.read(CONTEXT, foo, "readOnly"), returnsValue(foo.getReadOnly()));
        assertThat(INSTANCE.read(CONTEXT, foo, "protectedReadOnly"), returnsValue(foo.getProtectedReadOnly()));
        assertThat(INSTANCE.read(CONTEXT, foo, "_readWrite"), returnsValue(foo._readWrite));
        assertThat(INSTANCE.read(CONTEXT, foo, "_protectedReadWrite"), returnsValue(foo._protectedReadWrite));
        assertThat(INSTANCE.read(CONTEXT, foo, "readWrite"), returnsValue(foo.getReadWrite()));
        assertThat(INSTANCE.read(CONTEXT, foo, "protectedReadWrite"), returnsValue(foo.getProtectedReadWrite()));

        final Bar bar = new Bar();
        assertThat(INSTANCE.read(CONTEXT, bar, "STATIC_READ_ONLY"), returnsValue(Foo.STATIC_READ_ONLY));
        assertThat(INSTANCE.read(CONTEXT, bar, "PROTECTED_STATIC_READ_ONLY"), returnsValue(Foo.PROTECTED_STATIC_READ_ONLY));
        assertThat(INSTANCE.read(CONTEXT, bar, "staticReadOnly"), returnsValue(Foo.getStaticReadOnly()));
        assertThat(INSTANCE.read(CONTEXT, bar, "protectedStaticReadOnly"), returnsValue(Foo.getProtectedStaticReadOnly()));
        assertThat(INSTANCE.read(CONTEXT, bar, "c_staticReadWrite"), returnsValue(Foo.c_staticReadWrite));
        assertThat(INSTANCE.read(CONTEXT, bar, "c_protectedStaticReadWrite"), returnsValue(Foo.c_protectedStaticReadWrite));
        assertThat(INSTANCE.read(CONTEXT, bar, "staticReadWrite"), returnsValue(Foo.getStaticReadWrite()));
        assertThat(INSTANCE.read(CONTEXT, bar, "protectedStaticReadWrite"), returnsValue(Foo.getProtectedStaticReadWrite()));
        assertThat(INSTANCE.read(CONTEXT, bar, "_readOnly"), returnsValue(bar._readOnly));
        assertThat(INSTANCE.read(CONTEXT, bar, "_protectedReadOnly"), returnsValue(bar._protectedReadOnly));
        assertThat(INSTANCE.read(CONTEXT, bar, "readOnly"), returnsValue(bar.getReadOnly()));
        assertThat(INSTANCE.read(CONTEXT, bar, "protectedReadOnly"), returnsValue(bar.getProtectedReadOnly()));
        assertThat(INSTANCE.read(CONTEXT, bar, "_readWrite"), returnsValue(bar._readWrite));
        assertThat(INSTANCE.read(CONTEXT, bar, "_protectedReadWrite"), returnsValue(bar._protectedReadWrite));
        assertThat(INSTANCE.read(CONTEXT, bar, "readWrite"), returnsValue(bar.getReadWrite()));
        assertThat(INSTANCE.read(CONTEXT, bar, "protectedReadWrite"), returnsValue(bar.getProtectedReadWrite()));
    }

    @Test
    public void testFindSetterForProperty() throws Exception {
        final Foo foo = new Foo();
        final String oldFooStaticReadWriteValue = Foo.c_staticReadWrite;
        try {
            INSTANCE.write(CONTEXT, foo, "c_staticReadWrite", "newValue1");
            assertThat(Foo.c_staticReadWrite, is("newValue1"));
            INSTANCE.write(CONTEXT, foo, "staticReadWrite", "newValue2");
            assertThat(Foo.getStaticReadWrite(), is("newValue2"));
        } finally {
            Foo.c_staticReadWrite = oldFooStaticReadWriteValue;
        }
        INSTANCE.write(CONTEXT, foo, "_readWrite", "newValue1");
        assertThat(foo._readWrite, is("newValue1"));
        INSTANCE.write(CONTEXT, foo, "readWrite", "newValue2");
        assertThat(foo.getReadWrite(), is("newValue2"));

        final Bar bar = new Bar();
        final String oldBarStaticReadWriteValue = Foo.c_staticReadWrite;
        try {
            INSTANCE.write(CONTEXT, bar, "c_staticReadWrite", "newValue1");
            assertThat(Foo.c_staticReadWrite, is("newValue1"));
            INSTANCE.write(CONTEXT, bar, "staticReadWrite", "newValue2");
            assertThat(Foo.getStaticReadWrite(), is("newValue2"));
        } finally {
            Foo.c_staticReadWrite = oldBarStaticReadWriteValue;
        }
        INSTANCE.write(CONTEXT, bar, "_readWrite", "newValue1");
        assertThat(bar._readWrite, is("newValue1"));
        INSTANCE.write(CONTEXT, bar, "readWrite", "newValue2");
        assertThat(bar.getReadWrite(), is("newValue2"));
    }

    private Matcher<TypedValue> returnsValue(@Nonnull final Object value) {
        requireNonNull("value", value);
        return new TypeSafeMatcher<TypedValue>() {
            @Override public boolean matchesSafely(TypedValue item) { return item != null && value.equals(item.getValue()); }
            @Override public void describeTo(Description description) { description.appendText("is ").appendValue(value); }
        };
    }

    private static class Foo {

        public static final String STATIC_READ_ONLY = "staticX";
        private static final String PROTECTED_STATIC_READ_ONLY = "protectedStaticX";
        public static String c_staticReadWrite = "staticY";
        private static String c_protectedStaticReadWrite = "protectedStaticY";

        public static String getStaticReadOnly() {
            return STATIC_READ_ONLY;
        }

        private static String getProtectedStaticReadOnly() {
            return PROTECTED_STATIC_READ_ONLY;
        }

        public static String getStaticReadWrite() {
            return c_staticReadWrite;
        }

        public static void setStaticReadWrite(String staticReadWrite) {
            c_staticReadWrite = staticReadWrite;
        }

        private static String getProtectedStaticReadWrite() {
            return c_protectedStaticReadWrite;
        }

        private static void setProtectedStaticReadWrite(String protectedStaticReadWrite) {
            c_protectedStaticReadWrite = protectedStaticReadWrite;
        }

        public final String _readOnly = "x";
        protected final String _protectedReadOnly = "protectedX";
        private String _readWrite = "y";
        private String _protectedReadWrite = "protectedY";

        public String getReadOnly() {
            return _readOnly;
        }

        protected String getProtectedReadOnly() {
            return _protectedReadOnly;
        }

        public String getReadWrite() {
            return _readWrite;
        }

        public void setReadWrite(String readWrite) {
            _readWrite = readWrite;
        }

        private String getProtectedReadWrite() {
            return _protectedReadWrite;
        }

        private void setProtectedReadWrite(String protectedReadWrite) {
            _protectedReadWrite = protectedReadWrite;
        }
    }

    private static class Bar extends Foo {

        public String _readWrite = "overwritten";
        private String _protectedReadWrite = "protectedOverwritten";

        @Override
        public String getReadWrite() {
            return _readWrite;
        }

        @Override
        public void setReadWrite(String readWrite) {
            _readWrite = readWrite;
        }

        private String getProtectedReadWrite() {
            return _protectedReadWrite;
        }

        private void setProtectedReadWrite(String protectedReadWrite) {
            _protectedReadWrite = protectedReadWrite;
        }
    }

}
