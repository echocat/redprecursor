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

package org.echocat.redprecursor.annotations.utils.query;

import java.util.Collection;
import java.util.Map;

public class BooleanExpressionQuery implements ExpressionQuery {

    private final boolean _positive;

    public BooleanExpressionQuery() {
        _positive = true;
    }
    
    public BooleanExpressionQuery(boolean positive) {
        _positive = positive;
    }
    
    public BooleanExpressionQuery not() {
        return new BooleanExpressionQuery(!_positive);
    }
    
    public BooleanExpressionQuery no() {
        return not();
    }
    
    public boolean empty(Iterable<?> iterable) {
        return evaluate(iterable == null || !iterable.iterator().hasNext());
    }

    public boolean empty(String string) {
        return evaluate(string == null || string.isEmpty());
    }
    
    public boolean empty(Object[] array) {
        return evaluate(array == null || array.length == 0);
    }
    public boolean empty(Map<?, ?> map) {
        return evaluate(map == null || map.isEmpty());
    }

    public boolean content(Iterable<?> iterable) {
        return evaluate(iterable != null || iterable.iterator().hasNext());
    }

    public boolean content(String string) {
        return evaluate(string != null && !string.isEmpty());
    }

    public boolean content(Object[] array) {
        return evaluate(array != null && array.length > 0);
    }
    public boolean content(Map<?, ?> map) {
        return evaluate(map != null && !map.isEmpty());
    }

    public boolean items(Iterable<?> iterable) {
        return content(iterable);
    }

    public boolean items(String string) {
        return content(string);
    }

    public boolean items(Object[] array) {
        return content(array);
    }
    public boolean items(Map<?, ?> map) {
        return content(map);
    }

    public boolean blank(String string) {
        return evaluate(string == null || string.trim().isEmpty());
    }
    
    private boolean evaluate(boolean value) {
        return _positive ? value : !value;
    }
}
