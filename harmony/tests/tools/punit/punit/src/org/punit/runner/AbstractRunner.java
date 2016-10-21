/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.runner;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.punit.convention.*;
import org.punit.events.EventListener;
import org.punit.exception.*;
import org.punit.exception.IOException;
import org.punit.message.*;
import org.punit.method.builder.*;
import org.punit.method.runner.*;
import org.punit.reporter.*;
import org.punit.reporter.stream.console.*;
import org.punit.suite.builder.*;
import org.punit.type.*;
import org.punit.util.*;

public abstract class AbstractRunner implements Runner {

    private TestSuiteBuilder _testSuiteBuiler;

    private TestMethodBuilder _testMethodBuilder;

    private MethodRunner _methodRunner;

    private final List<EventListener> _eventListeners = new ArrayList<EventListener>(); 

    private TestResult _testResult = new TestResult();

    private ConsoleLogger _consoleLogger = new ConsoleLogger();

    private RunnerProperties _properties = new RunnerProperties();

    private ExecutorPool _executorPool;

    private Convention _convention;

	private String _folder;

    public AbstractRunner(TestSuiteBuilder testSuiteBuiler,
            TestMethodBuilder testMethodBuilder, MethodRunner testMethodRunner) {
        _testSuiteBuiler = testSuiteBuiler;
        _testMethodBuilder = testMethodBuilder;
        _methodRunner = testMethodRunner;
        _methodRunner.setEventListeners(_eventListeners);
        _methodRunner.setRunnerProperties(_properties);
        registerDefaultListeners();
        setConvention(new NameConvention());
    }

    public void setConvention(Convention convention) {
        _convention = convention;
        _testSuiteBuiler.setConvention(convention);
        _testMethodBuilder.setConvention(convention);
        _methodRunner.setConvention(convention);
    }

    public int run(Class<?> clazz) {
        onRunnerStart(clazz);
        Object[] testClasses = _testSuiteBuiler.buildTestClasses(clazz);
        runTestClasses(testClasses);
        onRunnerEnd(clazz);
        return _testResult.failures().size();
    }

	public int run(Class<?> clazz, RunnerProperties properties) {
        setRunnerProperties(properties);
        return run(clazz);
    }

    private void runTestClasses(Object[] testClasses) {
        startExecutorPool();
        for (int i = 0; i < testClasses.length; ++i) {
            Object testClass = testClasses[i];
            if (isTestSuiteLabel(testClass)) {
                onSuite(((TestSuiteLabel) testClass));
            } else {
                runTestClass((Class<?>) testClass);
            }
        }
        waitExecutorPoolTermination();
    }

    public void setExecutorPool(ExecutorPool pool) {
        _executorPool = pool;
    }

    private void startExecutorPool() {
        if (_executorPool != null) {
            _executorPool.start();
        }
    }

    private void waitExecutorPoolTermination() {
        if (_executorPool != null) {
            _executorPool.join();
        }
    }

    private void setRunnerProperties(RunnerProperties properties) {
        _properties = properties;
        _methodRunner.setRunnerProperties(_properties);
    }

    public void runVMs(Class<?> clazz, VM[] vms) {
        _properties.isParent = true;
        _properties.vms = vms;
        for (int i = 0; i < vms.length; ++i) {
            VM vm = vms[i];
            storeRunnerConfig(vm);
            storeRunnerProperties(vm);
            String command = generateVMCommand(clazz, vm);
            Process p = IOUtil.exec(command);
            readErrorStream(p);
            readOutputStream(p);
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
            deleteRunnerConfig(vm);
            deleteRunnerProperties(vm);
        }
        filterNonParentEventListeners();
        run(clazz);
    }

    private void readOutputStream(Process p) {
        new StreamReaderThread(p.getInputStream(), false).start();
    }

    private void readErrorStream(Process p) {
        new StreamReaderThread(p.getErrorStream(), true).start();
    }

    private void filterNonParentEventListeners() {
        final List<EventListener> newEventListener = new ArrayList<EventListener>();
        Iterator<EventListener> iter = _eventListeners.iterator();
        while (iter.hasNext()) {
			EventListener listener = iter.next();
			if (listener.supportParentRunner()) {
				newEventListener.add(listener);
			}
		}
        _eventListeners.clear();
        _eventListeners.addAll(newEventListener);
    }

    private void deleteRunnerProperties(VM vm) {
        IOUtil.deleteFile(runnerPropertiesFileName(vm));
    }

    private void deleteRunnerConfig(VM vm) {
        IOUtil.deleteFile(runnerConfigFileName(vm));
    }

    private void storeRunnerProperties(VM vm) {
        RunnerProperties properties = new RunnerProperties();
        properties.vmName = vm.punitName();
        properties.isIntermediate = true;
        IOUtil.serialize(properties, runnerPropertiesFileName(vm));
    }

    private void storeRunnerConfig(VM vm) {
        IOUtil.serialize(this, runnerConfigFileName(vm));
    }

    private String runnerConfigFileName(VM vm) {
        return resultFolder() + File.separator + vm.punitName()
                + ".cfg"; //$NON-NLS-1$
    }

    private String runnerPropertiesFileName(VM vm) {
        return resultFolder() + File.separator + vm.punitName()
                + ".props"; //$NON-NLS-1$
    }

    public RunnerProperties properties() {
        return _properties;
    }

    private String generateVMCommand(Class<?> clazz, VM vm) {
        final StringBuffer sb = new StringBuffer();
        sb.append(vm.path());
        sb.append(" "); //$NON-NLS-1$
        sb.append(this.getClass().getName());
        sb.append(" "); //$NON-NLS-1$
        sb.append(clazz.getName());
        sb.append(" "); //$NON-NLS-1$
        sb.append(runnerConfigFileName(vm));
        sb.append(" "); //$NON-NLS-1$
        sb.append(runnerPropertiesFileName(vm));
        return sb.toString();
    }

