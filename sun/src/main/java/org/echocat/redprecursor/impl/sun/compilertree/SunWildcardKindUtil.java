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
