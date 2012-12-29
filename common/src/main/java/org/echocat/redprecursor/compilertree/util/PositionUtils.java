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
