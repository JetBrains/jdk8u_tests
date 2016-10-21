/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.convention;

import java.lang.reflect.Method;

import org.punit.util.ReflectionUtil;

public class NameConvention extends AbstractConvention {

    private static final long serialVersionUID = -1252188754463864599L;

    public Method getCheckMethod(Method method) {
        String checkMethodName = "check_" + method.getName(); //$NON-NLS-1$
        return ReflectionUtil.getMethod(method.getDeclaringClass(),
                checkMethodName, method.getParameterTypes());
    }

    /**
     * Judges whether this method is a test method.
     * 
     * @return returns true if the test modifier, name, and parameter conform to
     *         the convention of a test method.
     */
    public boolean isTestMethod(Method method) {
        return isModifierValid(method) && isNameValid(method)
                && isParamValid(method);
    }

    /**
     * Judges whether the modifier of is method conforms to the convention of a
     * test method.
     * 
     * @param method
     *            to be judged
     * @return returns true if this method is public and non static.
     */
    protected boolean isModifierValid(Method method) {
        return ReflectionUtil.isPublic(method)
                && !ReflectionUtil.isStatic(method);
    }

    /**
     * Judges whether the name of is method conforms to the convention of a test
     * method.
     * 
     * @param method
     *            to be judged
     * @return returns true if the name of this method starts with "test", or
     *         the name is in the list of test interfaces which are added by
     *         <code>TestMethodBuilder.addTestInterfaces</code>
     */
    protected boolean isNameValid(Method method) {
        String name = method.getName();
        return name.startsWith("test"); //$NON-NLS-1$
    }

    /**
     * Judges whether the parameters of this method conform to the convention of
     * a test method.
     * 
     * @param method
     * @return <code>true</code> if the number of method parameters is zero
     *         and the test class is not marked as <code>Parameterized</code>,
     *         or the parameter length is 1, and the parameter is assignable
     *         from <code>Parameter</code>.
     */
    protected boolean isParamValid(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        Class<?>[] params = method.getParameterTypes();
        if (isParameterizedTest(clazz)) {
            return params.length == 1 && isParameter(params[0]);
        } else {
            return params.length == 0;
        }
    }

    public Method getAfterClassMethod(Class<?> test) {
        Method method = ReflectionUtil.getMethodAndSetAccessible(test,
                "afterClass", new Class[] {}); //$NON-NLS-1$
        if (method != null && ReflectionUtil.isStatic(method)) {
            return method;
        }
        return null;
    }

    public Method getBeforeClassMethod(Class<?> test) {
        Method method = ReflectionUtil.getMethodAndSetAccessible(test,
                "beforeClass", new Class[] {}); //$NON-NLS-1$
        if (method != null && ReflectionUtil.isStatic(method)) {
            return method;
        }
        return null;
    }

    public Method getSetUpMethod(Class<?> test) {
        return ReflectionUtil.getMethodAndSetAccessible(test,
                "setUp", new Class[] {}); //$NON-NLS-1$
    }

    public Method getTearDownMethod(Class<?> test) {
        return ReflectionUtil.getMethodAndSetAccessible(test,
                "tearDown", new Class[] {}); //$NON-NLS-1$
    }
}
