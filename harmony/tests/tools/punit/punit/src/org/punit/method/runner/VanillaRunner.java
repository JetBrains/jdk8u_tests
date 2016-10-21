/* (C) Copyright 2007, by Andrew Zhang */
package org.punit.method.runner;

import java.util.*;

import org.punit.events.EventListener;
import org.punit.method.builder.*;
import org.punit.reporter.*;
import org.punit.runner.*;
import org.punit.suite.builder.*;

public class VanillaRunner implements Runner {
	
	private static final long serialVersionUID = -3536655499041726649L;
	
	private Runner _runner;
	
	public VanillaRunner(Runner runner) {
		_runner = runner;
	}

	public TestMethodBuilder methodBuilder() {
		return _runner.methodBuilder();
	}

	public MethodRunner methodRunner() {
		return _runner.methodRunner();
	}

	public RunnerProperties properties() {
		return _runner.properties();
	}

	public void resultFolder(String folder) {
		_runner.resultFolder(folder);
	}

	public String resultFolder() {
		return _runner.resultFolder();
	}

	public int run(Class<?> clazz) {
		return _runner.run(clazz);
	}

	public int run(Class<?> clazz, RunnerProperties properties) {
		return _runner.run(clazz, properties);
	}

	public TestSuiteBuilder suiteBuilder() {
		return _runner.suiteBuilder();
	}

	public TestResult testResult() {
		return _runner.testResult();
	}

	public void addEventListener(EventListener listener) {
		_runner.addEventListener(listener);
	}

	public List<EventListener> eventListeners() {
		return _runner.eventListeners();
	}

	public void removeEventListener(EventListener listener) {
		_runner.removeEventListener(listener);
	}

	public String punitName() {
		return _runner.punitName();		
	}
}
