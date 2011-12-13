package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCTry;
import org.echocat.redprecursor.compilertree.Catch;
import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.Try;
import org.echocat.redprecursor.compilertree.base.Statement;

import java.util.List;

import static org.echocat.redprecursor.impl.sun.compilertree.IterableBuilder.toIterable;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunTry implements Try, SunStatement {

    private final JCTry _jc;
    private final SunNodeConverter _converter;

    public SunTry(JCTry jc, SunNodeConverter converter) {
        _jc = requireNonNull("jc", jc);
        _converter = requireNonNull("converter", converter);
    }

    @Override
    public List<? extends SunCatch> getCatches() {
        return _converter.jcsToNodes(_jc.catchers, SunCatch.class);
    }

    @Override
    public void setCatches(List<Catch> catches) {
        _jc.catchers = _converter.nodesToJcs(catches, SunCatch.class, JCCatch.class);
    }

    @Override
    public List<? extends SunStatement> getFinalizer() {
        return _converter.jcBlockToStatements(_jc.finalizer);
    }

    @Override
    public void setFinalizer(List<Statement> finalizer) {
        _jc.finalizer = _converter.statementsToJcBlock(finalizer, JCBlock.class);
    }

    @Override
    public List<? extends SunStatement> getBody() {
        return _converter.jcBlockToStatements(_jc.body);
    }

    @Override
    public void setBody(List<Statement> body) {
        _jc.body = _converter.statementsToJcBlock(body, JCBlock.class);
    }

    @Override
    public JCTry getJc() {
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
    public Iterable<SunNode> getAllEnclosedNodes() {
        return toIterable(getBody(), getCatches(), getFinalizer());
    }

    @Override
    public String toString() {
        return _jc.toString();
    }
}
