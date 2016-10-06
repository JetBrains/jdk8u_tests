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

package org.apache.harmony.test.share.reg;

/* ------------------------------------------------------------------------- */

public class RefRegressionRunner extends DRLRegressionRunner {

    /* 
     * ~-parameters analize
     */
    protected boolean setOption(String option, CrashTest test) {
        try {
            if (HLOMap.containsKey((Object) HLOMap.getKey(option)) ||
                    option.startsWith("~verifyLevel=") ||
                    option.equals("~jet") || option.equals("~opt") ||
                    option.equals("~server") || option.equals("client") ||
                    option.equals("~server_static")) {
                   result.setOutMsg("WARNING! Option " + option + 
                           " is not supported for the current runner!");                
                return true;
            } else {
                return super.setOption(option, test);
            }
        } catch (IndexOutOfBoundsException e) {
               result.setOutMsg("Unexpected exception: " + e);
        }
        
        return false;
    }

}
