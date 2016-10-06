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
package org.apache.harmony.test.func.reg.vm.btest4770;

import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

/** 
 * class name is missed in NoClassDefFoundError detail message
 * 
 */
public class ResolutionErrorMessage extends RegressionTest {

    public int test(Logger log, String[] args) {
        try {
            new Missing();
            log.warning("Configuration error: "
                    + "class btest4770.Missing should not be in classpath");
            return ERROR;
       } catch (NoClassDefFoundError e) {
           String errMsg = e.getMessage();
           if (errMsg != null && 
                   (errMsg.indexOf("btest4770/Missing") != -1 
                            || errMsg.indexOf("btest4770.Missing") != -1)) {
               return PASSED;
           }

           log.info("Class name is missed in the error message: " + e);
           return FAILED;
       } catch (Throwable t) {
           log.warning("Unexpected exception: " + t);
           t.printStackTrace();
           return ERROR;
       }
    }
    
    public static void main(String[] args) {
        System.exit(new ResolutionErrorMessage().test(Logger.global, args));
    }
}

class Missing {}
