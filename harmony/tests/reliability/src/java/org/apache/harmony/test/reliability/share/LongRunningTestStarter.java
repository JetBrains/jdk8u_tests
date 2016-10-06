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

package org.apache.harmony.test.reliability.share;

import java.util.Arrays;
import java.util.Calendar;

import org.apache.harmony.test.reliability.share.Test;
import org.apache.harmony.test.reliability.share.Result;

/**
 * @author Tatyana V. Doubtsova
 * @version $Revision: 1.2 $
 */

public class LongRunningTestStarter extends Test {

    int testTimeOut = 600;  // 10 minutes

    String[] testArgs = null;

    String testClassName = null;
    
    protected int parseArgs(String[] args) {
        
        if (args == null) {
            return 0;            
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-testClassName")){
                testClassName = args[i + 1];
                testArgs = new String[args.length - i - 2];
                
                for (int j = i + 2; j < args.length; ++j) {
                    testArgs[j - i - 2] = args[j];
                }
            }
            if (args[i].equalsIgnoreCase("-testTimeOut")) {
                testTimeOut = Integer.parseInt(args[i + 1]);
            }
        }
        
        if (testClassName == null) {
            log.add("No test to run");
            return 100;
        }

        return 0;
    }
    

    public int test(String args[]) {
        int result = Result.ERROR;

        if (parseArgs(args) != 0) {
            return error("LongRunningTestStarter: Invalid arguments, should be " +
                " -testTimeOut <secs> -testClassName <test's fully qualified class name> " +
                " <optional, test param1> <etc.>");
        }

        long curTime = Calendar.getInstance().getTimeInMillis();
        long startTime = curTime;
        long endTime = curTime + (int) (testTimeOut * 1000 * 0.9f);

        long iterTime = 0;
        int iteration = 0;
        try {
            while (curTime < endTime) {
                result = ((Test) Class.forName(testClassName).newInstance()).test(testArgs);
                System.gc();
                
                if (result != Result.PASS) {
                    log.add("*** iteration: " + iteration);
                    log.add("test result is " + result);
                    log.add("Test FAILED");
                    return result;
                }
                
                curTime = Calendar.getInstance().getTimeInMillis();
                if (iteration == 0) {
                    iterTime = curTime - startTime;
                    long tmp = curTime + (int) (testTimeOut * 1000 - iterTime * 4);
                    endTime = tmp > endTime ? endTime : tmp;
                }
                iteration++;
            }
        } catch (Exception e) {
            log.add("*** iteration: " + iteration);
            log.add("Exception during test execution: " + e);
            e.printStackTrace();
            log.add("Test FAILED");
            return Result.ERROR;
        }
        log.add("*** Total run iterations: " + iteration);
        log.add("Test result is " + result);
        log.add("Test PASSED");
        return result;
    }

    public static void main(String[] args) {
        System.exit(new LongRunningTestStarter().test(args));
    }

}
