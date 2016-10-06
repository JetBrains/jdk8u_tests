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
 * @author Oleg Oleinik
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.kernel.args;

import org.apache.harmony.test.reliability.share.Test;

import java.util.Random;

/**
 * Goal: check that Runtime.exec() passes big number of big-size environment variables
 * 
 * The test does:
 *
 * 1. Reads parameters, which are:
 *      1) args[0] - number of iterations and maximum number of environment variables; muliplied by
 *         'Multiplier' (typically 10) it is maximum size of enviroment variable value (number of chars).
 *      2) args[1] - path to VM executable file
 *
 * 3. Creates arrays of arrays of environment variables.
 *    Size of the array is args[0]
 *    Each array's array size is form 1 to args[0]
 *    Maximum size of environment variable/array element is from 1 to args[0] * 10 + 1.
 *
 * 4. Iterates through array of environemnt variables.
 *
 * 5. On each iteration invokes Runtime.exec(String[] cmd, String env) with command line to start 
 *    VM along with a number of environment variables and EnvApp class and 1 parameter, which is
 *    number of set environment variables. Typically, the command to execute looks like:
 *   <vm> App <param (is equal to the number of passed environment variables)>
 *
 */


public class ExecEnvTest extends Test {

    static Random r = new Random(10);
     
    String vmPath = "";

    String classPath = "";

    String appName = "org.apache.harmony.test.reliability.api.kernel.args.EnvApp";

    static String ENV_PREFIX = "env";

    int N_OF_ITERATIONS = 10;

    static int Multiplier = 10;

    public static final int ERROR_NO_ARGS = 0xFFFF;


    public static void main(String[] args) {
        System.exit(new ExecEnvTest().test(args));
    }


    public int test(String[] params) {

        boolean failed = false;
    
        parseParams(params);

        // environment variables to start a process with

        String[][] env = createEnvSets();

        // parameters to pass to EnvApp on each iteration

        String[][] appParams = createAppParamsSets(env);
            
        // for each set of environment variables:
        String[] s = new String[2];

        s[0] = "-classpath";
        s[1] = classPath; 

        for (int i = 0; i < env.length; ++i) {

            String[] cmdLine = createCmdLine(vmPath, s, appName, appParams[i]);

            // for (int x = 0; x < cmdLine.length; ++x) {
            //       log.add(" " + cmdLine[x]);
            // }
            // log.add("");

            // for (int x = 0; x < env[i].length; ++x) {
            //       log.add(" " + env[i][x]);
            // }
            // log.add("");

            try {

                // start VM and EnvApp along with next environment variable set

                Process p = Runtime.getRuntime().exec(cmdLine, env[i]);

                p.waitFor();
 
                if (p.exitValue() != 0 && p.exitValue() != env[i].length) {
                    log.add("Started process returned unexpected exit code " + p.exitValue() +
                        " instead of " + env[i].length); 
                    failed = true;
                }

                // System.out.println("Exit value: " + p.exitValue());


            } catch (Exception e) {
                e.printStackTrace();
                log.add("Iteration: env index is " + i);
                log.add("env variables number is "  + env[i].length + 
                    "\n" + "maximum length of the last env string is " + 
                    (getStringSize(env[i].length + 1) + 1) + " chars");
                return fail("Failed");
            }
        }

        if (failed) {
            return fail("Failed");
        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            N_OF_ITERATIONS = Integer.parseInt(params[0]);
        }        

        if (params.length >= 2) {
            vmPath = params[1];
        }        
        if (params.length >= 3) {
            classPath =  params[2];
        }        

    }

    String[] createCmdLine(String vmPath, String[] vmOptions, String appName, String[] appParams) {

        String[] cmdLine = new String[1 + vmOptions.length +  1 + appParams.length];

        // cmdLine should be: <vm> <vm option> <vm properties> App <param> .. <param>

        cmdLine[0] = vmPath;
        System.arraycopy(vmOptions, 0, cmdLine, 1, vmOptions.length);

        cmdLine[vmOptions.length + 1] = appName;

        System.arraycopy(appParams, 0, cmdLine, 1 + vmOptions.length + 1, appParams.length);

        return removeEmptyStrings(cmdLine);

    }


    String[] removeEmptyStrings(String[] s){
        int j = 0;
        String[] ss = new String[s.length];

        for (int i = 0; i < s.length; ++i){
            if ("".equals(s[i])) {
                continue;
            }
            ss[j++] = s[i];
        }

        String[] str = new String[j];

        System.arraycopy(ss, 0, str, 0, str.length);

        return str;
    }


    String[][] createEnvSets() {
            
        String[][] env = new String[N_OF_ITERATIONS][];

        for (int i = 0; i < env.length; ++i) {

            env[i] = createRndStrArray(i + 1);

        }
        // returned array is:
        // {"env0=x"}
        // {"env0=y", .. , "env2=v..v"};  v..v is 21 random chars
        //  ..
        // {"env0=z", .. , "env<N_OF_ITERATIONS>=w..w"}; w..w is (N_OF_ITERATIONS * 10 + 1) random chars 

        return env;

    }

    String[][] createAppParamsSets(String[][] env) {
        
        String[][] appParams = new String[env.length][];

        for (int i = 0; i < appParams.length; ++i) {

            appParams[i] = new String[1];
            appParams[i][0] = "" + env[i].length;

        }

        return appParams;
    }

    String[] createRndStrArray(int length) {

        String[] s = new String[length];

        for (int i = 0; i < s.length; ++i) {
            s[i] = ENV_PREFIX + i + "=\"" + createRndStr(getStringSize(i) + 1) + "\"";
        }
        return s;
    }


    String createRndStr(int length) {

        char[] ch = new char[length];

        for (int i = 0; i < length; ++i) {
                 
            while (true) {

                // looking for ASCII chars only:

                char c = (char)((char) r.nextInt(90) + ((char) 33));
                if (Character.isJavaIdentifierPart(c)) {
                    ch[i] = c;
                    break;
                }
            }

        }

        return new String(ch);
    }


    public static int getStringSize(int size) {
        return size * Multiplier;
    }
}


class EnvApp {

    public static void main(String[] args) {

        // first, check whether the parameter is passed into the EnvApp:
        if (args.length == 0) {

            System.exit(100);

        }

        // second, read the parameter which must be a number of passed environment variables:

        int i = Integer.parseInt(args[0]);

        for (int j = 0; j < i; ++j) {

            //String env = System.getenv(ExecEnvTest.ENV_PREFIX + j);
            String env = System.getProperty("-Dprop" + j);

            // third, check that each of the environment variable is in place

            if (env == null) {

                System.exit(-1 * j);

            }

            // finally, variable's length is as expected

            if (env.length() != (ExecEnvTest.getStringSize(j) + 3)) {

                System.exit(-10 * env.length());

            }

        }

        System.exit(i);

    }

}
