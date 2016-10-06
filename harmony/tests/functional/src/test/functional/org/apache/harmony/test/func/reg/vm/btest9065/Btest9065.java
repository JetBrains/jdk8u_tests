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
package org.apache.harmony.test.func.reg.vm.btest9065;

import java.io.File;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * Unable to replace system class loader
 * 
 */
public class Btest9065 extends RegressionTest {

    public int test(Logger log, String[] args) {
        ClassLoader scl = ClassLoader.getSystemClassLoader();
        System.out.println(scl);
        log.info(scl.toString());
        if (CustomSysLoader.class == scl.getClass()) {
            return PASSED;
        } else {
            return FAILED;
        }
    }

    public static void main(String[] args) {
        System.exit(new Btest9065().test(Logger.global, args));
    }

    public static class CustomSysLoader extends ClassLoader {

    public CustomSysLoader(ClassLoader parent) {
        super(parent);
    }
}
}

