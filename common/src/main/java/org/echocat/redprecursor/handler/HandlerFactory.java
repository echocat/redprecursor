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

import org.echocat.redprecursor.compilertree.Logger;
import org.echocat.redprecursor.compilertree.LoggerAware;
import org.echocat.redprecursor.compilertree.NodeFactory;
import org.echocat.redprecursor.compilertree.NodeFactoryAware;
import org.echocat.redprecursor.meta.AnnotationMetaFactory;
import org.echocat.redprecursor.meta.AnnotationMetaFactoryAware;
import org.echocat.redprecursor.meta.AnnotationMetaFactoryFactory;
import org.echocat.redprecursor.util.ClassLoaderAware;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.ServiceLoader;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class HandlerFactory {

    private ClassLoader _classLoader = getClass().getClassLoader();
    private AnnotationMetaFactory _annotationMetaFactory = AnnotationMetaFactoryFactory.getDefaultInstance();

    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    public void setClassLoader(@Nonnull ClassLoader classLoader) {
        _classLoader = requireNonNull("classLoader", classLoader);
    }

    public AnnotationMetaFactory getAnnotationMetaFactory() {
        return _annotationMetaFactory;
    }

    public void setAnnotationMetaFactory(@Nonnull AnnotationMetaFactory annotationMetaFactory) {
        _annotationMetaFactory = requireNonNull("annotationMetaFactory", annotationMetaFactory);
    }

    public Iterable<Handler> getFor(@Nonnull final NodeFactory nodeFactory, @Nonnull final Logger logger) {
        requireNonNull("nodeFactory", nodeFactory);
        requireNonNull("logger", logger);
        final ServiceLoader<Handler> loader = ServiceLoader.load(Handler.class, _classLoader);
        return new Iterable<Handler>() { @Override public Iterator<Handler> iterator() {
            loader.reload();
            final Iterator<Handler> i = loader.iterator();
            return new Iterator<Handler>() {
                @Override
                public Handler next() {
                    final Handler handler = i.next();
                    configure(handler, nodeFactory, logger);
                    return handler;
                }
                @Override public boolean hasNext() { return i.hasNext(); }
                @Override public void remove() { throw new UnsupportedOperationException(); }
            };
        }};
    }

    protected void configure(@Nonnull Handler handler, @Nonnull NodeFactory nodeFactory, @Nonnull Logger logger) {
        requireNonNull("handler", handler);
        requireNonNull("nodeFactory", nodeFactory);
        requireNonNull("logger", logger);
        if (handler instanceof NodeFactoryAware) {
            ((NodeFactoryAware) handler).setNodeFactory(nodeFactory);
        }
        if (handler instanceof LoggerAware) {
            ((LoggerAware) handler).setLogger(logger);
        }
        if (handler instanceof AnnotationMetaFactoryAware) {
            ((AnnotationMetaFactoryAware) handler).setAnnotationMetaFactory(_annotationMetaFactory);
        }
        if (handler instanceof ClassLoaderAware) {
            ((ClassLoaderAware) handler).setClassLoader(_classLoader);
        }
    }
}
