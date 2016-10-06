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
package org.apache.harmony.test.func.api.javax.management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.management.MBeanPermission;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 */
public class MBeanPermissionImpliesTest extends Test {

    /**
     * Path to the directory containing the files with the object names.
     */
    private String resDir;

    public void testTrue() {
        test(resDir + "/true.txt", false);
    }

    public void testFalse() {
        test(resDir + "/false.txt", true);
    }

    private void test(String path, boolean invert) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = "";
            while ((line = br.readLine()) != null) {
                MBeanPermission p1 = new MBeanPermission(line, "getAttribute");
                String line2 = br.readLine();
                if (line2 == null) {
                    break;
                }
                MBeanPermission p2 = new MBeanPermission(line2, "getAttribute");

                if (invert) {
                    assertFalse("\"" + line + "\".implies(\"" + line2 + "\")",
                        p1.implies(p2));
                } else {
                    assertTrue("\"" + line + "\".implies(\"" + line2 + "\")",
                        p1.implies(p2));
                }
            }
        } catch (Exception ex) {
            fail(ex);
        }
    }

    public void init() {
        resDir = getArg("resDir");
        if ((resDir == null) || !(new File(resDir).exists())) {
            fail("resDir not found: " + resDir);
            finish();
        }
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(MBeanPermissionImpliesTest.class, args));
    }
}
