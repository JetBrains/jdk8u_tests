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
package org.apache.harmony.test.func.api.java.beans.checkPropertiesAccess;

import java.beans.Beans;
import java.beans.Introspector;
import java.beans.PropertyEditorManager;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Beans, Introspector, PropertyEditorManager.
 * <p>
 * Verify that methods: Introspector#setBeanInfoSearchPath(java.lang.String[]),
 * PropertyEditorManager#registerEditor(java.lang.Class, java.lang.Class),
 * PropertyEditorManager#setEditorSearchPath(java.lang.String[]),
 * Beans#setDesignTime(boolean),Beans#setGuiAvailable(boolean) invoke
 * SecurityManager.checkPropertiesAccess() method.
 * <p>
 * Step-by-step encoding:
 * <ul>
 * <li>Create class which extends SecurityManager and redefines.
 * checkPropertiesAccess() so that this method always throws SecurityException.
 * <li>Set security manager using System.setSecurityManager(securityManager)
 * method.
 * <li>Invoke each verified method.
 * <li>Verify that each verified method throws security exception.
 * <li>Go to item#3 and repeat for each verified method.
 * 
 */
public class CheckPropertiesAccessTest extends MultiCase {
    boolean isSecurityManagerWasSet = false;

    public static void main(String[] args) {
        System.exit(new CheckPropertiesAccessTest().test(args));
    }

    protected void ssetUp() {
        if (isSecurityManagerWasSet)
            return;
        SecurityManager securityManager = new SecurityManager() {
            public void checkPropertiesAccess() throws SecurityException {
                throw new SecurityException("Forced exception");
            }
        };
        System.setSecurityManager(securityManager);
    }

    public Result testIntrospectorSetBeanInfoSearchPath() {
        try {
            ssetUp();
            Introspector.setBeanInfoSearchPath(new String[] {});
            return failed("SecurityException wasn't throw");
        } catch (SecurityException e) {
            return passed();
        }
    }

    public Result testPropertyEditorManagerRegisterEditor() {
        try {
            ssetUp();
            PropertyEditorManager.registerEditor(this.getClass(),
                PrimitiveProperyEditor.class);
            return failed("SecurityException wasn't throw");
        } catch (SecurityException e) {
            return passed();
        }
    }

    public Result testPropertyEditorManagerSetEditorSearchPath() {
        try {
            ssetUp();
            PropertyEditorManager.setEditorSearchPath(new String[] { this
                .getClass().getPackage().getName() });
            return failed("SecurityException wasn't throw");
        } catch (SecurityException e) {
            return passed();
        }
    }

    public Result testBeansSetDesignTime() {
        try {
            ssetUp();
            Beans.setDesignTime(true);
            return failed("SecurityException wasn't throw");
        } catch (SecurityException e) {
            return passed();
        }
    }

    public Result testBeansSetGuiAvailable() {
        try {
            ssetUp();
            Beans.setGuiAvailable(true);
            return failed("SecurityException wasn't throw");
        } catch (SecurityException e) {
            return passed();
        }
    }
}