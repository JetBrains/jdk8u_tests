package org.punit.events;

import java.lang.reflect.*;
import java.util.*;

import org.punit.runner.*;
import org.punit.type.*;
import org.punit.watcher.*;

public class VanillaEventListener implements EventListener {

	private static final long serialVersionUID = -2413131933921801719L;

	public VanillaEventListener() {
	}

	public void onClassEnd(Object testInstance, Throwable t) {
	}

	public void onClassStart(Object testInstance) {
	}

	public void onMethodEnd(Method method, Object testInstance,
			Object[] params, Throwable t, List<Watcher> Watchers) {
	}

	public void onMethodStart(Method method, Object testInstance,
			Object[] params) {
	}

	public void onRunnerEnd(Class<?> clazz, Runner runner) {
	}

	public void onRunnerStart(Class<?> clazz, Runner runner) {
	}

	public void onSuiteEnd(TestSuite suite) {
	}

	public void onSuiteStart(TestSuite suite) {

	}

	public void onWatcherEnd(Watcher watcher) {

	}

	public void onWatcherStart(Watcher watcher) {

	}

	public void onWatchersEnd(List<Watcher> watchers) {

	}

	public void onWatchersStart(List<Watcher> watchers) {

	}

	public boolean supportParentRunner() {
		return false;
	}

}
