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

import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;

import javax.tools.DiagnosticListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Queue;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunResolution {

    private final Attr _attr;
    private final LogDisabler _logDisabler;

    public SunResolution(Context context) {
        requireNonNull("context", context);
        _attr = Attr.instance(context);
        _logDisabler = new LogDisabler(context);
    }

    /**
     * During resolution, the resolver will emit resolution errors, but without appropriate file names and line numbers. If these resolution errors stick around
     * then they will be generated AGAIN, this time with proper names and line numbers, at the end. Therefore, we want to suppress the logger.
     */
    private static final class LogDisabler {

        private static final ThreadLocal<Queue<?>> QUEUE_CACHE = new ThreadLocal<Queue<?>>();

        private static final Field ERR_WRITER_FIELD;
        private static final Field WARN_WRITER_FIELD;
        private static final Field NOTICE_WRITER_FIELD;
        private static final Field DUMP_ON_ERROR_FIELD;
        private static final Field PROMPT_ON_ERROR_FIELD;
        private static final Field DIAGNOSTIC_LISTENER_FIELD;
        private static final Field DEFER_DIAGNOSTICS_FIELD;
        private static final Field DEFERRED_DIAGNOSTICS_FIELD;
        private static final boolean BOTHER;

        private final Log _log;
        private final Context _context;

        private PrintWriter _errWriter;
        private PrintWriter _warnWriter;
        private PrintWriter _noticeWriter;
        private Boolean _dumpOnError;
        private Boolean _promptOnError;
        private DiagnosticListener<?> _contextDiagnosticListener;
        private DiagnosticListener<?> _logDiagnosticListener;

        static {
            boolean bother;
            Field a = null;
            Field b = null;
            Field c = null;
            Field d = null;
            Field e = null;
            Field f = null;
            Field g = null;
            Field h = null;
            try {
                a = Log.class.getDeclaredField("errWriter");
                b = Log.class.getDeclaredField("warnWriter");
                c = Log.class.getDeclaredField("noticeWriter");
                d = Log.class.getDeclaredField("dumpOnError");
                e = Log.class.getDeclaredField("promptOnError");
                f = Log.class.getDeclaredField("diagListener");
                bother = true;
                a.setAccessible(true);
                b.setAccessible(true);
                c.setAccessible(true);
                d.setAccessible(true);
                e.setAccessible(true);
                f.setAccessible(true);
            } catch (Exception ignored) {
                bother = false;
            }

            try {
                g = Log.class.getDeclaredField("deferDiagnostics");
                h = Log.class.getDeclaredField("deferredDiagnostics");
                g.setAccessible(true);
                h.setAccessible(true);
            } catch (Exception ignored) {}

            ERR_WRITER_FIELD = a;
            WARN_WRITER_FIELD = b;
            NOTICE_WRITER_FIELD = c;
            DUMP_ON_ERROR_FIELD = d;
            PROMPT_ON_ERROR_FIELD = e;
            DIAGNOSTIC_LISTENER_FIELD = f;
            DEFER_DIAGNOSTICS_FIELD = g;
            DEFERRED_DIAGNOSTICS_FIELD = h;
            BOTHER = bother;
        }

        LogDisabler(Context context) {
            _log = Log.instance(context);
            _context = context;
        }

        boolean disableLoggers() {
            _contextDiagnosticListener = _context.get(DiagnosticListener.class);
            _context.put(DiagnosticListener.class, (DiagnosticListener<?>) null);
            boolean result;
            if (BOTHER) {
                result = true;
                final PrintWriter dummyWriter = new PrintWriter(new OutputStream() { @Override public void write(int b) throws IOException {
                    // Do nothing on purpose
                }});

                if (DEFER_DIAGNOSTICS_FIELD != null) {
                    try {
                        if (Boolean.TRUE.equals(DEFER_DIAGNOSTICS_FIELD.get(_log))) {
                            QUEUE_CACHE.set((Queue<?>) DEFERRED_DIAGNOSTICS_FIELD.get(_log));
                            final Queue<?> empty = new LinkedList<Object>();
                            DEFERRED_DIAGNOSTICS_FIELD.set(_log, empty);
                        }
                    } catch (Exception ignored) {}
                }

                if (result) {
                    try {
                        _errWriter = (PrintWriter) ERR_WRITER_FIELD.get(_log);
                        ERR_WRITER_FIELD.set(_log, dummyWriter);
                    } catch (Exception ignored) {
                        result = false;
                    }
                }

                if (result) {
                    try {
                        _warnWriter = (PrintWriter) WARN_WRITER_FIELD.get(_log);
                        WARN_WRITER_FIELD.set(_log, dummyWriter);
                    } catch (Exception ignored) {
                        result = false;
                    }
                }

                if (result) {
                    try {
                        _noticeWriter = (PrintWriter) NOTICE_WRITER_FIELD.get(_log);
                        NOTICE_WRITER_FIELD.set(_log, dummyWriter);
                    } catch (Exception ignored) {
                        result = false;
                    }
                }

                if (result) {
                    try {
                        _dumpOnError = (Boolean) DUMP_ON_ERROR_FIELD.get(_log);
                        DUMP_ON_ERROR_FIELD.set(_log, false);
                    } catch (Exception ignored) {
                        result = false;
                    }
                }

                if (result) {
                    try {
                        _promptOnError = (Boolean) PROMPT_ON_ERROR_FIELD.get(_log);
                        PROMPT_ON_ERROR_FIELD.set(_log, false);
                    } catch (Exception ignored) {
                        result = false;
                    }
                }

                if (result) {
                    try {
                        _logDiagnosticListener = (DiagnosticListener<?>) DIAGNOSTIC_LISTENER_FIELD.get(_log);
                        DIAGNOSTIC_LISTENER_FIELD.set(_log, null);
                    } catch (Exception ignored) {
                        result = false;
                    }
                }

                if (!result) {
                    enableLoggers();
                }
            } else {
                result = false;
            }
            return result;
        }

        void enableLoggers() {
            if (_contextDiagnosticListener != null) {
                _context.put(DiagnosticListener.class, _contextDiagnosticListener);
                _contextDiagnosticListener = null;
            }

            if (_errWriter != null) {
                try {
                    ERR_WRITER_FIELD.set(_log, _errWriter);
                    _errWriter = null;
                } catch (Exception ignored) {}
            }

            if (_warnWriter != null) {
                try {
                    WARN_WRITER_FIELD.set(_log, _warnWriter);
                    _warnWriter = null;
                } catch (Exception ignored) {}
            }

            if (_noticeWriter != null) {
                try {
                    NOTICE_WRITER_FIELD.set(_log, _noticeWriter);
                    _noticeWriter = null;
                } catch (Exception ignored) {}
            }

            if (_dumpOnError != null) {
                try {
                    DUMP_ON_ERROR_FIELD.set(_log, _dumpOnError);
                    _dumpOnError = null;
                } catch (Exception ignored) {}
            }

            if (_promptOnError != null) {
                try {
                    PROMPT_ON_ERROR_FIELD.set(_log, _promptOnError);
                    _promptOnError = null;
                } catch (Exception ignored) {}
            }

            if (_logDiagnosticListener != null) {
                try {
                    DIAGNOSTIC_LISTENER_FIELD.set(_log, _logDiagnosticListener);
                    _logDiagnosticListener = null;
                } catch (Exception ignored) {}
            }

            if (DEFER_DIAGNOSTICS_FIELD != null && QUEUE_CACHE.get() != null) {
                try {
                    DEFERRED_DIAGNOSTICS_FIELD.set(_log, QUEUE_CACHE.get());
                    QUEUE_CACHE.set(null);
                } catch (Exception ignored) {}
            }
        }
    }



/*    public Map<JCTree, JCTree> resolveMethodMember(SunNode node) {
        final ArrayDeque<JCTree> stack = new ArrayDeque<JCTree>();

        {
            SunNode n = node;
            while (n != null) {
                stack.push(n.get());
                n = n.up();
            }
        }

        _logDisabler.disableLoggers();
        try {
            final EnvFinder finder = new EnvFinder(node.getContext());
            while (!stack.isEmpty()) { stack.pop().accept(finder); }

            final TreeMirrorMaker mirrorMaker = new TreeMirrorMaker(node);
            final JCTree copy = mirrorMaker.copy(finder.copyAt());

            attrib(copy, finder.get());
            return mirrorMaker.getOriginalToCopyMap();
        } finally {
            _logDisabler.enableLoggers();
        }
    }

    public void resolveClassMember(JavacNode node) {
        final ArrayDeque<JCTree> stack = new ArrayDeque<JCTree>();

        {
            JavacNode n = node;
            while (n != null) {
                stack.push(n.get());
                n = n.up();
            }
        }

        _logDisabler.disableLoggers();
        try {
            final EnvFinder finder = new EnvFinder(node.getContext());
            while (!stack.isEmpty()) { stack.pop().accept(finder); }

            attrib(node.get(), finder.get());
        } finally {
            _logDisabler.enableLoggers();
        }
    }

    private void attrib(JCTree tree, Env<AttrContext> env) {
        if (tree instanceof JCBlock) { _attr.attribStat(tree, env); } else if (tree instanceof JCMethodDecl) { _attr.attribStat(((JCMethodDecl) tree).body, env); } else if (tree instanceof JCVariableDecl) { _attr.attribStat(tree, env); } else { throw new IllegalStateException("Called with something that isn't a block, method decl, or variable decl"); }
    }

    public static class TypeNotConvertibleException extends Exception {

        public TypeNotConvertibleException(String msg) {
            super(msg);
        }
    }

    public static Type ifTypeIsIterableToComponent(Type type, JavacAST ast) {
        final Types types = Types.instance(ast.getContext());
        final Symtab syms = Symtab.instance(ast.getContext());
        final Type boundType = types.upperBound(type);
        final Type elemTypeIfArray = types.elemtype(boundType);
        if (elemTypeIfArray != null) { return elemTypeIfArray; }

        final Type base = types.asSuper(boundType, syms.iterableType.tsym);
        if (base == null) { return syms.objectType; }

        final List<Type> iterableParams = base.allparams();
        return iterableParams.isEmpty() ? syms.objectType : types.upperBound(iterableParams.head);
    }

    public static JCExpression typeToJCTree(Type type, TreeMaker maker, JavacAST ast, boolean allowVoid) throws TypeNotConvertibleException {
        return typeToJCTree(type, maker, ast, false, allowVoid);
    }

    public static JCExpression createJavaLangObject(TreeMaker maker, JavacAST ast) {
        JCExpression out = maker.Ident(ast.toName("java"));
        out = maker.Select(out, ast.toName("lang"));
        out = maker.Select(out, ast.toName("Object"));
        return out;
    }

    private static JCExpression typeToJCTree(Type type, TreeMaker maker, JavacAST ast, boolean allowCompound, boolean allowVoid) throws TypeNotConvertibleException {
        int dims = 0;
        Type type0 = type;
        while (type0 instanceof ArrayType) {
            dims++;
            type0 = ((ArrayType) type0).elemtype;
        }

        JCExpression result = typeToJCTree0(type0, maker, ast, allowCompound, allowVoid);
        while (dims > 0) {
            result = maker.TypeArray(result);
            dims--;
        }
        return result;
    }

    private static JCExpression typeToJCTree0(Type type, TreeMaker maker, JavacAST ast, boolean allowCompound, boolean allowVoid) throws TypeNotConvertibleException {
        // NB: There's such a thing as maker.Type(type), but this doesn't work very well; it screws up anonymous classes, captures, and adds an extra prefix dot for some reason too.
        //  -- so we write our own take on that here.

        if (type.tag == Javac.getCtcInt(TypeTags.class, "BOT")) { return createJavaLangObject(maker, ast); }
        if (type.tag == Javac.getCtcInt(TypeTags.class, "VOID")) { return allowVoid ? primitiveToJCTree(type.getKind(), maker) : createJavaLangObject(maker, ast); }
        if (type.isPrimitive()) { return primitiveToJCTree(type.getKind(), maker); }
        if (type.isErroneous()) { throw new TypeNotConvertibleException("Type cannot be resolved"); }

        final TypeSymbol symbol = type.asElement();
        final List<Type> generics = type.getTypeArguments();

        JCExpression replacement = null;

        if (symbol == null) { throw new TypeNotConvertibleException("Null or compound type"); }

        if (symbol.name.length() == 0) {
            // Anonymous inner class
            if (type instanceof ClassType) {
                final List<Type> ifaces = ((ClassType) type).interfaces_field;
                final Type supertype = ((ClassType) type).supertype_field;
                if (ifaces != null && ifaces.length() == 1) {
                    return typeToJCTree(ifaces.get(0), maker, ast, allowCompound, allowVoid);
                }
                if (supertype != null) { return typeToJCTree(supertype, maker, ast, allowCompound, allowVoid); }
            }
            throw new TypeNotConvertibleException("Anonymous inner class");
        }

        if (type instanceof CapturedType || type instanceof WildcardType) {
            final Type lower;
            final Type upper;
            if (type instanceof WildcardType) {
                upper = ((WildcardType) type).getExtendsBound();
                lower = ((WildcardType) type).getSuperBound();
            } else {
                lower = type.getLowerBound();
                upper = type.getUpperBound();
            }
            if (allowCompound) {
                if (lower == null || lower.tag == Javac.getCtcInt(TypeTags.class, "BOT")) {
                    if (upper == null || upper.toString().equals("java.lang.Object")) {
                        return maker.Wildcard(maker.TypeBoundKind(BoundKind.UNBOUND), null);
                    }
                    return maker.Wildcard(maker.TypeBoundKind(BoundKind.EXTENDS), typeToJCTree(upper, maker, ast, false, false));
                } else {
                    return maker.Wildcard(maker.TypeBoundKind(BoundKind.SUPER), typeToJCTree(lower, maker, ast, false, false));
                }
            }
            if (upper != null) {
                return typeToJCTree(upper, maker, ast, allowCompound, allowVoid);
            }

            return createJavaLangObject(maker, ast);
        }

        final String qName = symbol.getQualifiedName().toString();
        if (qName.isEmpty()) { throw new TypeNotConvertibleException("unknown type"); }
        if (qName.startsWith("<")) { throw new TypeNotConvertibleException(qName); }
        final String[] baseNames = symbol.getQualifiedName().toString().split("\\.");
        replacement = maker.Ident(ast.toName(baseNames[0]));
        for (int i = 1; i < baseNames.length; i++) {
            replacement = maker.Select(replacement, ast.toName(baseNames[i]));
        }

        if (generics != null && !generics.isEmpty()) {
            final ListBuffer<JCExpression> args = ListBuffer.lb();
            for (Type t : generics) { args.append(typeToJCTree(t, maker, ast, true, false)); }
            replacement = maker.TypeApply(replacement, args.toList());
        }

        return replacement;
    }

    private static JCExpression primitiveToJCTree(TypeKind kind, TreeMaker maker) throws TypeNotConvertibleException {
        switch (kind) {
            case BYTE:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "BYTE"));
            case CHAR:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "CHAR"));
            case SHORT:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "SHORT"));
            case INT:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "INT"));
            case LONG:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "LONG"));
            case FLOAT:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "FLOAT"));
            case DOUBLE:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "DOUBLE"));
            case BOOLEAN:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "BOOLEAN"));
            case VOID:
                return maker.TypeIdent(Javac.getCtcInt(TypeTags.class, "VOID"));
            case NULL:
            case NONE:
            case OTHER:
            default:
                throw new TypeNotConvertibleException("Nulltype");
        }
    }*/
}
