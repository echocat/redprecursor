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
