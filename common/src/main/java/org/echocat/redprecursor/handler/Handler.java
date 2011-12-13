package org.echocat.redprecursor.handler;

import org.echocat.redprecursor.compilertree.CompilationUnit;

import javax.annotation.Nonnull;

public interface Handler {

    public interface Request {

        @Nonnull
        public CompilationUnit getCompilationUnit();
    }

    public void handle(@Nonnull Request request);
}
