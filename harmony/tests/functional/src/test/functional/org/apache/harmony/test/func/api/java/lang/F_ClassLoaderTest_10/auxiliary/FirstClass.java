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
package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_10.auxiliary;

public class FirstClass {

    public boolean start() {
        /*
         * Now class MyClassLoader will be loaded using system classloader by
         * its full name, MyClassLoader will be initiating class loader for
         * <package>.MyClassLoader class
         */
        MyClassLoader m = new MyClassLoader("");
        try {
            /*
             * Will not use system ClassLoader since it can't find MyClassLoader
             * by short name, will use MyClassLoader.loadClass(name) and should
             * throw LinkageError because MyClassLoader.defineClass(String) will
             * try to return class whose name already appears in the current
             * class loader's namespace
             */
            Class mca = this.getClass().getClassLoader().loadClass(
                "MyClassLoader");
            System.err.println("LinkageError wasn't thrown");
            return false;
        } catch (LinkageError err) {
            return true;
        } catch (Throwable thr) {
            System.err
                .println("LinkageError wasn't thrown, but another error or exception occured: ");
            thr.printStackTrace();
            return false;
        }
    }
}

