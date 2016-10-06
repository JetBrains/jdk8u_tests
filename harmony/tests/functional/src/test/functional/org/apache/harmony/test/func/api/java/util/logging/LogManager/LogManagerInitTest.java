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

package org.apache.harmony.test.func.api.java.util.logging.LogManager;


import java.util.logging.LogManager;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;



class myLogManager extends LogManager {
    public myLogManager () {
        super();
    }
}

public class LogManagerInitTest extends MultiCase{
    public static void main(String[] args) {
        System.exit(new LogManagerInitTest().test(args));
    }

    public Result testInitLogManager () {
        
        LogManager lm = new myLogManager();
         
        lm.getProperty("config");
        
        if (!lm.getClass().getName()
                .equals("org.apache.harmony.test.func.api.java.util.logging.LogManager.myLogManager")) {
            
            return failed ("Wrong class is returned: " + lm.getClass().getName()); 
        }
        return passed();    
    }
}
