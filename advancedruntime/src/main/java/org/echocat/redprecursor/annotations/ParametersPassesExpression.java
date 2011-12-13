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

import org.echocat.redprecursor.annotations.MethodCall.Parameters;
import org.echocat.redprecursor.annotations.ParametersPassesExpression.Evaluator;
import org.echocat.redprecursor.annotations.utils.AccessAlsoProtectedMembersReflectivePropertyAccessor;
import org.echocat.redprecursor.annotations.utils.MapPropertyAccessor;
import org.echocat.redprecursor.annotations.utils.MethodCallPropertyAccessor;
import org.echocat.redprecursor.meta.AnnotationEvaluator;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;
import static org.echocat.redprecursor.utils.ReportingUtils.throwMessage;

@Target({ METHOD })
@Retention(RUNTIME)
@Documented
@EvaluatedBy(Evaluator.class)
public @interface ParametersPassesExpression {

    /**
     * <p>Expression to evaluate against the provided parameter names on call of the method which was annotated with this annotation. Each parameter is
     * referenced with its original parameter name. The <code>this</code> keyword is also available with could reference instance and static members.
     * This expression will be evaluate without template format.</p>
     *
     * <p>See <a href="http://static.springsource.org/spring/docs/3.0.5.RELEASE/reference/expressions.html">Spring Expression Language (SpEL)</a> for more
     * information about expression language.</p>
     *
     * <p>Example: <code>@ParametersPassesExpression(&quot;this.expectedMinimumForMyParameter < myParameter&quot;)</code></p>
     *
     * <p>Special fields:
     * <ul>
     *     <li><code>#expression</code>: The expression of this annotation ({@link #value()}).</li>
     *     <li><code>#methodName</code>: Name of the method that was called.</li>
     *     <li><code>#parameters</code>: Provided {@link Parameters} instance for the called method.</li>
     * </ul>
     * </p>
     */
    public String value();

    /**
     * <p>This expression will be evaluated as template format if the expression ({@link #value()}) was not passed. All properties, fields, ... like in
     * {@link #value()} will be also available.</p>
     *
     * <p>Example: <code>@ParametersPassesExpression(
     *     {@link #value} = &quot;this.expectedMinimumForMyParameter < myParameter&quot;,
     *     {@link #messageOnViolation} = &quot;myParameter is ${myParameter} but is not larger than ${this.expectedMinimumForMyParameter}&quot;
     * )</code></p>
     */
    public String messageOnViolation() default "";

    @VisibleInStackTraces(false)
    public class Evaluator implements AnnotationEvaluator<ParametersPassesExpression, MethodCall<?>> {

        protected static final ParserContext MESSAGE_ON_VIOLATION_EXPRESSION_PARSER_CONTEXT = new TemplateParserContext("${", "}");
        protected static final List<PropertyAccessor> DEFAULT_PROPERTY_ACCESSORS = Arrays.asList(new MethodCallPropertyAccessor(), new AccessAlsoProtectedMembersReflectivePropertyAccessor(), new MapPropertyAccessor());

        private ExpressionParser _expressionParser = new SpelExpressionParser();

        public void setExpressionParser(@Nonnull ExpressionParser expressionParser) {
            _expressionParser = requireNonNull("expressionParser", expressionParser);
        }

        @Nonnull
        public ExpressionParser getExpressionParser() {
            return _expressionParser;
        }

        @Override
        public void evaluate(@Nonnull ParametersPassesExpression annotation, @Nonnull ElementType elementType, @Nonnull String elementName, @Nonnull MethodCall<?> value) {
            requireNonNull("annotation", annotation);
            requireNonNull("elementType", elementType);
            requireNonNull("elementName", elementName);
            requireNonNull("elementName", value);
            if (elementType != METHOD) {
                throw new IllegalArgumentException("This evaluator only supports " + METHOD + " elementTypes.");
            }
            if (!value.getMethodName().equals(elementName)) {
                throw new IllegalArgumentException("The provided elementName ('" + elementName + "') is not the same as the name ('" + value.getMethodName() + "') of the provided method call.");
            }
            try {
                final Expression expression = _expressionParser.parseExpression(annotation.value());
                final Boolean checkResult = evaluateExpression(annotation, Boolean.class, expression, value);
                if (!Boolean.TRUE.equals(checkResult)) {
                    throwMessageFor(annotation, value, null);
                }
            } catch (Exception e) {
                throwMessageFor(annotation, value, e);
            }
        }

        @Nullable
        protected <T> T evaluateExpression(@Nonnull ParametersPassesExpression annotation, @Nonnull Class<T> expectedResultType, @Nonnull Expression expression, @Nonnull MethodCall<?> methodCall) {
            final StandardEvaluationContext context = toContext(methodCall);
            context.setVariable("expression", annotation.value());
            return expression.getValue(context, expectedResultType);
        }

        @Nonnull
        protected StandardEvaluationContext toContext(@Nonnull MethodCall<?> methodCall) {
            final StandardEvaluationContext context = new StandardEvaluationContext(methodCall);
            context.setVariable("methodName", methodCall.getMethodName());
            context.setVariable("parameters", methodCall.getParameters());
            context.setPropertyAccessors(DEFAULT_PROPERTY_ACCESSORS);
            return context;
        }

        @Nonnull
        protected void throwMessageFor(@Nonnull ParametersPassesExpression annotation, @Nonnull MethodCall<?> methodCall, @Nullable Throwable cause) {
            final String messageOnViolation = formatViolationMessage(annotation, methodCall);
            throwMessage(IllegalArgumentException.class, messageOnViolation, cause);
        }

        @Nonnull
        protected String formatViolationMessage(@Nonnull ParametersPassesExpression annotation, @Nonnull MethodCall<?> methodCall) {
            final String messageOnViolation;
            final String plainMessageOnViolation = annotation.messageOnViolation();
            if (plainMessageOnViolation != null && !plainMessageOnViolation.trim().isEmpty()) {
                final Expression expression = _expressionParser.parseExpression(plainMessageOnViolation, MESSAGE_ON_VIOLATION_EXPRESSION_PARSER_CONTEXT);
                messageOnViolation = evaluateExpression(annotation, String.class, expression, methodCall);
            } else {
                messageOnViolation = "Expression not passed: " + annotation.value();
            }
            return messageOnViolation;
        }
    }
}
