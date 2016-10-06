/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.reg.vm.btest4492;

import org.apache.harmony.test.share.reg.RegressionTest;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

public class Btest4492 extends RegressionTest {
    public static void main(String[] args) {
        System.exit(new Btest4492().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        byte b[] = null;
        try {
            File f = new File(args[0]);
            b = new byte[(int) f.length()];
            FileInputStream stream = new FileInputStream(f);
            stream.read(b);
            stream.close();
        } catch (Exception e) {
            logger.severe("Unexpected exception: " + e);
            return fail();
        }

        String[] name = {null, "org.apache.harmony.test.func.reg.vm.btest4492.tmp"};

        for (int t = 0; t < name.length; t++) {
            KlassLoader kl = new KlassLoader();
            for (int i = 0; i < 3; i++) {
                try {
                    Class cl = kl.defineKlass(name[t], b);
                    if(i == 0) {
                        logger.info("Test passes step: " + i + " for " + name[t]);
                    } else {
                        logger.severe("Test failed at the step: " + i);
                        return fail();
                    }
                } catch (LinkageError e) {
                    if (i == 0) {
                        logger.severe("Step: " + i + " unexpected error: " + e);
                        return fail();
                    } else {
                        logger.info("Test passes step: " + i + " for " + name[t]);
                    }
                } catch (Throwable e) {
                    logger.severe("Test fails:" + e);
                    return fail();
                }
            }
        }
        return pass();
    }
}

class KlassLoader extends ClassLoader {
    public Class defineKlass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}

class tmp {}
