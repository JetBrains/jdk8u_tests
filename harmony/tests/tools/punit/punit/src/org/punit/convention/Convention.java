/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.convention;

import java.io.Serializable;
import java.lang.reflect.Method;

public interface Convention extends Serializable {
    
    /**
     * Judges whether this test class is excluded.
     * 
     * @return returns true if the test class is excluded.
     */
    public boolean isExcluded(Class <?> clazz);
    
    /**
     * Judges whether this method is a test method.
     * 
     * @return returns true if it is a test method.
     */
    public boolean isTestMethod(Method method);

    /**
     * Gets the corresponding check method to this test method
     * 
     * @param method
     *            the test method
     * @return
     */
    public Method getCheckMethod(Method method);
    
    public Class<? extends Throwable> getExpectedException(Method method);

    public int getConcurrentCount(Object testInstance, Method method);
    
    public Method getSetUpMethod(Class<?> test);
    
    public Method getTearDownMethod(Class<?> test);
    
    public Method getBeforeClassMethod(Class<?> test);
    
    public Method getAfterClassMethod(Class<?> test);

    public boolean isPUnitTest(Class<?> clazz);
    
    public boolean isParameter(Class<?> clazz);

    public boolean isParameterizedTest(Class<?> clazz);

    public boolean isLoopTest(Class<?> clazz);
    
    public long toWork(Object testInstance);
}
