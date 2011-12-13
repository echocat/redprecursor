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

package org.echocat.redprecursor.impl.sun.compilertree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings({ "InstanceVariableNamingConvention", "ParameterHidesMemberVariable", "AccessingNonPublicFieldOfAnotherObject" })
public abstract class TreeDiscovery<T> {

    @Nullable
    public List<T> discover(@Nullable T topNode, @Nullable T nodeToSearch) {
        final Deque<StackElement> stack = new ArrayDeque<StackElement>();
        stack.offer(new StackElement(topNode));
        boolean found = false;
        while (!stack.isEmpty() && !found) {
            final StackElement element = stack.getLast();
            if (isSearchedNode(element.node, nodeToSearch)) {
                found = true;
            } else {
                if (element.childIterator.hasNext()) {
                    final T next = element.childIterator.next();
                    stack.addLast(new StackElement(next));
                } else {
                    stack.removeLast();
                }
            }
        }
        return found ? stackNodesToList(stack) : null;
    }

    @Nonnull
    private List<T> stackNodesToList(@Nonnull Collection<StackElement> queue) {
        final List<T> result = new ArrayList<T>();
        for (StackElement element : queue) {
            result.add(element.node);
        }
        return result;
    }

    @Nonnull
    protected abstract Iterator<T> getIteratorFor(@Nullable T node);

    protected boolean isSearchedNode(@Nullable T currentNode, @Nullable T nodeToSearch) {
        return currentNode != null ? currentNode.equals(nodeToSearch) : nodeToSearch == null;
    }

    private class StackElement {

        private final T node;
        private final Iterator<T> childIterator;

        private StackElement(T node) {
            this.node = node;
            childIterator = getIteratorFor(node);
        }
    }
}
