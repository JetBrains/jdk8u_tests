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
/**
 */
package org.apache.harmony.test.func.reg.vm.btest4533;

import java.util.logging.Logger; 
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest4533 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest4533().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        try {
            SecurityManager sm = new MySecurityManager();
            System.setSecurityManager(sm);
            ClassLoader cl = new MyClassLoader();
        } catch(SecurityException e) {
            return pass();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return fail();
    }
    public static class MyClassLoader extends ClassLoader {}

    public static class MySecurityManager extends SecurityManager {

        private final boolean status = true;

        public MySecurityManager() {
            super();
        }
        public void checkPermission(java.security.Permission p) {
            if ("createClassLoader".equals(p.getName())) {
                check();
            }
        }
        public void createClassLoader() {
            check();
        }
        private void check() {
            if (status) {
                SecurityException ex = new SecurityException();
                throw ex;
            }
        }
    }
}
