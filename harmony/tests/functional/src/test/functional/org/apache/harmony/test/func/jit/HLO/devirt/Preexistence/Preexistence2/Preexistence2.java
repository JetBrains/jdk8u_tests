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
package org.apache.harmony.test.func.jit.HLO.devirt.Preexistence.Preexistence2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 16.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Preexistence2 extends Test {
    
    public static void main(String[] args) {
        System.exit(new Preexistence2().test(args));
    }
    
    public int test() {
        log.info("Start Preexistence2 test...");
        try {
            String result = null;
            A a = (A) inlineMethod();
            result = a.meth();
            log.info("result = " + result);
            if (result.equals("C")) return pass();
            else return fail("TEST FAILED: test result != C");
        } catch (Throwable e) {
            e.printStackTrace();
            return fail("TEST FAILED: unexpected " + e);
        }
    }
    
    Object inlineMethod() {
        A a = new C();
        return (B) a;
    }
}

class A {
    public String meth() {
        return "A";
    }
}

class B extends A {
    public String meth() {
        return "B";
    }
}

class C extends B {
    
    public String meth() {
        return "C";
    }
}


