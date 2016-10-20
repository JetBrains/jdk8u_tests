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

package org.apache.harmony.test.stress.misc.stressloads;

import java.lang.reflect.Field;
import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.stress.classloader.share.CommonClassLoader;

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.6 $
 */

public class StressLoadsThread7 extends Thread {

    public void run() {
        this.loadStress();
    }

    public void loadStress() {
        int hi;
        Field testField;
        Class classToLoad;

        try {
            while (true) {
                /* loading classes */
                classToLoad = new CommonClassLoader().loadClass("org/apache/"
                        + "harmony/test/stress/misc/stressloads/"
                        + "StressLoadsThread7Class.class",
                        "org.apache.harmony.test.stress.misc"
                                + ".stressloads.StressLoadsThread7Class");

                /* static initiating classes */
                testField = classToLoad.getField("hi");
                hi = testField.getInt(testField);

            }
        } catch (OutOfMemoryError er) {
            ReliabilityRunner.debug("Warning: out of memory");
        } catch (Throwable thr) {
            ReliabilityRunner.debug("StressLoadsThread7 test error");
            ReliabilityRunner.mainTest.addError(
                    StressLoadsRunner.loadsRunnerTest, thr);
        }
    }
}

/* Class Loader */
class StressLoadsThread7CL extends ClassLoader {
    StressLoadsThread7CL() {
        super();
    }

    public Class loadClass(String name, String name2) {
        InputStream is = getResourceAsStream(name);
        byte[] data = new byte[0];
        byte[] piece = new byte[512];
        int len;
        try {
            while ((len = is.read(piece)) != -1) {
                byte[] tmp = data;
                data = new byte[tmp.length + len];
                System.arraycopy(tmp, 0, data, 0, tmp.length);
                System.arraycopy(piece, 0, data, tmp.length, len);
                tmp = null;
            }
        } catch (IOException ex) {
            throw new Error(ex);
        }
        try {
            is.close();
        } catch (IOException ex) {
            throw new Error(ex);
        }
        return defineClass(name2, data, 0, data.length);
    }
}
