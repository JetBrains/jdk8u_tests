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
 * Goal: check that Runtime.exec() / VM process big number of big-size runtime properties (-D<property>)
 * 
 * The test does:
 *
 * 1. Reads parameters, which are:
 *      1) args[0] - number of iterations and maximum number of properties and muliplied by
 *         'Multiplier' (typically 10) is a maximum size of property (number of chars).
 *      2) args[1] - path to VM executable file
 *      3) args[2] - VM options separated by 'VM_OPTS_SEPARATOR' (typically, ":").
 *
 * 2. Creates an array of VM options to iterate through.
 *
 * 3. Creates arrays of arrays of properties.
 *    Size of the array is args[0]
 *    Each array's size is form 1 to args[0]
 *    Maximum size of property/array element is from 1 to args[0] * 10.
 *
 * 4. Iterates through VM options and for each VM option iterates through array of properties.
 *
 * 5. On each iteration invokes Runtime.exec() with command line to start VM with option, and properties
 *   run App class and pass 1 parameter. Typically, the command to execute looks like:
 *   <vm> <vm option> <-Dproperty>..<-Dproperty> App <param (is equal to the number of passed properties)>
 *
 */


public class VMCLPropertiesTest extends Test {

    static Random r = new Random(10);
     
    public static final int EXIT_VALUE = 0xFFFFF;

    String vmPath = "";

    String vmOpts = "";

    String classPath = "";

    String appName = "org.apache.harmony.test.reliability.api.kernel.args.AppProp";

    int N_OF_ITERATIONS = 10;

    int Multiplier = 10;

    String VM_OPTS_SEPARATOR = ":";

    public static String PROPERTY_LITERAL = "-D";

    public static String PROPERTY_PREFIX = "prop";


    public static int ERROR_NO_ARGS = -100000;

    public static void main(String[] args) {
        System.exit(new VMCLPropertiesTest().test(args));
    }


    public int test(String[] params) {

        boolean failed = false;
    
        parseParams(params);

        String[][] vmOptions = createVMOptsSets(vmOpts, classPath);

        String[][] vmProperties = createVmPropertiesSets();

        String[][] appParams = createAppParamsSets(vmProperties);
            
        for (int i = 0; i < vmOptions.length; ++i) {

            for (int j = 0; j < vmProperties.length; ++j) {

                String[] cmdLine = createCmdLine(vmPath, vmOptions[i], vmProperties[j], appName, appParams[j]);

                // for (int x = 0; x < cmdLine.length; ++x) {
                // log.add(cmdLine[x] + " ");
                // }
                // log.add("");

                try {

                    Process p = Runtime.getRuntime().exec(cmdLine);

                    p.waitFor();
                    int toRead = p.getInputStream().available();
                    byte[] out = new byte[toRead];
                    p.getInputStream().read(out, 0, toRead);
                    // log.add(new String(out));
                    toRead = p.getErrorStream().available();
                    out = new byte[toRead];
                    p.getErrorStream().read(out, 0, toRead);
                    // log.add(new String(out));

                    if (p.exitValue() != 0 && p.exitValue() != vmProperties[j].length) {
                        log.add("Started process returned unexpected exit code (problem?) " + p.exitValue() +
                            " instead of " + vmProperties[j].length); 
                        failed = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.add("Iteration: vm args index is " + i + ", properties index is " + j);
                    log.add("\n" + "VM argument: " + vmOptions[i][0] + "\n" +
                        "VM properties number is "  + vmProperties[j].length + 
                        "\n" + "maximum length of the last property string is " + 
                        (getStringSize(vmProperties[j].length + 1) + 1) + " chars");
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


    String[] createCmdLine(String vmPath, String[] vmOptions, String[] vmProperties, String appName, String[] appParams) {

        String[] cmdLine = new String[1 + vmOptions.length + vmProperties.length + 1 + appParams.length];

        // cmdLine should be: <vm> <vm option> <vm properties> App <param> .. <param>

        cmdLine[0] = vmPath;
        System.arraycopy(vmOptions, 0, cmdLine, 1, vmOptions.length);

        System.arraycopy(vmProperties, 0, cmdLine, vmOptions.length + 1, vmProperties.length);

        cmdLine[vmOptions.length + vmProperties.length + 1] = appName;

        System.arraycopy(appParams, 0, cmdLine, 1 + vmOptions.length + vmProperties.length + 1, appParams.length);

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

    String[][] createAppParamsSets(String[][] vmProperties) {
        
        String[][] appParams = new String[vmProperties.length][];

        for (int i = 0; i < appParams.length; ++i) {

            appParams[i] = new String[1];
            appParams[i][0] = "" + vmProperties[i].length;

        }

        return appParams;
    }

    String[][] createVmPropertiesSets() {
        
        String[][] vmProps = new String[N_OF_ITERATIONS][];

        for (int i = 0; i < vmProps.length; ++i) {

            vmProps[i] = createRndStrArray(i + 1);

        }

        return vmProps;
    }


    String[] createRndStrArray(int length) {

        String[] s = new String[length];

        for (int i = 0; i < s.length; ++i) {
            s[i] = PROPERTY_LITERAL + PROPERTY_PREFIX + i + "=\"" + createRndStr(getStringSize(i) + 1) + "\"";
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

class AppProp {

    public static void main(String[] args) {

        if (args.length == 0) {

            System.exit(100);

        }

        int i = Integer.parseInt(args[0]);

        for (int j = 0; j < i; ++j) {

            // System.out.println(System.getProperty(VMCLPropertiesTest.PROPERTY_PREFIX + j));

            if (System.getProperty("-Dprop" + j) == null) {

                System.exit(-1 * j);

            }

        }

        System.exit(i);

    }

}


