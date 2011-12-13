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

package org.echocat.redprecursor.annotations.javaxextensions;

import org.echocat.redprecursor.annotations.NonNegative;
import org.echocat.redprecursor.annotations.NonNegative.Evaluator;
import org.echocat.redprecursor.annotations.VisibleInStackTraces;
import org.echocat.redprecursor.meta.AnnotationEvaluator;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

@VisibleInStackTraces(false)
public class NonnegativeAnnotationEvaluator implements AnnotationEvaluator<Nonnegative, Number> {

    private static final Evaluator DELEGATE = new Evaluator();

    @Override
    public void evaluate(@Nonnull Nonnegative annotation, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable Number value) {
        DELEGATE.evaluate(new Impl(), elementType, elementName, value);
    }

    public static class Impl implements NonNegative {
        private Impl() {}
        @Override public String messageOnViolation() { return ""; }
        @Override public Class<? extends Annotation> annotationType() { return NonNegative.class; }
    }

}
