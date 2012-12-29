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
import java.util.Iterator;
import java.util.Map;

import static java.util.Collections.unmodifiableCollection;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class AnnotationMetaFactory implements Iterable<AnnotationMeta<?, ?>> {

    private final Map<Class<? extends Annotation>, AnnotationMeta<?, ?>> _annotationToMeta;

    public AnnotationMetaFactory(@Nonnull Map<Class<? extends Annotation>, AnnotationMeta<?, ?>> annotationToMeta) {
        _annotationToMeta = requireNonNull("annotationToMeta", annotationToMeta);
    }

    @Nonnull
    public <T extends Annotation, V> AnnotationMeta<T, V> getFor(@Nonnull Class<T> annotationType) throws NoAnnotationMetaAvailableException {
        final AnnotationMeta<?, ?> meta = _annotationToMeta.get(requireNonNull("annotationType", annotationType));
        if (meta == null) {
            throw new NoAnnotationMetaAvailableException("There is no meta available for '" + annotationType.getName() + "'.");
        }
        // noinspection unchecked
        return (AnnotationMeta<T, V>) meta;
    }

    @Nonnull
    public <T extends Annotation, V> AnnotationMeta<T, V> getFor(@Nonnull T annotation) throws NoAnnotationMetaAvailableException {
        requireNonNull("annotation", annotation);
        AnnotationMeta<?, ?> meta = _annotationToMeta.get(annotation.getClass());
        if (meta == null) {
            final Class<?>[] interfaces = annotation.getClass().getInterfaces();
            for (int i = 0; meta == null && i < interfaces.length; i++) {
                final Class<?> interfaze = interfaces[i];
                if (Annotation.class.isAssignableFrom(interfaze)) {
                    // noinspection unchecked
                    meta = _annotationToMeta.get((Class<? extends Annotation>) interfaze);
                }
            }
        }
        if (meta == null) {
            throw new NoAnnotationMetaAvailableException("There is no meta available for " + annotation + ".");
        }
        // noinspection unchecked
        return (AnnotationMeta<T, V>) meta;
    }

    @Override
    @Nonnull
    public Iterator<AnnotationMeta<?, ?>> iterator() {
        return unmodifiableCollection(_annotationToMeta.values()).iterator();
    }

    public static class NoAnnotationMetaAvailableException extends RuntimeException {

        public NoAnnotationMetaAvailableException(String message) {
            super(message);
        }
    }
}
