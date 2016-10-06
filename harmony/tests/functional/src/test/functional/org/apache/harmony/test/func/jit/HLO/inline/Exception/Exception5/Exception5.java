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
package org.apache.harmony.test.func.jit.HLO.inline.Exception.Exception5;

import java.util.Vector;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 06.09.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Exception5 extends Test {
        
    static Vector v;
    
    public static void main(String[] args) {
        System.exit(new Exception5().test(args));
    }

    public int test() {
        log.info("Start Exception5 test...");
        int flag = 0;
        try {
            for (int i=0; i<10000; i++) {
                Exception5.inlineMethod(null);
            }
            return fail("TEST FAILED: excpected exception wasn't thrown");
        } catch (Exception e) {
            return pass();
        }        
    }
            
    static void inlineMethod(Object obj ) {
        try {
            try {
            
            } finally {
                Class clazz = obj.getClass();
            }
        } finally { 
            try {
                v = new Vector(-1);
            } catch (Exception e) {
            
            }
        }
    }
    
}
