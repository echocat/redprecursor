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

import org.echocat.redprecursor.compilertree.Annotation;
import org.echocat.redprecursor.compilertree.CompilationUnit;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.handler.Handler.Request;

import javax.annotation.Nonnull;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class StatementCreationRequest implements Request, AnnotationBasedEvaluationExecuterStatementProducer.Request {

    private final Request _original;
    private final Principal _principal;
    private final Annotation _annotation;
    private final Identifier _identifierToCheck;
    private final Identifier _typeOfCheckTarget;
    private final MethodStatement _methodStatement;

    public StatementCreationRequest(@Nonnull Request original, @Nonnull Principal principal, @Nonnull Annotation annotation, @Nonnull Identifier identifierToCheck, @Nonnull Identifier typeOfCheckTarget, @Nonnull MethodStatement methodStatement) {
        _original = requireNonNull("original", original);
        _principal = requireNonNull("principal", principal);
        _annotation = requireNonNull("annotation", annotation);
        _identifierToCheck = requireNonNull("identifierToCheck", identifierToCheck);
        _typeOfCheckTarget = requireNonNull("typeOfCheckTarget", typeOfCheckTarget);
        _methodStatement = requireNonNull("methodStatement", methodStatement);
    }

    @Nonnull
    @Override
    public CompilationUnit getCompilationUnit() {
        return _original.getCompilationUnit();
    }

    @Override
    @Nonnull
    public Principal getPrincipal() {
        return _principal;
    }

    @Override
    @Nonnull
    public Annotation getAnnotation() {
        return _annotation;
    }

    @Override
    @Nonnull
    public Identifier getIdentifierToCheck() {
        return _identifierToCheck;
    }

    @Nonnull
    public Identifier getTypeOfCheckTarget() {
        return _typeOfCheckTarget;
    }

    @Override
    @Nonnull
    public MethodStatement getMethodStatement() {
        return _methodStatement;
    }
}
