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
