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

import org.echocat.redprecursor.annotations.MatchesPattern.Evaluator;
import org.echocat.redprecursor.annotations.VisibleInStackTraces;
import org.echocat.redprecursor.meta.AnnotationEvaluator;

import javax.annotation.MatchesPattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

@VisibleInStackTraces(false)
public class MatchesPatternAnnotationEvaluator implements AnnotationEvaluator<MatchesPattern, String> {

    private static final Evaluator DELEGATE = new Evaluator();

    @Override
    public void evaluate(@Nonnull MatchesPattern annotation, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable String value) {
        DELEGATE.evaluate(new Impl(annotation), elementType, elementName, value);
    }

    public static class Impl implements org.echocat.redprecursor.annotations.MatchesPattern {
        private final MatchesPattern _delegate;
        private Impl(MatchesPattern delegate) { _delegate = delegate; }
        @Override public String value() { return _delegate.value(); }
        @Override public int flags() { return _delegate.flags(); }
        @Override public String messageOnViolation() { return ""; }
        @Override public Class<? extends Annotation> annotationType() { return org.echocat.redprecursor.annotations.MatchesPattern.class; }
    }
}
