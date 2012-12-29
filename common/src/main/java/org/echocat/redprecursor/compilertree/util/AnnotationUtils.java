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

package org.echocat.redprecursor.compilertree.util;

import org.echocat.redprecursor.compilertree.Annotation;
import org.echocat.redprecursor.compilertree.MethodDeclaration;
import org.echocat.redprecursor.compilertree.Modifiers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class AnnotationUtils {

    @Nullable
    public static Annotation findAnnotationDeclarationAt(@Nonnull MethodDeclaration methodDeclaration, @Nonnull Class<? extends java.lang.annotation.Annotation> annotationTypeFoFind) {
        requireNonNull("methodDeclaration", methodDeclaration);
        requireNonNull("annotationTypeFoFind", annotationTypeFoFind);
        final Modifiers modifiers = methodDeclaration.getModifiers();
        Annotation parameterPassesExceptionAnnotation = null;
        if (modifiers != null) {
            final List<? extends Annotation> annotations = modifiers.getAnnotations();
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotationTypeFoFind.getName().equals(annotation.getType().getStringRepresentation())) {
                        parameterPassesExceptionAnnotation = annotation;
                        break;
                    }
                }
            }
        }
        return parameterPassesExceptionAnnotation;
    }

    private AnnotationUtils() {}
}
