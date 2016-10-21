/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.method.builder;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Collection;

import org.punit.convention.Convention;

/**
 * Interface for test method builder. The method builder builds a list of
 * methods, which will be executed by the method runner.
 */
public interface TestMethodBuilder extends Serializable {
    /**
     * Builds a collection of test methods from the test class. Returns a list
     * of methods.
     * 
     * @param clazz
     *            the test class to be extract methods from
     * @return a <code>Collection</code> of
     *         <code>java.lang.reflect.Method</code>.
     */
    public Collection<Method> extractTestMethods(Class<?> testClass); // Collection<Method>

    public void setConvention(Convention convention);
}
