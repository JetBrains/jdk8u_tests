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

package org.apache.harmony.test.func.api.java.lang.SecurityManager;

import java.io.IOException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;
public class SecurityManagerTest extends MultiCase {
    public static void main(String[] args) throws IOException {
        System.exit(new SecurityManagerTest().test(args));
    }

    public Result testGetClassContext() {
        SM sm = new SM();
        System.setSecurityManager(sm);
        
        int initialLength = sm.getClassContext().length;
        if(sm.getClassContext()[0] != SM.class) {
            return failed("Expected security manager class to be the first, got " + sm.getClassContext()[0]);
        }
        
        if(sm.getClassContext()[1] != this.getClass()) {
            return failed("Expected this class to be the second, got " + sm.getClassContext()[1]);
        }
        
        if(initialLength + 1!= deeper(sm)) {
            return failed("expected calls inside one class to increase stack");
        }
        
        return passed();
    }
    
    int deeper(SM sm) {
        return sm.getClassContext().length;
    }
}

class SM extends SecurityManager {
    public  Class[] getClassContext() {
        return super.getClassContext();
    }
}