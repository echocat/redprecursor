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

package org.echocat.redprecursor.meta;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class AnnotationMeta<T extends Annotation, V> {

    private final Class<T> _type;
    private final AnnotationEvaluator<T, V> _annotationEvaluator;

    public AnnotationMeta(@Nonnull Class<T> type, @Nonnull AnnotationEvaluator<T, V> annotationEvaluator) {
        _type = requireNonNull("type", type);
        _annotationEvaluator = requireNonNull("validationViolationMessageBuilder", annotationEvaluator);
    }

    @Nonnull
    public Class<T> getType() {
        return _type;
    }

    @Nonnull
    public AnnotationEvaluator<T, V> getAnnotationEvaluator() {
        return _annotationEvaluator;
    }
}
