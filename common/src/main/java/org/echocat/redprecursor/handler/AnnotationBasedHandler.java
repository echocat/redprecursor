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
 * Portions created by the Initial Developer are Copyright (C) 2012
 * the Initial Developer. All Rights Reserved.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.redprecursor.handler;

import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.BodyNode;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Node;
import org.echocat.redprecursor.compilertree.base.Statement;
import org.echocat.redprecursor.compilertree.util.PositionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Collections.singletonList;
import static org.echocat.redprecursor.compilertree.Modifier.ABSTRACT;
import static org.echocat.redprecursor.compilertree.Modifier.INTERFACE;
import static org.echocat.redprecursor.handler.Principal.RETURN;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public abstract class AnnotationBasedHandler extends MethodBasedHandler {

    public static class ParentMethodWithHandleableAnnotations extends ParentMethod {

        private final Collection<Annotation> _handlableAnnotations;

        public ParentMethodWithHandleableAnnotations(@Nonnull MethodDeclaration method, @Nonnull Collection<Annotation> handlableAnnotations) {
            super(method);
            _handlableAnnotations = requireNonNull("handlableAnnotations", handlableAnnotations);
        }

        @Nonnull
        public Collection<Annotation> getHandlableAnnotations() {
            return _handlableAnnotations;
        }
    }

    @Target({ TYPE })
    @Retention(RUNTIME)
    public static @interface HandlesAnnotations {

        public Class<? extends java.lang.annotation.Annotation>[] value();
    }

    protected boolean canHandle(@Nonnull Annotation annotation) {
        final Identifier identifier = requireNonNull("annotation", annotation).getType();
        final String annotationTypeName = identifier.getStringRepresentation();
        final HandlesAnnotations handlesAnnotationsAnnoation = getClass().getAnnotation(HandlesAnnotations.class);
        if (handlesAnnotationsAnnoation == null) {
            throw new IllegalArgumentException(getClass().getName() + " does not define @" + HandlesAnnotations.class.getSimpleName() + " and does not overwrite canHandle(Annotation).");
        }
        final Class<? extends java.lang.annotation.Annotation>[] handlesAnnotations = handlesAnnotationsAnnoation.value();
        boolean canHandle = false;
        for (int i = 0; !canHandle && i < handlesAnnotations.length; i++) {
            final Class<? extends java.lang.annotation.Annotation> handlesAnnotation = handlesAnnotations[i];
            if (handlesAnnotation.getName().equals(annotationTypeName)) {
                canHandle = true;
            }
        }
        return canHandle;
    }

    /**
     * @throws IllegalArgumentException if the given forAnnotation could not handle typeToCheck.
     */
    protected void assertThatTypeCouldHandled(@Nonnull Identifier typeToCheck, @Nonnull Annotation forAnnotation) {}

    @Nullable
    protected Expression generateExpressionFor(@Nonnull StatementCreationRequest statementCreationRequest) {
        final AnnotationBasedEvaluationExecuterStatementProducer statementProducer = new AnnotationBasedEvaluationExecuterStatementProducer(getNodeFactory());
        return statementProducer.produce(statementCreationRequest);
    }

    protected boolean handleAbstractMethods(@Nonnull Request request, @Nonnull MethodStatement methodStatement, @Nonnull Node parent) {
        return false;
    }

    protected boolean isAbstractMethod(@Nonnull Request request, @Nonnull MethodStatement methodStatement, @Nonnull Node parent) {
        boolean isAbstract = false;
        final Modifiers methodModifiers = methodStatement.getMethod().getModifiers();
        if (methodModifiers != null) {
            final List<Modifier> allModifiers = methodModifiers.getModifier();
            isAbstract = allModifiers != null && allModifiers.contains(ABSTRACT);
        }
        if (!isAbstract) {
            final Modifiers classModifiers = methodStatement.getCurrentClass().getModifiers();
            if (classModifiers != null) {
                final List<Modifier> allModifiers = classModifiers.getModifier();
                isAbstract = allModifiers != null && allModifiers.contains(INTERFACE);
            }
        }
        return isAbstract;
    }

    @Override
    protected void handleMethod(@Nonnull Request request, @Nonnull MethodStatement methodStatement, @Nonnull Node parent) {
        if (!isAbstractMethod(request, methodStatement, parent) || handleAbstractMethods(request, methodStatement, parent)) {
            final CompilationUnit compilationUnit = methodStatement.getCompilationUnit();
            final MethodDeclaration method = methodStatement.getMethod();
            final Modifiers modifiers = method.getModifiers();
            final Collection<Annotation> handlableAnnotations = new ArrayList<Annotation>();
            for (Annotation annotation : modifiers.getAnnotations()) {
                if (canHandle(annotation)) {
                    handlableAnnotations.add(annotation);
                }
            }
            if (handlableAnnotations.isEmpty()) {
                for (Node subNode : method.getAllEnclosedNodes()) {
                    handle(request, subNode, method, null, compilationUnit, methodStatement.getDeclarations());
                }
            } else {
                for (Node subNode : method.getAllEnclosedNodes()) {
                    handle(request, subNode, method, new ParentMethodWithHandleableAnnotations(method, handlableAnnotations), compilationUnit, methodStatement.getDeclarations());
                }
            }
            handleMethodParameters(request, new MethodStatement(compilationUnit, method, methodStatement.getDeclarations()));
        }
    }

    @Override
    protected void handleOther(@Nonnull Request request, @Nonnull Node node, @Nonnull Node parent, @Nonnull ParentMethod parentMethod, @Nonnull CompilationUnit compilationUnit, @Nullable ClassDeclaration[] classDeclarations) {
        if (node instanceof Return && parentMethod != null) {
            final MethodStatement methodStatement = new MethodStatement(compilationUnit, parentMethod.getMethod(), classDeclarations);
            handleReturn(request, methodStatement, (Return) node, parent, ((ParentMethodWithHandleableAnnotations) parentMethod).getHandlableAnnotations());
        }
    }

    protected void handleReturn(@Nonnull Request request, @Nonnull MethodStatement methodStatement, @Nonnull Return ret, @Nonnull Node parent, @Nonnull Collection<Annotation> handleAnnotations) {
        requireNonNull("request", request);
        requireNonNull("methodStatement", methodStatement);
        requireNonNull("ret", ret);
        requireNonNull("parent", parent);
        requireNonNull("handleAnnotations", handleAnnotations);
        final List<Statement> statementsToPrependToReturn = new ArrayList<Statement>();
        final Identifier identifierToCheck = getIdentifierToCheck(methodStatement, ret, statementsToPrependToReturn);
        prependStatementsToReturn(ret, parent, statementsToPrependToReturn);
        statementsToPrependToReturn.clear();
        for (Annotation annotation : handleAnnotations) {
            final MethodDeclaration method = methodStatement.getMethod();
            assertThatTypeCouldHandled(method.getResultType(), annotation);
            final StatementCreationRequest statementCreationRequest = new StatementCreationRequest(request, RETURN, annotation, identifierToCheck, methodStatement.getMethod().getResultType(), methodStatement);
            final Expression statement = generateExpressionFor(statementCreationRequest);
            if (statement != null) {
                statementsToPrependToReturn.add(getNodeFactory().createExpressionStatement(statement));
            }
        }
        prependStatementsToReturn(ret, parent, statementsToPrependToReturn);
    }

    protected void prependStatementsToReturn(@Nonnull Return ret, @Nonnull Node parent, @Nonnull List<Statement> statementsToPrependToReturn) {
        requireNonNull("ret", ret);
        requireNonNull("parent", parent);
        requireNonNull("statementsToPrependToReturn", statementsToPrependToReturn);
        if (!statementsToPrependToReturn.isEmpty()) {
            for (Statement statement : statementsToPrependToReturn) {
                PositionUtils.setPositionRecursive(statement, ret.getPosition());
            }
            if (!(parent instanceof BodyNode)) {
                throw new IllegalStateException("The parent (" + parent + ") of " + ret + " is not an instance of '" + BodyNode.class.getName() + "'?");
            }
            // noinspection unchecked
            final BodyNode<Node> enclosingNode = (BodyNode<Node>) parent;
            final List<Node> subNodes = new ArrayList<Node>(enclosingNode.getBody());
            final ListIterator<Node> i = subNodes.listIterator();
            int indexOfReturn = -1;
            while (i.hasNext() && indexOfReturn < 0) {
                final int potentialIndexOfReturn = i.nextIndex();
                final Node subNode = i.next();
                if (subNode instanceof Return && ret.getPosition().equals(subNode.getPosition())) {
                    indexOfReturn = potentialIndexOfReturn;
                }
            }
            if (indexOfReturn < 0) {
                throw new IllegalStateException("Could not find " + ret + " in parent node (" + parent + ").");
            }
            subNodes.addAll(indexOfReturn, statementsToPrependToReturn);
            enclosingNode.setBody(subNodes);
        }
    }

    @Nonnull
    protected Identifier getIdentifierToCheck(@Nonnull MethodStatement methodStatement, @Nonnull Return ret, @Nonnull List<Statement> statementsToPrependToReturn) {
        requireNonNull("methodStatement", methodStatement);
        requireNonNull("ret", ret);
        requireNonNull("statementsToPrependToReturn", statementsToPrependToReturn);
        Expression expressionToCheck = ret.getExpression();
        if (!(expressionToCheck instanceof Identifier)) {
            final NodeFactory nodeFactory = requireNonNull("Property nodeFactory", getNodeFactory());
            final String variableName = "generated$" + UUID.randomUUID().toString().replace('-', '$');
            final VariableDeclaration variableDeclaration = nodeFactory.createVariableDeclaration(variableName, methodStatement.getMethod().getResultType());
            variableDeclaration.setInitialValue(expressionToCheck);
            final Modifiers modifiers = variableDeclaration.getModifiers();
            modifiers.setModifiers(singletonList(Modifier.FINAL));
            expressionToCheck = nodeFactory.createIdentifier(variableName);
            ret.setExpression(expressionToCheck);
            statementsToPrependToReturn.add(variableDeclaration);
        }
        return (Identifier) expressionToCheck;
    }

    protected void handleMethodParameters(@Nonnull Request request, @Nonnull MethodStatement methodStatement) {
        requireNonNull("request", request);
        requireNonNull("methodStatement", methodStatement);
        final List<Statement> statementsToPrepend = new ArrayList<Statement>();
        final MethodDeclaration method = methodStatement.getMethod();
        final ListIterator<? extends VariableDeclaration> i = method.getParameterDeclarations().listIterator();
        while (i.hasNext()) {
            final int parameterIndex = i.nextIndex();
            final VariableDeclaration parameter = i.next();
            for (Annotation annotation : parameter.getModifiers().getAnnotations()) {
                if (canHandle(annotation)) {
                    final Expression expression = checkParameterAndCreateExpression(request, new MethodParameterStatement(methodStatement, parameter, parameterIndex), annotation);
                    if (expression != null) {
                        statementsToPrepend.add(getNodeFactory().createExpressionStatement(expression));
                    }
                }
            }
        }
        prependStatementsToBody(method, statementsToPrepend);
    }

    @Nullable
    protected Expression checkParameterAndCreateExpression(@Nonnull Request request, @Nonnull MethodParameterStatement methodParameterStatement, @Nonnull Annotation annotation) {
        requireNonNull("request", request);
        requireNonNull("methodParameterStatement", methodParameterStatement);
        requireNonNull("annotation", annotation);
        final NodeFactory nodeFactory = requireNonNull("Property nodeFactory", getNodeFactory());
        final VariableDeclaration parameter = methodParameterStatement.getParameter();
        final String parameterName = parameter.getName();
        final Key variableNameId = nodeFactory.createKey(parameterName);

        assertThatTypeCouldHandled(parameter.getType(), annotation);
        final StatementCreationRequest statementCreationRequest = new StatementCreationRequest(request, Principal.PARAMETER, annotation, variableNameId, parameter.getType(), methodParameterStatement);
        return generateExpressionFor(statementCreationRequest);
    }
}
