/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.type;

/**
 * Marks this test as a PUnit test suite.
 * 
 */
public interface TestSuite {
    
    /**
     * @return the test classes or test suites included in this test suite
     */
    public Class<?>[] testSuite();
}
