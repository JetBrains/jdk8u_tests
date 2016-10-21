/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.convention;

import java.lang.reflect.Method;

import org.junit.Test;

public class JUnitAnnotationConvention extends AnnotationConvention {

    private static final long serialVersionUID = 5775534409608907458L;

    public JUnitAnnotationConvention() {
        super(org.junit.After.class, org.junit.AfterClass.class,
                org.junit.Before.class, org.junit.BeforeClass.class,
                org.junit.Ignore.class, Test.class);
    }

    public Class<? extends Throwable> getExpectedException(Method method) {
        Test testAnnotation = (Test) method.getAnnotation(_test);
        if (testAnnotation == null) {
            return null;
        }
        Class<? extends Throwable> expected = testAnnotation.expected();
        if (expected == Test.None.class) {
            return null;
        }
        return expected;
    }

    public Method getCheckMethod(Method method) {
        return _delegate.getCheckMethod(method);
    }

    public int getConcurrentCount(Object testInstance, Method method) {
        return _delegate.getConcurrentCount(testInstance, method);
    }

}
