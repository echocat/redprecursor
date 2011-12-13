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
