/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
package org.apache.harmony.test.stress.jpda.jdwp.share.debuggee;

import org.apache.harmony.share.framework.jpda.TestErrorException;
import org.apache.harmony.share.framework.jpda.TestOptions;
import org.apache.harmony.test.stress.jpda.jdwp.share.JDWPQALogWriter;


import org.apache.harmony.share.DRLLogging;
import org.apache.harmony.share.LogFactory;

/**
 * Base debuggee class for JPDA QA tests.
 *
 * @author Ivan G. Popov
 * @version $Revision: 1.2 $
 */
public abstract class QARawDebuggee {

	public JDWPQALogWriter logWriter;
	public TestOptions settings;

    /**
     * Executes main actions of debuggee. This method must be implemented
     * by subclasses.
     *
     */
    public abstract void run();

    /**
     * Inintializes debuggee
     */
    public void onStart() {
        settings = new TestOptions();
        DRLLogging log = new LogFactory().getLogger();
        logWriter = new JDWPQALogWriter(log, "");
    }

    /**
     * Executes final stage of debuggee.
     *
     */
    public void onFinish() {
    }

    /**
     * Starts debuggee specified by <code>debuggeeClass</code>.
     * 
     * @param debuggeeClass debuggee's class
     */
	public static void runDebuggee(Class debuggeeClass) {
		QARawDebuggee debuggee = null;
		try {
			debuggee = (QARawDebuggee)debuggeeClass.newInstance();
		} catch (Exception e) {
			throw new TestErrorException("Debuggee can not be started: "
					+ debuggeeClass.getName(), e);
		}

		try {
			debuggee.onStart();
			debuggee.run();
		} catch (Throwable e) {
			debuggee.logWriter.printError(e);
		} finally {
			debuggee.onFinish();
		}
	}
}
