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
package org.apache.harmony.test.func.jit.HLO.dce.deadstring;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 20.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class DeadStringTest extends Test {
    
    public static void main(String[] args) {
          System.exit(new DeadStringTest().test(args));
    }

    public int test() {
        
            String valueToCheck = "dead string";
            String s = invoker();
        if(valueToCheck.substring(1).equals("ead string")) {
           return pass();
            }
            return fail("FAILED");
    }
    //in this method other method is invoked result of which is never used
    private String invoker() {
            for(int i=0; i<20000000; i++) {
               String s = deadStringMethod(5);
        }
        return "some other string";
    }
    //performs some operation on String
    private String deadStringMethod(int par) {
       String s = "     dead string";
       s = s.substring(par);
       return s;
    }

    
    
}
