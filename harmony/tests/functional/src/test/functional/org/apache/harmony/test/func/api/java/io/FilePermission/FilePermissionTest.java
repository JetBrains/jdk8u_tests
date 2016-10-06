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
/*
 * Created on 30.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.FilePermission;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.security.PermissionCollection;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

public final class FilePermissionTest extends IOMultiCase {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new FilePermissionTest(), args));
    }

    public Result testGetActions() {
        try {
            MultiThreadRunner.waitAtBarrier();
            if (!new FilePermission(Utils.TEMP_STORAGE, "read").getActions()
                    .equals("read")) {
                return failed("'read' file permission failed");
            }
            try {
                new FilePermission(Utils.TEMP_STORAGE, null);
                return failed("null file permission failed - expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
            if (!new FilePermission(Utils.TEMP_STORAGE, "read, write")
                    .getActions().equals("read,write")) {
                return failed("'read, write' file permission failed");
            }
            if (!new FilePermission(Utils.TEMP_STORAGE, "READ, WRITE")
                    .getActions().equals("read,write")) {
                return failed("'READ, WRITE' file permission failed");
            }
            try {
                new FilePermission(Utils.TEMP_STORAGE, "read;write");
                return failed("'read;write' file permission failed - expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
            if (!new FilePermission(Utils.TEMP_STORAGE, " read,write")
                    .getActions().equals("read,write")) {
                return failed("' read,write' file permission failed");
            }
            try {
                new FilePermission(Utils.TEMP_STORAGE, "read,write,");
                return failed("'read,write,' file permission failed - expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
            if (!new FilePermission(Utils.TEMP_STORAGE, "write,read")
                    .getActions().equals("read,write")) {
                return failed("'write,read' file permission failed");
            }
            if (!new FilePermission(Utils.TEMP_STORAGE, "write,read,write")
                    .getActions().equals("read,write")) {
                return failed("'write,read,write' file permission failed");
            }
            if (!new FilePermission(Utils.TEMP_STORAGE,
                    "write,read,delete,execute").getActions().equals(
                    "read,write,execute,delete")) {
                return failed("'write,read,delete,execute' file permission failed");
            }
        } catch (Throwable e) {
            return failed("failed " + e.getClass().getName() + " "
                    + e.getMessage());
        }
        return passed();
    }

    public Result testImplies() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (new FilePermission("/tmp/", "read").implies(null)) {
            return failed("implies(null) expected to be false");
        }
        if (!new FilePermission("/tmp/", "read").implies(new FilePermission(
                "/tmp/", "read"))) {
            return failed("case 1 failed");
        }
        if (new FilePermission("/tmp/", "read").implies(new FilePermission(
                "/tmp/", "read,write"))) {
            return failed("case 2 failed");
        }
        if (new FilePermission("/tmp/", "delete").implies(new FilePermission(
                "/tmp/", "read,write"))) {
            return failed("case 3 failed");
        }
        if (new FilePermission("/tmp/*", "delete").implies(new FilePermission(
                "/tmp/", "delete"))) {
            return failed("case 4 failed");
        }

        if (!new FilePermission(Utils.TEMP_STORAGE + "/*", "read")
                .implies(new FilePermission(Utils.TEMP_STORAGE + "/*", "read"))) {
            return failed("case 5 failed");
        }
        if (!new FilePermission(Utils.TEMP_STORAGE + File.separator + "*",
                "read").implies(new FilePermission(Utils.TEMP_STORAGE
                + File.separator + "a", "read"))) {
            return failed("case 6 failed");
        }

        if (File.separatorChar != '/') {
            if (new FilePermission(Utils.TEMP_STORAGE + "/*", "read")
                    .implies(new FilePermission(Utils.TEMP_STORAGE + "/a",
                            "read"))) {
                return failed("case 6a failed");
            }
        }

        //implies /* may behave differently on existent and non-existent dirs

        File f = File.createTempFile("testImplies", null, new File(
                Utils.TEMP_STORAGE));
        f.delete();

        if (!new FilePermission(f.getAbsolutePath() + "/*", "read")
                .implies(new FilePermission(f.getAbsolutePath() + "/*", "read"))) {
            return failed("case 7 failed");
        }
        if (!new FilePermission(f.getAbsolutePath() + File.separator + "*",
                "read").implies(new FilePermission(f.getAbsolutePath()
                + File.separator + "a", "read"))) {
            return failed("case 8 failed");
        }

        if (File.separatorChar != '/') {
            if (new FilePermission(f.getAbsolutePath() + "/*", "write")
                    .implies(new FilePermission(f.getAbsolutePath() + "/a",
                            "write"))) {
                return failed("case 8a failed");
            }
        }

        if (new FilePermission(f.getAbsolutePath() + File.separator + "*",
                "read").implies(new FilePermission(f.getAbsolutePath()
                + File.separator + "a" + File.separator + "b", "read"))) {
            return failed("case 9 failed");
        }

        return passed();
    }

    public Result testPermissionCollection() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        PermissionCollection ps = new FilePermission("/tmp/", "read")
                .newPermissionCollection();
        
        ps.add(new FilePermission("/tmp/", "write"));
        
        if(ps.implies(new FilePermission("/tmp/", "read,write"))) {
            return failed("case 1 failed");
        }

        if(!ps.implies(new FilePermission("/tmp/", "write"))) {
            return failed("case 2 failed");
        }

        ps.add(new FilePermission("/tmp/", "read"));

        if(!ps.implies(new FilePermission("/tmp/", "read,write"))) {
            return failed("case 3 failed");
        }

        if(!ps.implies(new FilePermission("/tmp/", "read"))) {
            return failed("case 4 failed");
        }
        
        
        return passed();
    }
}
