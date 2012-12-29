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

package org.echocat.redprecursor.handler;

import org.echocat.redprecursor.annotations.ParametersPassesExpression;
import org.echocat.redprecursor.annotations.utils.MethodCallUtils;
import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Node;
import org.echocat.redprecursor.compilertree.base.Statement;
import org.echocat.redprecursor.compilertree.util.AnnotationUtils;
import org.echocat.redprecursor.evaluation.AnnotationEvaluationExecuter;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class ParametersPassesExpressionAnnotationHandler extends MethodBasedHandler {

    @Override
    protected void handleMethod(@Nonnull Request request, @Nonnull final MethodStatement methodStatement, @Nonnull Node parent) {
        requireNonNull("request", request);
        requireNonNull("methodStatement", methodStatement);
        requireNonNull("parent", parent);
        final Annotation annotation = AnnotationUtils.findAnnotationDeclarationAt(methodStatement.getMethod(), ParametersPassesExpression.class);
        if (annotation != null) {
            final ExpressionProducerImpl expressionProducer = new ExpressionProducerImpl(getNodeFactory());
            final AnnotationMetaBasedStatementProducer.Request producingRequest = new AnnotationMetaBasedStatementProducer.Request() {
                @Nonnull @Override public Annotation getAnnotation() { return annotation; }
                @Nonnull @Override public MethodStatement getMethodStatement() { return methodStatement; }
            };
            final Expression expression = expressionProducer.produce(producingRequest);
            final Statement statement = getNodeFactory().createExpressionStatement(expression);
            prependStatementsToBody(methodStatement.getMethod(), statement);
        }
    }

    protected static class ExpressionProducerImpl extends AnnotationMetaBasedStatementProducer<AnnotationMetaBasedStatementProducer.Request> {

        private ExpressionProducerImpl(@Nonnull NodeFactory nodeFactory) {
            super(nodeFactory);
        }

        @Override
        @Nonnull
        public Expression produce(@Nonnull Request request) {
            requireNonNull("request", request);
            final NodeFactory nodeFactory = getNodeFactory();

            final MethodInvocation toMethodCallInvocation = nodeFactory.createMethodInvocation(
                nodeFactory.createFieldAccess(nodeFactory.createIdentifier(MethodCallUtils.class.getName()), "toMethodCall"),
                createArgumentsFor(nodeFactory, request)
            );

            final MethodInvocation executeInvocation = nodeFactory.createMethodInvocation(
                nodeFactory.createFieldAccess(nodeFactory.createIdentifier(AnnotationEvaluationExecuter.class.getName()), "execute"),
                getIdentifierOfAnnotationAndMetaInstance(request),
                nodeFactory.createFieldAccess(nodeFactory.createIdentifier(ElementType.class.getName()), METHOD.name()),
                nodeFactory.createLiteral(request.getMethodStatement().getMethod().getName()),
                toMethodCallInvocation
            );
            return executeInvocation;

        }

        @Nonnull
        protected List<Expression> createArgumentsFor(@Nonnull NodeFactory nodeFactory, @Nonnull Request request) {
            requireNonNull("nodeFactory", nodeFactory);
            requireNonNull("request", request);
            final MethodDeclaration method = request.getMethodStatement().getMethod();
            final String methodName = method.getName();
            final List<Expression> arguments = new ArrayList<Expression>();
            arguments.add(createThisClassAccessFor(nodeFactory, request));
            arguments.add(createThisAccessFor(nodeFactory, request));
            arguments.add(nodeFactory.createLiteral(methodName));
            addParameterNameAndValuesTo(nodeFactory, method, arguments);
            return arguments;
        }

        @Nonnull
        protected FieldAccess createThisClassAccessFor(@Nonnull NodeFactory nodeFactory, @Nonnull Request request) {
            requireNonNull("nodeFactory", nodeFactory);
            requireNonNull("request", request);
            return nodeFactory.createFieldAccess(
                nodeFactory.createIdentifier(request.getMethodStatement().getCurrentClass().getFullName()),
                "class"
            );
        }

        @Nonnull
        protected Expression createThisAccessFor(@Nonnull NodeFactory nodeFactory, @Nonnull Request request) {
            requireNonNull("nodeFactory", nodeFactory);
            requireNonNull("request", request);
            return isStatic(request) ? nodeFactory.createLiteral(null) : nodeFactory.createIdentifier("this");
        }

        protected void addParameterNameAndValuesTo(@Nonnull NodeFactory nodeFactory, @Nonnull MethodDeclaration ofMethod, @Nonnull List<Expression> target) {
            requireNonNull("nodeFactory", nodeFactory);
            requireNonNull("ofMethod", ofMethod);
            requireNonNull("target", target);
            final List<? extends VariableDeclaration> parameterDeclarations = ofMethod.getParameterDeclarations();
            if (parameterDeclarations != null) {
                for (VariableDeclaration parameterDeclaration : parameterDeclarations) {
                    target.add(nodeFactory.createLiteral(parameterDeclaration.getName()));
                    target.add(nodeFactory.createIdentifier(parameterDeclaration.getName()));
                }
            }
        }

        protected boolean isStatic(@Nonnull Request request) {
            requireNonNull("request", request);
            final Modifiers modifiers = request.getMethodStatement().getMethod().getModifiers();
            return isStatic(modifiers);
        }

        protected boolean isStatic(@Nonnull Modifiers modifiers) {
            requireNonNull("modifiers", modifiers);
            return modifiers != null && modifiers.getModifier() != null && modifiers.getModifier().contains(Modifier.STATIC);
        }
    }
}
