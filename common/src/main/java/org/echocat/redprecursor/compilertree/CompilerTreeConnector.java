package org.echocat.redprecursor.compilertree;

import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

public interface CompilerTreeConnector {

    public boolean canHandle(@Nonnull ProcessingEnvironment processingEnvironment);

    public void init(@Nonnull ProcessingEnvironment processingEnvironment);

    @Nonnull
    public Iterable<? extends CompilationUnit> toCompilationUnits(@Nonnull RoundEnvironment roundEnvironment);

    @Nonnull
    public NodeFactory getNodeFactoryFor(@Nonnull CompilationUnit compilationUnit);

    @Nonnull
    public Logger getLogger(@Nonnull CompilationUnit compilationUnit);
}
