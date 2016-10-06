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
package org.apache.harmony.test.func.reg.vm.btest4062;

import java.io.File;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * Neg: VM does not throw ClassFormatError when 
 * there are extra bytes at the end of class file
 *
 */

public class Btest4062 extends RegressionTest {

    public int test(Logger log, String[] args) throws Throwable {
       try {        
            Class cl = Class.forName("org.apache.harmony.test.func.reg.vm.btest4062.Test4062");
            cl.newInstance();
        } catch (ClassFormatError e) {            
            log.info(e.toString());
            return PASSED;
        }

        return FAILED;
    }


    public static void main(String[] args)  throws Throwable {
        System.exit(new Btest4062().test(Logger.global, args));
    }

}

