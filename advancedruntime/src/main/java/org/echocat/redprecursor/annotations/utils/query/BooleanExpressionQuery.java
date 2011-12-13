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
