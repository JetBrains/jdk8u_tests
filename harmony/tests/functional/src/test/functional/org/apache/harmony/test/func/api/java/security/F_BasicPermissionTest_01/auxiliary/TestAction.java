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
package org.apache.harmony.test.func.api.java.security.F_BasicPermissionTest_01.auxiliary;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Policy;
import java.util.Enumeration;

/**
 * Created on 09.12.2004 
 */
public class TestAction implements PrivilegedExceptionAction 
{
    public TestAction() {
        //System.out.println("TestAction location: " + this.getClass().getProtectionDomain().getCodeSource().getLocation());
    
        for (Enumeration en = Policy.getPolicy().getPermissions(this.getClass().getProtectionDomain()).elements(); en.hasMoreElements(); ) {
                System.out.println(en.nextElement());
        }

        System.out.println("TestAction ctor has been called.");
    }

    public Object run() throws Exception {

        QEPermission qeperm = new QEPermission("queuePrintJob");

        // check whether permission collection is of appropriate type
        PermissionCollection perms = qeperm.newPermissionCollection();
        if (!Class.forName("java.security.BasicPermissionCollection").equals(perms.getClass())) {
            throw new IllegalArgumentException("A class returned by BasicPermission.newPermissionCollection is of wrong type.");
        }
        
        System.out.print("Permission check...");
        // see if a logged user has rights to do operation
        try {
            AccessController.checkPermission(qeperm);
            System.out.println("OK");
        } catch (AccessControlException e) {
            System.out.println("Failed");
            throw new PrivilegedActionException(e);
        }                
        
        return null;
    }

}
