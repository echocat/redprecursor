package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import org.echocat.redprecursor.compilertree.Identifier;
import org.echocat.redprecursor.compilertree.NewArray;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Expression;

import java.util.List;

import static org.echocat.redprecursor.utils.ContractUtil.*;

public class SunNewArray implements NewArray, SunExpression {

    private final JCNewArray _jc;
    private final SunNodeConverter _converter;

    public SunNewArray(JCNewArray jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunExpression> getElements() {
        return _converter.jcsToNodes(_jc.elems, SunExpression.class);
    }

    @Override
    public void setElements(List<Expression> elements) {
        _jc.elems = _converter.nodesToJcs(elements, SunExpression.class, JCExpression.class);
    }

    @Override
    public List<? extends SunExpression> getDimensions() {
        return _converter.jcsToNodes(_jc.dims, SunExpression.class);
    }

    @Override
    public void setDimensions(List<Expression> dimensions) {
        _jc.dims = _converter.nodesToJcs(requireNonEmpty("dimensions", dimensions), SunExpression.class, JCExpression.class);
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
        return IterableBuilder.<SunNode>toIterable(getType()).append(getDimensions(), getElements());
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
    public JCNewArray getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
