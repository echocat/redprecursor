package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import org.echocat.redprecursor.compilertree.ClassDeclaration;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.NewClass;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunNewClass implements NewClass, SunExpression {

    private final JCNewClass _jc;
    private final SunNodeConverter _converter;

    public SunNewClass(JCNewClass jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getEnclosing() {
        return _converter.jcToNode(_jc.encl, SunExpression.class);
    }

    @Override
    public void setEnclosing(Expression enclosing) {
        _jc.encl = _converter.nodeToJc(enclosing, SunExpression.class, JCExpression.class);
    }

    @Override
    public List<? extends SunExpression> getArguments() {
        return _converter.jcsToNodes(_jc.args, SunExpression.class);
    }

    @Override
    public void setArguments(List<Expression> arguments) {
        _jc.args = _converter.nodesToJcs(arguments, SunExpression.class, JCExpression.class);
    }

    @Override
    public SunIdentifier getType() {
        return _converter.jcToNode(_jc.clazz, SunIdentifier.class);
    }

    @Override
    public void setType(Identifier type) {
        _jc.clazz = _converter.nodeToJc(type, SunIdentifier.class, JCExpression.class);
    }

    @Override
    public List<? extends SunExpression> getTypeParameters() {
        return _converter.jcsToNodes(_jc.typeargs, SunExpression.class);
    }

    @Override
    public void setTypeParameters(List<Expression> typeParameters) {
        _jc.typeargs = _converter.nodesToJcs(typeParameters, SunExpression.class, JCExpression.class);
    }

    @Override
    public SunClassDeclaration getClassDeclaration() {
        return _converter.jcToNode(_jc.def, SunClassDeclaration.class);
    }

    @Override
    public void setClassDeclaration(ClassDeclaration classDeclaration) {
        _jc.def = _converter.nodeToJc(classDeclaration, SunClassDeclaration.class, JCClassDecl.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getEnclosing(), getType(), getClassDeclaration()).append(getArguments(), getTypeParameters());
    }

    @Override
    public Position getPosition() {
        return _converter.jcPositionToPosition(_jc.pos);
    }

    @Override
    public void setPosition(Position position) {
        _jc.pos = _converter.positionToJcPosition(position);
    }

    @Override
    public JCNewClass getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
