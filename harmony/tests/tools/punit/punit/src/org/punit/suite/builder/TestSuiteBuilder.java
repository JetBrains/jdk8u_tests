/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.suite.builder;

import java.io.Serializable;

import org.punit.convention.Convention;

/**
 * Interface for test suite builder.
 * @see TestSuiteLabel
 */
public interface TestSuiteBuilder extends Serializable {
    /**
     * Builds the test suite. Returns a list of test classes. TestSuite will be
     * presented as TestSuiteLabel object.
     * 
     * @param clazz
     * @return
     */
    public Object[] buildTestClasses(Class<?> testSutie);

    public void setConvention(Convention convention);
}
