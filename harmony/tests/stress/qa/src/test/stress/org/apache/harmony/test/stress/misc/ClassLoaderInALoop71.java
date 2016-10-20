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

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;
import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.CommonClassLoader;

public class ClassLoaderInALoop71 extends TestCase {

    public void test() {
        long iterations;

        iterations = Integer.getInteger(
                "org.apache.harmony.test." + "stress.misc."
                        + "ClassLoaderInALoop71.iterations", 100000).intValue();

        ReliabilityRunner.debug("Number of iterations is: " + iterations);

        // creating class loaders in a loop
        for (long i = 0; i < iterations; i++) {
            try {
                new CommonClassLoader()
                        .loadClass(
                                "org/apache/harmony/test/stress/misc/ClassLoaderInALoop71Obj.class",
                                "org.apache.harmony.test.stress.misc.ClassLoaderInALoop71Obj");
            } catch (OutOfMemoryError er) {
                ReliabilityRunner
                        .debug("Java heap overflow while creating class loaders. Fail.");
                ReliabilityRunner.mainTest.addError(this, er);
            } catch (Throwable thr) {
                ReliabilityRunner.debug(thr.toString());
                ReliabilityRunner.mainTest.addError(this, thr);
            }
        }
    }
}
