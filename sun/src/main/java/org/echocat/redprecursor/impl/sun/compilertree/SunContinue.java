package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCContinue;
import org.echocat.redprecursor.compilertree.Continue;
import org.echocat.redprecursor.compilertree.Position;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunContinue implements Continue, SunStatement {

    private final JCContinue _jc;
    private final SunNodeConverter _converter;

    public SunContinue(JCContinue jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public String getLabel() {
        return _converter.nameToString(_jc.label);
    }

    @Override
    public void setLabel(String label) {
        _jc.label = _converter.stringToName(label);
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
    public JCContinue getJc() {
        return _jc;
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}