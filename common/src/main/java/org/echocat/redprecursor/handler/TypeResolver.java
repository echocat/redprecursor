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

import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.TypeEnabledNode;

import javax.annotation.Nonnull;
import java.util.*;

import static java.util.Collections.unmodifiableMap;

public class TypeResolver {

    private TypeResolver() {}

    @Nonnull
    public static Identifier resolveRawIdentifier(@Nonnull InClassStatement statement, @Nonnull NodeFactory nodeFactory, @Nonnull Identifier toResolve) {
        final Map<String, TypeParameter> nameToParameterType = getNameToTypeParameterFor(statement);
        Identifier identifier = toResolve;
        while (nameToParameterType.containsKey(getRawIdentifierName(identifier)) || identifier instanceof TypeApply) {
            if (identifier instanceof TypeApply) {
                identifier = resolveRawIdentifier((TypeApply) identifier);
            } else {
                final TypeParameter typeParameter = nameToParameterType.get(getRawIdentifierName(identifier));
                identifier = resolveRawIdentifier(nodeFactory, typeParameter, toResolve);
            }
        }
        return identifier;
    }

    @Nonnull
    private static String getRawIdentifierName(@Nonnull Identifier identifier) {
        final String result;
        if (identifier instanceof TypeEnabledNode) {
            result = ((TypeEnabledNode) identifier).getType().getStringRepresentation();
        } else {
            result = identifier.getStringRepresentation();
        }
        return result;
    }

    @Nonnull
    private static Identifier resolveRawIdentifier(@Nonnull NodeFactory nodeFactory, @Nonnull TypeParameter typeParameter, @Nonnull Identifier toResolve) {
        final Identifier identifier;
        final List<? extends Expression> bounds = typeParameter.getBounds();
        if (bounds.isEmpty()) {
            if (toResolve instanceof ArrayType) {
                identifier = nodeFactory.createArrayType(nodeFactory.createIdentifier(Object.class.getName()));
            } else {
                identifier = nodeFactory.createIdentifier(Object.class.getName());
            }
        } else {
            final Expression expression = bounds.get(0);
            if (!(expression instanceof Identifier)) {
                throw new IllegalArgumentException("The expression " + toResolve + " is not of type identifier. It contains: " + expression);
            }
            if (toResolve instanceof ArrayType) {
                identifier = nodeFactory.createArrayType(expression);
            } else {
                identifier = (Identifier) expression;
            }
        }
        return identifier;
    }

    @Nonnull
    public static Identifier resolveRawIdentifier(@Nonnull TypeApply toResolve) {
        final Expression raw = toResolve.getRaw();
        if (!(raw instanceof Identifier)) {
            throw new IllegalArgumentException("The typeApply " + toResolve + " does not contain a raw value of type identifier. It contains: " + raw);
        }
        return (Identifier) raw;
    }



    @Nonnull
    public static Collection<TypeParameter> getTypeParametersFor(@Nonnull InClassStatement statement) {
        return getNameToTypeParameterFor(statement).values();
    }

    @Nonnull
    public static Map<String, TypeParameter> getNameToTypeParameterFor(@Nonnull InClassStatement statement) {
        final Map<String, TypeParameter> result = new HashMap<String, TypeParameter>();
        result.putAll(getNameToTypeParameterForClassOnly(statement));
        if (statement instanceof MethodStatement) {
            result.putAll(getNameToTypeParameterForMethodOnly((MethodStatement) statement));
        }
        return unmodifiableMap(result);
    }

    @Nonnull
    public static Map<String, TypeParameter> getNameToTypeParameterForClassOnly(@Nonnull InClassStatement statement) {
        final Map<String, TypeParameter> result = new HashMap<String, TypeParameter>();
        final ClassDeclaration[] declarations = statement.getDeclarations();
        if (declarations.length == 1) {
            addTypeParametersToMapIfNotExists(result, declarations[0]);
        } else {
            for (int i = declarations.length - 1; i >= 0; i--) {
                final ClassDeclaration declaration = declarations[i];
                addTypeParametersToMapIfNotExists(result, declaration);
                if (isStatic(declaration)) {
                    break;
                }
            }
        }
        return unmodifiableMap(result);
    }

    @Nonnull
    public static Map<String, TypeParameter> getNameToTypeParameterForMethodOnly(@Nonnull MethodStatement statement) {
        final Map<String, TypeParameter> result = new HashMap<String, TypeParameter>();
        final MethodDeclaration method = statement.getMethod();
        addTypeParametersToMapIfNotExists(result, method);
        return unmodifiableMap(result);
    }

    @Nonnull
    private static List<TypeParameter> getTypeParametersFor(@Nonnull ClassDeclaration declaration) {
        @SuppressWarnings("unchecked")
        final List<TypeParameter> typeParameters = (List<TypeParameter>) declaration.getTypeParameters();
        return typeParameters != null ? typeParameters : Collections.<TypeParameter>emptyList();
    }

    @Nonnull
    private static List<TypeParameter> getTypeParametersFor(@Nonnull MethodDeclaration declaration) {
        @SuppressWarnings("unchecked")
        final List<TypeParameter> typeParameters = (List<TypeParameter>) declaration.getTypeParameters();
        return typeParameters != null ? typeParameters : Collections.<TypeParameter>emptyList();
    }

    private static void addTypeParametersToMapIfNotExists(@Nonnull Map<String, TypeParameter> to, @Nonnull ClassDeclaration declaration) {
        addTypeParametersToMapIfNotExists(to, getTypeParametersFor(declaration));
    }

    private static void addTypeParametersToMapIfNotExists(@Nonnull Map<String, TypeParameter> to, @Nonnull MethodDeclaration declaration) {
        addTypeParametersToMapIfNotExists(to, getTypeParametersFor(declaration));
    }

    private static void addTypeParametersToMapIfNotExists(@Nonnull Map<String, TypeParameter> to, @Nonnull List<TypeParameter> typeParameters) {
        for (TypeParameter typeParameter : typeParameters) {
            if (!to.containsKey(typeParameter.getName())) {
                to.put(typeParameter.getName(), typeParameter);
            }
        }
    }

    private static boolean isStatic(@Nonnull ClassDeclaration declaration) {
        final Modifiers modifiers = declaration.getModifiers();
        final boolean result;
        if (modifiers != null) {
            final List<Modifier> modifier = modifiers.getModifier();
            result = modifier != null && modifier.contains(Modifier.STATIC);
        } else {
            result = false;
        }
        return result;
    }

}
