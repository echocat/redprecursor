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

package org.echocat.redprecursor;

import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.handler.Handler.Request;
import org.echocat.redprecursor.handler.Handler;
import org.echocat.redprecursor.handler.HandlerFactory;

import javax.annotation.Nonnull;
import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class RedprecursorProcessor implements Processor {

    private ClassLoader _classLoader;
    private CompilerTreeConnectorFactory _connectorFactory;
    private HandlerFactory _handlerFactory;

    private CompilerTreeConnector _connector;
    private ProcessingEnvironment _processingEnvironment;

    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    public void setClassLoader(@Nonnull ClassLoader classLoader) {
        _classLoader = requireNonNull("classLoader", classLoader);
    }

    public CompilerTreeConnectorFactory getConnectorFactory() {
        return _connectorFactory;
    }

    public void setConnectorFactory(CompilerTreeConnectorFactory connectorFactory) {
        _connectorFactory = requireNonNull("connectorFactory", connectorFactory);
    }

    public HandlerFactory getHandlerFactory() {
        return _handlerFactory;
    }

    public void setHandlerFactory(HandlerFactory handlerFactory) {
        _handlerFactory = requireNonNull("handlerFactory", handlerFactory);
    }

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        _processingEnvironment = requireNonNull("processingEnvironment", processingEnvironment);
        try {
            if (_classLoader == null) {
                _classLoader = RedprecursorProcessor.class.getClassLoader();
            }
            if (_connectorFactory == null) {
                _connectorFactory = new CompilerTreeConnectorFactory();
            }
            if (_handlerFactory == null) {
                _handlerFactory = new HandlerFactory();
            }
            _connector = _connectorFactory.getBy(processingEnvironment);
        } catch (Exception e) {
            processingEnvironment.getMessager().printMessage(Kind.ERROR, "Fatal error while initialization of compilation process: " + formatMessageFor(e));
        } catch (ExceptionInInitializerError e) {
            processingEnvironment.getMessager().printMessage(Kind.ERROR, "Fatal error while initialization of compilation process: " + formatMessageFor(e.getException() != null ? e.getException() : e));
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        final boolean result;
        if (_connector != null && _processingEnvironment != null && _handlerFactory != null && _classLoader != null) {
            for (final CompilationUnit compilationUnit : _connector.toCompilationUnits(roundEnvironment)) {
                final Logger logger = _connector.getLogger(compilationUnit);
                try {
                    final NodeFactory nodeFactory = _connector.getNodeFactoryFor(compilationUnit);
                    final Request request = new Request() {
                        @Nonnull @Override public CompilationUnit getCompilationUnit() { return compilationUnit; }
                    };
                    for (Handler handler : _handlerFactory.getFor(nodeFactory, logger)) {
                        handler.handle(request);
                    }
                } catch (Exception e) {
                    logger.error(compilationUnit, "Fatal error while try to process " + compilationUnit.getSourceFile().getName() + ".", e);
                } catch (ExceptionInInitializerError e) {
                    logger.error(compilationUnit, "Fatal error while try to process " + compilationUnit.getSourceFile().getName() + ".", e.getException() != null ? e.getException() : e);
                }
            }
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return emptySet();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>(asList("*"));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        return emptySet();
    }

    @Nonnull
    protected String formatMessageFor(@Nonnull Throwable t) {
        final StringWriter writer = new StringWriter();
        //noinspection ThrowableResultOfMethodCallIgnored
        requireNonNull("t", t).printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
