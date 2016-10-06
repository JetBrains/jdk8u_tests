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
package org.apache.harmony.test.func.api.java.security.F_AccessControlContextTest_01;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.DomainCombiner;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 06.12.2004 
 */
public class F_AccessControlContextTest_01 extends ScenarioTest 
{

    private Policy policy = Policy.getPolicy();    
    //ProtectionDomain[] dummyCurrentDomains = null;
    //ProtectionDomain[] dummyAssignedDomains = null;
    //static int index = 0;

    public int test()
    {
        if ("false".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"))) {
            return error("Extra policy feature is disabled.");
        }
        
//        for (Enumeration en = Policy.getPolicy().getPermissions(this.getClass().getProtectionDomain()).elements(); en.hasMoreElements();) {
//            System.out.println(en.nextElement());
//        }
        //dummyCurrentDomains = new ProtectionDomain[2][1];
        //dummyAssignedDomains = new ProtectionDomain[2][1];       
        
        System.setSecurityManager(new SecurityManager());
 
        Policy.getPolicy().refresh();        
        
        AccessControlContext currContext = AccessController.getContext();
        try {
            currContext.checkPermission(new RuntimePermission("getProtectionDomain"));
            return fail("The current access control context mustn't have a permission to get a protection domain though it has one. Test failed.");            
        } catch (AccessControlException e) {
        }
        
        Policy.getPolicy().refresh();
                
        //log.info("\tProtection domain of the main execution thread after a \"getProtectionDomain\" permission has been added:\n" + this.getClass().getProtectionDomain());
        
        PermissionCollection permissions = new Permissions();        
        permissions.add(new RuntimePermission("createClassLoader"));
        if (!permissions.implies(new RuntimePermission("createClassLoader"))) {
            return fail("An operation of adding new permission into PermissionCollection failed. Test failed.");
        }
        
        ProtectionDomain pd = null;
        try {
            String user_dir = System.getProperty("qe.suite");            
            pd = new ProtectionDomain(new CodeSource(new URL("file:/" + user_dir + "/-"), new Certificate[] {}), permissions, ClassLoader.getSystemClassLoader(), null);
        } catch (MalformedURLException e) {        
            e.printStackTrace();
            return error("Couldn't create URL.");
        }
        
        log.info("\tA protection domain for the context being constructed:\n" + pd);
        
        ProtectionDomain[] pds = {pd};
        AccessControlContext intermediateContext = new AccessControlContext(pds);
        
        if (intermediateContext.equals(currContext))
        {
            return fail("Contexts are equal while they mustn't be. Test failed.");
        }       
        
        DomainCombiner dc = new MyDomainCombiner2();
        AccessControlContext finalContext = new AccessControlContext(intermediateContext, dc);
        
        /*log.info("\tDummy current domains before:");
        for (int i = 0; i < dummyCurrentDomains.length; i++) {
            for (int j = 0; j < dummyCurrentDomains[i].length; j++) {
                log.info("dummyCurrentDomains[" + i + "][" + j + "]: " + dummyCurrentDomains[i][j]);    
            }            
        }

        log.info("\tDummy assigned domains before:");
        for (int i = 0; i < dummyAssignedDomains.length; i++) {
            for (int j = 0; j < dummyAssignedDomains[i].length; j++) {
                log.info("dummyAssignedDomains[" + i + "][" + j + "]: " + dummyAssignedDomains[i][j]);    
            }            
        }*/
        
        MySecureClassLoader scl;
        try {
            scl = (MySecureClassLoader)AccessController.doPrivileged(new PrivilegedAction() {            
                public Object run()
                {
                    return new MySecureClassLoader(ClassLoader.getSystemClassLoader());
                }
            }, finalContext);
        } catch (AccessControlException e) {
            e.printStackTrace();
            return fail("Test failed.");
        }

        /*log.info("\tDummy current domains after:");
        for (int i = 0; i < dummyCurrentDomains.length; i++) {
            for (int j = 0; j < dummyCurrentDomains[i].length; j++) {
                log.info("dummyCurrentDomains[" + i + "][" + j + "]: " + dummyCurrentDomains[i][j]);    
            }            
        }

        log.info("\tDummy assigned domains after:");
        for (int i = 0; i < dummyAssignedDomains.length; i++) {
            for (int j = 0; j < dummyAssignedDomains[i].length; j++) {
                log.info("dummyAssignedDomains[" + i + "][" + j + "]: " + dummyAssignedDomains[i][j]);    
            }            
        }*/      
        
        if (finalContext.hashCode() == currContext.hashCode())
        {
            return fail("Hash codes are equal. Test failed.");
        }
        
        if (finalContext.hashCode() != intermediateContext.hashCode())
        {
            return fail("Hash codes aren't equal. Test failed.");
        }
        
        return pass("Test passed.");
    }

//    private void UpdatePolicyFile(final String content) throws IOException
//    {
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream(testArgs[0]);
//            out.write(content.getBytes());
//        } finally {
//            if (out != null)
//            {
//                out.close();
//            }
//        }
//    } 
    
    public static void main(String[] args) 
    {
        System.exit(new F_AccessControlContextTest_01().test(args));
    }
    
    class MySecureClassLoader extends SecureClassLoader
    {
        public MySecureClassLoader(ClassLoader parent)
        {            
            super(parent);
            log.info("\tMySecureClassLoader ctor has been called.");            
        }
    }
    
    public class MyDomainCombiner2 implements DomainCombiner
    {
        public ProtectionDomain[] combine(ProtectionDomain[] currentDomains, ProtectionDomain[] assignedDomains) 
        {           
            ProtectionDomain[] resultPDs = new ProtectionDomain[2];
            resultPDs[0] = assignedDomains[0];            

            PermissionCollection newPerms = new Permissions(); 
            for (int i = 0; i < currentDomains.length; i++) {
                PermissionCollection origPerms = currentDomains[0].getPermissions();
                for (Enumeration en = origPerms.elements(); en.hasMoreElements();) {
                    Permission p = (Permission)en.nextElement();
                    if (!newPerms.implies(p)) {
                        newPerms.add(p);
                    }
                }
            }
            
            for (int i = 0; i < assignedDomains.length; i++) {
                PermissionCollection origPerms = assignedDomains[0].getPermissions();
                for (Enumeration en = origPerms.elements(); en.hasMoreElements();) {
                    Permission p = (Permission)en.nextElement();
                    if (!newPerms.implies(p)) {
                        newPerms.add(p);
                    }
                }
            }
            
            ProtectionDomain pd = new ProtectionDomain(currentDomains[0].getCodeSource(), newPerms, currentDomains[0].getClassLoader(), null);
            resultPDs[1] = pd;
            
            return resultPDs;
        }        
    }
}