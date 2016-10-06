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
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.reliability.api.kernel.args;

import org.apache.harmony.test.reliability.share.Test;

import java.util.Random;


/**
 * Goal: check that Runtime.exec() / VM process big number of big application arguments
 * 
 * The test does:
 *
 * 1. Reads parameters, which are:
 *      1) args[0] - number of iterations and maximum number of arguments and muliplied by
 *         'Multiplier' (typically 10) is a maximum size of an argument (number of chars).
 *      2) args[1] - path to VM executable file
 *      3) args[2] - VM options separated by 'VM_OPTS_SEPARATOR' (typically, ":").
 *
 * 2. Creates an array of VM options to iterate through.
 *
 * 3. Creates arrays of arrays of application parameters.
 *    Size of the array is args[0]
 *    Each array's size is form 1 to args[0]
 *    Maximum size of application parameter/array element is from 10 to args[0] * 10.
 *
 * 4. Iterates through VM opetions and for each VM option iterates through array of application
 *    parameters.
 *
 * 5. On each iteration invokes Runtime.exec() with command line to start VM with option, run App
 *   class and pass application parameters. Typically, the command to execute looks like:
 *   <vm> <vm option> App <param> .. <param>
 *
 */


public class AppVMCLParamTest extends Test {

    static Random r = new Random(10);
     
    public static final int EXIT_VALUE = 0xFFFFF;

    String vmPath = "";

    String vmOpts = "";

    String classPath = "";

    String appName = "org.apache.harmony.test.reliability.api.kernel.args.AppParam";

    int N_OF_ITERATIONS = 10;

    int Multiplier = 10;

    String VM_OPTS_SEPARATOR = ":";


    public static void main(String[] args) {
        System.exit(new AppVMCLParamTest().test(args));
    }


    public int test(String[] params) {

        boolean failed = false;
    
        parseParams(params);

        String[][] vmOptions = createVMOptsSets(vmOpts, classPath);

        String[][] appParams = createAppParamsSets();
            
        for (int i = 0; i < vmOptions.length; ++i) {

            for (int j = 0; j < appParams.length; ++j) {

                String[] cmdLine = createCmdLine(vmPath, vmOptions[i], appName, appParams[j]);

                //for (int x = 0; x < cmdLine.length; ++x) {
                //      log.add(" " + cmdLine[x]);
                //}
                //log.add("");

                try {

                    Process p = Runtime.getRuntime().exec(cmdLine);

                    p.waitFor();

                    if (p.exitValue() != 0 && p.exitValue() != appParams[j].length + 1) {
                        log.add("Started process returned unexpected exit code (problem?) " + p.exitValue() +
                            " instead of " + (appParams[j].length + 1)); 
                        failed = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.add("Iteration: vm args index is " + i + ", application args index is " + j);
                    log.add("\n" + "VM argument: " + vmOptions[i][0] + "\n" +
                        "application arguments number is "  + appParams[j].length + 
                        "\n" + "maximum length of the last argument string is " + 
                        (getStringSize(appParams[j].length + 1) + 1) + " chars");
                    return fail("Failed");
                }
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
            vmOpts =  params[2];
        }        

        if (params.length >= 4) {
            classPath =  params[3];
        }        

    }


    String[] createCmdLine(String vmPath, String[] vmOptions, String appName, String[] appParams) {

        String[] cmdLine = new String[1 + vmOptions.length + 1 + 1 + appParams.length];

        // cmdLine should be: <vm> <vm option> App <number_of_params> <param> .. <param>

        cmdLine[0] = vmPath;
        System.arraycopy(vmOptions, 0, cmdLine, 1, vmOptions.length);

        cmdLine[vmOptions.length + 1] = appName;
        cmdLine[vmOptions.length + 1 + 1] = "" + (appParams.length + 1);

        System.arraycopy(appParams, 0, cmdLine, 1 + vmOptions.length + 1 + 1, appParams.length);

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

    String[][] createAppParamsSets() {
        
        String[][] appParams = new String[N_OF_ITERATIONS][];

        for (int i = 0; i < appParams.length; ++i) {

            appParams[i] = createRndStrArray(i + 1);

        }

        return appParams;
    }


    String[] createRndStrArray(int length) {

        String[] s = new String[length];

        for (int i = 0; i < s.length; ++i) {
            s[i] = "\"" + createRndStr(getStringSize(i) + 1) + "\"";
        }
        return s;
    }


    String createRndStr(int length) {

        char[] ch = new char[length];

        for (int i = 0; i < length; ++i) {
                 
            while (true) {
                char c = (char) r.nextInt(0xffff);
                if (Character.isJavaIdentifierPart(c)) {
                    ch[i] = c;
                    break;
                }
            }

        }

        return new String(ch);
    }


    String[][] createVMOptsSets(String vmOpts, String classPath) {

        // we do not create cobination of several vm options, just, one option at each run
        // first, extract options from input parameter:

        String[] vmopts = vmOpts.split(VM_OPTS_SEPARATOR);

        String[][] s = new String[vmopts.length][];

        for (int i = 0; i < s.length; ++i){
            s[i] = new String[3];
            s[i][0] = "-classpath";
            s[i][1] = classPath; 
            s[i][2] = vmopts[i];
        }

        return s;
    }


    int getStringSize(int size) {
        return size * Multiplier;
    }
}



class AppParam {

    public static void main(String[] args) {

        int retValue = -10;

        if (args.length == 0) {

            retValue = -1;

        } else {

            retValue = args.length;

        }

        System.exit(retValue);
    }

}

