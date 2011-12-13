package org.echocat.redprecursor.handler;

import org.echocat.redprecursor.annotations.NonNull;
import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Statement;
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
    protected Statement generateStatementFor(@Nonnull StatementCreationRequest request) {
        final NodeFactory nodeFactory = requireNonNull("Property nodeFactory", getNodeFactory());
        final Statement result;
        final Identifier identifierToCheck = request.getIdentifierToCheck();
        final Principal principal = request.getPrincipal();
        if (principal == RETURN && nodeFactory.findTypeFor(identifierToCheck) instanceof PrimitiveType) {
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
