package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree;
import org.echocat.redprecursor.impl.sun.compilertree.SunNode.Implementations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class SunNodeInformationFactory {

    public Map<Class<? extends SunNode>, SunNodeInformation<?, ?>> getTypeToInformation() {
        final Map<Class<? extends SunNode>, SunNodeInformation<?, ?>> result = new HashMap<Class<? extends SunNode>, SunNodeInformation<?, ?>>();
        final Package ourPackage = SunNodeConverter.class.getPackage();
        final Implementations implementations = ourPackage.getAnnotation(Implementations.class);
        for (final Class<? extends SunNode> nodeType : implementations.value()) {
            result.put(nodeType, getInformation(ourPackage, nodeType));
        }
        return unmodifiableMap(result);
    }

    private SunNodeInformation<?, ?> getInformation(Package aPackage, Class<? extends SunNode> nodeType) {
        if (nodeType.isInterface()) {
            throw new IllegalArgumentException("The nodeType " + nodeType.getName() + " which was annotated at package " + aPackage + " with @" + Implementations.class.getName() + " is an interface.");
        }
        final Class<?> jcType = getJcType(aPackage, nodeType);
        return createInformation(nodeType, jcType);
    }

    private SunNodeInformation<?, ?> createInformation(Class<? extends SunNode> nodeType, Class<?> jcType) {
    // noinspection unchecked
        final Constructor<? extends SunNode>[] constructors = (Constructor<? extends SunNode>[]) nodeType.getConstructors();
        SunNodeInformation<?, ?> information = null;
        for (final Constructor<? extends SunNode> constructor : constructors) {
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            if ((parameterTypes.length == 1 || parameterTypes.length == 2) && parameterTypes[0].equals(jcType)) {
                if (parameterTypes.length == 1) {
                    // noinspection unchecked
                    information = createInformation((Class<SunNode>) nodeType, constructor, (Class<JCTree>) jcType, false);
                } else if (parameterTypes[1].isAssignableFrom(SunNodeConverter.class)){
                    // noinspection unchecked
                    information = createInformation((Class<SunNode>) nodeType, constructor, (Class<JCTree>) jcType, true);
                }
            }
        }
        if (information == null) {
            throw new IllegalArgumentException("Could not find a possible constructor at " + nodeType.getName() + ".");
        }
        return information;
    }

    private Class<?> getJcType(Package aPackage, Class<? extends SunNode> nodeType) {
        final Method getJcMethod;
        try {
            getJcMethod = nodeType.getDeclaredMethod("getJc");
        } catch (NoSuchMethodException ignored) {
            throw new IllegalArgumentException("The nodeType " + nodeType.getName() + " which was annotated at package " + aPackage + " with @" + Implementations.class.getName() + " does not have a getJc() method.");
        }
        final Class<?> jcType = getJcMethod.getReturnType();
        if (Modifier.isAbstract(jcType.getModifiers())) {
            throw new IllegalArgumentException("The nodeType " + nodeType.getName() + " which was annotated at package " + aPackage + " with @" + Implementations.class.getName() + " does return at getJc() an abstract type.");
        }
        return jcType;
    }

    private SunNodeInformation<?, ?> createInformation(final Class<SunNode> nodeType, final Constructor<? extends SunNode> constructor, final Class<JCTree> jcType, final boolean withConverter) {
        final SunNodeInformation<?, ?> information;
        information = new SunNodeInformation<SunNode, JCTree>(nodeType, jcType) { @Override public SunNode newInstance(JCTree jc, SunNodeConverter converter) {
            try {
                return withConverter ? constructor.newInstance(jc, converter) : constructor.newInstance(jc);
            } catch (InvocationTargetException e) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                throw new RuntimeException("Could not create an instance of " + nodeType.getName() + ".", e.getTargetException());
            } catch (Exception e) {
                throw new RuntimeException("Could not create an instance of " + nodeType.getName() + ".", e);
            }
        }};
        return information;
    }
}
