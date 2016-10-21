/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.convention;

import java.lang.reflect.Method;

import org.punit.type.Concurrent;
import org.punit.type.Loop;
import org.punit.type.Parameter;
import org.punit.type.Parameterized;
import org.punit.type.Test;

abstract public class AbstractConvention implements Convention {

    public boolean isExcluded(Class <?> method) {
        return false;
    }

    public Class<? extends Throwable> getExpectedException(Method method) {
        return null;
    }

    public int getConcurrentCount(Object testInstance, Method method) {
        if (testInstance instanceof Concurrent) {
            return ((Concurrent) testInstance).concurrentCount();
        }
        return 0;
    }

    public boolean isPUnitTest(Class<?> clazz) {
        return Test.class.isAssignableFrom(clazz);
    }

    public boolean isParameter(Class<?> clazz) {
        return Parameter.class.isAssignableFrom(clazz);
    }

    public boolean isParameterizedTest(Class<?> clazz) {
        return Parameterized.class.isAssignableFrom(clazz);
    }

    public boolean isLoopTest(Class<?> clazz) {
        return Loop.class.isAssignableFrom(clazz);
    }

    public long toWork(Object testInstance) {
        if (isLoopTest(testInstance.getClass())) {
            return ((Loop) testInstance).toWork();
        }
        return 0;
    }
}
