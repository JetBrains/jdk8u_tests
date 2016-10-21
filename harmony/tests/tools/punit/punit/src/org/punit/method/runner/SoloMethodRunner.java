/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.method.runner;


public class SoloMethodRunner extends AbstractMethodRunner {

	private static final long serialVersionUID = 3278612571978181393L;

	protected void runImpl() throws Throwable {
		runMethod();
	}

}
