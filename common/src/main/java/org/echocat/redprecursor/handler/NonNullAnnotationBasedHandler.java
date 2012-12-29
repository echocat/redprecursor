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

import org.echocat.redprecursor.annotations.NonNull;
import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.handler.AnnotationBasedHandler.HandlesAnnotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.echocat.redprecursor.handler.Principal.RETURN;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

@HandlesAnnotations({
    NonNull.class,
    Nonnull.class
})
public class NonNullAnnotationBasedHandler extends AnnotationBasedHandler {

    @Override
    @Nullable
    protected Expression generateExpressionFor(@Nonnull StatementCreationRequest request) {
        final NodeFactory nodeFactory = requireNonNull("Property nodeFactory", getNodeFactory());
        final Expression result;
        final Principal principal = request.getPrincipal();
        if (principal == RETURN && request.getMethodStatement().getMethod().getResultType() instanceof PrimitiveType) {
            // It is not necessary to create a null check for a primitive value. Skip it...
            result = null;
        } else {
            final AnnotationBasedEvaluationExecuterStatementProducer statementProducer = new AnnotationBasedEvaluationExecuterStatementProducer(nodeFactory);
            result = statementProducer.produce(request);
        }
        return result;
    }

    @Override
    protected void assertThatTypeCouldHandled(@Nonnull Identifier typeToCheck, @Nonnull Annotation forAnnotation) {
        if (typeToCheck instanceof PrimitiveType) {
            final Logger logger = requireNonNull("Property logger", getLogger());
            logger.error(typeToCheck, "@" + forAnnotation.getType().getStringRepresentation() + " does not support primitive data types.");
        }
    }
}
