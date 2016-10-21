/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart;

import org.punit.type.*;
import org.punit.watcher.*;

public class TestSuiteReporter extends AbstractChartReporter {

    private static final long serialVersionUID = -2748710567265810151L;

    public TestSuiteReporter(ChartRender render) {
        super(render);
    }
    
    protected DatasetKey getKey(Watcher watcher) {
        return new DatasetKeyImpl(watcher, currentTestSuite());
    }

    public static class DatasetKeyImpl implements DatasetKey {
        private Watcher _watcher;
        
        private TestSuite _testSuite;

        public DatasetKeyImpl(Watcher watcher, TestSuite suite) {
            _watcher = watcher;
            _testSuite = suite;
        }

        public int hashCode() {
            return _watcher.hashCode() + _testSuite.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof DatasetKeyImpl)) {
                return false;
            }
            DatasetKeyImpl key = (DatasetKeyImpl) obj;
            return _watcher.equals(key._watcher)  && _testSuite.equals(key._testSuite);
        }

        public Watcher watcher() {
            return _watcher;
        }
        
        public String punitName() {
            return _testSuite.getClass().getName();
        }
    }
}
