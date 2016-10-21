/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtil {

    public static <T extends Annotation> Method getMethodByAnnotation(
            Class<?> clazz, Class<T> annotationClass) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            Method method = methods[i];
            Annotation annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
}
