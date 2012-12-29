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
