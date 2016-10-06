/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** 
 * @author Tatyana V. Doubtsova
 * @version $Revision: 1.2 $
 */  
package org.apache.harmony.test.reliability.share;

import org.apache.harmony.test.reliability.share.Test;
import org.apache.harmony.test.reliability.share.Result;

public class ReliabilityTestStarter extends Test{
    
    String testReliabilityMode = "longrunning";
    int testReliabilityModePosition = -1;
    
    protected int parseArgs(String[] args) {
        if (args == null) {
            return 0;            
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-testReliabilityMode")) {
                testReliabilityModePosition = i;
                testReliabilityMode = args[i + 1];
            }
        }
        return 0;
    }

    public int test(String args[]) {
        int result = Result.ERROR;

        parseArgs(args);

        if (testReliabilityMode.equalsIgnoreCase("longrunning")) {
            return new LongRunningTestStarter().test(args);
        }

        //if (testReliabilityMode.equalsIgnoreCase("monitor")) {
        //    return new ResourceMonitorTestStarter().test(args);            
        //}
        
        log.add("ReliabilityTestStarter: Unknown Reliability Mode " + testReliabilityMode +
            ", should be \"longrunning\" ");
        return Result.ERROR;
    }

    public static void main(String[] args) {
        System.exit(new ReliabilityTestStarter().test(args));
    }

}
