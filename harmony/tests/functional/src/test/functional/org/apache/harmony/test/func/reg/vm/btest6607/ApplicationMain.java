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
package org.apache.harmony.test.func.reg.vm.btest6607;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class ApplicationMain extends MultiCase {
    
    static Logger log = Logger.global;
    private static String[] args; 
    private String errorStr;
    
    private Result run(String test, boolean positive, String errMsg) {
        try {
            log.info("-------------- " + test + " ---------------");
            String[] cmd = new String[] {
                    args[0], 
                    "-cp", 
                    System.getProperty("java.class.path"),
                    test};
            Process proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();
            int res = proc.exitValue();
            catchOut(proc);
            if (positive && res == 0 && catchErr(proc, null)
                || !positive && res != 0 && catchErr(proc, "NoSuchMethod")) {
                return passed();
            } else {
                return failed(errMsg);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return new Result(Result.ERROR, "Unexpected exception: " + t);
        } finally {
            log.info("---------------------------------------------");
        }
    }
    
    private void catchOut(Process process) {
        log.info(new String(readStream(process.getInputStream())));        
    }
    
    private boolean catchErr(Process process, String expected) {
        byte [] errorOutput = readStream(process.getErrorStream());
        errorStr = new String(errorOutput);
        log.info(errorStr);

        if (expected != null) {
            return (errorStr.indexOf(expected) >= 0); 
        } else {
            return errorStr.trim().length() == 0;
        }
    }
    
    private byte[] readStream(InputStream stream1) {
        byte [] out = new byte[0];
        try {
            for (int available = stream1.available(); available != 0; available = stream1.available()){
                byte[] tmp = new byte [available + out.length];
                System.arraycopy(out, 0, tmp, 0, out.length);
                stream1.read(tmp, out.length, available);
                out = tmp;
            }
        } catch (IOException e) {
            log.info("IOException during stream reading: " + e);
            e.printStackTrace();
        }
        return out;
    }

    public Result test0() {
        return run("org.apache.harmony.test.func.reg.vm.btest6607.ValidMain", true, "Non-public application class");
    }
    
    public Result test1() {
        Result res = run("org.apache.harmony.test.func.reg.vm.btest6607.InvalidMain1", false, "Non-public method main");
        // workaround for inconsistent error message in RI
        errorStr = errorStr.toLowerCase();
        if (res.getResult() == Result.FAIL 
                && errorStr.indexOf("public")!= -1 
                && errorStr.indexOf("method")!= -1
                && errorStr.indexOf("main")!= -1){
            return passed();
        }
        return res;
    }

    public Result test2() {
        return run("org.apache.harmony.test.func.reg.vm.btest6607.InvalidMain2", false, "Non-static method main");
    }

    public Result test3() {
        return run("org.apache.harmony.test.func.reg.vm.btest6607.InvalidMain3", false, "Non-void method main");
    }

    public Result test4() {
        return run("org.apache.harmony.test.func.reg.vm.btest6607.ValidMain2", true, "Other modifiers in main");
    }
    
    public Result test5() {
        return run("org.apache.harmony.test.func.reg.vm.btest6607.ValidMain3", true, "Inherited method main");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(
                   "Misconfiguration: the TestedRuntime is expected as first argument");
            System.exit(Result.ERROR);
        }
        ApplicationMain.args = args;
        System.exit(new ApplicationMain().test(args));
    }
}

class ValidMain {
    public static void main(String[] args) {}
}

class ValidMain2 {
    public static final synchronized strictfp void main(String[] args) {}
}

class ValidMain3 extends ValidMain {
}

class InvalidMain1 {
    protected static void main(String[] args) {}
}

class InvalidMain2 {
    public void main(String[] args) {}
}

class InvalidMain3 {
    public static int main(String[] args) {
        return 123;
    }
}