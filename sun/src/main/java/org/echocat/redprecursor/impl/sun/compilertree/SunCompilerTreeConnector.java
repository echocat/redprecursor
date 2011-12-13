package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import org.echocat.redprecursor.compilertree.CompilationUnit;
import org.echocat.redprecursor.compilertree.CompilerTreeConnector;
import org.echocat.redprecursor.compilertree.Logger;

import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.HashSet;
import java.util.Set;

import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunCompilerTreeConnector implements CompilerTreeConnector {

    private Trees _trees;
    private TreeMaker _treeMaker;
    private JavacElements _elements;
    private Attr _attr;
    private Enter _enter;
    private MemberEnter _memberEnter;
    private Context _context;
    private com.sun.tools.javac.util.Log _log;
    private ProcessingEnvironment _processingEnvironment;

    @Override
    public boolean canHandle(@Nonnull ProcessingEnvironment processingEnvironment) {
        requireNonNull("processingEnvironment", processingEnvironment);
        return processingEnvironment instanceof JavacProcessingEnvironment;
    }

    @Override
    @Nonnull
    public void init(@Nonnull ProcessingEnvironment processingEnvironment) {
        requireNonNull("processingEnvironment", processingEnvironment);
        if (!canHandle(processingEnvironment)) {
            throw new IllegalArgumentException("This compiler tree connector could not handle " + processingEnvironment + ".");
        }
        _trees = Trees.instance(processingEnvironment);
        final Context context = ((JavacProcessingEnvironment) processingEnvironment).getContext();
        _treeMaker = TreeMaker.instance(context);
        _elements = JavacElements.instance(context);
        _attr = Attr.instance(context);
        _enter = Enter.instance(context);
        _memberEnter = MemberEnter.instance(context);
        _log = com.sun.tools.javac.util.Log.instance(context);
        _processingEnvironment = processingEnvironment;
        _context = context;
    }

    @Override
    @Nonnull
    public Iterable<? extends SunCompilationUnit> toCompilationUnits(@Nonnull RoundEnvironment roundEnvironment) {
        requireNonNull("roundEnvironment", roundEnvironment);
        if (_trees == null || _treeMaker == null || _elements == null) {
            throw new IllegalStateException("init() method was not called.");
        }
        final Set<? extends Element> rootElements = roundEnvironment.getRootElements();
        final Set<SunCompilationUnit> compilationUnits = new HashSet<SunCompilationUnit>();
        for (Element rootElement : rootElements) {
            final JCCompilationUnit jcCompilationUnit = toJcCompilationUnit(rootElement);
            final SunNodeConverter converter = new SunNodeConverter(_treeMaker, _elements, jcCompilationUnit, _enter, _memberEnter);
            final SunNodeFactory nodeFactory = new SunNodeFactory(_treeMaker, converter, jcCompilationUnit, _attr, _context);
            compilationUnits.add(new SunCompilationUnitWithNodeFactory(jcCompilationUnit, converter, nodeFactory));
        }
        return compilationUnits;
    }

    @Override
    @Nonnull
    public SunNodeFactory getNodeFactoryFor(@Nonnull CompilationUnit compilationUnit) {
        final SunCompilationUnitWithNodeFactory compilationUnitWithNodeFactory = castNonNullParameterTo("compilationUnit", compilationUnit, SunCompilationUnitWithNodeFactory.class);
        return compilationUnitWithNodeFactory.getNodeFactory();
    }

    @Override
    @Nonnull
    public Logger getLogger(@Nonnull CompilationUnit compilationUnit) {
        final SunCompilationUnit sunCompilationUnit = castNonNullParameterTo("compilationUnit", compilationUnit, SunCompilationUnit.class);
        if (_log == null || _processingEnvironment == null) {
            throw new IllegalStateException("init() method was not called.");
        }
        return new SunLogger(_log, _processingEnvironment, sunCompilationUnit);
    }

    private JCCompilationUnit toJcCompilationUnit(Element element) {
        final TreePath path = _trees == null ? null : _trees.getPath(element);
        return path != null ? (JCCompilationUnit) path.getCompilationUnit() : null;
    }

    private static class SunCompilationUnitWithNodeFactory extends SunCompilationUnit {

        private final SunNodeFactory _nodeFactory;

        private SunCompilationUnitWithNodeFactory(JCCompilationUnit jc, SunNodeConverter converter, SunNodeFactory nodeFactory) {
            super(jc, converter);
            _nodeFactory = nodeFactory;
        }

        private SunNodeFactory getNodeFactory() {
            return _nodeFactory;
        }
    }
}
