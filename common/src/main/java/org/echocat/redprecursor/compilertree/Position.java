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

import javax.annotation.Nonnegative;

public class Position {

    private final long _line;
    private final long _column;

    public Position(@Nonnegative long line, @Nonnegative long column) {
        if (line < 0) {
            throw new IllegalArgumentException("The provided line is negative.");
        }
        if (column < 0) {
            throw new IllegalArgumentException("The provided column is negative.");
        }
        _line = line;
        _column = column;
    }

    public long getLine() {
        return _line;
    }

    public long getColumn() {
        return _column;
    }

    @Override
    public String toString() {
        return _line + ":" + _column;
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || getClass() != o.getClass()) {
            result = false;
        } else {
            final Position other = (Position) o;
            result = _line == other._line && _column == other._column;
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = (int) (_line ^ (_line >>> 32));
        result = 31 * result + (int) (_column ^ (_column >>> 32));
        return result;
    }
}
