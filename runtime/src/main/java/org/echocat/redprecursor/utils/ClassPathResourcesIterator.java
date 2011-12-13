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

package org.echocat.redprecursor.utils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class ClassPathResourcesIterator implements Iterator<URL> {

    private final Enumeration<URL> _resourcesEnumeration;

    public ClassPathResourcesIterator(@Nonnull ClassLoader classLoader, @Nonnull String location) {
        requireNonNull("location", location);
        try {
            _resourcesEnumeration = requireNonNull("classLoader", classLoader).getResources(location);
        } catch (IOException e) {
            throw new RuntimeException("Could not load resources from '" + location + "'.", e);
        }
    }

    @Override
    public boolean hasNext() {
        return _resourcesEnumeration.hasMoreElements();
    }

    @Override
    public URL next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return _resourcesEnumeration.nextElement();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
