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
package org.apache.harmony.test.func.api.java.security.F_AccessControllerTest_03;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.BasicPermission;
import java.security.DomainCombiner;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.SecurityPermission;
import java.util.PropertyPermission;

import org.apache.harmony.test.func.share.ScenarioTest;
import auxiliary.AccessTestClass;
import org.apache.harmony.share.Result;

/**
 * Created on 18.11.2004
 */
public class F_AccessControllerTest_03 extends ScenarioTest 
{
//    ProtectionDomain[][] dummyCurrentDomains = null;
//    ProtectionDomain[][] dummyAssignedDomains = null;
    //static int index = 0;

    private String propertyName;
    public int test() 
    {        
//        dummyCurrentDomains = new ProtectionDomain[2][1];
//        dummyAssignedDomains = new ProtectionDomain[2][1];
        
        System.setSecurityManager(new SecurityManager());
        propertyName = testArgs[0];
        return (GetPropertyWithoutPermission() != Result.PASS ||
                GetPropertyWithPermission() != Result.PASS) ? fail("Test failed") : pass("Test passed");                
    }
    
    private int GetPropertyWithoutPermission()
    {
        log.info("Trying to get a system property without a required permission.");
        AccessTestClass atc = new AccessTestClass(propertyName);
        try
        {
            log.info(atc.getSystemProperty());
            return fail("first");
        }
        catch (AccessControlException e)
        {            
            return Result.PASS;
        }        
    }    
    
    private int GetPropertyWithPermission()
    {
        log.info("\n\nTrying to get a system property with a required permission.");
        DomainCombiner newDomainCombiner = new MyDomainCombiner();
        AccessControlContext newContext = new AccessControlContext(AccessController.getContext(), newDomainCombiner);
        
/*        log.info("Dummy current domains before:");
        for (int i = 0; i < dummyCurrentDomains.length; i++) {
            for (int j = 0; j < dummyCurrentDomains[i].length; j++) {
                log.info("dummyCurrentDomains[" + i + "][" + j + "]: " + dummyCurrentDomains[i][j]);    
            }            
        }

        log.info("Dummy assigned domains before:");
        for (int i = 0; i < dummyAssignedDomains.length; i++) {
            for (int j = 0; j < dummyAssignedDomains[i].length; j++) {
                log.info("dummyAssignedDomains[" + i + "][" + j + "]: " + dummyAssignedDomains[i][j]);    
            }            
        }
*/        
        AccessTestClass atc = (AccessTestClass)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run()
            {
//                return  (this.getClass().getProtectionDomain());
                return new AccessTestClass(propertyName);
            }            
        }, newContext);        
        log.info(""+atc);
/*        log.info("Dummy current domains after:");
        for (int i = 0; i < dummyCurrentDomains.length; i++) {
            for (int j = 0; j < dummyCurrentDomains[i].length; j++) {
                log.info("dummyCurrentDomains[" + i + "][" + j + "]: " + dummyCurrentDomains[i][j]);    
            }            
        }

        log.info("Dummy assigned domains after:");
        for (int i = 0; i < dummyAssignedDomains.length; i++) {
            for (int j = 0; j < dummyAssignedDomains[i].length; j++) {
                log.info("dummyAssignedDomains[" + i + "][" + j + "]: " + dummyAssignedDomains[i][j]);    
            }            
        }
*/
        
        if (newContext.getDomainCombiner() == null)
        {
                return fail("second");
        }
        
        try
        {
            log.info(atc.getSystemProperty());
            return Result.PASS;
        }
        catch (AccessControlException e)
        {
            e.printStackTrace();
            return Result.FAIL;
        }        
    }

    public static void main(String[] args) 
    {
        System.exit(new F_AccessControllerTest_03().test(args));
    }
    
    public class MyDomainCombiner implements DomainCombiner
    {        
        public ProtectionDomain[] combine(ProtectionDomain[] currentDomains, 
                                          ProtectionDomain[] assignedDomains) 
        {            
            log.info("MyDomainCombiner.combine() has been called");
            if (currentDomains[0] == null) {
                throw new RuntimeException ("A protection domain of the main execution thread is null. Test failed.");
            }
            
            //log.info("A " + (index + 1) + " call for DomainCombiner.combine() method");
            ProtectionDomain originalPD = currentDomains[1];
            PermissionCollection perms = AccessTestClass.policy.getPermissions(originalPD);
            //log.info("A set of permissions of the protection domain of the main execution thread is:");
            //log.info(perms);
            if (!perms.isReadOnly())
            {
                BasicPermission p = new PropertyPermission(propertyName, "read");
                log.info("A permission which will be added: " + p+ "\n");
                perms.add(p);                
                perms.setReadOnly();
            }
            
            //currentDomains[1] = new ProtectionDomain(originalPD.getCodeSource(), perms, originalPD.getClassLoader(), null);
            ProtectionDomain[] pd = new ProtectionDomain[1];
            pd[0] = new ProtectionDomain(originalPD.getCodeSource(), perms, originalPD.getClassLoader(), null);
//            dummyCurrentDomains[index] = currentDomains;
//            dummyAssignedDomains[index] = assignedDomains;
            //index++;

            //return currentDomains;
            return pd;
        }
    }
}