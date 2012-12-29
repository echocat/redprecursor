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

import org.echocat.redprecursor.annotations.RegEx.Evaluator;
import org.echocat.redprecursor.meta.AnnotationEvaluator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.MessageFormat;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.echocat.redprecursor.utils.ReportingUtils.throwMessageFor;

@Documented
@TypeQualifier(applicableTo = String.class)
@Retention(RetentionPolicy.RUNTIME)
@EvaluatedBy(Evaluator.class)
public @interface RegEx {

    /**
     * <p>This property could contain the message which will be thrown in case of an validation error.</p>
     *
     * <p>The content will be evaluated by {@link MessageFormat}. The available properties are:
     * <ul>
     *      <li><code>{0}</code>: Name of the subject. In case of a method parameter this is the parameter name. In case of a method and its return value this is the method name.</li>
     *      <li><code>{1}</code>: Actual value which does not pass the check.</li>
     * </ul></p>
     */
    public String messageOnViolation() default "";

    @VisibleInStackTraces(false)
    public static class Evaluator implements AnnotationEvaluator<RegEx, String> {

        @Override
        public void evaluate(@Nonnull RegEx annotation, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable String value) {
            if (!isRegEx(value)) {
                throwMessageFor(PatternSyntaxException.class, annotation, " is no valid regex.", elementType, elementName, value);
            }
        }

        protected boolean isRegEx(@Nullable String value) {
            boolean result;
            if (value != null) {
                try {
                    Pattern.compile(value);
                    result = true;
                } catch (PatternSyntaxException ignored) {
                    result = false;
                }
            } else {
                result = false;
            }
            return result;
        }
    }

}
