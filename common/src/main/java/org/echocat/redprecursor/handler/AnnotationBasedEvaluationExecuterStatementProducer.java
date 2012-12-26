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
