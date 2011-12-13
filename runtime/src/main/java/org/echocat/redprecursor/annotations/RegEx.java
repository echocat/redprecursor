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
