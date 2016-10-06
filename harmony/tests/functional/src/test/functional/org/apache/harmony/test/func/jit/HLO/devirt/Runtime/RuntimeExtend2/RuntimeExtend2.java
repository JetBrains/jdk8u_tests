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
package org.apache.harmony.test.func.jit.HLO.devirt.Runtime.RuntimeExtend2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 31.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class RuntimeExtend2 extends Test {

    long num = 9999;
    AuxiliaryClass au = new AuxiliaryClass();
    static RuntimeExtend2 obj = new RuntimeExtend2();
    
    public static void main(String[] args) {
        System.exit(new RuntimeExtend2().test(args));
    }

    public int test() {
        log.info("Start RuntimeExtend2 test...");
        try {
            long result = 0;
            for (int k=0; k<10; k++) {    
                for (int i=0; i<10000; i++) {    
                    obj = new RuntimeExtend2();
                    result = obj.testMethod();
                    if (i == 9999) au.changeObj();
                    result = obj.testMethod();
                    result = obj.testMethod();
                }
            }
            if (result == 333) return pass();
            else return fail("TEST FAILED: result=" + result);
        } catch(Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }

    protected long testMethod() {
        return num;
    }    
}


class AuxiliaryClass {
    
    void changeObj() throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Class child = Class.forName("org.apache.harmony.test.func."
                + "jit.HLO.devirt.Runtime.RuntimeExtend2.TestPackage.Child");
        RuntimeExtend2.obj = (RuntimeExtend2) child.newInstance();
    }
}
