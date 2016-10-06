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
package org.apache.harmony.test.func.reg.vm.btest4040;

import java.io.FileInputStream;
import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

// FIXME:
public class Btest4040 extends RegressionTest {

    public int test(Logger logger, String[] args) {
        int t = 0;
        int step = 10000;
        FileInputStream fis;
        try {
             while (true) {
                 fis = new FileInputStream("src/test/bug4040/Btest4040.java");
                 t++;
                 if ( t >= step ) {
                     System.err.println(t);
                     step  += 10000;
                 }
                 fis.close();
                 if (t >= 300000) {
                     break;
                 }
             }
        } catch (java.io.FileNotFoundException e) {
            return pass();
        } catch (Throwable e) {
            System.err.println("Test failed: unexpected error");
            e.printStackTrace();
            return fail();
            
        }
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new Btest4040().test(Logger.global, args));

    }

}
