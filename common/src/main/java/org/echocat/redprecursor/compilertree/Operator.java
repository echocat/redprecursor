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

public enum Operator {
    ERRONEOUS("erroneus"),

    POSITIVE("+"),
    NEGATIVE("-"),
    NOT("!"),
    BIT_NOT("~"),
    PRE_INCREMENT("++x"),
    PRE_DECREMENT("--x"),
    POST_INCREMENT("x++"),
    POST_DECREMENT("x--"),

    OR("||"),
    AND("&&"),
    BIT_OR("|"),
    BIT_XOR("^"),
    BIT_AND("&"),
    EQUALS("=="),
    NOT_EQUALS("!="),
    LESSER_THAN("<"),
    GREATER_THAN(">"),
    LESSER_THAN_OR_EQUALS("<="),
    GREATER_THAN_OR_EQUALS(">="),
    S_LEFT("<<"),
    S_RIGHT(">>"),
    US_RIGHT(">>>"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    MODULO("%"),

    BIT_OR_ASSIGNMENT("|="),
    BIT_XOR_ASSIGNMENT("^="),
    BIT_AND_ASSIGNMENT("&="),
    S_LEFT_ASSIGNMENT("<<="),
    S_RIGHT_ASSIGNMENT(">>="),
    US_RIGHT_ASSIGNMENT(">>>="),
    PLUS_ASSIGNMENT("+="),
    MINUS_ASSIGNMENT("-="),
    MULTIPLICATION_ASSIGNMENT("*="),
    DIVISION_ASSIGNMENT("/="),
    MODULO_ASSIGNMENT("/="),

    LET_EXPRESSION("let");

    private final String _stringRepresentation;

    Operator(String stringRepresentation) {
        _stringRepresentation = stringRepresentation;
    }

    @Override
    public String toString() {
        return _stringRepresentation;
    }
}
