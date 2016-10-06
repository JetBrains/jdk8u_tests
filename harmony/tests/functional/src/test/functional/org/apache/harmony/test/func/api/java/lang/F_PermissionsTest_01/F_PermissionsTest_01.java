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
 * Created on 28.01.2005
 * Last modification G.Seryakova
 * Last modified on 28.01.2005
 * 
 * Test for RuntimePermission and ReflectPermission constructors.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_PermissionsTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.security.Permission;
import java.lang.reflect.ReflectPermission;

/**
 * Test for RuntimePermission and ReflectPermission constructors.
 * 
 */
public class F_PermissionsTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_PermissionsTest_01().test(args));
    }

    public int test() {
        int stat = 0;
        String input = "createClassLoader,getClassLoader,setContextClassLoader,"
            + "enableContextClassLoaderOverride,setSecurityManager,createSecurityManager,"
            + "exitVM,shutdownHooks,setFactory,setIO,modifyThread,stopThread,modifyThreadGroup,"
            + "getProtectionDomain,readFileDescriptor,writeFileDescriptor,accessDeclaredMembers,"
            + "queuePrintJob,getStackTrace,setDefaultUncaughtExceptionHandler,preferences,"
            + "getenv.*,loadLibrary.*,accessClassInPackage.*,defineClassInPackage.*";
        String names[] = input.split(",");
        Permission perms[] = new Permission[names.length+1];
        
        for (int i = 0; i < names.length; i++) {
            perms[i] = new RuntimePermission(names[i]);
            log.info(names[i] + "has created.");
        }
        perms[names.length] = new ReflectPermission("suppressAccessChecks");
        log.info("suppressAccessChecks has created.");
        
        for (int i = 0; i < names.length; i++) {
            if  (!perms[i].getName().equalsIgnoreCase(names[i])) {
                log.info("For " + names[i] + " name is not equal.");
                stat++;
            }
        } 
        if  (!perms[names.length].getName().equalsIgnoreCase("suppressAccessChecks")) {
            log.info("For suppressAccessChecks name is not equal.");
            stat++;
        }
        
        for (int i = 0; i < names.length; i++) {
            if  (!perms[i].implies(new RuntimePermission(perms[i].getName()))) {
                log.info("For " + names[i] + " implies() return false.");
                stat++;
            }
        }
        if  (!perms[names.length].implies(new ReflectPermission(perms[names.length].getName()))) {
            log.info("For suppressAccessChecks implies() return false.");
            stat++;
        }
        
        if (stat == 0) {
            return pass();
        } else {
            return fail("FAILED.");
        }
    }
}