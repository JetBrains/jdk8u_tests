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
package org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_09.auxiliary.FirstClass;

import org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_09.auxiliary.SecondClass.SecondClass;
import org.apache.harmony.test.func.api.java.lang.F_ClassLoaderTest_09.auxiliary.MyClassLoader;

public class FirstClass {

    public boolean test() {
        System.err.println("Method FirstClass.test() invoked");
        try {
            MyClassLoader mcl = (MyClassLoader)this.getClass().getClassLoader();
            mcl.i = 1;
            Class mca = mcl.loadClass("SecondClass");
            if ((!mca.getClassLoader().equals(
                SecondClass.class.getClassLoader()))
                || (!mcl.equals(SecondClass.class.getClassLoader()))) {
                System.err
                    .println("SecondClass is loaded with wrong ClassLoader:"
                        + mca.getClassLoader());
                return false;
            }
            SecondClass[] aa = am();
            SecondClass x = aa[2];
            if (x != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    SecondClass[] am() {
        SecondClass a = new SecondClass();
        SecondClass[] arr = new SecondClass[10];
        arr[2] = a;
        return arr;
    }
}

