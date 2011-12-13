package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import org.echocat.redprecursor.compilertree.ArrayType;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.Position;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunArrayType implements ArrayType, SunIdentifier {

    private final JCArrayTypeTree _jc;
    private final SunNodeConverter _converter;

    public SunArrayType(JCArrayTypeTree jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public SunIdentifier getType() {
        return _converter.jcToNode(_jc.elemtype, SunIdentifier.class);
    }

    @Override
    public void setType(Identifier type) {
        _jc.elemtype = _converter.nodeToJc(requireNonNull("type", type), SunIdentifier.class, JCExpression.class);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable(getType());
    }

    @Override
    public JCArrayTypeTree getJc() {
        return _jc;
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
    public String getStringRepresentation() {
        return _jc.toString();
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
