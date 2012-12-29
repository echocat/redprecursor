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

import static org.echocat.redprecursor.utils.ContractUtil.*;

public class AnnotationAndMeta<T extends Annotation, V> {

    private final T _annotation;
    private final AnnotationMeta<T, V> _meta;

    public AnnotationAndMeta(@Nonnull T annotation, @Nonnull AnnotationMeta<T, V> meta) {
        _annotation = requireNonNull("annotation", annotation);
        _meta = requireNonNull("meta", meta);
    }

    @Nonnull
    public T getAnnotation() {
        return _annotation;
    }

    @Nonnull
    public <E extends Annotation> E getAnnotation(@Nonnull Class<E> ofExpectedType) {
        final Annotation annotation = _annotation;
        return castNonNullParameterTo("property annotation", annotation, ofExpectedType);
    }

    @Nonnull
    public AnnotationMeta<T, V> getMeta() {
        return _meta;
    }
}
