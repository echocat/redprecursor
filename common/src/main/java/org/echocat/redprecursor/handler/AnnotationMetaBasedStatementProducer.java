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

import org.echocat.redprecursor.annotations.AnnotationDiscovery;
import org.echocat.redprecursor.annotations.AnnotationProxier;
import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Declaration;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Statement;
import org.echocat.redprecursor.handler.AnnotationMetaBasedStatementProducer.Request;
import org.echocat.redprecursor.meta.AnnotationAndMeta;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.util.*;
import java.util.Map.Entry;

import static java.text.DateFormat.MEDIUM;
import static java.util.Arrays.asList;
import static java.util.Locale.US;
import static org.echocat.redprecursor.compilertree.Modifier.*;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

/**
 * <p>Base class for producing statements based on the annotation provided by {@link Request#getAnnotation()}.</p>
 *
 * <p>The implementing class could use {@link #getIdentifierOfAnnotationAndMetaInstance(Request)} to get an identifier which references the runtime instance
 * of {@link AnnotationAndMeta} which contains the requested annotation. This instance could be used to create in {@link #produce(Request)} statements based on
 * this annotation.</p>
 */
public abstract class AnnotationMetaBasedStatementProducer<T extends Request> {

    public static final String ANNOTATION_METAS_FIELD_NAME = "generated$redprecursor$annotationMetas";

    public interface Request {
        @Nonnull public Annotation getAnnotation();
        @Nonnull public MethodStatement getMethodStatement();
    }

    private final NodeFactory _nodeFactory;

    public AnnotationMetaBasedStatementProducer(@Nonnull NodeFactory nodeFactory) {
        _nodeFactory = requireNonNull("nodeFactory", nodeFactory);
    }

    protected NodeFactory getNodeFactory() {
        return _nodeFactory;
    }

    @Nonnull
    public abstract Statement produce(@Nonnull T request);

    @Nonnull
    protected Identifier getIdentifierOfAnnotationAndMetaInstance(@Nonnull T request) {
        final MethodStatement methodStatement = request.getMethodStatement();
        final ClassDeclaration topClass = methodStatement.getTopClass();
        final List<Declaration> declarations = new ArrayList<Declaration>(topClass.getDeclarations());
        VariableDeclaration fieldDeclaration = null;
        for (Declaration declaration : declarations) {
            if (declaration instanceof VariableDeclaration) {
                final VariableDeclaration existingFieldDeclaration = (VariableDeclaration) declaration;
                if (ANNOTATION_METAS_FIELD_NAME.equals(existingFieldDeclaration.getName())) {
                    fieldDeclaration = existingFieldDeclaration;
                }
            }
        }
        if (fieldDeclaration == null) {
            fieldDeclaration = createFieldDeclaration();
            declarations.add(0, fieldDeclaration);
            topClass.setDeclarations(declarations);
        }
        final int index = setInitialValueOfFieldDeclarationAndReturnCurrentIndex(request, fieldDeclaration);
        final FieldAccess arrayFieldAccess = _nodeFactory.createFieldAccess(_nodeFactory.createIdentifier(methodStatement.getTopClass().getFullName()), fieldDeclaration.getName());
        return _nodeFactory.createArrayAccess(arrayFieldAccess, _nodeFactory.createLiteral(index));
    }

    @Nonnull
    protected VariableDeclaration createFieldDeclaration() {
        final Identifier identifier = _nodeFactory.createIdentifier(AnnotationAndMeta.class.getName());
        final Wildcard wildcard1 = _nodeFactory.createWildcard(WildcardKind.UNBOUND, null);
        final Wildcard wildcard2 = _nodeFactory.createWildcard(WildcardKind.UNBOUND, null);
        final TypeApply typeApply = _nodeFactory.createTypeApply(identifier, Arrays.<Identifier>asList(wildcard1, wildcard2));
        final ArrayType arrayType = _nodeFactory.createArrayType(typeApply);
        final VariableDeclaration fieldDeclaration = _nodeFactory.createVariableDeclaration(ANNOTATION_METAS_FIELD_NAME, arrayType);

        fieldDeclaration.setModifiers(createModifiers());
        return fieldDeclaration;
    }

    protected int setInitialValueOfFieldDeclarationAndReturnCurrentIndex(@Nonnull Request request, @Nonnull VariableDeclaration fieldDeclaration) {
        final Expression initialValue = fieldDeclaration.getInitialValue();
        final List<Expression> elements = new ArrayList<Expression>();
        if (initialValue != null) {
            if (!(initialValue instanceof NewArray)) {
                throw new IllegalStateException("The field '" + fieldDeclaration.getName() + "' was already but does not contain an " + NewArray.class.getName() + " definition. Current value: " + initialValue);
            }
            final NewArray newArray = (NewArray) initialValue;
            elements.addAll(newArray.getElements());
        }
        final int index = elements.size();
        elements.add(createMethodInvocation(request));
        fieldDeclaration.setInitialValue(_nodeFactory.createNewArrayValue(elements));
        return index;
    }

