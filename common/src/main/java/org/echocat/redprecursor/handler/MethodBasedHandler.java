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

import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Node;
import org.echocat.redprecursor.compilertree.base.Statement;
import org.echocat.redprecursor.compilertree.util.PositionUtils;
import org.echocat.redprecursor.meta.AnnotationMetaFactory;
import org.echocat.redprecursor.meta.AnnotationMetaFactoryAware;
import org.echocat.redprecursor.meta.AnnotationMetaFactoryFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public abstract class MethodBasedHandler implements Handler, NodeFactoryAware, AnnotationMetaFactoryAware, LoggerAware {

    public static class ParentMethod {

        private final MethodDeclaration _method;

        public ParentMethod(@Nonnull MethodDeclaration method) {
            _method = requireNonNull("methodDeclaration", method);
        }

        @Nonnull
        public MethodDeclaration getMethod() {
            return _method;
        }
    }

    private NodeFactory _nodeFactory;
    private AnnotationMetaFactory _annotationMetaFactory = AnnotationMetaFactoryFactory.getDefaultInstance();
    private Logger _logger;

    @Override
    public void setNodeFactory(@Nonnull NodeFactory nodeFactory) {
        _nodeFactory = requireNonNull("nodeFactory", nodeFactory);
    }

    public NodeFactory getNodeFactory() {
        return _nodeFactory;
    }

    @Override
    public void setAnnotationMetaFactory(@Nonnull AnnotationMetaFactory annotationMetaFactory) {
        _annotationMetaFactory = requireNonNull("annotationMetaFactory", annotationMetaFactory);
    }

    public AnnotationMetaFactory getAnnotationMetaFactory() {
        return _annotationMetaFactory;
    }

    @Override
    public void setLogger(@Nonnull Logger logger) {
        _logger = requireNonNull("logger", logger);
    }

    public Logger getLogger() {
        return _logger;
    }

    @Override
    public void handle(@Nonnull Request request) {
        final CompilationUnit compilationUnit = request.getCompilationUnit();
        handle(request, compilationUnit, null, null, compilationUnit, null, null);
    }

    protected void handle(@Nonnull Request request, @Nonnull Node node, @Nullable Node parent, @Nullable ParentMethod parentMethod, @Nonnull CompilationUnit compilationUnit, @Nullable ClassDeclaration topClassDeclaration, @Nullable ClassDeclaration lastClassDeclaration) {
        requireNonNull("request", request);
        requireNonNull("node", node);
        requireNonNull("compilationUnit", compilationUnit);
        if (node instanceof MethodDeclaration) {
            final MethodDeclaration method = (MethodDeclaration) node;
            final MethodStatement methodStatement = new MethodStatement(compilationUnit, topClassDeclaration, lastClassDeclaration, method);
            handleMethod(request, methodStatement, parent);
        } else {
            handleOther(request, node, parent, parentMethod, compilationUnit, topClassDeclaration, lastClassDeclaration);
            for (Node subNode : node.getAllEnclosedNodes()) {
                if (subNode instanceof ClassDeclaration) {
                    handle(request, subNode, node, parentMethod, compilationUnit, topClassDeclaration == null ? (ClassDeclaration) subNode : topClassDeclaration, (ClassDeclaration) subNode);
                } else {
                    handle(request, subNode, node, parentMethod, compilationUnit, topClassDeclaration, lastClassDeclaration);
                }
            }
        }
    }

    protected abstract void handleMethod(@Nonnull Request request, @Nonnull MethodStatement methodStatement, @Nonnull Node parent);

    protected void handleOther(@Nonnull Request request, @Nonnull Node node, @Nonnull Node parent, @Nonnull ParentMethod parentMethod, @Nonnull CompilationUnit compilationUnit, @Nullable ClassDeclaration topClassDeclaration, @Nullable ClassDeclaration lastClassDeclaration) {}

    protected void prependStatementsToBody(@Nonnull MethodDeclaration method, @Nonnull Statement... toPrepend) {
        requireNonNull("method", method);
        requireNonNull("toPrepend", toPrepend);
        prependStatementsToBody(method, asList(toPrepend));
    }

    protected void prependStatementsToBody(@Nonnull MethodDeclaration method, @Nonnull List<Statement> toPrepend) {
        requireNonNull("method", method);
        requireNonNull("toPrepend", toPrepend);
        if (!toPrepend.isEmpty()) {
            for (Statement statement : toPrepend) {
                PositionUtils.setPositionRecursive(statement, method.getPosition());
            }
            final List<Statement> statements = new ArrayList<Statement>(method.getBody());
            statements.addAll(0, toPrepend);
            method.setBody(statements);
        }
    }
}
