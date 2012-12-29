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

import org.echocat.redprecursor.annotations.AnnotationDiscovery;
import org.echocat.redprecursor.annotations.AnnotationProxier;
import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Declaration;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.handler.AnnotationMetaBasedStatementProducer.Request;
import org.echocat.redprecursor.meta.AnnotationAndMeta;

import javax.annotation.Generated;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DateFormat;
import java.util.*;
import java.util.Map.Entry;

import static java.text.DateFormat.MEDIUM;
import static java.util.Arrays.asList;
import static java.util.Locale.US;
import static org.echocat.redprecursor.compilertree.Modifier.*;
import static org.echocat.redprecursor.handler.TypeResolver.resolveRawIdentifier;
import static org.echocat.redprecursor.util.ModifiersUtil.hasModifier;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

/**
 * <p>Base class for producing statements based on the annotation provided by {@link Request#getAnnotation()}.</p>
 *
 * <p>The implementing class could use {@link #getIdentifierOfAnnotationAndMetaInstance(Request)} to get an identifier which references the runtime instance
 * of {@link AnnotationAndMeta} which contains the requested annotation. This instance could be used to create in {@link #produce(Request)} statements based on
 * this annotation.</p>
 */
public abstract class AnnotationMetaBasedStatementProducer<T extends Request> {

    private static volatile long c_currentFieldNumber = 1;
    private static final Map<String, Long> CLASS_NAME_TO_FIELD_NUMBER = new HashMap<String, Long>();

    public static final String ANNOTATION_METAS_FIELD_NAME_PREFIX = "generated$redprecursor$annotationMetas";
    public static final String ANNOTATION_METAS_CLASS_NAME_SUFFIX = "$Generated$Redprecursor";

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
    public abstract Expression produce(@Nonnull T request);

    @Nonnull
    protected Identifier getIdentifierOfAnnotationAndMetaInstance(@Nonnull T request) {
        final MethodStatement methodStatement = request.getMethodStatement();
        final ClassDeclaration targetClass = selectTargetClassFor(methodStatement);
        final List<Declaration> declarations = new ArrayList<Declaration>(targetClass.getDeclarations());
        VariableDeclaration fieldDeclaration = null;
        for (Declaration declaration : declarations) {
            if (declaration instanceof VariableDeclaration) {
                final VariableDeclaration existingFieldDeclaration = (VariableDeclaration) declaration;
                if (existingFieldDeclaration.getName().startsWith(ANNOTATION_METAS_FIELD_NAME_PREFIX)) {
                    fieldDeclaration = existingFieldDeclaration;
                }
            }
        }
        if (fieldDeclaration == null) {
            fieldDeclaration = createFieldDeclaration(targetClass, null);
            declarations.add(0, fieldDeclaration);
            targetClass.setDeclarations(declarations);
        }
        final int index = setInitialValueOfFieldDeclarationAndReturnCurrentIndex(request, fieldDeclaration);
        final FieldAccess arrayFieldAccess = _nodeFactory.createFieldAccess(_nodeFactory.createIdentifier(targetClass.getFullName()), fieldDeclaration.getName());
        return _nodeFactory.createArrayAccess(arrayFieldAccess, _nodeFactory.createLiteral(index));
    }

    @Nonnull
    protected ClassDeclaration selectTargetClassFor(@Nonnull MethodStatement methodStatement) {
        final ClassDeclaration topClass = methodStatement.getTopClass();

        final ClassDeclaration result;
        if (hasModifier(topClass, ENUM) || hasModifier(topClass, INTERFACE)) {
            final CompilationUnit compilationUnit = methodStatement.getCompilationUnit();
            final List<Declaration> declarations = new ArrayList<Declaration>(compilationUnit.getDeclarations());
            final String targetClassName = topClass.getFullName() + ANNOTATION_METAS_CLASS_NAME_SUFFIX;
            ClassDeclaration extraClass = null;
            for (Declaration declaration : declarations) {
                if (declaration instanceof ClassDeclaration) {
                    if (targetClassName.equals(((ClassDeclaration) declaration).getFullName())) {
                        extraClass = (ClassDeclaration) declaration;
                    }
                }
            }
            if (extraClass == null) {
                final Modifiers modifier = _nodeFactory.createModifier(asList(FINAL));
                modifier.setAnnotations(asList(createGeneratedAnnotation()));
                extraClass = _nodeFactory.createClassDeclaration(targetClassName, modifier);
                declarations.add(extraClass);
                compilationUnit.setDeclarations(declarations);
            }
            result = extraClass;
        } else {
            result = topClass;
        }
        return result;
    }

    @Nonnull
    protected VariableDeclaration createFieldDeclaration(@Nonnull ClassDeclaration forClass, @Nullable Modifier access) {
        final Identifier identifier = _nodeFactory.createIdentifier(AnnotationAndMeta.class.getName());
        final Wildcard wildcard1 = _nodeFactory.createWildcard(WildcardKind.UNBOUND, null);
        final Wildcard wildcard2 = _nodeFactory.createWildcard(WildcardKind.UNBOUND, null);
        final TypeApply typeApply = _nodeFactory.createTypeApply(identifier, Arrays.<Identifier>asList(wildcard1, wildcard2));
        final ArrayType arrayType = _nodeFactory.createArrayType(typeApply);
        final VariableDeclaration fieldDeclaration = _nodeFactory.createVariableDeclaration(ANNOTATION_METAS_FIELD_NAME_PREFIX + getFieldNumberFor(forClass), arrayType);

        fieldDeclaration.setModifiers(createModifiers(access));
        return fieldDeclaration;
    }

    @Nonnegative
    protected long getFieldNumberFor(@Nonnull ClassDeclaration classDeclaration) {
        synchronized (CLASS_NAME_TO_FIELD_NUMBER) {
            final String fullName = classDeclaration.getFullName();
            Long fieldNumber = CLASS_NAME_TO_FIELD_NUMBER.get(fullName);
            if (fieldNumber == null) {
                fieldNumber = c_currentFieldNumber++;
                CLASS_NAME_TO_FIELD_NUMBER.put(fullName, fieldNumber);
            }
            return fieldNumber;
        }
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
            final Identifier identifier = resolveRawIdentifier(methodStatement, _nodeFactory, parameterDeclaration.getType());
            methodParameters.add(_nodeFactory.createFieldAccess(identifier, "class"));
        }
        return methodParameters;
    }

    @Nonnull
    protected Modifiers createModifiers(@Nullable Modifier access) {
        final List<Modifier> effective = access != null ? asList(access, FINAL, STATIC) : asList(FINAL, STATIC);
        final Modifiers modifiers = _nodeFactory.createModifier(effective);
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
