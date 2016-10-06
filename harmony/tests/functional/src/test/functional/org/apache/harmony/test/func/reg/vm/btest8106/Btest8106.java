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
package org.apache.harmony.test.func.reg.vm.btest8106;

import org.apache.harmony.test.share.reg.RegressionTest;
import java.util.logging.Logger;

public class Btest8106 extends RegressionTest {

    class Test8106 extends java.lang.Thread {
        public void run () {
            long nn = 0x7FFFFFFFFFFFFFFL;
            System.out.println("Long sleep time = " + nn);
            try{
                Thread.sleep(nn);
            } catch (InterruptedException e){
            }
        }
    }
        
    public static void main(String[] args) {
        System.exit(new Btest8106().test(Logger.global, args));
    }
    
    public int test (final Logger logger, String[] args){
        Thread t = new Test8106();
        t.start();
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
        }
        t.interrupt();
        try{
            t.join();
        } catch (Exception e){
            return fail();
        }
        return pass();
   }
}
