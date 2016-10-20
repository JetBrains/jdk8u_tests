/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/**
 * Created on 16.12.2004 
 * @author Alexei S.Vaskin
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.test.stress.api.java.security.S_AccessControllerTest_01;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;

import org.apache.harmony.share.Test;

public class S_AccessControllerTest_01 extends Test {

    static {
        count = 0;
    }

    public int test() {       	
        
        if ("false".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"))) {
            log.info("An extra policy feature is disabled. Standard system-wide policy will be used instead.");
        }
        
        //log.info("Setting SecurityManager");
        System.setSecurityManager(new SecurityManager());
        
        DummyRunner dr = new DummyRunner();
        try {
            dr.run();
        } catch (AccessControlException e) {
            e.printStackTrace();
            return fail("Test failed.");
        }        
        
        return pass("Test passed.");
    }

    public static void main(String[] args) {        
        System.exit(new S_AccessControllerTest_01().test(args));
    }
    
    class DummyRunner {
        
        public void run() {            
          ++count;
          innerNumber = count;
          
            if (count < Integer.parseInt(testArgs[0])) {
                DummyRunner dr = new DummyRunner();
                dr.run();
            }            

            String property = (String)AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    //System.out.println("Class instance number: " + innerNumber);                    //
                    log.info("Class instance number: " + innerNumber);                    //
                    return System.getProperty("os.name");                    
                }
            //}, AccessController.getContext());
            });
            //log.info("OS name: " + property);
        }
        
        int innerNumber;
    }    
    
    static int count;    
}
