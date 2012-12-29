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

import com.sun.tools.javac.code.BoundKind;
import org.echocat.redprecursor.compilertree.WildcardKind;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunWildcardKindUtil {

    private static final Map<WildcardKind, BoundKind> OUR_TO_SUN;
    private static final Map<BoundKind, WildcardKind> SUN_TO_OUR;

    static {
        final Map<WildcardKind, BoundKind> ourToSun = new HashMap<WildcardKind, BoundKind>();
        final Map<BoundKind, WildcardKind> sunToOur = new HashMap<BoundKind, WildcardKind>();
        registerOurWithSunOne(WildcardKind.EXTENDS, BoundKind.EXTENDS, ourToSun, sunToOur);
        registerOurWithSunOne(WildcardKind.SUPER, BoundKind.SUPER, ourToSun, sunToOur);
        registerOurWithSunOne(WildcardKind.UNBOUND, BoundKind.UNBOUND, ourToSun, sunToOur);
        OUR_TO_SUN = unmodifiableMap(ourToSun);
        SUN_TO_OUR = unmodifiableMap(sunToOur);
    }

    private static void registerOurWithSunOne(WildcardKind our, BoundKind sun, Map<WildcardKind, BoundKind> ourToSun, Map<BoundKind, WildcardKind> sunToOur) {
        ourToSun.put(our, sun);
        sunToOur.put(sun, our);
    }

    public static BoundKind ourToSun(WildcardKind our) {
        requireNonNull("our", our);
        final BoundKind sun = OUR_TO_SUN.get(our);
        if (sun == null) {
            throw new IllegalStateException("This system does not know " + our + ".");
        }
        return sun;
    }

    public static WildcardKind sunToOur(BoundKind sun) {
        final WildcardKind primitive = SUN_TO_OUR.get(sun);
        if (primitive == null) {
            throw new IllegalArgumentException("This system does not know: " + sun);
        }
        return primitive;
    }

    private SunWildcardKindUtil() {}


}
