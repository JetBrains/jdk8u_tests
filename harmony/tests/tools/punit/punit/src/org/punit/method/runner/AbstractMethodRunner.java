/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.method.runner;

import java.lang.reflect.*;
import java.util.*;

import org.punit.assertion.*;
import org.punit.convention.*;
import org.punit.events.EventListener;
import org.punit.exception.*;
import org.punit.runner.*;
import org.punit.type.*;
import org.punit.type.Parameter;
import org.punit.util.*;
import org.punit.watcher.*;

public abstract class AbstractMethodRunner implements MethodRunner {

    protected ArrayList<Watcher> _watchers = new ArrayList<Watcher>();

    private TimeWatcher _timeWatcher = new TimeWatcher();

    private List<EventListener> _eventListeners;

    private RunnerProperties _runnerProperties;

    protected Convention _convention;

    private transient ToStopThread _toStopThread;

    private transient boolean _shouldStop;

    protected transient Object _testInstance;

    protected transient Method _method;

    protected transient Object[] _params;

    protected transient Class<?> _class;

    protected transient Method _setUpMethod;

    protected transient Method _tearDownMethod;

    private transient Method _checkMethod;

    protected transient Class<? extends Throwable> _expectedException;

    public AbstractMethodRunner() {
        _watchers.add(_timeWatcher);
        _shouldStop = true;
    }

    public void setEventListeners(List<EventListener> eventListeners) {
        _eventListeners = eventListeners;
    }

    public void setRunnerProperties(RunnerProperties props) {
        _runnerProperties = props;
    }

    public void addWatcher(Watcher watcher) {
        _watchers.add(watcher);
    }

    public void removeWatcher(Watcher watcher) {
        _watchers.remove(watcher);
    }

    public void removeTimeWatcher() {
        _watchers.remove(_timeWatcher);
    }

    public void run(Object testInstance, Method method, Object[] params) {
        onMethodStart(method, testInstance, params);
        Throwable throwable = null;
        try {
            if (needsRunMethod()) {
                init(testInstance, method, params);
                setUpBeforeWatchers(params);
                startWatchers(testInstance, method, params);
                setUpAfterWatchers(params);
                startToStopThread();
                runImpl();
                stopToStopThread();
                runCheckMethod(testInstance, params);
            } else {
                startWatchers(testInstance, method, params);
            }
        } catch (Throwable t) {
            throwable = t;
        } finally {
            try {
                if (needsRunMethod()) {
                    tearDownBeforeWatchers(params);
                }
            } catch (Throwable t) {
                throwable = t;
            } finally {
                try {
                    stopWatchers(testInstance, method, params);
                    if (needsRunMethod()) {
                        tearDownAfterWatchers(params);
                    }
                } catch (Throwable t) {
                    throwable = t;
                }
            }
        }
        onMethodEnd(method, testInstance, params, throwable);
    }

    private void startToStopThread() {
        if (_toStopThread != null) {
            _toStopThread.start();
        }
    }

    private void stopToStopThread() {
        if (_toStopThread != null) {
            _toStopThread.close();
            _toStopThread = null;
        }
    }

    private void runCheckMethod(Object testInstance, Object[] params) {
        if (_checkMethod != null) {
            ReflectionUtil.invokeMethod(_checkMethod, testInstance, params);
        }
    }

    private void onMethodStart(final Method method, final Object testInstance,
            final Object[] params) {
    	Iterator<EventListener> iter = _eventListeners.iterator();
    	while(iter.hasNext()) {
    		EventListener listener = iter.next();
    		listener.onMethodStart(method, testInstance, params);
    	}
    }

    private void onMethodEnd(final Method method, final Object testInstance,
            final Object[] params, final Throwable t) {
    	Iterator<EventListener> iter = _eventListeners.iterator();
    	while (iter.hasNext()) {
			EventListener listener = iter.next();
			listener.onMethodEnd(method, testInstance, params, t, _watchers);
		}
    }

    protected abstract void runImpl() throws Throwable;

    protected final void init(Object testInstance, Method method,
            Object[] params) {
        _testInstance = testInstance;
        _class = testInstance.getClass();
        _params = params;
        _method = method;
        _setUpMethod = _convention.getSetUpMethod(_class);
        _tearDownMethod = _convention.getTearDownMethod(_class);
        _checkMethod = _convention.getCheckMethod(method);
        _expectedException = _convention.getExpectedException(method);
        long toWork = _convention.toWork(_testInstance);
        if (toWork > 0) {
            _toStopThread = new ToStopThread(new Runnable() {
                public void run() {
                    stop();
                }
            }, toWork);
            _shouldStop = false;
        } 
    }

    public boolean shouldStop() {
        return _shouldStop;
    }

    protected synchronized void stop() {
        _shouldStop = true;
    }

    /**
     * This method might be overridden by subclass. The runner may do some more
     * things during this step.
     * 
     */
    protected void setUpBeforeWatchers(Object[] params) throws Throwable {
        if (_convention.isPUnitTest(_class)) {
            ((Test) _testInstance).setUpBeforeWatchers();
        } else if (_convention.isParameterizedTest(_class)) {
            ((Parameterized) _testInstance)
                    .setUpBeforeWatchers((Parameter) params[0]);
        }
    }

