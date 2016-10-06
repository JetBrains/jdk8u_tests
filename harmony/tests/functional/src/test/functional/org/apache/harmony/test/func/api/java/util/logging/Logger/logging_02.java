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

package org.apache.harmony.test.func.api.java.util.logging.Logger;


import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.util.Locale;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;



class myLogger extends Logger {
    public myLogger (String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }
}

public class logging_02 extends MultiCase{
    public static void main(String[] args) {
        System.exit(new logging_02().test(args));
    }

    public Result testInitLogging () {
        String loggerName = "org.apache.harmony.test.func.api.java.util.logging.Logger.logging_02";
        //String bundleName = "org.apache.harmony.test.func.api.java.util.logging.Logger.logging_02";
        String bundleName = "org.apache.harmony.test.func.api.java.util.logging.Logger.someResources";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, Locale.US);
        
        Logger logger = new myLogger(loggerName, bundleName);
         
        return passed();    
    }
}
