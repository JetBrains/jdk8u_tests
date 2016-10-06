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
package org.apache.harmony.test.func.reg.vm.btest5571;

import java.util.TimeZone;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

/** 
 * JVM should provide "user.timezone" system property,
 * by default set to the Java ID of the system timezone.
 * Some implementations of java.util.TimeZone rely on this property.           
 * 
 */
public class UserTimezoneProperty extends RegressionTest {

    public int test(Logger logger, String[] args) {
        String tz = System.getProperty("user.timezone");
        
        logger.info("\"user.timezone\" property value is " + tz);
        if (tz == null) {
            return FAILED;
        }
        
        String defaultTZ = TimeZone.getDefault().getID();
        if (!defaultTZ.equals(tz)) {
            logger.warning("JVM-provided timezone ID does not match " 
                    + " to the one obtained via API: "
                    + tz + " vs. " + defaultTZ);
            //return FAILED;
        }
        
        return PASSED;
    }

    public static void main(String[] args) {
        System.exit(new UserTimezoneProperty().test(Logger.global, args));
    }
}
