package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.Modifiers;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.VariableDeclaration;
import org.echocat.redprecursor.compilertree.base.Expression;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunVariableDeclaration implements VariableDeclaration, SunDeclaration, SunStatement {

    private final JCVariableDecl _jc;
    private final SunNodeConverter _converter;

    public SunVariableDeclaration(JCVariableDecl jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunExpression getInitialValue() {
        return _converter.jcToNode(_jc.init, SunExpression.class);
    }

    @Override
    public void setInitialValue(Expression initialValue) {
        final SunExpression sunExpression = castNonNullParameterTo("initialValue", initialValue, SunExpression.class);
        _jc.init = sunExpression.getJc();
    }

    @Override
    public SunModifiers getModifiers() {
        return _converter.jcToNode(_jc.mods, SunModifiers.class);
    }

    @Override
    public void setModifiers(Modifiers modifiers) {
        final SunModifiers sunModifiers = castNonNullParameterTo("modifiers", modifiers, SunModifiers.class);
        _jc.mods = sunModifiers.getJc();
    }

    @Override
    public String getName() {
        return _converter.nameToString(_jc.name);
    }

    @Override
    public void setName(String name) {
        _jc.name = _converter.stringToName(requireNonNull("name", name));
    }

    @Override
    public SunIdentifier getType() {
        return _converter.jcToNode(_jc.vartype, SunIdentifier.class);
    }

    @Override
    public void setType(Identifier type) {
        final SunIdentifier sunType = castNonNullParameterTo("type", type, SunIdentifier.class);
        _jc.vartype = sunType.getJc();
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getInitialValue(), getModifiers(), getType());
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
    public JCVariableDecl getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
