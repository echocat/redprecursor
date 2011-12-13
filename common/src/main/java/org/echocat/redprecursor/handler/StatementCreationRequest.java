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
