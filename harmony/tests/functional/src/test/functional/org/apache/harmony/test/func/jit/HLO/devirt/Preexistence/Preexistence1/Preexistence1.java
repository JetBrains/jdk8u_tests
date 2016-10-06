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
package org.apache.harmony.test.func.jit.HLO.devirt.Preexistence.Preexistence1;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 16.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Preexistence1 extends Test {
    
    private static A a = new A(0);
    
    public static void main(String[] args) {
        System.exit(new Preexistence1().test(args));
    }
    
    public int test() {
        log.info("Start Preexistence1 test...");
        try {
            String result = new String();
            for (int i=0; i<10000; i++) {
                a = new A(i);
                result = a.foo(i);
                int k = meth();
                result = a.foo(i);
            }
            log.info("result = " + result);
            if (result.equals("B9999")) return pass();
            else return fail("TEST FAILED: unexpected test result != B9999");
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }
    
    private final int meth(){
        final String str = new String((a = new B(0)).toString());
        return 0;
    }
}

class A {
    int k;
    
    A (int i) {
        k=i;
    }
    
    String foo(int i) {
        return "A"+i;
    }
}

class B extends A {
    
    B(int i) {
        super(i);
    }
    
    String foo(int i) {
        return "B"+i;
    }
}
