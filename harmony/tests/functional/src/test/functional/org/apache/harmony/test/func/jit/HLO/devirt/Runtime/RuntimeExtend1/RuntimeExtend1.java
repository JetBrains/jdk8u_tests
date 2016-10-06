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
package org.apache.harmony.test.func.jit.HLO.devirt.Runtime.RuntimeExtend1;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 31.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class RuntimeExtend1 extends Test {

    String str = "RuntimeExtend";
    AuxiliaryClass au = new AuxiliaryClass();
    static RuntimeExtend1 obj = new RuntimeExtend1();
    
    public static void main(String[] args) {
        System.exit(new RuntimeExtend1().test(args));
    }

    public int test() {
        log.info("Start RuntimeExtend1 test...");
        String auxiliaryDir = testArgs[0];
        String result = new String();
        obj = new RuntimeExtend1();
        try {
            au.changeObj(auxiliaryDir);
            for (int i=0; i<10000; i++) {    
                result = obj.testMethod();
            }
        } catch(Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
        if (result.equals("RuntimeExtend")) return pass();
        else return fail("TEST FAILED: result=" + result);
    }

    String testMethod() {
        return str;
    }    
}


class AuxiliaryClass {
    
    void changeObj(String path) throws Exception {
        URL url = new URL("file://" + path + "/child.jar");
        if (!new File(url.toURI()).exists()) {
            // TODO: remove this check?
            throw new RuntimeException("TEST EXCEPTION: url file:/" + path
                    + "/child.jar is not valid");
        }
        URLClassLoader loader = new URLClassLoader(new URL[] { url });
        Class child = loader.loadClass("org.apache.harmony.test.func." +
                "jit.HLO.devirt.Runtime.RuntimeExtend1.Child");
        RuntimeExtend1.obj = (RuntimeExtend1) child.newInstance();
    }
}
