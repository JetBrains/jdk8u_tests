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
 * Created on 24.01.2005
 * Last modification G.Seryakova
 * Last modified on 24.01.2005
 *  
 * 
 */
package org.apache.harmony.test.func.api.java.lang.F_ClassTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.io.DataInputStream;
import java.io.InputStream;

/**
 */
public class F_ClassTest_01 extends ScenarioTest {
    
    public static void main(String[] args) {
        System.exit(new F_ClassTest_01().test(args));
    }

     public int test() {
        int stat = 0;
        Class a = null;
        Class b = null;
        log.info("1");
        boolean systemDefaultStat = Integer.class.desiredAssertionStatus();
        ClassLoader myCL = new TestClassLoader(ClassLoader.getSystemClassLoader());
        
        String name = "A";
        try {
            a = myCL.loadClass(name);
        } catch (ClassNotFoundException ex) {
            log.info(name);
            ex.printStackTrace();
        }
        if (a.desiredAssertionStatus() != systemDefaultStat) {
            stat++;
        }
        log.info("must be " + systemDefaultStat + " is " + a.desiredAssertionStatus());
        log.info("" + myCL);
        log.info("" + a.getClassLoader());
        
        Object sigA[] = a.getSigners();
        if (sigA !=null) {
            for (int i = 0; i < sigA.length; i++) {
                log.info("signer " + i + ":" + ((Integer)sigA[i]).intValue());
            }
        } else {
            log.info("signers is null"); 
        }
        
        log.info("2");
        name = "B";
        myCL.setClassAssertionStatus("org.apache.harmony.test.func.api.java.lang.F_ClassTest_01.auxiliary." + name, true);  
        try {
            b = myCL.loadClass(name);
        } catch (ClassNotFoundException ex) {
            log.info(name);
            ex.printStackTrace();
        }

        if ((!b.desiredAssertionStatus()) || (a.desiredAssertionStatus() != systemDefaultStat)) {
            stat++;
        }
        log.info("must be " + systemDefaultStat + " is " + a.desiredAssertionStatus());
        log.info("must be true is " + b.desiredAssertionStatus());
        

        log.info("3");
        myCL.setDefaultAssertionStatus(true);
        if ((!b.desiredAssertionStatus()) || (!a.desiredAssertionStatus())) {
            stat++;
        }
        log.info("must be true is " + a.desiredAssertionStatus());
        log.info("must be true is " + b.desiredAssertionStatus());
        
        
        log.info("4");
        myCL.setPackageAssertionStatus("org.apache.harmony.test.func.api.java.lang.F_ClassTest_01.auxiliary", false);
        if ((!b.desiredAssertionStatus()) || (a.desiredAssertionStatus())) {
            stat++;
        }
        log.info("must be false is " + a.desiredAssertionStatus());
        log.info("must be true is " + b.desiredAssertionStatus());
        
        if (stat > 0) {
            return fail("FAILED.");
        } else {
            return pass();
        }
    }
}
class TestClassLoader extends ClassLoader {
    
    public TestClassLoader(ClassLoader clsLoader) {
        super(clsLoader);
    }
    
    public Class findClass(String name) throws ClassNotFoundException {
        Class cls = null;
        try {
            InputStream source = getResourceAsStream("org/apache/harmony/test/func/api/java/lang/F_ClassTest_01/auxiliary/" + name + ".class");
            DataInputStream dataStream = new DataInputStream(source);
            int size = dataStream.available(); 
            byte[] classData = new byte[size];
            dataStream.readFully(classData);
            String packageName = "org.apache.harmony.test.func.api.java.lang.F_ClassTest_01.auxiliary";
            if (getPackage(packageName) == null) {
                definePackage(packageName, "", "", "", "", "", "", null);
            }
            cls = defineClass(packageName + "." + name, classData, 0, size, this.getClass().getProtectionDomain());
            setSigners(cls, new Object[] {new Integer(15), new Integer(23)});
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
}