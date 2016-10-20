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

package org.apache.harmony.test.share.stress.generator;

import java.util.StringTokenizer;

import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.share.stress.generator.Generator;
import org.apache.harmony.test.share.stress.util.CriticalSectionController;

/**
 * New thread for tests execution inside it.
 * 
 * @author Alexander D. Shipilov
 * @version $Revision: 1.9 $
 */
public class Thread extends Generator {
    /**
     * Creates new <code>ThreadToRun</code> and continue tests execution
     * inside it.
     * 
     */
    protected void setContext() {
        ReliabilityRunner.debug("Thread.setContext " + " { " + params + "}");
        int threadsCount;

        StringTokenizer st = new StringTokenizer(params);
        String str = st.nextToken();
        try {
            threadsCount = (new Integer(str)).intValue();
            params = params.replaceFirst(str, "");
        } catch (NumberFormatException exc) {
            threadsCount = 1;
        }

        ReliabilityRunner.debug("Starting " + threadsCount + " threads");
        for (int i = 0; i < threadsCount; i++) {
            new ThreadToRun().start();
        }
    }

    /**
     * Thread to be created.
     * 
     * @author Alexander D. Shipilov
     * @version $Revision: 1.9 $
     */
    private class ThreadToRun extends java.lang.Thread {
        public void run() {
            ReliabilityRunner.debug("ThreadToRun.run()");
            try {
                execute();
            } catch (OutOfMemoryError err) {
                while (CriticalSectionController.isCriticalSection()) {
                }
            }
        }
    }
}
