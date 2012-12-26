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
 * Portions created by the Initial Developer are Copyright (C) 2012
 * the Initial Developer. All Rights Reserved.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.redprecursor.annotations;

import org.echocat.redprecursor.annotations.MatchesPattern.Evaluator;
import org.echocat.redprecursor.meta.AnnotationEvaluator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.echocat.redprecursor.utils.ReportingUtils.throwMessageFor;

@Target({ FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
@TypeQualifier(applicableTo = CharSequence.class)
@Documented
@EvaluatedBy(Evaluator.class)
public @interface MatchesPattern {

    /**
     * Pattern that the property value must pass.
     *
     * @see Pattern#compile(String, int)
     */
    @RegEx
    public String value();

    /**
     * @see Pattern#compile(String, int)
     */
    public int flags() default 0;

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
    public static class Evaluator implements AnnotationEvaluator<MatchesPattern, CharSequence> {

        private static final ReferenceQueue<MatchesPattern> PATTERN_REFERENCE_QUEUE = new ReferenceQueue<MatchesPattern>();
        private static final Map<Reference<? extends MatchesPattern>, Pattern> PATTERN_CACHE = new ConcurrentHashMap<Reference<? extends MatchesPattern>, Pattern>();

        @Override
        public void evaluate(@Nonnull MatchesPattern annotation, @Nonnull ElementType elementType, @Nonnull String elementName, @Nullable CharSequence value) {
            final Pattern pattern = getPatternFor(annotation);
            if (value == null || !pattern.matcher(value).matches()) {
                throwMessageFor(annotation, " does not matches pattern '" + getPatternFor(annotation) +"'.", elementType, elementName, value);
            }
        }

        @Nonnull
        private static Pattern getPatternFor(MatchesPattern annotation) {
            final Reference<MatchesPattern> reference = getReferenceFor(annotation);
            Pattern pattern = PATTERN_CACHE.get(reference);
            if (pattern == null) {
                pattern = Pattern.compile(annotation.value());
                PATTERN_CACHE.put(reference, pattern);
            }
            return pattern;
        }

        @Nonnull
        private static Reference<MatchesPattern> getReferenceFor(MatchesPattern annotation) {
            clearReferences();
            return new WeakReference<MatchesPattern>(annotation, PATTERN_REFERENCE_QUEUE);
        }

        private static void clearReferences() {
            Reference<? extends MatchesPattern> reference = PATTERN_REFERENCE_QUEUE.poll();
            while (reference != null) {
                PATTERN_CACHE.remove(reference);
                reference = PATTERN_REFERENCE_QUEUE.poll();
            }
        }
    }
}
