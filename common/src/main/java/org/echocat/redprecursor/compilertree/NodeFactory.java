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

package org.echocat.redprecursor.compilertree;

import org.echocat.redprecursor.compilertree.base.Expression;
import org.echocat.redprecursor.compilertree.base.ExpressionStatement;
import org.echocat.redprecursor.compilertree.base.Node;
import org.echocat.redprecursor.compilertree.base.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface NodeFactory {

    @Nonnull
    public List<? extends Node> getTreeFor(Node nodeToBuildTreeFor);

    @Nullable
    public Identifier findTypeFor(Expression expressionToFindFor);

    public Annotation createAnnotation(Identifier type);

    public ArrayAccess createArrayAccess(Expression toAccess, Expression atIndex);

    public ArrayType createArrayType(Expression type);

    public Assert createAssert(Expression toCheck);

    public Assign createAssign(Expression toAssign, Expression to);

    public AssignWithOperation createAssignWithOperation(Expression toAssign, Operator operator, Expression to);

    public BinaryCondition createBinaryCondition(Expression left, Operator operator, Expression right);

    public Block createBlock();
    
    public Break createBreak();

    public Case createCase(Expression mustMatch);

    public Cast createCast(Expression cast, Identifier to);

    public Catch createCatch(VariableDeclaration parameterDeclaration);

    public ClassDeclaration createClassDeclaration(String name, Modifiers modifier);

    public Conditional createConditional(Expression condition);

    public Continue createContinue();

    public DoWhile createDoWhile(Expression expression);

    public Erroneous createErroneous(List<Node> errors);

    public ExpressionStatement createExpressionStatement(Expression expression);

    public FieldAccess createFieldAccess(Expression containedIn, String fieldName);

    public Identifier createIdentifier(String identifier);

    public Identifier createIdentifier(Expression containedIn, String identifier);

    public For createFor(List<Statement> initStatements, Expression condition, List<ExpressionStatement> stepStatements);

    public ForEach createForEach(VariableDeclaration parameterDeclaration, Expression expression);

    public If createIf(Expression condition);

    public InstanceOf createInstanceOf(Expression isInstanceOf, Identifier type);

    public Import createImport(Node identifier, boolean isStatic);

    public Labelled createLabelled(String name);

    public LetExpression createLetExpression(List<VariableDeclaration> variableDeclarations, Node expression);

    public Literal createLiteral(Object value);

    public Literal createLiteral(Primitive primitive, Object value);

    public MethodDeclaration createMethodDeclaration(String name, Modifiers modifier, Identifier returnType, List<VariableDeclaration> parameterDeclarations);

    public MethodInvocation createMethodInvocation(Expression method, Expression... arguments);

    public MethodInvocation createMethodInvocation(Expression method, List<Expression> arguments);

    public Modifiers createModifier(List<Modifier> modifiers);

    public NewArray createNewArrayValue(Expression... values);

    public NewArray createNewArrayValue(List<Expression> values);

    public NewArray createNewArray(Identifier typeOfElements, Expression... dimensions);

    public NewArray createNewArray(Identifier typeOfElements, List<Expression> dimensions);

    public NewClass createNewClass(Identifier type, Expression... arguments);

    public NewClass createNewClass(Identifier type, List<Expression> arguments);

    public Parens createParens(Expression value);

    public Return createReturn(Expression value);

    public Skip createSkip();

    public Switch createSwitch(Expression condition);

    public Synchronized createSynchronized(Expression lock);

    public Throw createThrow(Expression toThrow);

    public Try createTry();

    public PrimitiveType createPrimitiveType(Primitive primitive);

    public Key createKey(String id);

    public TypeApply createTypeApply(Identifier expression, List<Identifier> arguments);

    public TypeParameter createTypeParameter(String name);

    public Unary createUnary(Operator operator, Expression value);

    public VariableDeclaration createVariableDeclaration(String name, Identifier type);

    public While createWhile(Expression expression);

    public Wildcard createWildcard(WildcardKind kind, Expression expression);
}
