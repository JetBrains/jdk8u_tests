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
/*
 * Created on 17.11.2004
 */
package org.apache.harmony.test.func.api.java.security.F_PolicyTest_01;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.SecurityPermission;
import java.util.Enumeration;
import java.util.PropertyPermission;
import java.util.Vector;

import auxiliary.GetSystemProperty; 

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_PolicyTest_01 extends ScenarioTest {
    
    final private String property = "user.dir"; 
    final private PropertyPermission pp = new PropertyPermission(property, "read");

    /**
     * check ability to re-set Policy by own class and get right property after refreshing
     */
    public int test() {
        
        GetSystemProperty gsp = new GetSystemProperty();
        
        try {
            log.info(""+gsp.get(property, "read"));
        } catch (AccessControlException e) {
            return fail("PropertyPermission must be in java.policy and should be checked without exception");
        }
        
        Policy.setPolicy(new TestPolicy());
        // there are getProtectionDomain and getPolicy in created Policy
        
        ProtectionDomain pd = this.getClass().getProtectionDomain();
        if (Policy.getPolicy().getPermissions(pd) != null) {
            return fail("Permissions must be empty in created Policy");
        }
        
        try {
            AccessController.checkPermission(pp); 
            // shouldn't print property
            return fail("new policy shouldn't have "+property+" property");
        } catch (AccessControlException e) {
            // right way
        }
        
        Policy.getPolicy().refresh();
        
        try {
            log.info("property = "+System.getProperty(property)); 
        } catch (AccessControlException e) {
            return fail("refreshed policy must have "+property+" property");
        }
        
        return pass();
    }
    
    public static void main(String[] args) {
        System.exit(new F_PolicyTest_01().test(args));
    }
    
    public class TestPolicy extends Policy {

        private Vector allowedPermissionArr = new Vector();
        private TestPolicy() {
            allowedPermissionArr.add(new RuntimePermission("getProtectionDomain"));
            allowedPermissionArr.add(new SecurityPermission("getPolicy"));
        }

        public void refresh() {
            allowedPermissionArr.clear();
            allowedPermissionArr.add(pp);
        }
        
        public PermissionCollection getPermissions(ProtectionDomain pd) {
            return null;
        }

        public PermissionCollection getPermissions(CodeSource cs) {
            return null;
        }

        public boolean implies(ProtectionDomain pd, Permission p) {
            boolean res = false;
            log.info("check "+p + "("+super.implies(pd, p)+")");
            if (allowedPermissionArr.contains(p))
                res = true;
            else
                for (Enumeration e = pd.getPermissions().elements(); e.hasMoreElements() ;) {
                    if (e.nextElement().equals(p)) {
                        res = true;
                    }
                }
            log.info("... = "+res);
            return res;        
        }
    }
    
}
