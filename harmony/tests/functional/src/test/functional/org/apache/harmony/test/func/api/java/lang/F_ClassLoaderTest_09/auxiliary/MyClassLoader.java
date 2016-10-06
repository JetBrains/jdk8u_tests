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
package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_09.auxiliary;

import java.io.*;

public class MyClassLoader extends ClassLoader {
    public int    i      = 0;
    public String prefix = null;

    public MyClassLoader(String prefixString) {
        super();
        prefix = prefixString;
    }

    public Class findClass(String name) {
        String fs = System.getProperty("file.separator");
        try {
            FileInputStream fis;
            if (i == 0) {
                System.err.println("Loading FirstClass...");
                fis = new FileInputStream(prefix + fs + "auxiliary" + fs + "FirstClass" + fs + name.replace('.', '/') + ".class");
                byte[] classToBytes = new byte[fis.available()];
                fis.read(classToBytes);
                return defineClass(
                    "org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_09.auxiliary.FirstClass."
                        + name, classToBytes, 0, classToBytes.length);
            } else {
                System.err.println("Loading SecondClass...");
                fis = new FileInputStream(prefix + fs + "auxiliary" + fs + "SecondClass" + fs + name.replace('.', '/') + ".class");
                byte[] classToBytes = new byte[fis.available()];
                fis.read(classToBytes);
                return defineClass(
                    "org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_09.auxiliary.SecondClass."
                        + name, classToBytes, 0, classToBytes.length);
            }
        } catch (Exception e) {
            System.err.println("Unexpected exception during classloading: ");
            e.printStackTrace();
            return null;
        }
    }
}
