package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.DefinitionsEnabledNode;

import javax.tools.JavaFileObject;
import java.util.List;

public interface CompilationUnit extends DefinitionsEnabledNode {

    public List<? extends Annotation> getPackageAnnotations();

    public void setPackageAnnotations(List<Annotation> packageAnnotations);

    public JavaFileObject getSourceFile();

}
