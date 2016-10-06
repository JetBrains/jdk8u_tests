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
package org.apache.harmony.test.func.api.java.security.F_BasicPermissionTest_01;

import java.security.AccessControlException;

import java.security.AccessController;
import java.security.Policy;
import java.security.PrivilegedActionException;
import java.util.Enumeration;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.test.func.api.java.security.F_BasicPermissionTest_01.auxiliary.TestAction;

/**
 * Created on 07.12.2004 
 */
public class F_BasicPermissionTest_01 extends ScenarioTest 
{
    public int test() 
    {
        try {
/*            LoginContext lc = new LoginContext("Login1");
            lc.login();            
            Subject subject = lc.getSubject();*/
            for (Enumeration en = Policy.getPolicy().getPermissions(this.getClass().getProtectionDomain()).elements(); en.hasMoreElements(); ) {
                log.info(""+en.nextElement());
            }
     
            System.setSecurityManager(new SecurityManager());
            log.info("TestAction construction...");
            TestAction action = new TestAction();
            log.info((action != null) ? "OK" : "Failed");  
            AccessController.doPrivileged(action);
            //Subject.doAs(subject, action);
            //lc.logout();
/*            Principal principals[] = (Principal[])subject.getPrincipals().toArray(new Principal[0]);
            for (int i=0; i<principals.length; i++) {
                if (principals[i] instanceof com.sun.security.auth.NTUserPrincipal
                      || principals[i] instanceof com.sun.security.auth.UnixPrincipal) {
                    String loggedInUserName = principals[i].getName();
                    
                    log.info(loggedInUserName);
                }
            }           
*/            
/*        } catch (LoginException le) {
            le.printStackTrace();
            return fail("Login failed.");*/ 
        } catch (PrivilegedActionException pae) {            
            Exception e = pae.getException();            
/*            if (e instanceof PrivilegedActionException) {
                //return pass("You don't have enough rights to perform an action. Test passed."); 
            }*/            
            if (e instanceof ClassNotFoundException) {
                return error("Class not found: " + e.getMessage());
            }            
            if (e instanceof IllegalArgumentException) {
                return fail(e.getMessage());
            }
            
            e.printStackTrace();
            return fail("Test failed");
        }        
        
        return pass("Test passed");
    }

    public static void main(String[] args) 
    {
        System.exit(new F_BasicPermissionTest_01().test(args));
    }    
}

/*class TestAction implements PrivilegedExceptionAction
{
    public Object run() throws Exception
    {
        QEPermission qeperm = new QEPermission("quePrintJob");

        // check whether permission collection is of appropriate type
        PermissionCollection perms = qeperm.newPermissionCollection();
        if (!Class.forName("java.security.BasicPermissionCollection").equals(perms.getClass())) {
            throw new IllegalArgumentException("A class returned by BasicPermission.newPermissionCollection is of wrong type.");
        }
        
        // see if a logged user has rights to do operation
        AccessController.checkPermission(qeperm);        
        
        return null;
    } 
}
*/