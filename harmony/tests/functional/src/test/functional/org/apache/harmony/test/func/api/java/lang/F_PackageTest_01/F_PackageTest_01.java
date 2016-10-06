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
package org.apache.harmony.test.func.api.java.lang.F_PackageTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.io.DataInputStream;
import java.io.InputStream;

/**
 */
public class F_PackageTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_PackageTest_01().test(args));
    }

    public int test() {
        int ind = 0;
        ClassLoader loader = new TestClassLoader();
        try {
            Class cls1 = loader.loadClass("auxiliary.A");
            Class cls2 = loader.loadClass("auxiliary.B");
            Class cls3 = loader.loadClass("auxiliary1.D");

            Package pkg = cls1.getPackage();
            Package pkg1 = cls3.getPackage();
            String name = "org.apache.harmony.test.func.api.java.lang.F_PackageTest_01.auxiliary";

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

            if (pkg.isSealed()) {
                log.info("Package mustn't be sealed.");
                ind++;
            }

            if (!pkg.equals(cls1
                    .getMethod("getPackage", new Class[] { String.class })
                    .invoke(null,
                            new Object[] { "org.apache.harmony.test.func.api.java.lang.F_PackageTest_01.auxiliary" }))) {
                log.info("Package.getPackage() doesn't return expected result.");
                ind++;
            }

            Package pkgs[] = Package.getPackages();
            Package pkgs1[] = (Package[]) cls2.getMethod("getPackages",
                    new Class[] {}).invoke(null, new Object[] {});
            for (int i = 0; i < pkgs1.length; i++) {
                for (int j = 0; j < pkgs.length; j++) {
                    if (pkgs1[i].getName().equals(pkgs[j].getName()) && pkgs1[i].hashCode() == pkgs[j].hashCode()) {
                        pkgs1[i] = null;
                        break;
                    }
                }
            }
            
            for (int i = 0; i < pkgs1.length; i++) {
                if (pkgs1[i] != null) {
                    if (!pkgs1[i].equals(pkg) && !pkgs1[i].equals(pkg1)) { 
                        log.info(pkgs1[i].toString() + " not expected package.");
                        ind++;
                    }
                }
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

class TestClassLoader extends ClassLoader {

    public TestClassLoader() {
        super();
    }

    public Class findClass(String name) throws ClassNotFoundException {
        Class cls = null;
        try {
            InputStream source = getResourceAsStream("org/apache/harmony/test/func/api/java/lang/F_PackageTest_01/"
                    + convertName(name));
            DataInputStream dataStream = new DataInputStream(source);
            int size = dataStream.available();
            byte[] classData = new byte[size];
            dataStream.readFully(classData);
            String packageName = "org.apache.harmony.test.func.api.java.lang.F_PackageTest_01."
                    + extractPackage(name);
            if (getPackage(packageName) == null) {
                definePackage(packageName, "not exist", "1.4.2", "unknown",
                        "auxiliary", "1.0", "my", null);
            }
            cls = defineClass(
                    "org.apache.harmony.test.func.api.java.lang.F_PackageTest_01."
                            + name, classData, 0, size);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

        if (cls == null) {
            throw new ClassNotFoundException("Class " + name + "has not found.");
        }
        return cls;
    }

    private String convertName(String name) {
        String result;
        if (name == null) {
            result = name;
        } else {
            result = name.replace('.', '/') + ".class";
        }
        return result;
    }

    private String extractPackage(String name) {
        String result;
        if (name == null) {
            result = name;
        } else {
            result = name.substring(0, name.lastIndexOf("."));
        }
        return result;

    }
}