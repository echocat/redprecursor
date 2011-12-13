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

package org.echocat.redprecursor.evaluation;

import org.echocat.redprecursor.annotations.VisibleInStackTraces;
import org.echocat.redprecursor.meta.AnnotationAndMeta;
import org.echocat.redprecursor.meta.AnnotationMeta;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

@VisibleInStackTraces(false)
public class AnnotationEvaluationExecuter {

    public static <T extends Annotation> void execute(@Nonnull AnnotationAndMeta<T, ?> annotationAndMeta, @Nonnull ElementType elementType, @Nonnull String elementName, @Nonnull Object value) {
        requireNonNull("annotationAndMeta", annotationAndMeta);
        requireNonNull("elementType", elementType);
        requireNonNull("elementName", elementName);
        // noinspection unchecked
        final AnnotationMeta<T, Object> meta = (AnnotationMeta<T, Object>) annotationAndMeta.getMeta();
        final T annotation = annotationAndMeta.getAnnotation();
        meta.getAnnotationEvaluator().evaluate(annotation, elementType, elementName, value);
    }

    private AnnotationEvaluationExecuter() {}
}
