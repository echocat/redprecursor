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

import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.MethodInvocation;
import org.echocat.redprecursor.compilertree.NodeFactory;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.evaluation.AnnotationEvaluationExecuter;
import org.echocat.redprecursor.handler.AnnotationBasedEvaluationExecuterStatementProducer.Request;
import org.echocat.redprecursor.meta.AnnotationAndMeta;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static org.echocat.redprecursor.handler.Principal.RETURN;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

/**
 * Produces statements which calls {@link AnnotationEvaluationExecuter#execute(AnnotationAndMeta, ElementType, String, Object)} with the provided annotation
 * (provided by {@link Request}).
 */
public class AnnotationBasedEvaluationExecuterStatementProducer extends AnnotationMetaBasedStatementProducer<Request> {

    public interface Request extends AnnotationMetaBasedStatementProducer.Request {
        @Nonnull public Identifier getIdentifierToCheck();
        @Nonnull public Principal getPrincipal();
    }

    public AnnotationBasedEvaluationExecuterStatementProducer(@Nonnull NodeFactory nodeFactory) {
        super(nodeFactory);
    }

    @Override
    @Nonnull
    public Expression produce(@Nonnull Request request) {
        requireNonNull("request", request);
        final NodeFactory nodeFactory = getNodeFactory();
        final Identifier identifierOfAnnotationInstance = getIdentifierOfAnnotationAndMetaInstance(request);
        final Identifier identifierToCheck = request.getIdentifierToCheck();
        final MethodInvocation executeInvocation = nodeFactory.createMethodInvocation(
            nodeFactory.createFieldAccess(nodeFactory.createIdentifier(AnnotationEvaluationExecuter.class.getName()), "execute"),
            identifierOfAnnotationInstance,
            nodeFactory.createFieldAccess(nodeFactory.createIdentifier(ElementType.class.getName()), request.getPrincipal() == RETURN ? METHOD.name() : PARAMETER.name()),
            nodeFactory.createLiteral(request.getPrincipal() == RETURN ? request.getMethodStatement().getMethod().getName() : identifierToCheck.getStringRepresentation()),
            identifierToCheck
        );
        return executeInvocation;
    }
}
