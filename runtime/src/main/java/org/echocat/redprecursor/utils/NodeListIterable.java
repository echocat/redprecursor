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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class NodeListIterable<N extends Node> implements Iterable<N> {

    @Nonnull
    public static NodeListIterable<Element> childElementsOf(@Nonnull Element element, @Nonnull String childName) {
        final NodeList nodeList = requireNonNull("element", element).getElementsByTagName(requireNonNull("childName", childName));
        return new NodeListIterable<Element>(nodeList, Element.class);
    }

    @Nullable
    public static Element childElementOf(@Nonnull Element element, @Nonnull String childName) {
        final NodeListIterator<Element> i = childElementsOf(element, childName).iterator();
        return i.hasNext() ? i.next() : null;
    }

    private final NodeList _nodeList;
    private final Class<N> _expectedType;

    public NodeListIterable(@Nonnull NodeList nodeList, @Nonnull Class<N> expectedType) {
        _nodeList = requireNonNull("nodeList", nodeList);
        _expectedType = requireNonNull("expectedType", expectedType);
    }

    @Override
    public NodeListIterator<N> iterator() {
        return new NodeListIterator<N>(_nodeList, _expectedType);
    }
}
