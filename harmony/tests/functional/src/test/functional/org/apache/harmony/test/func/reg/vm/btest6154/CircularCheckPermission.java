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
package org.apache.harmony.test.func.reg.vm.btest6154;

import java.security.Permission;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * ClassCircularityError after setting custom SecurityManager 
 *
 */
public class CircularCheckPermission extends RegressionTest {

    public int test(Logger log, String[] args) {
        try {      
            SecurityManager manager = System.getSecurityManager();
            System.setSecurityManager(new CustomSM());
            System.setSecurityManager(manager);
            return PASSED;
        } catch (ClassCircularityError cce) {
            cce.printStackTrace();
            return FAILED;
        }
        catch (Throwable t) {
            log.info("Unexpected excepton: " + t);
            t.printStackTrace();
            return ERROR;
        }
    }
    
    public static void main(String[] args) {
        System.exit(new CircularCheckPermission().test(Logger.global, args));
    }
}

class CustomSM extends SecurityManager {

    public void checkPermission(Permission arg0) {
        new CustomInternals();
    }
    
    static class CustomInternals{}
}