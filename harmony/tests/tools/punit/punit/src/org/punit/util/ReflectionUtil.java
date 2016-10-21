/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.punit.exception.ReflectionException;

public class ReflectionUtil {

    public static Object newInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz
                    .getDeclaredConstructor(new Class[] {});
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[] {});
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static Class<?> newClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName,
            Class<?>[] params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, params);
            return method;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets a field with a given value after converting the value to the field
     * type.
     * 
     * @param field
     *            the field to assign
     * @param value
     *            the string value to assign
     */
    public static void setField(Object testInstance, Field field, String value) {
        try {
            field.setAccessible(true);
            Class<?> fieldClass = field.getType();
            if (fieldClass.equals(int.class)) {
                int intVal = Integer.parseInt(value);
                field.setInt(testInstance, intVal);
            } else if (fieldClass.equals(long.class)) {
                long longVal = Long.parseLong(value);

                field.setLong(testInstance, longVal);
            } else {
                field.set(testInstance, value);
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(nfe);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    /**
     * Gets the method and sets the accessible to true
     * 
     * @param clazz
     * @param methodName
     * @param params
     * @return
     */
    public static Method getMethodAndSetAccessible(Class<?> clazz,
            String methodName, Class<?>[] params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, params);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object invokeMethod(Method method, Object instance,
            Object[] params) throws ReflectionException {
        try {
            return method.invoke(instance, params);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException(e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        } catch (InvocationTargetException e) {
            throw new ReflectionException(e.getTargetException());
        }
    }

    public static boolean isPublic(Method method) {
        int modifier = method.getModifiers();
        return isBitSet(modifier, Modifier.PUBLIC);
    }

    public static boolean isStatic(Method method) {
        int modifier = method.getModifiers();
        return isBitSet(modifier, Modifier.STATIC);
    }

    private static boolean isBitSet(int value, int expectedBit) {
        return (value & expectedBit) != 0;
    }
}
