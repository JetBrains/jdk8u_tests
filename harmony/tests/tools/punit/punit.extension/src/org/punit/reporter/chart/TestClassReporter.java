/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart;

import org.punit.watcher.*;

public class TestClassReporter extends AbstractChartReporter {

    private static final long serialVersionUID = 6447796730865433640L;

    public TestClassReporter(ChartRender render) {
        super(render);
    }
    
    protected DatasetKey getKey(Watcher watcher) {
        return new DatasetKeyImpl(watcher, _testInstance.getClass());
    }

    public static class DatasetKeyImpl implements DatasetKey {
        private Watcher _watcher;

        private Class<?> _clazz;

        public DatasetKeyImpl(Watcher watcher, Class<?> clazz) {
            _watcher = watcher;
            _clazz = clazz;
        }

        public int hashCode() {
            return _watcher.hashCode() + _clazz.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof DatasetKeyImpl)) {
                return false;
            }
            DatasetKeyImpl key = (DatasetKeyImpl) obj;
            return _watcher.equals(key._watcher) && _clazz.equals(key._clazz);
        }

        public Watcher watcher() {
            return _watcher;
        }
        
        public String punitName() {
            return _clazz.getName();
        }
    }
}
