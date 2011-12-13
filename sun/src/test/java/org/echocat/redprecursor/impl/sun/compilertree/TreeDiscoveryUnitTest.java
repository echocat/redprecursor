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

import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static org.echocat.redprecursor.impl.sun.compilertree.TreeDiscoveryUnitTest.Node.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class TreeDiscoveryUnitTest {

    @Test
    public void testDiscover() throws Exception {
        final TreeDiscovery<Node> discovery = new TreeDiscovery<Node>() { @Nonnull @Override protected Iterator<Node> getIteratorFor(@Nullable Node node) {
            return node.toIterator();
        }};
        final Node root = node("root").append(
            node("1").append(
                node("1.1"),
                node("1.2").append(
                    node("1.2.1"),
                    node("1.2.2"),
                    node("1.2.3")
                ),
                node("1.3")
            ),
            node("2").append(
                node("2.1").append(
                    node("2.1.1"),
                    node("2.1.2"),
                    node("2.1.3"),
                    node("2.1.4")
                ),
                node("2.2").append(
                    node("2.2.1"),
                    node("2.2.2"),
                    node("2.2.3"),
                    node("2.2.4")
                ),
                node("2.3").append(
                    node("2.3.1"),
                    node("2.3.2"),
                    node("2.3.3"),
                    node("2.3.4")
                )
            ),
            node("3"),
            node("4")
        );

        assertThat(discovery.discover(root, node("2.1.3")), is(nodes("root", "2", "2.1", "2.1.3")));
        assertThat(discovery.discover(root, node("1.2.3")), is(nodes("root", "1", "1.2", "1.2.3")));
        assertThat(discovery.discover(root, node("1.2")), is(nodes("root", "1", "1.2")));
        assertThat(discovery.discover(root, node("1.3")), is(nodes("root", "1", "1.3")));
        assertThat(discovery.discover(root, node("1.3.1")), nullValue());
    }

    static class Node {

        static Node node(String name) {
            return new Node(name);
        }

        static List<Node> nodes(String... names) {
            final List<Node> nodes = new ArrayList<Node>();
            for (String name : names) {
                nodes.add(node(name));
            }
            return nodes;
        }

        private final Collection<Node> _children = new ArrayList<Node>();
        private final String _name;

        private Node(String name) {
            _name = name;
        }

        private Node append(Node... nodes) {
            _children.addAll(asList(nodes));
            return this;
        }

        private Iterator<Node> toIterator() {
            return _children.iterator();
        }

        @Override
        public boolean equals(Object o) {
            final boolean result;
            if (this == o) {
                result = true;
            } else if (o == null || getClass() != o.getClass()) {
                result = false;
            } else {
                final Node node = (Node) o;
                result = _name != null ? _name.equals(node._name) : node._name == null;
            }
            return result;
        }

        @Override
        public int hashCode() {
            return _name != null ? _name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return _name;
        }
    }

}
