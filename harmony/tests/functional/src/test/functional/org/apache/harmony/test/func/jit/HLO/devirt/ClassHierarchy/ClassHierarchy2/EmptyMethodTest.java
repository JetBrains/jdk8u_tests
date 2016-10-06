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
package org.apache.harmony.test.func.jit.HLO.devirt.ClassHierarchy.ClassHierarchy2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 2.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class EmptyMethodTest extends Test {
            
    public static void main(String[] args) {
        System.exit(new EmptyMethodTest().test(args));
    }

    private A obj = new D();
    
    public int test() {
        log.info("Start EmptyMethodTest test...");
        try {
            String str = new String();
            for (int i=0; i<1000000; i++) {
                str = obj.toString();
            }
            log.info("inlineMethod() returned: " + str);
            if(obj.getClass().getName().equals("org.apache.harmony.test.func" +
                    ".jit.HLO.devirt.ClassHierarchy.ClassHierarchy2.D") 
                    || !str.equals("B")) 
                return pass();
            else return fail("TEST FAILED: possible devirtualization error");
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }
}


class A {
    public String toString() {
        return "A";
    }
}

class B extends A {
    public String toString() {
        return "B";
    }
}

class C extends B {
    public String toString() {
        return super.toString();
    }
}

class D extends C {
    
}