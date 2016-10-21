/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.suite.builder;

import org.punit.type.TestSuite;

/**
 * Represents a test suite label. TestSuiteBuilder inserts this label in the
 * result to indicate the start and the end of a test suite.
 * 
 * @see TestSuiteBuilder#buildTestClasses(Class)
 */
public interface TestSuiteLabel {

    /**
     * @return the test suite it represents
     */
    public TestSuite suite();

    /**
     * @return <code>true</code> if it represents the start of a test suite.
     */
    public boolean isStart();
}
