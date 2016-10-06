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
/**
 */

package org.apache.harmony.test.func.api.java.util.PropertyPermission;

import java.security.PermissionCollection;
import java.util.PropertyPermission;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 22, 2006
 */
public class PropertyPermissionTest extends MultiCase {

    public Result testConstructor() {
        PropertyPermission[] perm = {
                new PropertyPermission("java.home.*", "read"),
                new PropertyPermission("java.home.*", "read,write"),
                new PropertyPermission("java.home.*", "WRITE, READ"),
                new PropertyPermission("java.home.*", " reaD "), };
        String[] actions = { "read", "read,write", "read,write", "read" };

        for (int i = 0; i < perm.length; i++) {
            assertEquals(perm[i].getName(), "java.home.*");
            assertEquals(perm[i].getActions(), actions[i]);
        }
        return result();
    }

    public Result testNewContainer() {
        PermissionCollection collection = new PropertyPermission("permission",
                "write,read").newPermissionCollection();

        assertNotNull(collection);

        return result();
    }

    public static void main(String[] args) {
        System.exit(new PropertyPermissionTest().test(args));
    }

}