    private void onRunnerStart(final Class<?> clazz) {
        Iterator <EventListener> iter = _eventListeners.iterator();
        while(iter.hasNext()) {
        	EventListener listener = iter.next();
			listener.onRunnerStart(clazz, AbstractRunner.this);
        }
   
    }

    private void onRunnerEnd(final Class<?> clazz) {
    	Iterator <EventListener> iter = _eventListeners.iterator();
    	while(iter.hasNext()) {
    		EventListener listener = iter.next();
			listener.onRunnerEnd(clazz, AbstractRunner.this);
    	}
    }

    private void runTestClass(final Class<?> clazz) {
        if (_executorPool == null) {
            runTestClassImpl(clazz);
        } else {
            _executorPool.execute(new RunTestClassTask(clazz));
        }
    }

    public void resultFolder(String folder) {
    	_folder = folder;
    }
    
    public String resultFolder() {
    	if(_folder == null) {
    		_folder = Messages.getString("reporter.01"); //$NON-NLS-1$
    	}
    	return _folder;
    }
    
    private class RunTestClassTask implements Runnable {
        private Class<?> _clazz;

        public RunTestClassTask(Class<?> clazz) {
            _clazz = clazz;
        }

        public void run() {
            AbstractRunner runner = (AbstractRunner) AbstractRunner.this
                    .clone();
            runner.runTestClassImpl(_clazz);
        }

    }

    private void runTestClassImpl(final Class<?> clazz) {
        final Object testInstance = ReflectionUtil.newInstance(clazz);
        Throwable exception = null;
        Collection<Method> testMethods = extractTestMethods(clazz);
        onClassStart(testInstance);
        try {
			beforeClass(clazz);
			Iterator<Method> iter = testMethods.iterator();
			while (iter.hasNext()) {
				Method method = iter.next();
				runTestMethod(testInstance, method);
			}
		} catch (Throwable t) {
            exception = t;
        } finally {
            try {
                afterClass(clazz);
            } catch (Throwable t) {
            	if(exception == null) {
            		exception = t;
            	}
            }
        }
        onClassEnd(testInstance, exception);
    }

    private void beforeClass(Class<?> clazz) {
        Method beforeClassMethod = _convention.getBeforeClassMethod(clazz);
        if (beforeClassMethod != null) {
            ReflectionUtil.invokeMethod(beforeClassMethod, null,
                    new Object[] {});
        }
    }

    private void afterClass(Class<?> clazz) {
        Method afterClassMethod = _convention.getAfterClassMethod(clazz);
        if (afterClassMethod != null) {
            ReflectionUtil
                    .invokeMethod(afterClassMethod, null, new Object[] {});
        }
    }

    private void onSuite(final TestSuiteLabel suiteLabel) {
		Iterator<EventListener> iter = _eventListeners.iterator();
		while (iter.hasNext()) {
			EventListener listener = iter.next();
			TestSuite suite = suiteLabel.suite();
			if (suiteLabel.isStart()) {
				listener.onSuiteStart(suite);
			} else {
				listener.onSuiteEnd(suite);
			}
		}
	}

    private void onClassStart(final Object testInstance) {
		Iterator<EventListener> iter = _eventListeners.iterator();
		while (iter.hasNext()) {
			EventListener listener = iter.next();
			listener.onClassStart(testInstance);
		}
	}

    private void onClassEnd(final Object testInstance, final Throwable t) {
		Iterator<EventListener> iter = _eventListeners.iterator();
		while (iter.hasNext()) {
			EventListener listener = iter.next();
			listener.onClassEnd(testInstance, t);
		}
	}

    private void runTestMethod(Object testInstance, Method method) {
        if (_convention.isParameterizedTest(testInstance.getClass())) {
            Parameterized pInstance = (Parameterized) testInstance;
            Parameter[] params = pInstance.parameters();
            for (int i = 0; i < params.length; ++i) {
                _methodRunner.run(testInstance, method,
                        new Object[] { params[i] });
            }
        } else {
            _methodRunner.run(testInstance, method, new Object[] {});
        }
    }

    private Collection<Method> extractTestMethods(Class<?> testClass) {
        return _testMethodBuilder.extractTestMethods(testClass);
    }

    public TestMethodBuilder methodBuilder() {
        return _testMethodBuilder;
    }

    public MethodRunner methodRunner() {
        return _methodRunner;
    }

    public TestSuiteBuilder suiteBuilder() {
        return _testSuiteBuiler;
    }

    private void registerDefaultListeners() {
        addEventListener(_testResult);
        addEventListener(_consoleLogger);
    }

    public List<EventListener> eventListeners() {
        return _eventListeners;
    }

    public void addEventListener(EventListener listener) {
        _eventListeners.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        _eventListeners.remove(listener);
    }

    public void removeConsoleLogger() {
        removeEventListener(_consoleLogger);
    }

    public ConsoleLogger consoleLogger() {
        return _consoleLogger;
    }

    private boolean isTestSuiteLabel(Object testClass) {
        return testClass instanceof TestSuiteLabel;
    }

    public Object clone() {
        try {
            AbstractRunner runner = (AbstractRunner) super.clone();
            runner._methodRunner = (MethodRunner) _methodRunner.clone();
            return runner;
        } catch (CloneNotSupportedException e) {
            throw new ReflectionException(e);
        }
    }

    public TestResult testResult() {
        return _testResult;
    }
}
