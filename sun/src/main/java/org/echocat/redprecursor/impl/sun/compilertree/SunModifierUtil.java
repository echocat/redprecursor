package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.code.Flags;
import org.echocat.redprecursor.compilertree.Modifier;

import java.util.*;
import java.util.Map.Entry;

import static java.util.Collections.unmodifiableMap;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunModifierUtil {

    private static final Map<Modifier, Long> MODIFIER_TO_CODE;
    private static final Map<Long, Modifier> CODE_TO_MODIFIER;

    static {
        final Map<Modifier, Long> modifierToCode = new EnumMap<Modifier, Long>(Modifier.class);
        final Map<Long, Modifier> codeToModifier = new HashMap<Long, Modifier>();

        registerOperatorWithCode(Modifier.PUBLIC, Flags.PUBLIC, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.PRIVATE, Flags.PRIVATE, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.PROTECTED, Flags.PROTECTED, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.STATIC, Flags.STATIC, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.FINAL, Flags.FINAL, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.SYNCHRONIZED, Flags.SYNCHRONIZED, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.VOLATILE, Flags.VOLATILE, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.VOLATILE, Flags.VOLATILE, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.TRANSIENT, Flags.TRANSIENT, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.NATIVE, Flags.NATIVE, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.INTERFACE, Flags.INTERFACE, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.ABSTRACT, Flags.ABSTRACT, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.STRICTFP, Flags.STRICTFP, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.ENUM, Flags.ENUM, modifierToCode, codeToModifier);
        registerOperatorWithCode(Modifier.VARARGS, Flags.VARARGS, modifierToCode, codeToModifier);

        MODIFIER_TO_CODE = unmodifiableMap(modifierToCode);
        CODE_TO_MODIFIER = unmodifiableMap(codeToModifier);
    }

    private static void registerOperatorWithCode(Modifier modifier, long sunCode, Map<Modifier, Long> modifierToCode, Map<Long, Modifier> codeToModifier) {
        modifierToCode.put(modifier, sunCode);
        codeToModifier.put(sunCode, modifier);
    }


    public static long modifierToCode(Modifier modifier) {
        requireNonNull("modifier", modifier);
        final Long code = MODIFIER_TO_CODE.get(modifier);
        if (code == null) {
            throw new IllegalStateException("This system does not know " + modifier + ".");
        }
        return code;
    }

    public static Modifier codeToModifier(long code) {
        final Modifier modifier = CODE_TO_MODIFIER.get(code);
        if (modifier == null) {
            throw new IllegalArgumentException("This system does not know the code: " + code);
        }
        return modifier;
    }

    public static long getValue(List<Modifier> modifiers) {
        long result = 0;
        if (modifiers != null) {
            for (Modifier modifier : modifiers) {
                result |= modifierToCode(modifier);
            }
        }
        return result;
    }

    public static List<Modifier> getModifiers(long value) {
        final List<Modifier> result = new ArrayList<Modifier>();
        for (Entry<Long, Modifier> codeAndModifier : CODE_TO_MODIFIER.entrySet()) {
            if ((value & codeAndModifier.getKey()) != 0) {
                result.add(codeAndModifier.getValue());
            }
        }
        return result;
    }

    private SunModifierUtil() {}
}
