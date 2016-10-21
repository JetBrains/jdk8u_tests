/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.suite.builder;

import org.punit.type.*;


/**
 * Default implementation for TestSuiteLabel.
 * @see TestSuiteLabel
 */
public class TestSuiteLabelImpl implements TestSuiteLabel {
    private TestSuite _suite;
    private boolean _isStart;

    public TestSuiteLabelImpl(TestSuite suite, boolean isStart) {
        _suite = suite;
        _isStart = isStart;
    }

    public TestSuite suite() {
        return _suite;
    }
    
    public boolean isStart() {
        return _isStart;
    }
}
