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

public enum Modifier {
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    STATIC("static"),
    FINAL("final"),
    SYNCHRONIZED("synchronized"),
    VOLATILE("volatile"),
    TRANSIENT("transient"),
    NATIVE("native"),
    INTERFACE("interface"),
    ABSTRACT("abstract"),
    STRICTFP("strictfp"),
    ENUM("enum"),
    VARARGS("varargs");

    private final String _stringRepresentation;

    Modifier(String stringRepresentation) {
        _stringRepresentation = stringRepresentation;
    }

    @Override
    public String toString() {
        return _stringRepresentation;
    }
}
