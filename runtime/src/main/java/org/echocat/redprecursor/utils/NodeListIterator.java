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

package org.echocat.redprecursor.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class NodeListIterator<N extends Node> implements Iterator<N> {

    private final NodeList _nodeList;
    private final Class<N> _expectedType;

    private int _index;

    public NodeListIterator(@Nonnull NodeList nodeList, @Nonnull Class<N> expectedType) {
        _nodeList = requireNonNull("nodeList", nodeList);
        _expectedType = requireNonNull("expectedType", expectedType);
    }

    @Override
    public boolean hasNext() {
        return _index < _nodeList.getLength();
    }

    @Override
    public N next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final Node node = _nodeList.item(_index++);
        return _expectedType.cast(node);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
