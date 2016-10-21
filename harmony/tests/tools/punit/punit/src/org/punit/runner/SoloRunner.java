/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.runner;

import org.punit.message.*;
import org.punit.method.builder.*;
import org.punit.method.runner.*;
import org.punit.suite.builder.*;
import org.punit.util.*;

public class SoloRunner extends AbstractRunner {

	private static final long serialVersionUID = 5938649527831492676L;

	public static void main(String[] args) {
		RunnerUtil.run(new SoloRunner(), args);
	}

	public SoloRunner() {
		super(new TestSuiteBuilderImpl(), new MethodBuilderImpl(),
				new SoloMethodRunner());
	}

	public String punitName() {
		return Messages.getString("runner.01"); //$NON-NLS-1$
	}

}
