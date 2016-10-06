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
package org.apache.harmony.test.func.jit.HLO.uce.unreachfunc;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 24.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class UnreachFuncTest extends Test {
    
    public static void main(String[] args) {
          System.exit(new UnreachFuncTest().test(args));
    }

    public int test() {
            int i = 0;
        invoker(i);
            return pass();
    }
    //this method contains unreachable code
    private int invoker(int par) {
        int val = par;
            for(int i=0; i<20000000; i++) {
               if(par==1) { //unreachable code is going here
                 val+= unreachMethod();
                 val+= unreachMethod2();
                 val+= unreachMethod3(3);
               }

        }
        return val;
    }

    //performs some operation on int
    private int unreachMethod() {
        int i = 0;
        i++;
        return i;
    }
    
    private int unreachMethod2() {
        int i = 10;
        i++;
        return i;
    }

    private int unreachMethod3(int valueToCheck) {
        int i = valueToCheck;
        i++;
        return i;
    }

}
