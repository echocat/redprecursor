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
