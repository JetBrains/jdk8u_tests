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
package org.apache.harmony.test.func.api.java.security.F_PermissionsTest_01;

import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.SecurityPermission;
import java.util.Enumeration;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 02.12.2004 
 */
public class F_PermissionsTest_01 extends ScenarioTest 
{
    public int test() 
    {
        System.setSecurityManager(new SecurityManager());        
        int i = 1;
        PermissionCollection permCollection = Policy.getPolicy().getPermissions(this.getClass().getProtectionDomain()); 
        for (Enumeration en = permCollection.elements(); en.hasMoreElements();)
        {
            Permission perm = (Permission)en.nextElement();
            String permName = perm.getName();
            if (permName.trim().equals(""))
            {
                return fail("Couldn't get the name of a permission.");
            }
            log.info("Permission " + i++ + " name is: " + permName);
            String actions = perm.getActions();
            actions = null;
            if (perm.getActions() == null) {
                return fail("There is a security breach in Permission.getName. Test failed");
            }
            log.info(" " + perm.getActions());            
        }
        
        // by default the permission collection is not readonly 
        // unless it is set to readonly by a call to setReadOnly 
        if (permCollection.isReadOnly())
        {
            return fail("The permission collection can't be readonly by default. Test failed.");
        }
        permCollection.setReadOnly();
        try {
            permCollection.add(new SecurityPermission("createAccessControlContext"));
            return fail("The permission collection isn't readonly though it must be. Test failed.");
        } catch (SecurityException e) {
            
        }        
        
        Permissions perms = new Permissions();
        perms.add(new SecurityPermission("createAccessControlContext"));
        perms.add(new SecurityPermission("setPolicy"));        
        if (!perms.implies(new SecurityPermission("setPolicy")))
        {
            return fail("The permission collection doesn't have required permission. Test failed.");
        }
                
        
        return pass("Test passed.");
    }

    public static void main(String[] args) 
    {
        System.exit(new F_PermissionsTest_01().test());
    }
}
