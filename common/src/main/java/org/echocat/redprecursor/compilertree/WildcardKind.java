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

package org.echocat.redprecursor.compilertree;

public enum WildcardKind {
    EXTENDS("? extends "),
    SUPER("? super "),
    UNBOUND("?");

    private final String _name;

    private WildcardKind(String name) { _name = name; }

    public String toString() {
        return _name;
    }
}
