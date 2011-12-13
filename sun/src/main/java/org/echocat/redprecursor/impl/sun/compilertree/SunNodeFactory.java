package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.JavacScope;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import org.echocat.redprecursor.compilertree.*;
import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.ExpressionStatement;
import org.echocat.redprecursor.compilertree.base.Node;
import org.echocat.redprecursor.compilertree.base.Statement;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunNodeFactory implements NodeFactory {

    private static final TreeDiscovery<SunNode> DISCOVERY = new TreeDiscovery<SunNode>() {
        @Override
        protected Iterator<SunNode> getIteratorFor(@Nonnull SunNode node) {
            //noinspection unchecked
            return (Iterator<SunNode>) node.getAllEnclosedNodes().iterator();
        }

        @Override
        protected boolean isSearchedNode(@Nonnull SunNode currentNode, @Nonnull SunNode nodeToSearch) {
            return nodeToSearch.getJc().equals(currentNode.getJc());
        }
    };

    private final TreeMaker _treeMaker;
    private final SunNodeConverter _converter;
    private final Attr _attr;
    private final JCCompilationUnit _jcCompilationUnit;
    private final Context _context;

    public SunNodeFactory(TreeMaker treeMaker, SunNodeConverter converter, JCCompilationUnit jcCompilationUnit, Attr attr, Context context) {
        _treeMaker = requireNonNull("treeMaker", treeMaker);
        _converter = requireNonNull("converter", converter);
        _jcCompilationUnit = requireNonNull("jcCompilationUnit", jcCompilationUnit);
        _attr = requireNonNull("attr", attr);
        _context = requireNonNull("context", context);
    }

    @Nonnull
    @Override
    public List<? extends SunNode> getTreeFor(Node nodeToBuildTreeFor) {
        final SunNode node = castNonNullParameterTo("nodeToBuildTreeFor", nodeToBuildTreeFor, SunNode.class);
        final SunCompilationUnit compilationUnit = _converter.jcToNode(_jcCompilationUnit, SunCompilationUnit.class);
        return DISCOVERY.discover(compilationUnit, node);
    }

    @Override
    public SunIdentifier findTypeFor(Expression expressionToFindFor) {
        final SunExpression sunExpression = castNonNullParameterTo("expressionToFindFor", expressionToFindFor, SunExpression.class);

        final JavacTrees trees = JavacTrees.instance(new DelegatingContext(_context) {
            @Override public <T> T get(Class<T> key) {
                return JavacTrees.class.equals(key) || JavacTaskImpl.class.equals(key) ? null : super.get(key);
            }
            @Override public <T> void put(Class<T> clazz, T data) {
                if (!JavacTrees.class.equals(clazz)) {
                    super.put(clazz, data);
                }
            }
        });
        final TreePath path = TreePath.getPath(_jcCompilationUnit, sunExpression.getJc());
        final JavacScope scope = trees.getScope(path);
        final Env<AttrContext> env = scope.getEnv();
        final Type type = _attr.attribExpr(sunExpression.getJc(), env, Type.noType);
        final SunIdentifier result;
        if (type != null && type.tag < TypeTags.VOID) {
            final JCPrimitiveTypeTree typeExpression = _treeMaker.TypeIdent(type.tag);
            result = _converter.jcToNode(typeExpression, SunPrimitiveType.class);
        } else if (type != null) {
            final JCIdent jc = _treeMaker.Ident(type.tsym);
            result = _converter.jcToNode(jc, SunIdentifier.class);
        } else {
            result = null;
        }
        return result;
    }

    @Override
    public SunAnnotation createAnnotation(Identifier type) {
        final JCExpression jcType = _converter.nodeToJc(requireNonNull("type", type), SunIdentifier.class, JCExpression.class);
        final JCAnnotation jc = _treeMaker.Annotation(jcType, com.sun.tools.javac.util.List.<JCExpression>nil());
        return _converter.jcToNode(jc, SunAnnotation.class);
    }

    @Override
    public SunArrayAccess createArrayAccess(Expression toAccess, Expression atIndex) {
        final SunExpression sunToAccess = castNonNullParameterTo("toAccess", toAccess, SunExpression.class);
        final SunExpression sunAtIndex = castNonNullParameterTo("atIndex", atIndex, SunExpression.class);
        final JCArrayAccess jc = _treeMaker.Indexed(sunToAccess.getJc(), sunAtIndex.getJc());
        return _converter.jcToNode(jc, SunArrayAccess.class);
    }

    @Override
    public ArrayType createArrayType(Expression type) {
        final JCExpression jcType = _converter.nodeToJc(requireNonNull("type", type), SunExpression.class, JCExpression.class);
        final JCArrayTypeTree jc = _treeMaker.TypeArray(jcType);
        return _converter.jcToNode(jc, SunArrayType.class);
    }

    @Override
    public SunAssert createAssert(Expression toCheck) {
        final SunExpression expression = castNonNullParameterTo("toCheck", toCheck, SunExpression.class);
        final JCAssert jc = _treeMaker.Assert(expression.getJc(), null);
        return _converter.jcToNode(jc, SunAssert.class);
    }

    @Override
    public SunAssign createAssign(Expression toAssign, Expression to) {
        final SunExpression sunToAssign = castNonNullParameterTo("toAssign", toAssign, SunExpression.class);
        final SunExpression sunTo = castNonNullParameterTo("to", to, SunExpression.class);
        final JCAssign jc = _treeMaker.Assign(sunToAssign.getJc(), sunTo.getJc());
        return _converter.jcToNode(jc, SunAssign.class);
    }

    @Override
    public SunAssignWithOperation createAssignWithOperation(Expression toAssign, Operator operator, Expression to) {
        final SunExpression sunToAssign = castNonNullParameterTo("toAssign", toAssign, SunExpression.class);
        requireNonNull("operator", operator);
        final SunExpression sunTo = castNonNullParameterTo("to", to, SunExpression.class);
        final JCAssignOp jc = _treeMaker.Assignop(SunOperatorUtil.operatorToCode(operator), sunToAssign.getJc(), sunTo.getJc());
        return _converter.jcToNode(jc, SunAssignWithOperation.class);
    }

    @Override
    public SunBinaryCondition createBinaryCondition(Expression left, Operator operator, Expression right) {
        final SunExpression sunLeft = castNonNullParameterTo("left", left, SunExpression.class);
        requireNonNull("operator", operator);
        final SunExpression sunRight = castNonNullParameterTo("right", right, SunExpression.class);
        final JCBinary jc = _treeMaker.Binary(SunOperatorUtil.operatorToCode(operator), sunLeft.getJc(), sunRight.getJc());
        return _converter.jcToNode(jc, SunBinaryCondition.class);
    }

    @Override
    public Block createBlock() {
        final JCBlock jc = _treeMaker.Block(0, com.sun.tools.javac.util.List.<JCStatement>nil());
        return _converter.jcToNode(jc, SunBlock.class);
    }

    @Override
    public SunBreak createBreak() {
        final JCBreak jc = _treeMaker.Break(null);
        return _converter.jcToNode(jc, SunBreak.class);
    }

    @Override
    public SunCase createCase(Expression mustMatch) {
        final SunExpression sunMustMatch = castNonNullParameterTo("mustMatch", mustMatch, SunExpression.class);
        final JCCase jc = _treeMaker.Case(sunMustMatch.getJc(), com.sun.tools.javac.util.List.<JCStatement>nil());
        return _converter.jcToNode(jc, SunCase.class);
    }

    @Override
    public SunCast createCast(Expression cast, Identifier to) {
        final JCExpression jcCast = _converter.nodeToJc(requireNonNull("cast", cast), SunExpression.class, JCExpression.class);
        final JCExpression jcTo = _converter.nodeToJc(requireNonNull("to", to), SunIdentifier.class, JCExpression.class);
        final JCTypeCast jc = _treeMaker.TypeCast(jcTo, jcCast);
        return _converter.jcToNode(jc, SunCast.class);
    }

    @Override
    public SunCatch createCatch(VariableDeclaration parameterDeclaration) {
        final SunVariableDeclaration sunParameterDeclaration = castNonNullParameterTo("sunParameterDeclaration", parameterDeclaration, SunVariableDeclaration.class);
        final JCCatch jc = _treeMaker.Catch(sunParameterDeclaration.getJc(), _treeMaker.Block(0, com.sun.tools.javac.util.List.<JCStatement>nil()));
        return _converter.jcToNode(jc, SunCatch.class);
    }

    @Override
    public SunClassDeclaration createClassDeclaration(String name, Modifiers modifiers) {
        final Name jcName = _converter.stringToName(requireNonNull("name", name));
        final SunModifiers sunModifiers = castNonNullParameterTo("modifiers", modifiers, SunModifiers.class);
        final JCClassDecl jc = _treeMaker.ClassDef(sunModifiers.getJc(), jcName, com.sun.tools.javac.util.List.<JCTypeParameter>nil(), null, com.sun.tools.javac.util.List.<JCExpression>nil(), null);
        return _converter.jcToNode(jc, SunClassDeclaration.class);
    }

    @Override
    public SunConditional createConditional(Expression condition) {
        final SunExpression sunCondition = castNonNullParameterTo("condition", condition, SunExpression.class);
        final JCConditional jc = _treeMaker.Conditional(sunCondition.getJc(), null, null);
        return _converter.jcToNode(jc, SunConditional.class);
    }

    @Override
    public SunContinue createContinue() {
        final JCContinue jc = _treeMaker.Continue(null);
        return _converter.jcToNode(jc, SunContinue.class);
    }

    @Override
    public SunDoWhile createDoWhile(Expression expression) {
        final SunExpression sunExpression = castNonNullParameterTo("expression", expression, SunExpression.class);
        final JCDoWhileLoop jc = _treeMaker.DoLoop(null, sunExpression.getJc());
        return _converter.jcToNode(jc, SunDoWhile.class);
    }

    @Override
    public SunErroneous createErroneous(List<Node> errors) {
        final com.sun.tools.javac.util.List<JCTree> jcErrors = _converter.nodesToJcs(errors, SunNode.class, JCTree.class);
        final JCErroneous jc = _treeMaker.Erroneous(jcErrors);
        return _converter.jcToNode(jc, SunErroneous.class);
    }

    @Override
    public SunExpressionStatement createExpressionStatement(Expression expression) {
        final JCExpression jcExpression = _converter.nodeToJc(requireNonNull("expression", expression), SunExpression.class, JCExpression.class);
        final JCExpressionStatement jc = _treeMaker.Exec(jcExpression);
        return _converter.jcToNode(jc, SunExpressionStatement.class);
    }

    @Override
    public SunFieldAccess createFieldAccess(Expression containedIn, String fieldName) {
        final JCExpression jcContainedId = _converter.nodeToJc(requireNonNull("containedIn", containedIn), SunExpression.class, JCExpression.class);
        final Name jcName = _converter.stringToName(requireNonEmpty("fieldName", fieldName));
        final JCFieldAccess jc = _treeMaker.Select(jcContainedId, jcName);
        return _converter.jcToNode(jc, SunFieldAccess.class);
    }

    @Override
    public SunIdentifier createIdentifier(Expression containedIn, String identifier) {
        JCExpression expression = _converter.nodeToJc(containedIn, SunExpression.class, JCExpression.class);
        int i = 0;
        final String[] parts = requireNonEmpty("identifier", identifier).split("\\.");
        if (expression == null) {
            expression = _treeMaker.Ident(_converter.stringToName(parts[i++]));
        }
        while (i < parts.length) {
            expression = _treeMaker.Select(expression, _converter.stringToName(parts[i++]));
        }
        return _converter.jcToNode(expression, SunIdentifier.class);
    }

    @Override
    public SunIdentifier createIdentifier(String fieldName) {
        return createIdentifier(null, fieldName);
    }


    @Override
    public SunFor createFor(List<Statement> initStatements, Expression condition, List<ExpressionStatement> stepStatements) {
        final com.sun.tools.javac.util.List<JCStatement> jcInitStatements = _converter.nodesToJcs(initStatements, SunStatement.class, JCStatement.class);
        final JCExpression jcCondition = _converter.nodeToJc(condition, SunExpression.class, JCExpression.class);
        final com.sun.tools.javac.util.List<JCExpressionStatement> jcStepStatements = _converter.nodesToJcs(stepStatements, SunExpressionStatement.class, JCExpressionStatement.class);
        final JCForLoop jc = _treeMaker.ForLoop(jcInitStatements, jcCondition, jcStepStatements, null);
        return _converter.jcToNode(jc, SunFor.class);
    }

    @Override
    public SunForEach createForEach(VariableDeclaration parameterDeclaration, Expression expression) {
        final JCVariableDecl jcVariableDeclaration = _converter.nodeToJc(requireNonNull("parameterDeclaration", parameterDeclaration), SunVariableDeclaration.class, JCVariableDecl.class);
        final JCExpression jcExpression = _converter.nodeToJc(requireNonNull("expression", expression), SunExpression.class, JCExpression.class);
        final JCEnhancedForLoop jc = _treeMaker.ForeachLoop(jcVariableDeclaration, jcExpression, null);
        return _converter.jcToNode(jc, SunForEach.class);
    }

    @Override
    public SunIf createIf(Expression condition) {
        final JCExpression jcCondition = _converter.nodeToJc(requireNonNull("condition", condition), SunExpression.class, JCExpression.class);
        final JCIf jc = _treeMaker.If(jcCondition, null, null);
        return _converter.jcToNode(jc, SunIf.class);
    }

    @Override
    public Import createImport(Node identifier, boolean isStatic) {
        final JCTree jcIdentifier = _converter.nodeToJc(requireNonNull("identifier", identifier), SunNode.class, JCTree.class);
        final JCImport jc = _treeMaker.Import(jcIdentifier, isStatic);
        return _converter.jcToNode(jc, SunImport.class);
    }

    @Override
    public SunInstanceOf createInstanceOf(Expression isInstanceOf, Identifier type) {
        final JCExpression jcIsInstanceOf = _converter.nodeToJc(requireNonNull("isInstanceOf", isInstanceOf), SunExpression.class, JCExpression.class);
        final JCTree jcType = _converter.nodeToJc(requireNonNull("type", isInstanceOf), SunIdentifier.class, JCTree.class);
        final JCInstanceOf jc = _treeMaker.TypeTest(jcIsInstanceOf, jcType);
        return _converter.jcToNode(jc, SunInstanceOf.class);
    }

    @Override
    public SunLabelled createLabelled(String name) {
        final Name sunName = _converter.stringToName(requireNonNull("name", name));
        final JCLabeledStatement jc = _treeMaker.Labelled(sunName, null);
        return _converter.jcToNode(jc, SunLabelled.class);
    }

    @Override
    public SunLetExpression createLetExpression(List<VariableDeclaration> variableDeclarations, Node expression) {
        final com.sun.tools.javac.util.List<JCVariableDecl> jcVariableDeclarations = _converter.nodesToJcs(requireNonNull("variableDeclarations", variableDeclarations), SunVariableDeclaration.class, JCVariableDecl.class);
        final JCTree jcExpression = _converter.nodeToJc(requireNonNull("expression", expression), SunNode.class, JCTree.class);
        final LetExpr jc = _treeMaker.LetExpr(jcVariableDeclarations, jcExpression);
        return _converter.jcToNode(jc, SunLetExpression.class);
    }

    @Override
    public SunLiteral createLiteral(Object value) {
        final Primitive primitive = Primitive.getTypeFor(value);
        if (primitive == null) {
            throw new IllegalArgumentException("The given value is not a primitive: " + value);
        }
        return createLiteral(primitive, value);
    }

    @Override
    public SunLiteral createLiteral(Primitive primitive, Object value) {
        requireNonNull("primitive", primitive);
        if (!primitive.isValueOfType(value)) {
            throw new IllegalArgumentException("The given value (" + value + ") is not of given primitive: " + primitive);
        }
        final int code = SunPrimitiveUtil.primitiveToCode(primitive);
        final JCLiteral jc = _treeMaker.Literal(code, value);
        return _converter.jcToNode(jc, SunLiteral.class);
    }

    @Override
    public SunMethodDeclaration createMethodDeclaration(String name, Modifiers modifier, Identifier returnType, List<VariableDeclaration> parameterDeclarations) {
        final Name jcName = _converter.stringToName(requireNonNull("name", name));
        final JCModifiers jcModifiers = _converter.nodeToJc(requireNonNull("modifier", modifier), SunModifiers.class, JCModifiers.class);
        final JCExpression jcType = _converter.nodeToJc(requireNonNull("returnType", returnType), SunIdentifier.class, JCExpression.class);
        final com.sun.tools.javac.util.List<JCVariableDecl> jcVariableDecls = _converter.nodesToJcs(parameterDeclarations, SunVariableDeclaration.class, JCVariableDecl.class);
        final JCMethodDecl jc = _treeMaker.MethodDef(requireNonNull("jcModifiers", jcModifiers), jcName, jcType, com.sun.tools.javac.util.List.<JCTypeParameter>nil(), jcVariableDecls, com.sun.tools.javac.util.List.<JCExpression>nil(), null, null);
        return _converter.jcToNode(jc, SunMethodDeclaration.class);
    }

    @Override
    public SunMethodInvocation createMethodInvocation(Expression method, Expression... arguments) {
        return createMethodInvocation(method, asList(arguments));
    }

    @Override
    public SunMethodInvocation createMethodInvocation(Expression method, List<Expression> arguments) {
        final JCExpression jcMethodExpression = _converter.nodeToJc(requireNonNull("method", method), SunExpression.class, JCExpression.class);
        final com.sun.tools.javac.util.List<JCExpression> jcArgumentExpressions = _converter.nodesToJcs(arguments, SunExpression.class, JCExpression.class);
        final JCMethodInvocation jc = _treeMaker.Apply(com.sun.tools.javac.util.List.<JCExpression>nil(), jcMethodExpression, jcArgumentExpressions);
        return _converter.jcToNode(jc, SunMethodInvocation.class);
    }

    @Override
    public SunModifiers createModifier(List<Modifier> modifiers) {
        final JCModifiers jcModifiers = _treeMaker.Modifiers(SunModifierUtil.getValue(modifiers));
        return _converter.jcToNode(jcModifiers, SunModifiers.class);
    }

    @Override
    public NewArray createNewArrayValue(Expression... values) {
        return createNewArrayValue(Arrays.asList(values));
    }

    @Override
    public NewArray createNewArrayValue(List<Expression> values) {
        final JCNewArray jc = _treeMaker.NewArray(null, com.sun.tools.javac.util.List.<JCExpression>nil(), null);
        final SunNewArray newArray = _converter.jcToNode(jc, SunNewArray.class);
        newArray.setElements(values);
        return newArray;
    }

    @Override
    public NewArray createNewArray(Identifier typeOfElements, Expression... dimensions) {
        return createNewArray(typeOfElements, asList(dimensions));
    }

    @Override
    public SunNewArray createNewArray(Identifier typeOfElements, List<Expression> dimensions) {
        final JCExpression jcTypeOfElements = _converter.nodeToJc(requireNonNull("typeOfElements", typeOfElements), SunIdentifier.class, JCExpression.class);
        final com.sun.tools.javac.util.List<JCExpression> jcDimensions = _converter.nodesToJcs(requireNonEmpty("dimensions", dimensions), SunExpression.class, JCExpression.class);
        final JCNewArray jc = _treeMaker.NewArray(jcTypeOfElements, com.sun.tools.javac.util.List.<JCExpression>nil(), jcDimensions);
        return _converter.jcToNode(jc, SunNewArray.class);
    }

    @Override
    public NewClass createNewClass(Identifier type, Expression... arguments) {
        return createNewClass(type, asList(arguments));
    }

    @Override
    public SunNewClass createNewClass(Identifier type, List<Expression> arguments) {
        final JCExpression jcType = _converter.nodeToJc(requireNonNull("type", type), SunIdentifier.class, JCExpression.class);
        final com.sun.tools.javac.util.List<JCExpression> jcArguments = _converter.nodesToJcs(requireNonEmpty("arguments", arguments), SunExpression.class, JCExpression.class);
        final JCNewClass jc = _treeMaker.NewClass(null, com.sun.tools.javac.util.List.<JCExpression>nil(), jcType, jcArguments, null);
        return _converter.jcToNode(jc, SunNewClass.class);
    }

    @Override
    public SunParens createParens(Expression value) {
        final JCExpression jcValue = _converter.nodeToJc(requireNonNull("value", value), SunKey.class, JCExpression.class);
        final JCParens jc = _treeMaker.Parens(jcValue);
        return _converter.jcToNode(jc, SunParens.class);
    }

    @Override
    public SunReturn createReturn(Expression value) {
        final JCExpression jcValue = _converter.nodeToJc(value, SunKey.class, JCExpression.class);
        final JCReturn jc = _treeMaker.Return(jcValue);
        return _converter.jcToNode(jc, SunReturn.class);
    }

    @Override
    public SunSkip createSkip() {
        final JCSkip jc = _treeMaker.Skip();
        return _converter.jcToNode(jc, SunSkip.class);
    }

    @Override
    public SunSwitch createSwitch(Expression condition) {
        final JCExpression jcCondition = _converter.nodeToJc(requireNonNull("condition", condition), SunKey.class, JCExpression.class);
        final JCSwitch jc = _treeMaker.Switch(jcCondition, com.sun.tools.javac.util.List.<JCCase>nil());
        return _converter.jcToNode(jc, SunSwitch.class);
    }

    @Override
    public SunSynchronized createSynchronized(Expression lock) {
        final JCExpression jcLock = _converter.nodeToJc(requireNonNull("lock", lock), SunKey.class, JCExpression.class);
        final JCSynchronized jc = _treeMaker.Synchronized(jcLock, _treeMaker.Block(0, com.sun.tools.javac.util.List.<JCStatement>nil()));
        return _converter.jcToNode(jc, SunSynchronized.class);
    }

    @Override
    public SunThrow createThrow(Expression toThrow) {
        final JCExpression jcToThrow = _converter.nodeToJc(requireNonNull("toThrow", toThrow), SunExpression.class, JCExpression.class);
        final JCThrow jc = _treeMaker.Throw(jcToThrow);
        return _converter.jcToNode(jc, SunThrow.class);
    }

    @Override
    public SunTry createTry() {
        final JCBlock body = _treeMaker.Block(0, com.sun.tools.javac.util.List.<JCStatement>nil());
        final JCBlock finalizer = _treeMaker.Block(0, com.sun.tools.javac.util.List.<JCStatement>nil());
        final JCTry jcTry = _treeMaker.Try(body, com.sun.tools.javac.util.List.<JCCatch>nil(), finalizer);
        return _converter.jcToNode(jcTry, SunTry.class);
    }

    @Override
    public SunPrimitiveType createPrimitiveType(Primitive primitive) {
        final int code = SunPrimitiveUtil.primitiveToCode(requireNonNull("primitive", primitive));
        final JCPrimitiveTypeTree typeExpression = _treeMaker.TypeIdent(code);
        return _converter.jcToNode(typeExpression, SunPrimitiveType.class);
    }

    @Override
    public SunKey createKey(String id) {
        final JCIdent jc = _treeMaker.Ident(_converter.stringToName(requireNonNull("id", id)));
        return _converter.jcToNode(jc, SunKey.class);
    }

    @Override
    public SunTypeApply createTypeApply(Identifier expression, List<Identifier> arguments) {
        final JCExpression jcExpression = _converter.nodeToJc(requireNonNull("expression", expression), SunIdentifier.class, JCExpression.class);
        final com.sun.tools.javac.util.List<JCExpression> jcArguments = _converter.nodesToJcs(requireNonNull("arguments", arguments), SunIdentifier.class, JCExpression.class);
        final JCTypeApply jc = _treeMaker.TypeApply(jcExpression, jcArguments);
        return _converter.jcToNode(jc, SunTypeApply.class);
    }

    @Override
    public SunTypeParameter createTypeParameter(String name) {
        final Name jcName = _converter.stringToName(requireNonNull("name", name));
        final JCTypeParameter jc = _treeMaker.TypeParam(jcName, null);
        return _converter.jcToNode(jc, SunTypeParameter.class);
    }

    @Override
    public SunUnary createUnary(Operator operator, Expression value) {
        final JCExpression jcValue = _converter.nodeToJc(requireNonNull("value", value), SunKey.class, JCExpression.class);
        final JCUnary jc = _treeMaker.Unary(SunOperatorUtil.operatorToCode(operator), jcValue);
        return _converter.jcToNode(jc, SunUnary.class);
    }

    @Override
    public SunVariableDeclaration createVariableDeclaration(String name, Identifier type) {
        requireNonNull("name", name);
        final SunIdentifier sunType = castNonNullParameterTo("type", type, SunIdentifier.class);
        final JCModifiers jcModifiers = _treeMaker.Modifiers(0);
        final Name jcName = _converter.stringToName(name);
        final JCVariableDecl jcVariableDecl = _treeMaker.VarDef(jcModifiers, jcName, sunType.getJc(), null);
        return _converter.jcToNode(jcVariableDecl, SunVariableDeclaration.class);
    }

    @Override
    public SunWhile createWhile(Expression expression) {
        final SunExpression sunExpression = castNonNullParameterTo("expression", expression, SunExpression.class);
        final JCDoWhileLoop jc = _treeMaker.DoLoop(null, sunExpression.getJc());
        return _converter.jcToNode(jc, SunWhile.class);
    }

    @Override
    public Wildcard createWildcard(WildcardKind kind, Expression expression) {
        requireNonNull("kind", kind);
        final SunExpression sunExpression = castNullableParameterTo("expression", expression, SunExpression.class);
        final BoundKind boundKind = SunWildcardKindUtil.ourToSun(kind);
        final TypeBoundKind typeBoundKind = _treeMaker.TypeBoundKind(boundKind);
        final JCWildcard jc = _treeMaker.Wildcard(typeBoundKind, sunExpression != null ? sunExpression.getJc() : null);
        return _converter.jcToNode(jc, SunWildcard.class);
    }
}
