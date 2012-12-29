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

package org.echocat.redprecursor.annotations.javaxextensions;

import org.echocat.redprecursor.annotations.NonNull;
import org.echocat.redprecursor.annotations.NonNull.Evaluator;
import org.echocat.redprecursor.annotations.VisibleInStackTraces;
import org.echocat.redprecursor.meta.AnnotationEvaluator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

@VisibleInStackTraces(false)
public class NonnullAnnotationEvaluator implements AnnotationEvaluator<Nonnull, Object> {

    private static final Evaluator DELEGATE = new Evaluator();

    @Override
    public void evaluate(@Nonnull Nonnull annotation, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable Object value) {
        DELEGATE.evaluate(new Impl(), elementType, elementName, value);
    }

    public static class Impl implements NonNull {
        private Impl() {}
        @Override public String messageOnViolation() { return ""; }
        @Override public Class<? extends Annotation> annotationType() { return NonNull.class; }
    }
}
