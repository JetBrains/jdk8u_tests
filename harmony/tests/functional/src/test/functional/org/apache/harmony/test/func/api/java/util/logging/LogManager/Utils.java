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
/**
 */

package org.apache.harmony.test.func.api.java.util.logging.LogManager;

import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.LogManager;

import org.apache.harmony.share.Result;

/*
 * 25.10.2005
 */
class Utils {

    public static LogManager getLogManager() {
        LogManager lm = LogManager.getLogManager();
        lm.reset();
        return lm;
    }

    static int verifyLogManager(Properties props) {
        LogManager lm = LogManager.getLogManager();
        Enumeration e = props.propertyNames();
        boolean failed = false;
        while (e.hasMoreElements()) {
            String elem = (String) e.nextElement();
            if (!props.getProperty(elem).equals(lm.getProperty(elem))) {
                failed = true;
                System.err.println(elem + ": " + props.getProperty(elem) + " != "
                        + lm.getProperty(elem));
            }
        }

        return failed ? Result.FAIL : Result.PASS;
    }
}
