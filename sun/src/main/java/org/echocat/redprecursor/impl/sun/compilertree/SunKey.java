package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import org.echocat.redprecursor.compilertree.Key;
import org.echocat.redprecursor.compilertree.Position;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunKey implements Key, SunIdentifier {

    private final JCIdent _jc;
    private final SunNodeConverter _converter;

    public SunKey(JCIdent jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
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
    public Iterable<SunNode> getAllEnclosedNodes() {
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
    public JCIdent getJc() {
        return _jc;
    }

    @Override
    public String getStringRepresentation() {
        final Symbol symbol = _jc.sym != null ? _jc.sym : _converter.nameToType(_jc.name);
        return symbol != null ? symbol.toString() : _converter.nameToString(_jc.name);
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
