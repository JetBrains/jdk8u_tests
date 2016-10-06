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
package org.apache.harmony.test.func.api.java.security.auth.F_AuthPermissionTest_01;

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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.AuthPermission;
import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 31.08.2005
 */
public class F_AuthPermissionTest_01 extends ScenarioTest {

    private Policy m_Policy;
    //ProtectionDomain[] pds;
    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {
        
        String vendor = System.getProperty("java.vendor");
        String osName = System.getProperty("os.name");        
        String loginModule = ((osName.indexOf("Windows") != -1) ? "org.apache.harmony.auth.module.NTLoginModule"
                                                                : "org.apache.harmony.auth.module.UnixLoginModule");
        m_Policy = new MyPolicy();
        Policy.setPolicy(m_Policy);
        
        if (Policy.getPolicy().getPermissions(this.getClass().getProtectionDomain()).implies(new AuthPermission("doAsPrivileged"))) {
            return error("Protection domain mustn't have a security permission 'AuthPermission doAsPrivileged'");
        }
        
        System.setSecurityManager(new SecurityManager());
        Configuration.setConfiguration(new MyConfiguration(loginModule));
        
        try {
            LoginContext lc = new LoginContext("Login1");
            log.info("Trying to login...");
            lc.login();
            final Subject subject = lc.getSubject();            
            final AccessControlContext acc = new AccessControlContext(AccessController.getContext(), new MyDomainCombiner());
            AccessController.doPrivileged(new PrivilegedAction() {
                /* (non-Javadoc)
                 * @see java.security.PrivilegedAction#run()
                 */
                public Object run() {
                    log.info("Trying to get a system property 'user.name' on behalf of logged user");
                    Subject.doAsPrivileged(subject, new PrivilegedAction() {
                        /* (non-Javadoc)
                         * @see java.security.PrivilegedAction#run()
                         */
                        public Object run() {
                            log.info("Current user: " + System.getProperty("user.name"));
                            return null;
                        }
                    }, acc);
                    return null;
                }
            }, acc);
        } catch (LoginException e) {
            e.printStackTrace();
            return error("Error:" + e.getMessage());
        } catch (AccessControlException e) {
            e.printStackTrace();
            return fail("Test failed");
        }
        
/*        for (int i = 0; i < pds.length; i++) {
            for (Enumeration enum = Policy.getPolicy().getPermissions(pds[i]).elements(); enum.hasMoreElements(); ) {
                System.out.println(enum.nextElement());
            }
        }*/
        
        return pass("Test passed");
    }

    final class MyConfiguration extends Configuration {

        private String m_loginModule; 
        private AppConfigurationEntry[] m_appConfEntries;
        public MyConfiguration(String loginModule) {
            super();
            this.m_loginModule = loginModule;
            Map options = new HashMap();
            options.put("debug_only", "true");
            m_appConfEntries = new AppConfigurationEntry[] {
                    new AppConfigurationEntry(loginModule, AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)
            };
            log.info("MyConfiguration ctor has been called");
        }

        /* (non-Javadoc)
         * @see javax.security.auth.login.Configuration#getAppConfigurationEntry(java.lang.String)
         */
        public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
            return m_appConfEntries;
        }

        /* (non-Javadoc)
         * @see javax.security.auth.login.Configuration#refresh()
         */
        public void refresh() {
        }

    }
    
    final class MyDomainCombiner implements DomainCombiner {
        /**
         * 
         */
        public MyDomainCombiner() {
            log.info("MyDomainCombiner ctor has been called");
        }

        /* (non-Javadoc)
         * @see java.security.DomainCombiner#combine(java.security.ProtectionDomain[], java.security.ProtectionDomain[])
         */
        public ProtectionDomain[] combine(ProtectionDomain[] currentDomains, ProtectionDomain[] assignedDomains) {
            ProtectionDomain[] resultPDs = new ProtectionDomain[currentDomains.length];
            //pds = new ProtectionDomain[currentDomains.length];
            for (int i = 0; i < currentDomains.length; i++) {                
                PermissionCollection origPerms = currentDomains[i].getPermissions();
                PermissionCollection newPerms = new Permissions();
                for (Enumeration perms = origPerms.elements(); perms.hasMoreElements();) {
                    newPerms.add((Permission)perms.nextElement());
                }

                newPerms.add(new AuthPermission("doAsPrivileged"));
                resultPDs[i] = new ProtectionDomain(currentDomains[i].getCodeSource(), newPerms);
                //pds[i] = resultPDs[i];
            }
            
            return resultPDs; 
        }
    }
    
    final class MyPolicy extends Policy {
        private Policy m_systemPolicy;
        public MyPolicy() {
            super();
            m_systemPolicy = Policy.getPolicy();
            log.info("MyPolicy ctor has been called");
        }
        
        /* (non-Javadoc)
         * @see java.security.Policy#getPermissions(java.security.CodeSource)
         */
        public PermissionCollection getPermissions(CodeSource codeSource) {
            return m_systemPolicy.getPermissions(codeSource);
        }
        
        /* (non-Javadoc)
         * @see java.security.Policy#getPermissions(java.security.ProtectionDomain)
         */
        public PermissionCollection getPermissions(ProtectionDomain pd) {
            return m_systemPolicy.getPermissions(pd);
        }
        
        /* (non-Javadoc)
         * @see java.security.Policy#refresh()
         */
        public void refresh() {
        }
        
        
    }
    
    public static void main(String[] args) {
        System.exit(new F_AuthPermissionTest_01().test(args));
    }
}
