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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class IterableBuilder<T> implements Iterable<T> {

    private final Collection<T> _nodes = new ArrayList<T>();

    public static <T> IterableBuilder<T> toIterable() {
        return new IterableBuilder<T>();
    }

    public static <T> IterableBuilder<T> toIterable(T... nodes) {
        return new IterableBuilder<T>().append(nodes);
    }

    public static <T> IterableBuilder<T> toIterable(Iterable<? extends T>... nodeIterables) {
        //noinspection unchecked
        return new IterableBuilder().append(nodeIterables);
    }

    public IterableBuilder<T> append(T... nodes) {
        if (nodes != null) {
            for (T node : nodes) {
                if (node != null) {
                    _nodes.add(node);
                }
            }
        }
        return this;
    }

    public IterableBuilder<T> append(Iterable<? extends T>... nodeIterables) {
        if (nodeIterables != null) {
            for (Iterable<? extends T> nodes : nodeIterables) {
                if (nodes != null) {
                    for (T node : nodes) {
                        if (node != null) {
                            _nodes.add(node);
                        }
                    }
                }
            }
        }
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<T> i = _nodes.iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public T next() {
                return i.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This iterator is read only.");
            }
        };
    }
}
