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
package org.apache.harmony.test.func.jit.HLO.dce.deadcatch;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 26.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class DeadCatchTest extends Test {
    
    public static void main(String[] args) {
          System.exit(new DeadCatchTest().test(args));
    }

    public int test() {
            invoker();
            if(check != 1) return fail("FAILED");
            return pass();
    }

        private int check = 0;

        public void invoker() {
           for(int i = 0; i< 20000000; ++i) {
              deadCatchMethod(i);
           }
           for(int i = 0; i< 20000001; ++i) {
              deadCatchMethod(i);
           }
        }

        public void deadCatchMethod(int i) {
           check = 0;
           int j = i;  //"semi"-dead variable
           long l = j; //"semi"-dead variable
           double d = (double) l; //"semi"-dead variable
           double d2 = 0.0; //"semi"-dead variable
           try {
              d2 = l * 0.99;
              if(i > 19999999 ) 
                throw new Exception();
       
           }
           catch(Exception e) {
              log.info("l = " + l + ", d= " + d + ", d2= " + d2);
              if(l == 20000000 && d == 2.0E7 && d2 == 1.98E7 ) {
                check = 1;
              }
           }
       }
}
