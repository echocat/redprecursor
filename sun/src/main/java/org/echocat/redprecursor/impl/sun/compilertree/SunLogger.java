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

package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Log;
import org.echocat.redprecursor.compilertree.Logger;
import org.echocat.redprecursor.compilertree.base.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import static org.echocat.redprecursor.impl.sun.compilertree.SunLogger.Level.*;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunLogger implements Logger {


    protected static enum Level {
        INFO,
        WARN,
        ERROR
    }

    private final Log _log;
    private final ProcessingEnvironment _processingEnvironment;
    private final SunCompilationUnit _sunCompilationUnit;

    public SunLogger(@Nonnull Log log, @Nonnull ProcessingEnvironment processingEnvironment, @Nonnull SunCompilationUnit sunCompilationUnit) {
        _log = requireNonNull("log", log);
        _processingEnvironment = requireNonNull("processingEnvironment", processingEnvironment);
        _sunCompilationUnit = requireNonNull("sunCompilationUnit", sunCompilationUnit);
    }

    @Nonnull
    @Override
    public Locale getLocale() {
        return _processingEnvironment.getLocale();
    }

    @Override
    public void info(@Nonnull Node node, @Nonnull String message, @Nullable Throwable t) {
        log(INFO, node, message, t);
    }

    @Override
    public void info(@Nonnull Node node, @Nonnull String message) {
        log(INFO, node, message, null);
    }

    @Override
    public void info(@Nonnull Node node, @Nonnull Throwable t) {
        log(INFO, node, null, t);
    }

    @Override
    public void warn(@Nonnull Node node, @Nonnull String message, @Nullable Throwable t) {
        log(WARN, node, message, t);
    }

    @Override
    public void warn(@Nonnull Node node, @Nonnull String message) {
        log(WARN, node, message, null);
    }

    @Override
    public void warn(@Nonnull Node node, @Nonnull Throwable t) {
        log(WARN, node, null, t);
    }

    @Override
    public void error(@Nonnull Node node, @Nonnull String message, @Nullable Throwable t) {
        log(ERROR, node, message, t);
    }

    @Override
    public void error(@Nonnull Node node, @Nonnull String message) {
        log(ERROR, node, message, null);
    }

    @Override
    public void error(@Nonnull Node node, @Nonnull Throwable t) {
        log(ERROR, node, null, t);
    }

    protected void log(@Nonnull Level level, @Nonnull Node node, @Nullable String message, @Nullable Throwable t) {
        final SunNode sunNode = castNonNullParameterTo("node", node, SunNode.class);
        final StringBuilder targetMessage = new StringBuilder();
        if (message != null) {
            targetMessage.append(message);
        } else if (t != null) {
            targetMessage.append(t.getMessage());
        } else {
            throw new NullPointerException("Neither message nor t was provided.");
        }
        if (t != null) {
            final StringWriter out = new StringWriter();
            t.printStackTrace(new PrintWriter(out));
            targetMessage.append(out);
        }

        final JCTree jc = sunNode.getJc();
        final JCCompilationUnit jcCompilationUnit = _sunCompilationUnit.getJc();
        final JavaFileObject oldSource = _log.currentSource().getFile();
        try {
            _log.useSource(jcCompilationUnit.sourcefile);
            if (level == Level.INFO) {
                _log.note(jc, "proc.messager", targetMessage.toString());
            } else if (level == Level.WARN) {
                _log.warning(jc, "proc.messager", targetMessage.toString());
            } else {
                _log.error(jc, "proc.messager", targetMessage.toString());
            }
        } finally {
            _log.useSource(oldSource);
        }
    }
}
