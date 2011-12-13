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
            final StatementProducerImpl statementProducer = new StatementProducerImpl(getNodeFactory());
            final AnnotationMetaBasedStatementProducer.Request producingRequest = new AnnotationMetaBasedStatementProducer.Request() {
                @Nonnull @Override public Annotation getAnnotation() { return annotation; }
                @Nonnull @Override public MethodStatement getMethodStatement() { return methodStatement; }
            };
            final Statement statement = statementProducer.produce(producingRequest);
            prependStatementsToBody(methodStatement.getMethod(), statement);
        }
    }

    protected static class StatementProducerImpl extends AnnotationMetaBasedStatementProducer<AnnotationMetaBasedStatementProducer.Request> {

        private StatementProducerImpl(@Nonnull NodeFactory nodeFactory) {
            super(nodeFactory);
        }

        @Override
        @Nonnull
        public Statement produce(@Nonnull Request request) {
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
            return nodeFactory.createExpressionStatement(executeInvocation);

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
