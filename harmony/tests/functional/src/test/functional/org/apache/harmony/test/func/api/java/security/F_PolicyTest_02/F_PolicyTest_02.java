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
 * Created on 17.11.2004
 */
package org.apache.harmony.test.func.api.java.security.F_PolicyTest_02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AccessControlException;
import java.security.Policy;

import auxiliary.GetSystemProperty;

import org.apache.harmony.test.func.share.ScenarioTest;

public class F_PolicyTest_02 extends ScenarioTest {
    
    final private String oldproperty = "user.name"; 
    final private String newproperty = "user.dir"; 
    //final private PropertyPermission pp = new PropertyPermission(property, "read");

    /**
     * change java.policy file and then refresh Policy. it must get new permission values  
     */
    public int test() {
    // set required policy [from auxiliary/java.policy.static file]
    //restorePolicyFile();
        
        GetSystemProperty gsp = new GetSystemProperty();
        
/*        for (Enumeration e = Policy.getPolicy().getPermissions(gsp.getClass().getProtectionDomain()).elements(); e.hasMoreElements();) {
            System.out.println(e.nextElement());
        }
/**/        
        
        try {
            gsp.get(oldproperty, "read");
            return fail("there isn't "+oldproperty+" in java.policy, so it should throw exception");
        } catch (AccessControlException e) {
            //
        }
        try {
            gsp.get(oldproperty);
        } catch (AccessControlException e) {
            return fail(oldproperty+" must be in java.policy with auxiliary.jar, so it should be checked without exception");
        }
        
        // change java.policy file and then refresh Policy
        try {
            FileOutputStream fos = new FileOutputStream(testArgs[0]);
            fos.write("grant codeBase \"file:${qe.test.path}/auxiliary/auxiliary.jar\" {\n".getBytes());
            fos.write("    permission    java.util.PropertyPermission \"".getBytes()); fos.write(newproperty.getBytes()); fos.write("\", \"read\";\n".getBytes());
            fos.write("};".getBytes());
            fos.write("grant {".getBytes());
            fos.write("    permission    java.lang.RuntimePermission \"getProtectionDomain\";\n".getBytes());
            fos.write("    permission    java.security.SecurityPermission \"getPolicy\";\n".getBytes());
            fos.write("    permission    java.io.FilePermission \"<<ALL FILES>>\", \"read, write\";\n".getBytes());
            fos.write("};\n".getBytes());
            fos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Policy.getPolicy().refresh();
        
        try {
            gsp.get(newproperty, "read");
            return fail("there isn't "+newproperty+" in NEW java.policy (grant), so it should throw exception");
        } catch (AccessControlException e) {
            //
        } finally {
            //restorePolicyFile();
        }
        try {
            System.out.println("property = "+gsp.get(newproperty)); 
        } catch (AccessControlException e) {
            return fail("NEW java.policy have "+newproperty+" property for auxiliary.jar so, it should be checked without exception through doPrivilaged()");
        } finally {
            //restorePolicyFile();
        }
        
        return pass();
    }
    
    public void restorePolicyFile() {
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(testArgs[0]+".static"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(testArgs[0]));
            while(br.ready()) {
                bw.write(br.read());
            }
            bw.close();
            br.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) {
        System.exit(new F_PolicyTest_02().test(args));
    }
    
}
