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

import com.sun.tools.javac.code.TypeTags;
import org.echocat.redprecursor.compilertree.Primitive;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunPrimitiveUtil {

    private static final Map<Primitive, Integer> PRIMITIVE_TO_CODE;
    private static final Map<Integer, Primitive> CODE_TO_PRIMITIVE;

    static {
        final Map<Primitive, Integer> primitiveToSunCode = new HashMap<Primitive, Integer>();
        final Map<Integer, Primitive> sunCodeToPrimitive = new HashMap<Integer, Primitive>();
        registerPrimitiveWithSunCode(Primitive.BYTE, TypeTags.BYTE, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.SHORT, TypeTags.SHORT, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.INT, TypeTags.INT, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.LONG, TypeTags.LONG, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.FLOAT, TypeTags.FLOAT, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.DOUBLE, TypeTags.DOUBLE, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.BOOLEAN, TypeTags.BOOLEAN, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.CHAR, TypeTags.CHAR, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.STRING, TypeTags.CLASS, primitiveToSunCode, sunCodeToPrimitive);
        registerPrimitiveWithSunCode(Primitive.NULL, TypeTags.BOT, primitiveToSunCode, sunCodeToPrimitive);
        PRIMITIVE_TO_CODE = unmodifiableMap(primitiveToSunCode);
        CODE_TO_PRIMITIVE = unmodifiableMap(sunCodeToPrimitive);
    }

    private static void registerPrimitiveWithSunCode(Primitive primitive, int sunCode, Map<Primitive, Integer> primitiveToSunCode, Map<Integer, Primitive> sunCodeToPrimitive) {
        primitiveToSunCode.put(primitive, sunCode);
        sunCodeToPrimitive.put(sunCode, primitive);
    }

    public static int primitiveToCode(Primitive primitive) {
        requireNonNull("primitive", primitive);
        final Integer code = PRIMITIVE_TO_CODE.get(primitive);
        if (code == null) {
            throw new IllegalStateException("This system does not know " + primitive + ".");
        }
        return code;
    }

    public static Primitive codeToPrimitive(int code) {
        final Primitive primitive = CODE_TO_PRIMITIVE.get(code);
        if (primitive == null) {
            throw new IllegalArgumentException("This system does not know the code: " + code);
        }
        return primitive;
    }


    private SunPrimitiveUtil() {}
}
