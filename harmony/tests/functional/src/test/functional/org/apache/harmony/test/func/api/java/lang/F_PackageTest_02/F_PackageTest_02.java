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
 * Created on 12.04.2005
 * Last modification G.Seryakova
 * Last modified on 14.04.2005
 *  
 * 
 */
package org.apache.harmony.test.func.api.java.lang.F_PackageTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.test.func.api.java.lang.F_PackageTest_02.auxiliary.*;

/**
 */
public class F_PackageTest_02 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_PackageTest_02().test(args));
    }

    public int test() {
        int ind = 0;
        try {
            Class cls1 = A.class;
            Class cls2 = B.class;
            
            Package pkg = cls1.getPackage();
            String name = "org.apache.harmony.test.func.api.java.lang.F_PackageTest_02.auxiliary";

            log.info("Class loader: " + cls1.getClassLoader());
            if (!pkg.getName().trim().equals(name)) {
                log.info("Package name isn't expected. Must be: " + name
                        + ", but is: " + pkg.getName());
                ind++;
            }
            if (!pkg.getImplementationTitle().trim().equals("auxiliary")) {
                log.info("Package ImplementationTitle isn't expected. ");
                log.info("Must be: auxiliary, but is: "
                        + pkg.getImplementationTitle());
                ind++;
            }
            if (!pkg.getImplementationVendor().trim().equals("my")) {
                log.info("Package ImplementationVendor isn't expected. ");
                log.info("Must be: my, but is: "
                        + pkg.getImplementationVendor());
                ind++;
            }
            if (!pkg.getImplementationVersion().trim().equals("1.0")) {
                log.info("Package ImplementationVersion isn't expected. ");
                log.info("Must be: 1.0, but is: "
                        + pkg.getImplementationVersion());
                ind++;
            }
            if (!pkg.getSpecificationTitle().trim().equals("not exist")) {
                log.info("Package SpecificationTitle isn't expected. ");
                log.info("Must be: not exist, but is: "
                        + pkg.getSpecificationTitle());
                ind++;
            }
            if (!pkg.getSpecificationVendor().trim().equals("unknown")) {
                log.info("Package SpecificationVendor isn't expected. ");
                log.info("Must be: unknown, but is: "
                        + pkg.getSpecificationVendor());
                ind++;
            }
            if (!pkg.getSpecificationVersion().trim().equals("1.4.2")) {
                log.info("Package SpecificationVersion isn't expected. ");
                log.info("Must be: 1.4.2, but is: "
                        + pkg.getSpecificationVersion());
                ind++;
            }

            if ((!pkg.isCompatibleWith("1.0"))
                    || (!pkg.isCompatibleWith("1.4.2"))
                    || (pkg.isCompatibleWith("1.5"))) {
                log.info("Package isCompatibleWith isn't expected. ");
                log.info("Must be: true, true, false, but is: "
                        + pkg.isCompatibleWith("1.0") + ", "
                        + pkg.isCompatibleWith("1.4.2") + ", "
                        + pkg.isCompatibleWith("1.5"));
                ind++;
            }

            if ((pkg.toString().trim().indexOf(name) < 0)
                    || (pkg.toString().trim().indexOf("not exist") < 0)
                    || (pkg.toString().trim().indexOf("1.4.2") < 0)) {
                log.info("Package.toString isn't expected. ");
                log.info("Must contain: " + name
                        + ", not exist and 1.4.2");
                log.info("but is: " + pkg.toString());
                ind++;
            }

            if (!pkg.isSealed()) {
                log.info("Package must be sealed.");
                ind++;
            }
            
            if (!pkg.isSealed(cls1.getProtectionDomain().getCodeSource().getLocation())) {
                log.info("Package must be sealed with respect to " + cls1.getProtectionDomain().getCodeSource().getLocation());
                ind++;
            }
            
            if (B.getFieldFromA() != 12) {
                log.info("Not expected result for field from A class.");
                ind++;
            }

            try {
                Class cls3 = C.class;
                log.info("Loading of class C must throw exception.");
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ind++;
        }
        if (ind == 0) {
            return pass();
        } else {
            return fail("failed.");
        }
    }
}