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
package org.apache.harmony.test.func.jit.HLO.inline.StaticField.StaticField1;

import java.util.Vector;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 10.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class StaticField1 extends Test {

    protected static Vector v = new Vector(1);
    
    public static void main(String[] args) {
        System.exit((new StaticField1()).test(args));
    }

    static StaticField1 au =  new AuxiliaryClass();
    
    public int test() {
        log.info("Start StaticField1 test ...");
        try {
            for (int i=0; i<100; i++) {
                for (int j=0; j<100; j++) {
                    au.inlineMethod();
                }
            }
            if (!v.isEmpty()) return fail("TEST FAILED: " +
                    "StaticField1.v is not empty");
            Integer[] child = (Integer[]) AuxiliaryClass.v
                    .toArray(new Integer[AuxiliaryClass.v.size()]);
            for (int i=0; i<1000; i++) {
                if (child[i].intValue() != 111) 
                    return fail("TEST FAILED: " +
                            "some of AuxiliaryClass.v members has " +
                            "unexpected value");
            }
            return pass();
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }

    void inlineMethod() {
        v.add(new Integer(999));
    }
}

class AuxiliaryClass extends StaticField1 {
    
    static Vector v = new Vector(1);
    
    AuxiliaryClass() {
        StaticField1.au = new StaticField1();
    }
    
    final void inlineMethod() {
        v.add(new Integer(111));
    }
}


