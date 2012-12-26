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
