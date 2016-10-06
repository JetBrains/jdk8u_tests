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
 * Created on 07.09.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.ErrorManager;


import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

import java.util.logging.ErrorManager;
import java.util.logging.Logger;

public class ErrorManagerTest extends MultiCase{
    public final int[] fields = {
        ErrorManager.CLOSE_FAILURE,
        ErrorManager.FLUSH_FAILURE,
        ErrorManager.FORMAT_FAILURE, 
        ErrorManager.GENERIC_FAILURE, 
        ErrorManager.OPEN_FAILURE,
        ErrorManager.WRITE_FAILURE, 
    };
    public final int[] values = {
            3,
            2,
            5,
            0,
            4,
            1
    };
    
    public static void main(String[] args) {
        System.exit(new ErrorManagerTest().test(args));
    }

    
    public Result testErrorManager() {
        Logger logger = Logger.getLogger("ErrorManagerLogger");
        ErrorManager manager = new ErrorManager();
        for (int i = 0; i<fields.length; i++) {
            if (values[i] != fields[i]) {
                return failed ("Field " + fields[i] + " should be equals to " + i);
            }
        }
        return passed();
    }

}