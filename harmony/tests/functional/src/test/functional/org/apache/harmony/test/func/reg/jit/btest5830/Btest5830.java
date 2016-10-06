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
 
package org.apache.harmony.test.func.reg.jit.btest5830;

import java.security.Permission;

public class Btest5830 {   
    
    public static void main(String args []){
        new Btest5830().Btest5830();
        System.err.println("PASSED!");
    }

    public void Btest5830() {
        SecurityManager manager = System.getSecurityManager();
        System.setSecurityManager(new FIS_SecurityManager());
        System.setSecurityManager(manager);    
    }


    class FIS_SecurityManager extends SecurityManager {
        public void checkPermission(Permission perm) {}   
    }

}
