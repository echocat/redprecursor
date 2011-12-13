/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is echocat redprecursor.
 *
 * The Initial Developer of the Original Code is Gregor Noczinski.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
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
