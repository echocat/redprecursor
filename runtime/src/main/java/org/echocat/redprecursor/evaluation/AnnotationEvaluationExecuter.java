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

package org.echocat.redprecursor.evaluation;

import org.echocat.redprecursor.annotations.VisibleInStackTraces;
import org.echocat.redprecursor.meta.AnnotationAndMeta;
import org.echocat.redprecursor.meta.AnnotationMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

@VisibleInStackTraces(false)
public class AnnotationEvaluationExecuter {

    public static <A extends Annotation> void execute(@Nonnull AnnotationAndMeta<A, ?> annotationAndMeta, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable Object value) {
        requireNonNull("annotationAndMeta", annotationAndMeta);
        requireNonNull("elementType", elementType);
        requireNonNull("elementName", elementName);
        // noinspection unchecked
        final AnnotationMeta<A, Object> meta = (AnnotationMeta<A, Object>) annotationAndMeta.getMeta();
        final A annotation = annotationAndMeta.getAnnotation();
        meta.getAnnotationEvaluator().evaluate(annotation, elementType, elementName, value);
    }

    private AnnotationEvaluationExecuter() {}
}
