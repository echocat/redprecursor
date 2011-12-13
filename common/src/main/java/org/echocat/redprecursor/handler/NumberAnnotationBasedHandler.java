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

package org.echocat.redprecursor.handler;

import org.echocat.redprecursor.annotations.*;
import org.echocat.redprecursor.compilertree.Annotation;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.Primitive;
import org.echocat.redprecursor.compilertree.PrimitiveType;
import org.echocat.redprecursor.handler.AnnotationBasedHandler.HandlesAnnotations;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static org.echocat.redprecursor.compilertree.Primitive.*;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

@HandlesAnnotations({
    GreaterThan.class,
    GreaterThanOrEqualTo.class,
    GreaterThanZero.class,
    GreaterThanOrEqualToZero.class,
    NonNegative.class,
    Nonnegative.class
})
public class NumberAnnotationBasedHandler extends AnnotationBasedHandler {

    @Override
    protected void assertThatTypeCouldHandled(@Nonnull Identifier typeToCheck, @Nonnull Annotation forAnnotation) {
        if (typeToCheck instanceof PrimitiveType) {
            final Primitive primitive = ((PrimitiveType) typeToCheck).getPrimitive();
            if (primitive != BYTE &&
                primitive != SHORT &&
                primitive != INT &&
                primitive != LONG &&
                primitive != FLOAT &&
                primitive != DOUBLE) {
                requireNonNull("Property logger", getLogger()).error(typeToCheck, "@" + forAnnotation.getType().getStringRepresentation() + " is not applicable to " + typeToCheck.getStringRepresentation() + ".");
            }
        } else {
            final String className = typeToCheck.getStringRepresentation();
            if (!Byte.class.getName().equals(className) &&
                !Short.class.getName().equals(className) &&
                !Integer.class.getName().equals(className) &&
                !Long.class.getName().equals(className) &&
                !Float.class.getName().equals(className) &&
                !Double.class.getName().equals(className)) {
                requireNonNull("Property logger", getLogger()).error(typeToCheck, "@" + forAnnotation.getType().getStringRepresentation() + " is not applicable to " + typeToCheck.getStringRepresentation() + ".");
            }
        }
    }
}
