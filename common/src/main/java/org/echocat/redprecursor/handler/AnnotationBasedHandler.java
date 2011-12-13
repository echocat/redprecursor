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
    protected Statement generateStatementFor(@Nonnull StatementCreationRequest statementCreationRequest) {
        final AnnotationBasedEvaluationExecuterStatementProducer statementProducer = new AnnotationBasedEvaluationExecuterStatementProducer(getNodeFactory());
        return statementProducer.produce(statementCreationRequest);
    }

    @Override
    protected void handleMethod(@Nonnull Request request, @Nonnull MethodStatement methodStatement, @Nonnull Node parent) {
        final CompilationUnit compilationUnit = methodStatement.getCompilationUnit();
        final MethodDeclaration method = methodStatement.getMethod();
        final ClassDeclaration topClassDeclaration = methodStatement.getTopClass();
        final ClassDeclaration lastClassDeclaration = methodStatement.getCurrentClass();
        final Modifiers modifiers = method.getModifiers();
        final Collection<Annotation> handlableAnnotations = new ArrayList<Annotation>();
        for (Annotation annotation : modifiers.getAnnotations()) {
            if (canHandle(annotation)) {
                handlableAnnotations.add(annotation);
            }
        }
        if (handlableAnnotations.isEmpty()) {
            for (Node subNode : method.getAllEnclosedNodes()) {
                handle(request, subNode, method, null, compilationUnit, topClassDeclaration, lastClassDeclaration);
            }
        } else {
            for (Node subNode : method.getAllEnclosedNodes()) {
                handle(request, subNode, method, new ParentMethodWithHandleableAnnotations(method, handlableAnnotations), compilationUnit, topClassDeclaration, lastClassDeclaration);
            }
        }
        handleMethodParameters(request, new MethodStatement(compilationUnit, topClassDeclaration, lastClassDeclaration, method));
    }

    @Override
    protected void handleOther(@Nonnull Request request, @Nonnull Node node, @Nonnull Node parent, @Nonnull ParentMethod parentMethod, @Nonnull CompilationUnit compilationUnit, @Nullable ClassDeclaration topClassDeclaration, @Nullable ClassDeclaration lastClassDeclaration) {
        if (node instanceof Return && parentMethod != null) {
            final MethodStatement methodStatement = new MethodStatement(compilationUnit, topClassDeclaration, lastClassDeclaration, parentMethod.getMethod());
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
            final Statement statement = generateStatementFor(statementCreationRequest);
            if (statement != null) {
                statementsToPrependToReturn.add(statement);
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
        final ListIterator<? extends VariableDeclaration> i = methodStatement.getMethod().getParameterDeclarations().listIterator();
        while (i.hasNext()) {
            final int parameterIndex = i.nextIndex();
            final VariableDeclaration parameter = i.next();
            for (Annotation annotation : parameter.getModifiers().getAnnotations()) {
                if (canHandle(annotation)) {
                    final Statement statement = checkParameterAndCreateStatement(request, new MethodParameterStatement(methodStatement, parameter, parameterIndex), annotation);
                    if (statement != null) {
                        statementsToPrepend.add(statement);
                    }
                }
            }
        }
        prependStatementsToBody(methodStatement.getMethod(), statementsToPrepend);
    }

    @Nullable
    protected Statement checkParameterAndCreateStatement(@Nonnull Request request, @Nonnull MethodParameterStatement methodParameterStatement, @Nonnull Annotation annotation) {
        requireNonNull("request", request);
        requireNonNull("methodParameterStatement", methodParameterStatement);
        requireNonNull("annotation", annotation);
        final NodeFactory nodeFactory = requireNonNull("Property nodeFactory", getNodeFactory());
        final String parameterName = methodParameterStatement.getParameter().getName();
        final Key variableNameId = nodeFactory.createKey(parameterName);

        final VariableDeclaration parameter = methodParameterStatement.getParameter();
        assertThatTypeCouldHandled(parameter.getType(), annotation);
        final StatementCreationRequest statementCreationRequest = new StatementCreationRequest(request, Principal.PARAMETER, annotation, variableNameId, methodParameterStatement.getParameter().getType(), methodParameterStatement);
        final Statement statement = generateStatementFor(statementCreationRequest);
        return statement;
    }
}
