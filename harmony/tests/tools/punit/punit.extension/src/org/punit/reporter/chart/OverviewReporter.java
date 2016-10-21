/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart;

import org.punit.message.*;
import org.punit.watcher.*;

public class OverviewReporter extends AbstractChartReporter {

	private static final long serialVersionUID = -1673081832150876946L;

	public OverviewReporter(ChartRender render) {
		super(render);
	}
	
	protected final DatasetKey getKey(Watcher watcher) {
		return new DatasetKeyImpl(watcher);
	}

	public static class DatasetKeyImpl implements DatasetKey {
		private Watcher _watcher;

		public DatasetKeyImpl(Watcher watcher) {
			_watcher = watcher;
		}

		public int hashCode() {
			return _watcher.hashCode();
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof DatasetKeyImpl)) {
				return false;
			}
			DatasetKeyImpl key = (DatasetKeyImpl) obj;
			return _watcher.equals(key._watcher);
		}

		public Watcher watcher() {
			return _watcher;
		}

		public String punitName() {
			return Messages.getString("reporter.02"); //$NON-NLS-1$
		}
	}
}
