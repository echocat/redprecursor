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

package org.echocat.redprecursor.annotations;

import org.echocat.redprecursor.annotations.GreaterThan.Evaluator;
import org.echocat.redprecursor.meta.AnnotationEvaluator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.text.MessageFormat;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.echocat.redprecursor.utils.ReportingUtils.throwMessageFor;

@Target({ FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
@TypeQualifier(applicableTo = Number.class)
@Documented
@EvaluatedBy(Evaluator.class)
public @interface GreaterThan {

    /**
     * The property to must greater than this value.
     */
    public double value();

    /**
     * <p>This property could contain the message which will be thrown in case of an validation error.</p>
     *
     * <p>The content will be evaluated by {@link MessageFormat}. The available properties are:
     * <ul>
     * <li><code>{0}</code>: Name of the subject. In case of a method parameter this is the parameter name. In case of a method and its return value this is the method name.</li>
     * <li><code>{1}</code>: Actual value which does not pass the check.</li>
     * </ul></p>
     */
    public String messageOnViolation() default "";

    @VisibleInStackTraces(false)
    public static class Evaluator implements AnnotationEvaluator<GreaterThan, Number> {

        @Override
        public void evaluate(@Nonnull GreaterThan annotation, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable Number value) {
            if (value == null || !(value.doubleValue() > annotation.value())) {
                throwMessageFor(annotation, " is not greater than " + annotation.value() + ".", elementType, elementName, value);
            }
        }
    }
}
