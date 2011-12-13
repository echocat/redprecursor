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

import com.sun.tools.javac.util.Context;

public class DelegatingContext extends Context {

    private final Context _delegate;

    public DelegatingContext(Context delegate) {
        _delegate = delegate;
    }

    @Override public <T> void put(Key<T> key, Factory<T> fac) {_delegate.put(key, fac);}
    @Override public <T> void put(Key<T> key, T data) {_delegate.put(key, data);}
    @Override public <T> T get(Key<T> key) {return _delegate.get(key);}
    @Override public <T> T get(Class<T> clazz) {return _delegate.get(clazz);}
    @Override public <T> void put(Class<T> clazz, T data) {_delegate.put(clazz, data);}
    @Override public <T> void put(Class<T> clazz, Factory<T> fac) {_delegate.put(clazz, fac);}
    @Override public void dump() {_delegate.dump();}
    @Override public void clear() {_delegate.clear();}
}
