package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCSkip;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Skip;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunSkip implements Skip, SunStatement {

    private final JCSkip _jc;
    private final SunNodeConverter _converter;

    public SunSkip(JCSkip jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public Iterable<? extends SunNode> getAllEnclosedNodes() {
        return toIterable();
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
    public JCSkip getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
