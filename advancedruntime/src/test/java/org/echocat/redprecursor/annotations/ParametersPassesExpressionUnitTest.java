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

import org.echocat.redprecursor.annotations.ParametersPassesExpression.Evaluator;
import org.junit.Test;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static org.echocat.redprecursor.annotations.AnnotationProxier.proxyAnnotation;
import static org.echocat.redprecursor.annotations.utils.MethodCallUtils.toMethodCall;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class ParametersPassesExpressionUnitTest {

    private static final int MY_STATIC_SECRET_VARIABLE = 666;
    @SuppressWarnings({"FieldCanBeLocal"})
    private final int _mySecretVariable = 1;

    @Test
    public void testEvaluateWithNoMethodElementType() throws Exception {
        try {
            new Evaluator().evaluate(proxyAnnotation(ParametersPassesExpression.class), PARAMETER, "foo", mock(MethodCall.class));
            fail("Expected exception missing.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("This evaluator only supports " + METHOD + " elementTypes."));
        }
    }

    @Test
    public void testEvaluateWithNotMatchingElementNameAndMethodName() throws Exception {
        try {
            final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "bar");
            new Evaluator().evaluate(proxyAnnotation(ParametersPassesExpression.class), METHOD, "foo", methodCall);
            fail("Expected exception missing.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("The provided elementName ('foo') is not the same as the name ('bar') of the provided method call."));
        }
    }

    @Test
    public void testPassEvaluate() throws Exception {
        final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "test", "a", 1, "b", 2);
        evaluate(methodCall, "a == 1");
        evaluate(methodCall, "a == this._mySecretVariable");
        evaluate(methodCall, "a == (this.MY_STATIC_SECRET_VARIABLE - 665)");
        evaluate(methodCall, "a != null");
        evaluate(methodCall, "a < b");
    }

    private void evaluate(MethodCall<ParametersPassesExpressionUnitTest> methodCall, String expression) {
        new Evaluator().evaluate(proxyAnnotation(ParametersPassesExpression.class, "value", expression), METHOD, "test", methodCall);
    }

    @Test
    public void testEvaluationViolation() throws Exception {
        final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "test", "a", 1, "b", 2);
        evaluateAndExpectExceptionWithMessage(methodCall, "a != 1", "${a}", "1");
        evaluateAndExpectExceptionWithMessage(methodCall, "a != this._mySecretVariable", "Failed with ${this._mySecretVariable}", "Failed with 1");
        evaluateAndExpectExceptionWithMessage(methodCall, "a != (this.MY_STATIC_SECRET_VARIABLE - 665)", "The method name is ${#methodName}", "The method name is test");
        evaluateAndExpectExceptionWithMessage(methodCall, "a == null", "${a + 2}", "3");
        evaluateAndExpectExceptionWithMessage(methodCall, "a > b", "${#expression}", "a > b");
    }

    private void evaluateAndExpectExceptionWithMessage(MethodCall<ParametersPassesExpressionUnitTest> methodCall, String expression, String violationMessage, String expectedExceptionMessage) {
        try {
            new Evaluator().evaluate(proxyAnnotation(ParametersPassesExpression.class, "value", expression, "messageOnViolation", violationMessage), METHOD, "test", methodCall);
            fail("Expected exception missing.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(expectedExceptionMessage));
        }
    }

    @Test
    public void testEvaluationThrowsException() throws Exception {
        final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "test", "a", 1, "b", '2');
        try {
            new Evaluator().evaluate(proxyAnnotation(ParametersPassesExpression.class, "value", "a eq b"), METHOD, "test", methodCall);
            fail("Expected exception missing.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Expression not passed: a eq b"));
            assertThat(e.getCause() instanceof SpelEvaluationException, is(true));
        }
    }

    @Test
    public void testEvaluateExpression() throws Exception {
        final Evaluator evaluator = new Evaluator();
        final ParametersPassesExpression annotation = proxyAnnotation(ParametersPassesExpression.class);
        final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "test", "a", 1, "b", 2);
        assertThat(evaluator.evaluateExpression(annotation, String.class, evaluator.getExpressionParser().parseExpression("a + b"), methodCall), is("3"));
        assertThat(evaluator.evaluateExpression(annotation, Object.class, evaluator.getExpressionParser().parseExpression("a + b"), methodCall), is((Object) 3));
        assertThat(evaluator.evaluateExpression(annotation, Object.class, evaluator.getExpressionParser().parseExpression("this._mySecretVariable"), methodCall), is((Object)_mySecretVariable));
        assertThat(evaluator.evaluateExpression(annotation, Object.class, evaluator.getExpressionParser().parseExpression("this.MY_STATIC_SECRET_VARIABLE"), methodCall), is((Object)MY_STATIC_SECRET_VARIABLE));
        assertThat(evaluator.evaluateExpression(annotation, String.class, evaluator.getExpressionParser().parseExpression("this.name"), methodCall), is(getName()));
    }

    @Test
    public void testToContext() throws Exception {
        final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "test", "a", 1, "b", 2);
        final StandardEvaluationContext context = new Evaluator().toContext(methodCall);
        assertThat(context.getRootObject().getValue(), is((Object) methodCall));
        assertThat(context.lookupVariable("methodName"), is((Object) "test"));
        assertThat(context.lookupVariable("parameters"), is((Object) methodCall.getParameters()));
        assertThat(context.getPropertyAccessors(), is(Evaluator.DEFAULT_PROPERTY_ACCESSORS));
    }

    @Test
    public void testThrowMessageForEmptyMessageOnViolation() throws Exception {
        final ParametersPassesExpression annotation = proxyAnnotation(ParametersPassesExpression.class, "value", "myExpression", "messageOnViolation", "");
        final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "test", "a", 1, "b", 2);
        try {
            new Evaluator().throwMessageFor(annotation, methodCall, null);
            fail("Exception missing");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Expression not passed: " + annotation.value()));
            assertThat(e.getCause(), nullValue());
        }
        final Throwable cause = new Throwable("myCause");
        try {
            new Evaluator().throwMessageFor(annotation, methodCall, cause);
            fail("Exception missing");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Expression not passed: " + annotation.value()));
            assertThat(e.getCause(), is(cause));
        }
    }

    @Test
    public void testThrowMessageForMessageOnViolation() throws Exception {
        final ParametersPassesExpression annotation = proxyAnnotation(ParametersPassesExpression.class, "value", "myExpression", "messageOnViolation", "My message - ${#expression}");
        final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "test", "a", 1, "b", 2);
        try {
            new Evaluator().throwMessageFor(annotation, methodCall, null);
            fail("Exception missing");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("My message - " + annotation.value()));
            assertThat(e.getCause(), nullValue());
        }
        final Throwable cause = new Throwable("myCause");
        try {
            new Evaluator().throwMessageFor(annotation, methodCall, cause);
            fail("Exception missing");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("My message - " + annotation.value()));
            assertThat(e.getCause(), is(cause));
        }
    }

    @Test
    public void testFormatViolationMessage() throws Exception {
        final ParametersPassesExpression annotation1 = proxyAnnotation(ParametersPassesExpression.class, "value", "myExpression", "messageOnViolation", "");
        final ParametersPassesExpression annotation2 = proxyAnnotation(ParametersPassesExpression.class, "value", "myExpression", "messageOnViolation", "My message - ${#expression}");
        final MethodCall<ParametersPassesExpressionUnitTest> methodCall = toMethodCall(ParametersPassesExpressionUnitTest.class, this, "test", "a", 1, "b", 2);
        assertThat(new Evaluator().formatViolationMessage(annotation1, methodCall), is("Expression not passed: " + annotation1.value()));
        assertThat(new Evaluator().formatViolationMessage(annotation2, methodCall), is("My message - " + annotation1.value()));
    }

    public String getName() {
        return "bla";
    }
}
