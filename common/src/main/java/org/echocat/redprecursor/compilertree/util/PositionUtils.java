package org.echocat.redprecursor.compilertree.util;

import org.echocat.redprecursor.compilertree.Position;
import org.echocat.redprecursor.compilertree.base.Node;

import javax.annotation.Nonnull;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class PositionUtils {

    public static void setPositionRecursive(@Nonnull Node node, @Nonnull Position position) {
        requireNonNull("node", node);
        requireNonNull("position", position);
        node.setPosition(position);
        for (Node subNode : node.getAllEnclosedNodes()) {
            setPositionRecursive(subNode, position);
        }
    }

    private PositionUtils() {}

}
