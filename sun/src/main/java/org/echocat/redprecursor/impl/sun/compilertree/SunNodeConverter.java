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

import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.MemberEnter;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Position.LineMap;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.WildcardKind;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.Node;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.*;
import java.util.Map.Entry;

import static java.util.Collections.*;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunNodeConverter {

    private final TreeMaker _treeMaker;
    private final JavacElements _elements;
    private final JCCompilationUnit _compilationUnit;
    private final Map<Class<? extends JCTree>, SunNodeInformation<?, ?>> _jcTypeToInformation;
    private final Enter _enter;
    private final MemberEnter _memberEnter;

    public SunNodeConverter(TreeMaker treeMaker, JavacElements elements, JCCompilationUnit compilationUnit, Enter enter, MemberEnter memberEnter) {
        this(treeMaker, elements, compilationUnit, enter, memberEnter, new SunNodeInformationFactory());
    }

    public SunNodeConverter(TreeMaker treeMaker, JavacElements elements, JCCompilationUnit compilationUnit, Enter enter, MemberEnter memberEnter, SunNodeInformationFactory informationFactory) {
        _treeMaker = requireNonNull("treeMaker", treeMaker);
        _elements = requireNonNull("elements", elements);
        _enter = requireNonNull("enter", enter);
        _memberEnter = requireNonNull("memberEnter", memberEnter);
        _compilationUnit = requireNonNull("compilationUnit", compilationUnit);
        final Map<Class<? extends SunNode>, SunNodeInformation<?, ?>> nodeTypeToInformation = informationFactory.getTypeToInformation();
        _jcTypeToInformation = buildJcToInformation(nodeTypeToInformation);
    }

    private Map<Class<? extends JCTree>, SunNodeInformation<?, ?>> buildJcToInformation(Map<Class<? extends SunNode>, SunNodeInformation<?, ?>> nodeTypeToInformation) {
        final Map<Class<? extends JCTree>, SunNodeInformation<?, ?>> jcTypeToInformation = new HashMap<Class<? extends JCTree>, SunNodeInformation<?, ?>>();
        for (Entry<Class<? extends SunNode>, SunNodeInformation<?, ?>> nodeTypeAndInformation : nodeTypeToInformation.entrySet()) {
            final SunNodeInformation<?, ?> information = nodeTypeAndInformation.getValue();
            final Class<? extends JCTree> jcType = information.getJcType();
            jcTypeToInformation.put(jcType, information);
        }
        return jcTypeToInformation;
    }

    public Symbol nameToType(Name name) {
        Scope.Entry entry = _compilationUnit.namedImportScope.lookup(name);
        if (entry == null || entry.sym == null) {
            entry = _compilationUnit.starImportScope.lookup(name);
        }
        return entry != null ? entry.sym : null;
    }

    public <I extends Node, R extends JCTree> R nodeToJc(I node, Class<? extends I> requiredInputType, Class<? extends R> requiredResultType) {
        requireNonNull("requiredInputType", requiredInputType);
        requireNonNull("requiredResultType", requiredResultType);
        final R jc;
        if (node == null) {
            jc = null;
        } else {
            final SunNode sunNode = (SunNode) castNullableParameterTo("node", node, requiredInputType);
            final JCTree plainJc = sunNode.getJc();
            jc = requiredResultType.cast(plainJc);
        }
        return jc;
    }

    public <I extends JCTree, R extends SunNode> R jcToNode(I jc, Class<R> requiredResultType) {
        requireNonNull("requiredResultType", requiredResultType);
        final R node;
        if (jc == null) {
            node = null;
        } else {
            // noinspection unchecked
            final SunNodeInformation<R, I> information = (SunNodeInformation<R, I>) _jcTypeToInformation.get(jc.getClass());
            if (information == null) {
                throw new IllegalArgumentException("The type of " + jc + " (" + jc.getClass().getName() + ") is unknown.");
            }
            if (!requiredResultType.isAssignableFrom(information.getNodeType())) {
                throw new IllegalArgumentException("The required result type " + requiredResultType.getName() + " could not handled for " + jc + ".");
            }
            node = information.newInstance(jc, this);
        }
        return node;
    }

    public <T extends JCStatement> T statementsToJcBlock(List<? extends Statement> statements, Class<T> expectedType) {
        if (!JCStatement.class.equals(expectedType) && !JCBlock.class.equals(expectedType)) {
            throw new IllegalStateException("Only " + JCStatement.class.getName() + " and " + JCBlock.class.getName() + " are currently supported as expectedType.");
        }
        com.sun.tools.javac.util.List<JCStatement> jcStatements = com.sun.tools.javac.util.List.nil();
        if (statements != null) {
            // noinspection RedundantCast,unchecked
            final List<SunStatement> sunStatements = (List)castNonNullListParameterTo("statements", (List)statements, SunStatement.class);
            for (SunStatement sunStatement : sunStatements) {
                jcStatements = jcStatements.append(sunStatement.getJc());
            }
        }
        final T result;
        if (JCStatement.class.equals(expectedType) && jcStatements.size() == 1) {
            // noinspection unchecked
            result = (T) jcStatements.iterator().next();
        } else {
            // noinspection unchecked
            result = (T) _treeMaker.Block(0, jcStatements);
        }
        return result;
    }

    public List<? extends SunStatement> jcBlockToStatements(JCStatement jc) {
        final List<SunStatement> statements = new ArrayList<SunStatement>();
        if (jc != null) {
            if (jc instanceof JCBlock) {
                for (JCStatement jcStatement : ((JCBlock)jc).stats) {
                    final SunStatement statement = jcToNode(jcStatement, SunStatement.class);
                    statements.add(statement);
                }
            } else {
                final SunStatement statement = jcToNode(jc, SunStatement.class);
                statements.add(statement);
            }
        }
        return unmodifiableList(statements);
    }

    public <R extends JCTree, I extends Node> com.sun.tools.javac.util.List<R> nodesToJcs(List<I> nodes, Class<? extends I> expectedInputType, Class<? extends R> expectedResultType) {
        com.sun.tools.javac.util.List<R> jcs = com.sun.tools.javac.util.List.nil();
        if (nodes != null) {
            //noinspection unchecked
            final List<I> sunNodes = (List<I>) castNonNullListParameterTo("nodes", nodes, expectedInputType);
            for (I node : sunNodes) {
                final JCTree jc = ((SunNode) node).getJc();
                jcs = jcs.append(expectedResultType.cast(jc));
            }
        }
        return jcs;
    }

    public <T extends SunNode> List<? extends T> jcsToNodes(com.sun.tools.javac.util.List<? extends JCTree> jcs, Class<? extends T> expectedType) {
        final List<T> expressions = new ArrayList<T>();
        if (jcs != null) {
            for (JCTree jc : jcs) {
                final T node = jcToNode(jc, expectedType);
                expressions.add(node);
            }
        }
        return unmodifiableList(expressions);
    }

    public Map<String, ? extends SunExpression> jcArgumentExpressionsToExpressionMap(com.sun.tools.javac.util.List<JCExpression> jcExpressions) {
        final Map<String, SunExpression> argumentMap = new LinkedHashMap<String, SunExpression>();
        if (jcExpressions != null) {
            for (JCExpression jcExpression : jcExpressions) {
                if (jcExpression instanceof JCAssign) {
                    final JCAssign jcAssign = (JCAssign) jcExpression;
                    final Name name =  ((JCIdent)jcAssign.lhs).name;
                    final JCExpression valueJcExpression = jcAssign.rhs;
                    argumentMap.put(name.toString(), jcToNode(valueJcExpression, SunExpression.class));
                } else {
                    argumentMap.put(null, jcToNode(jcExpression, SunExpression.class));
                }
            }
        }
        return unmodifiableMap(argumentMap);
    }

    public com.sun.tools.javac.util.List<JCExpression> expressionMapToJcParameterExpressions(Map<String, ? extends Expression> arguments) {
        com.sun.tools.javac.util.List<JCExpression> jcArguments = com.sun.tools.javac.util.List.nil();
        if (arguments != null) {
            // noinspection RedundantCast,unchecked
            final Map<String, SunExpression> sunArguments = (Map)castNonNullMapParameterTo("arguments", (Map)arguments, String.class, SunExpression.class);
            for (Entry<String, SunExpression> parameter : sunArguments.entrySet()) {
                final String key = parameter.getKey();
                final SunExpression value = parameter.getValue();
                final JCIdent ident = _treeMaker.Ident(_elements.getName(key));
                final JCAssign assignment = _treeMaker.Assign(ident, value.getJc());
                jcArguments = jcArguments.append(assignment);
            }
        }
        return jcArguments;
    }

    public Name stringToName(CharSequence charSequence) {
        return charSequence != null ? _elements.getName(charSequence) : null;
    }

    public String nameToString(Name name) {
        return name != null ? name.toString() : null;
    }

    public int positionToJcPosition(Position position) {
        final LineMap lineMap = _compilationUnit.getLineMap();
        return position != null ? (int) lineMap.getPosition(position.getLine(), position.getColumn()) : 0;
    }

    public Position jcPositionToPosition(long position) {
        final LineMap lineMap = _compilationUnit.getLineMap();
        final long lineNumber = lineMap.getLineNumber(position);
        final long columnNumber = lineMap.getColumnNumber(position);
        return new Position(lineNumber, columnNumber);
    }

    public Env<AttrContext> getEnvFor(JCTree tree, Env<AttrContext> parent) {
        final Env<AttrContext> result;
        if (tree instanceof JCCompilationUnit) {
            result = _enter.getTopLevelEnv((JCCompilationUnit) tree);
        } else if (tree instanceof JCClassDecl) {
            result = _enter.classEnv((JCClassDecl) tree, parent);
        } else if (tree instanceof JCMethodDecl) {
            result = _memberEnter.getMethodEnv((JCMethodDecl) tree, parent);
        } else if (tree instanceof JCVariableDecl) {
            result = _memberEnter.getInitEnv((JCVariableDecl) tree, parent);
        } else {
            result = null;
        }
        return result;
    }

    public TypeBoundKind wildcardKindToTypeBoundKind(WildcardKind wildcardKind) {
        final BoundKind boundKind = SunWildcardKindUtil.ourToSun(requireNonNull("wildcardKind", wildcardKind));
        final TypeBoundKind typeBoundKind = _treeMaker.TypeBoundKind(boundKind);
        return typeBoundKind;
    }

    public BoundKind wildcardKindToBoundKind(WildcardKind wildcardKind) {
        final TypeBoundKind typeBoundKind = wildcardKindToTypeBoundKind(wildcardKind);
        return typeBoundKind != null ? typeBoundKind.kind : null;
    }

    public WildcardKind typeBoundKindToWildcardKind(TypeBoundKind typeBoundKind) {
        final BoundKind boundKind = requireNonNull("typeBoundKind", typeBoundKind).kind;
        return boundKindToWildcardKind(boundKind);
    }

    public WildcardKind boundKindToWildcardKind(BoundKind boundKind) {
        final WildcardKind wildcardKind = SunWildcardKindUtil.sunToOur(requireNonNull("boundKind", boundKind));
        return wildcardKind;
    }

    public TreeMaker getTreeMaker() {
        return _treeMaker;
    }
}