    private void setUpAfterWatchers(Object[] params) throws Throwable {
        if (_convention.isPUnitTest(_class)) {
            ((Test) _testInstance).setUpAfterWatchers();
        } else if (_convention.isParameterizedTest(_class)) {
            ((Parameterized) _testInstance)
                    .setUpAfterWatchers((Parameter) params[0]);
        } else {
            setUp();
        }
    }

    private void tearDownBeforeWatchers(Object[] params) throws Throwable {
        if (_convention.isPUnitTest(_class)) {
            ((Test) _testInstance).tearDownBeforeWatchers();
        } else if (_convention.isParameterizedTest(_class)) {
            ((Parameterized) _testInstance)
                    .tearDownBeforeWatchers((Parameter) params[0]);
        } else {
            tearDown();
        }
    }

    private void tearDownAfterWatchers(Object[] params) throws Throwable {
        if (_convention.isPUnitTest(_class)) {
            ((Test) _testInstance).tearDownAfterWatchers();
        } else if (_convention.isParameterizedTest(_class)) {
            ((Parameterized) _testInstance)
                    .tearDownAfterWatchers((Parameter) params[0]);
        }
    }

    private void setUp() throws Throwable {
        if (_setUpMethod != null) {
            try {
                ReflectionUtil.invokeMethod(_setUpMethod, _testInstance,
                        new Object[] {});
            } catch (ReflectionException e) {
                throw e.getCause();
            }
        }
    }

    private void tearDown() throws Throwable {
        if (_tearDownMethod != null) {
            try {
                ReflectionUtil.invokeMethod(_tearDownMethod, _testInstance,
                        new Object[] {});
            } catch (ReflectionException e) {
                throw e.getCause();
            }
        }
    }

    protected final void startWatchers(final Object testInstance,
            final Method method, final Object[] params) {
        onWatchersStart(testInstance, method, params);
        for(int i = _watchers.size() - 1; i >= 0; i--) {
        	Watcher watcher = (Watcher) _watchers.get(i);
            onWatcherStart(watcher, testInstance, method, params);
            watcher.start();
        }
    }

    private void onWatchersStart(final Object testInstance,
            final Method method, final Object[] params) {
    	Iterator<EventListener> iter = _eventListeners.iterator();
    	while(iter.hasNext()) {
    		EventListener listener = iter.next();
    		listener.onWatchersStart(_watchers);
    	}
    }

    private void onWatcherStart(final Watcher watcher,
            final Object testInstance, final Method method,
            final Object[] params) {
    	Iterator<EventListener> iter = _eventListeners.iterator();
		while (iter.hasNext()) {
			EventListener listener = iter.next();
			listener.onWatcherStart(watcher);
		}
	}

    protected final void stopWatchers(final Object testInstance,
			final Method method, final Object[] params) {
		Iterator<Watcher> iter = _watchers.iterator();
		while (iter.hasNext()) {
			Watcher watcher = iter.next();
			watcher.stop();
			onWatcherEnd(watcher, testInstance, method, params);
		}
		onWatchersEnd(testInstance, method, params);
	}

    private void onWatcherEnd(final Watcher watcher, final Object testInstance,
			final Method method, final Object[] params) {
		Iterator<EventListener> iter = _eventListeners.iterator();
		while (iter.hasNext()) {
			EventListener listener = iter.next();
			listener.onWatcherEnd(watcher);
		}
	}

    private void onWatchersEnd(final Object testInstance, final Method method,
			final Object[] params) {
		Iterator<EventListener> iter = _eventListeners.iterator();
		while (iter.hasNext()) {
			EventListener listener = iter.next();
			listener.onWatchersEnd(_watchers);
		}
	}

    public List<Watcher> watchers() {
        return _watchers;
    }

    public boolean needsRunMethod() {
        return !_runnerProperties.isParent;
    }

    @SuppressWarnings("unchecked")
	public Object clone() {
        try {
			final AbstractMethodRunner methodRunner = (AbstractMethodRunner) super
					.clone();
			methodRunner._watchers = (ArrayList<Watcher>) _watchers.clone();
			methodRunner._watchers.clear();
			Iterator<Watcher> iter = _watchers.iterator();
			while (iter.hasNext()) {
				Watcher watcher = iter.next();
				methodRunner.addWatcher(watcher.cloneSelf());
			}
			return methodRunner;
		} catch (CloneNotSupportedException e) {
			throw new ReflectionException(e);
		}
    }

    public void setConvention(Convention convention) {
        _convention = convention;
    }

    protected void runMethod() throws Throwable {
        if (_expectedException == null) {
            do {
                invokeMethod();
            } while (!shouldStop());
        } else {
            Assert.assertException(_expectedException, new CodeRunner() {
                public void run() throws Throwable {
                    invokeMethod();
                }
            });
        }
    }

    private void invokeMethod() throws Throwable {
        try {
            ReflectionUtil.invokeMethod(_method, _testInstance, _params);
        } catch (ReflectionException e) {
            throw e.getCause();
        }
    }
}
