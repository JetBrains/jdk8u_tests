/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Vladimir A. Ivanov, Alexander D. Shipilov, Khen G. Kim
 * @version $Revision: 1.6 $
 */

package org.apache.harmony.vts.test.vm.jvms.share;

import java.lang.reflect.Method;

import org.apache.harmony.share.Test;

public class vmVerifierRun extends Test {

    private boolean instantiate = false;
    private boolean run = false;
    private boolean init = false;
    private String className = "";
    private String[] tstArgs;
    private Class expectedEx;
    private Throwable causeEx;
    private Class clVerifyError = null;
    private Class clNoSuchMethodException = null;    

    void parseArgs(String[] params) {
        if (params == null || params.length < 2) {
            throw new IllegalArgumentException("parameters should be defined");
        }
        if ("load".equalsIgnoreCase(params[0])) {
            init = false;
            instantiate = false;
            run = false;
        } else if ("initialize".equalsIgnoreCase(params[0])) {
            init = true;
            instantiate = false;
            run = false;
        } else if ("instantiate".equalsIgnoreCase(params[0])) {
            instantiate = true;
            run = false;
        } else if ("run".equalsIgnoreCase(params[0])) {
            instantiate = true;
            run = true;
        } else {
            throw new IllegalArgumentException("mode should be defined");
        }
        className = params[1];
        if (params.length > 2) {
            try {
                expectedEx = Class.forName(params[2]);
                tstArgs = new String[params.length - 3];
                for (int i = 3; i < params.length; i++) {
                    tstArgs[i - 3] = params[i];
                }
            } catch (Exception e) {
                expectedEx = null;
                tstArgs = new String[params.length - 2];
                for (int i = 2; i < params.length; i++) {
                    tstArgs[i - 2] = params[i];
                }
            }

        } else {
            expectedEx = null;
            tstArgs = null;
        }
    }

    public int test() {
        return fail("bad mode");
    }
    public int test(String[] args) {
        Class cl;
        Class[] param;
        Method m;
        Object obj;

        try {
            log.setLevel(getLogLevel(args));
            removeLogLevel(args);
            parseArgs(testArgs);
        } catch (IllegalArgumentException e) {
            return fail(e.getMessage());
        }
        try {
            if(clVerifyError == null) {
              clVerifyError = Class.forName("java.lang.VerifyError");
            }
            if(clNoSuchMethodException == null) {
               clNoSuchMethodException = Class.forName("java.lang.NoSuchMethodException");
            }
        if(init) {
           cl = Class.forName(className, true, this.getClass().getClassLoader());
        }
            else { 
                cl = Class.forName(className);
            }
            if (instantiate == false && run == false) {
                if (expectedEx == null) {
                    return pass("Load: PASSED without exception");
                }else{
                    return fail("Load: FAILED expected exception is not thrown");
                }
            }
        } catch (ClassNotFoundException e) {
            return fail(e.toString()); // "Load: unexpected ClassNotFoundException exception");
        } catch (Exception e) {
            if (expectedEx != null 
                       && expectedEx.isAssignableFrom(e.getClass())) {
                return pass("Load: PASSED with expected exception " + e);
            }
            return fail("Load: unexpected exception " + e);
        } catch (Error e) {
            if (expectedEx != null 
                    && (expectedEx.isAssignableFrom(e.getClass())  ||
                        clVerifyError.isAssignableFrom(e.getClass()))) {
                return pass("Load: PASSED with expected error " + e);
            }
            return fail("Load: unexpected error " + e);
        }

        try {
            obj = cl.newInstance();
            if (expectedEx == null && run == false) {
                return pass("Instantiate: PASSED without exception");
            }
        } catch (Exception e) {
            if (expectedEx != null 
                       && expectedEx.isAssignableFrom(e.getClass())) {
                return pass("Instantiate: PASSED with expected exception " + e);
            }
            return fail("Instantiate: unexpected exception " + e);
        } catch (Error e) {
            if (expectedEx != null 
                    && (expectedEx.isAssignableFrom(e.getClass()) ||
                        clVerifyError.isAssignableFrom(e.getClass()))) {
                return pass("Instantiate: PASSED with expected error " + e);
            }
            return fail("Instantiate: unexpected error " + e);
        }

        try {
            m = cl.getMethod("test", (Class[])null);
            m.invoke(obj, (Object[])null);
        } catch (Exception e) {
                 causeEx = e.getCause();
            if (causeEx != null && expectedEx != null 
                       && expectedEx.isAssignableFrom(causeEx.getClass())) {
                return pass("Run: PASSED with expected exception " + causeEx);
            }
            else if (expectedEx != null && clNoSuchMethodException.isAssignableFrom(e.getClass())) {
                  return fail("Load: exception expected");
            }
            return fail("Run: unexpected exception " + causeEx +" Cause: " + e);
        } catch (Error e) {
            if (expectedEx != null 
                    && (expectedEx.isAssignableFrom(e.getClass())  ||
                        clVerifyError.isAssignableFrom(e.getClass()))) {
                return pass("Run: PASSED with expected error " + e);
            }
            return fail("Run: unexpected error " + e);
        }
        if (expectedEx != null) {
            return fail("exception expected");
        }
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new vmVerifierRun().test(args));
    }
}