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
