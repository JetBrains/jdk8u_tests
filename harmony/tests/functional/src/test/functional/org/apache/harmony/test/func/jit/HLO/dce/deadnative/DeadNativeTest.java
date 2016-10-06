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
package org.apache.harmony.test.func.jit.HLO.dce.deadnative;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 31.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class DeadNativeTest extends Test {
    public DeadNativeTest() {
           System.loadLibrary("dcetest");
        }
    public static void main(String[] args) {
          System.exit(new DeadNativeTest().test(args));
    }

    public int test() {
            if(!invoker()) return fail("FAILED");
            return pass();
    }


        public boolean invoker() {
            double d = calc(); //"semi"-dead value
            nativeMethodSetAndStore("" + d);
            log.info("setting to native method " + d);
            String dStr = "" + calc();
            String str = nativeMethodGet();
            log.info("from native method " + str);
            return dStr.equals(str);
        }


       private double calc() {
           double d = 0.0;
           for(int i = 0; i < 200000; ++i) {
             d = 1000000.;
             for(;;) {
                d *= .9;
                if(d < 0.001) break;
             }
           }    
           return d;
       }
       public native void nativeMethodSet(String obj);
       public native void nativeMethodFakeSet(String obj);
       public native void nativeMethodSetAndStore(String obj);
       public native String nativeMethodGet();
       public native void nativeDeleteRef();

}
