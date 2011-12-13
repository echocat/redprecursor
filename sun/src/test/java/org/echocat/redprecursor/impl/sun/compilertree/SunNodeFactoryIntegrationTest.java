package org.echocat.redprecursor.impl.sun.compilertree;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.echocat.redprecursor.RedprecursorProcessor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SunNodeFactoryIntegrationTest {

    @Rule
    public final TemporaryFolder _temporaryFolder = new TemporaryFolder();

    @Test
    public void test() throws Throwable {
        final File sourceFile = _temporaryFolder.newFile("Test.java");
        final File classFile = new File(sourceFile.getParentFile(), FilenameUtils.getBaseName(sourceFile.getName()) + ".class");

        writeResourceTo("Test.java.txt", sourceFile);
        compile(sourceFile);
        executeMethodIn(classFile);

        assertThat(sourceFile.isFile(), equalTo(true));
    }

    private void executeMethodIn(File classFile) throws Throwable {
        final URLClassLoader loader = new URLClassLoader(new URL[]{ classFile.getParentFile().toURI().toURL() });
        final Class<?> testClass = loader.loadClass("Test");
        final Object test = testClass.newInstance();
        final Method callMethod = testClass.getMethod("call", String.class, Integer.class);
        try {
            callMethod.invoke(test, new Object[]{"abc", 2});
        } catch (InvocationTargetException  e) {
            throw e.getTargetException();
        }
    }

    private void compile(File... files) {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        final Iterable<? extends JavaFileObject> compilationUnits2 = fileManager.getJavaFileObjects(files);
        final DiagnosticCollector<Object> listener = null; //new DiagnosticCollector<Object>();
        final CompilationTask task = compiler.getTask(null, fileManager, listener, Arrays.<String>asList("-processor", RedprecursorProcessor.class.getName()), null, compilationUnits2);
        //final CompilationTask task = compiler.getTask(null, fileManager, listener, Arrays.<String>asList(), null, compilationUnits2);
        task.call();
    }

    private void writeResourceTo(String sourceResource, File tempFile) throws IOException {
        final InputStream is = getClass().getResourceAsStream(sourceResource);
        if (is == null) {
            throw new FileNotFoundException("Could not find the resource " + sourceResource);
        }
        try {
            final FileOutputStream fos = new FileOutputStream(tempFile);
            try {
                IOUtils.copy(is, fos);
            } finally {
                IOUtils.closeQuietly(fos);
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
