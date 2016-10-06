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
package org.apache.harmony.test.func.reg.vm.btest2533;

import java.lang.reflect.Constructor;
import java.io.*;

public class Btest2533_test extends ClassLoader {
    public static void main(String[] args) {
        try {
            Btest2533_test testClassLoader = new Btest2533_test();
            Class c = testClassLoader.findClass("org.apache.harmony.test.func.reg.vm.btest2533.Btest2533_aux1");
            System.out.println("Class = " + c);
            Constructor cc = c.getConstructor(new Class[0]);
            System.out.println("Constructor = " + cc);
            Object o = cc.newInstance(new Object[0]);
            System.out.println("Instance = " + o);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public Class findClass(String name) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String resName = name.replace('.', '/') + ".class";
            System.out.println("resName = " + resName);
            InputStream is = cl.getResourceAsStream(resName);
            System.out.println("is = " + is);
            byte[] bytes = new byte[600];
            int classLength = is.read(bytes);
            System.out.println("read bytes = " + classLength);
            return defineClass(null, bytes, 0, classLength);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    private byte[] loadClassData(String name) {
        // load the class data from the connection
        //. . .
        return null;
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        if(name.startsWith("java.lang"))
            return Thread.currentThread().getContextClassLoader().loadClass(name);
        return null;
    }

}
