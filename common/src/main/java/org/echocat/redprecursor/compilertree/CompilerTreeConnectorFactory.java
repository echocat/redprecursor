package org.echocat.redprecursor.compilertree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.Iterator;
import java.util.ServiceLoader;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class CompilerTreeConnectorFactory {

    private ClassLoader _classLoader;

    public CompilerTreeConnectorFactory() {
        this(null);
    }

    public CompilerTreeConnectorFactory(@Nullable ClassLoader classLoader) {
        _classLoader = classLoader != null ? classLoader : CompilerTreeConnectorFactory.class.getClassLoader();
    }

    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    public void setClassLoader(@Nonnull ClassLoader classLoader) {
        _classLoader = classLoader;
    }

    @Nonnull
    public CompilerTreeConnector getBy(@Nonnull ProcessingEnvironment processingEnvironment) {
        requireNonNull("processingEnvironment", processingEnvironment);
        final Iterator<CompilerTreeConnector> i = ServiceLoader.load(CompilerTreeConnector.class, _classLoader).iterator();
        CompilerTreeConnector result = null;
        while (i.hasNext() && result == null) {
            final CompilerTreeConnector potentialConnector = i.next();
            if (potentialConnector.canHandle(processingEnvironment)) {
                result = potentialConnector;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Could not find any implemenation of '" + CompilerTreeConnector.class.getName() + "' for " + processingEnvironment + ".");
        }
        result.init(processingEnvironment);
        return result;
    }
}
