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
package org.apache.harmony.test.func.jit.HLO.dce.timetest;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 20.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class TimeTest extends Test {
    
    public static void main(String[] args) {
          System.exit(new TimeTest().test(args));
    }

    public int test() {

        long time = System.currentTimeMillis(); 
        int i = invoker();
            long curTime = System.currentTimeMillis();
            log.info("Time 1, spent: " + (curTime - time));

            time = System.currentTimeMillis(); 
        i = invoker();
            curTime = System.currentTimeMillis();
            log.info("Time 2, spent: " + (curTime - time));
        if((curTime - time) < 1500 ) {
           return pass();
            }
            return fail("FAILED");
    }

    private int invoker() {
          for(int i=0; i< 20000000; ++i) {
             double d = calc(); //dead code
          }
          return 2;

    }
    //"dead" calculations
    private double calc() {
            double d = 10000000.;
            for(;;) {
               d *= .9;
               if(d < 0.001) break;
            }
            return d;
    }
}
