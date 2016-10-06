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
/*
 * Created on 30.08.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.LoggingPermission;



import java.util.logging.LoggingPermission;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;



public class LoggingPermissionTest extends MultiCase{
    
    public static void main(String[] args) {
        System.exit(new LoggingPermissionTest().test(args));
    }

    public Result testLoggingPermission()  {
        LoggingPermission permission1 = new LoggingPermission("control", null);
        LoggingPermission permission2 = new LoggingPermission("control", "");
        try {
            LoggingPermission permission3 = new LoggingPermission("check", null);
            LoggingPermission permission4 = new LoggingPermission("control", "notEmptyString");
            return failed ("LoggingPermission does'n throw IllegalArgumentException!");
        }
        catch (IllegalArgumentException e) {
            
        }
 
        return passed();
    }
        
}