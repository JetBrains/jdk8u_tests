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
package org.apache.harmony.test.func.api.javax.management.remote;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.security.Policy;
import javax.management.remote.JMXPrincipal;
import javax.management.remote.SubjectDelegationPermission;

/**
 */
public class SecurePolicy extends Policy {

    public boolean delegate = false;

    public boolean foundPrincipal = false;

    static String correct = "correct";

    static String bad = "bad";

    public static final Principal correctPrincipal = new JMXPrincipal(correct);

    public static final Principal badPrincipal = new JMXPrincipal(bad);

    public static final SubjectDelegationPermission goodPermission = new SubjectDelegationPermission(
            correctPrincipal.getClass().getName() + "." + correct);

    public void refresh() {
    }

    public PermissionCollection getPermissions(CodeSource codeSource) {
        return new Permissions();
    }

    public boolean implies(ProtectionDomain domain, Permission permission) {

        boolean access = false;
        if (goodPermission.equals(permission)) {
            access = delegate;
        }

        Principal pr[] = domain.getPrincipals();

        for (int i = 0; i < pr.length; i++) {
            if (pr[i].equals(correctPrincipal)) {
                foundPrincipal = true;
                return true;
            } else {
                return false;
            }
        }
        return access;
    }

}