    @Nonnull
    protected MethodInvocation createMethodInvocation(@Nonnull Request request) {
        final MethodStatement methodStatement = request.getMethodStatement();
        final String classFullName = methodStatement.getCurrentClass().getFullName();
        final MethodInvocation methodInvocation;
        if (classFullName != null) {
            methodInvocation = createMethodInvocationForAnnotationDiscovery(request);
        } else {
            methodInvocation = createMethodInvocationForAnnotationProxy(request);
        }
        return methodInvocation;
    }

    @Nonnull
    protected MethodInvocation createMethodInvocationForAnnotationDiscovery(@Nonnull Request request) {
        final MethodStatement methodStatement = request.getMethodStatement();
        final MethodInvocation methodInvocation;
        final String methodName;
        if (request.getMethodStatement().getMethod().getResultType() == null) {
            methodName = methodStatement instanceof MethodParameterStatement ? "discoverConstructorParameterAnnotationAndMeta" : "discoverConstructorAnnotationAndMeta";
        } else {
            methodName = methodStatement instanceof MethodParameterStatement ? "discoverMethodParameterAnnotationAndMeta" : "discoverMethodAnnotationAndMeta";
        }
        final Identifier methodIdentifier = _nodeFactory.createIdentifier(AnnotationDiscovery.class.getName() + "." + methodName);
        final List<Expression> methodParameters = createMethodInvocationParameters(request);
        methodInvocation = _nodeFactory.createMethodInvocation(methodIdentifier, methodParameters);
        return methodInvocation;
    }

    @Nonnull
    protected MethodInvocation createMethodInvocationForAnnotationProxy(@Nonnull Request request) {
        final Annotation annotation = request.getAnnotation();
        final MethodInvocation methodInvocation;
        final Identifier methodIdentifier = _nodeFactory.createIdentifier(AnnotationProxier.class.getName() + ".createAnnotationAndMeta");
        final List<Expression> methodParameters = new ArrayList<Expression>();
        methodParameters.add(_nodeFactory.createFieldAccess(annotation.getType(), "class"));
        for (Entry<String, ? extends Expression> keyAndValue : annotation.getArguments().entrySet()) {
            methodParameters.add(_nodeFactory.createLiteral(keyAndValue.getKey()));
            methodParameters.add(keyAndValue.getValue());
        }
        methodInvocation = _nodeFactory.createMethodInvocation(methodIdentifier, methodParameters);
        return methodInvocation;
    }

    @Nonnull
    protected List<Expression> createMethodInvocationParameters(@Nonnull Request request) {
        final Annotation annotation = request.getAnnotation();
        final MethodStatement methodStatement = request.getMethodStatement();
        final List<Expression> methodParameters = new ArrayList<Expression>();
        methodParameters.add(_nodeFactory.createFieldAccess(annotation.getType(), "class"));
        final String fullName = methodStatement.getCurrentClass().getFullName();
        if (fullName != null) {
            methodParameters.add(_nodeFactory.createFieldAccess(_nodeFactory.createIdentifier(fullName), "class"));
        } else {
            methodParameters.add(_nodeFactory.createMethodInvocation(_nodeFactory.createIdentifier("getClass")));
        }
        if (methodStatement.getMethod().getResultType() != null) {
            // This is no constructor...
            methodParameters.add(_nodeFactory.createLiteral(methodStatement.getMethod().getName()));
        }
        if (methodStatement instanceof MethodParameterStatement) {
            methodParameters.add(_nodeFactory.createLiteral(((MethodParameterStatement) methodStatement).getParameterIndex()));
        }
        for (VariableDeclaration parameterDeclaration : methodStatement.getMethod().getParameterDeclarations()) {
            methodParameters.add(_nodeFactory.createFieldAccess(parameterDeclaration.getType(), "class"));
        }
        return methodParameters;
    }

    @Nonnull
    protected Modifiers createModifiers() {
        final Modifiers modifiers = _nodeFactory.createModifier(asList(PRIVATE, FINAL, STATIC));
        modifiers.setAnnotations(asList(createGeneratedAnnotation()));
        return modifiers;
    }

    @Nonnull
    protected Annotation createGeneratedAnnotation() {
        final Annotation annotation = _nodeFactory.createAnnotation(_nodeFactory.createIdentifier(Generated.class.getName()));
        final Map<String, Expression> arguments = new HashMap<String, Expression>();
        arguments.put("value", _nodeFactory.createLiteral("Generated by " + getClass().getName()));
        arguments.put("date", _nodeFactory.createLiteral(DateFormat.getDateTimeInstance(MEDIUM, MEDIUM, US).format(new Date())));
        annotation.setArguments(arguments);
        return annotation;
    }

}
