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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.Util;

/* ------------------------------------------------------------------------- */

public class RegressionRunner extends ExecUnit {

    private final String classID = "RegressionRunner";
    
    static final String CMD = "toRun";
    
    static final String CRASH_COMMAND = "crash";
    static final int CRASH_MODE = 1;
    
    static final String NEG_CRASH_COMMAND = "negcrash";
    static final int NEG_CRASH_MODE = 2;

    static final String HANG_COMMAND = "hang";
    static final int HANG_MODE = 3;

    static final String ECHO_COMMAND = "echo";
    static final int ECHO_MODE = 4;

    static final String RUN_COMMAND = "run";
    static final int RUN_MODE = 5;

    static final String RUN104_COMMAND = "run104";
    static final int RUN104_MODE = 6;

    protected int testMode = -1;

    protected File              runDirectory = null;
    protected String[]          envp;
    
    protected String generalVMOptions = decode("generalVMOption");
    protected String testTimeout;

    /* --------------------------------------------------------------------- */

    public String[] parseParamOther(String[] args) {
        String [] tmpStore;
        String [] cmd;
        int cmdstart = 1;
        
        if ((args != null) && (args.length > 2)) {
            if (args[0].equalsIgnoreCase(CMD)) {
                cmdstart = 2;
                if (args[1].equalsIgnoreCase(CRASH_COMMAND)) {
                    testMode = CRASH_MODE;
                } else if (args[1].equalsIgnoreCase(NEG_CRASH_COMMAND)) {
                    testMode = NEG_CRASH_MODE;            
                } else if (args[1].equalsIgnoreCase(HANG_COMMAND)) {
                    testMode = HANG_MODE;            
                } else if (args[1].equalsIgnoreCase(RUN_COMMAND)) {
                    testMode = RUN_MODE;
                } else if (args[1].equalsIgnoreCase(RUN104_COMMAND)) {
                    testMode = RUN104_MODE;
                } else {
                    result.setOutMsg("Please, set arguments for the test!");
                    return null;
                }
            } else if (args[0].equalsIgnoreCase(ECHO_COMMAND)) {
                testMode = ECHO_MODE;            
            } else {
                result.setOutMsg("Unsupported test execution mode: " + args[0]);
                return null;
            }
        } else {
            result.setOutMsg("Please, set execution mode for the test!");
            return null;
        }
        
        tmpStore = new String[args.length - cmdstart];
        for (int i = cmdstart, j = 0; i < args.length; i++, j++) {
            tmpStore[j] = args[i];
        }

        cmd = subsEnvToValue(tmpStore);
        runDirectory = getRunDirectory(cmd);
        envp = getEnviroment(cmd);
        return clearServiceCommand(cmd);
    }

    public int runOther(String[] args) {    
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\trunOther(): ";
        
        int execStatus;
        String [] cmdarray = parseParamOther(args);
        
        if (cmdarray == null) {
            return cfg.getRepError()[0];
        }

        PipedOutputStream stream = new PipedOutputStream();
        PrintStream ps = new PrintStream(stream);
        (new THRedirector(stream, result)).start();
        try {
            synchronized (this) {
                wait(50);
            }
        } catch (Exception e) {};
        
        switch (testMode) {
        case CRASH_MODE:
            execStatus = crash_command(cmdarray, envp, runDirectory, ps);
            break;
        case NEG_CRASH_MODE:
            execStatus = neg_crash_command(cmdarray, envp, runDirectory, ps);
            break;
        case HANG_MODE:
            execStatus = hang_command(cmdarray, envp, runDirectory, ps);
            break;
        case ECHO_MODE:
            return echo_command(cmdarray);
        case RUN_MODE:
            execStatus = run_command(cmdarray, envp, runDirectory, ps);
            break;
        case RUN104_MODE:
            execStatus = neg_crash_command(getCrashArgs(cmdarray), envp, runDirectory, ps);
            break;
        default: 
             return cfg.getRepError()[0];
        };
        
        try {
            synchronized (this) {
                stream.flush();
            }
        } catch (Exception e) {};
        
        return execStatus;
    }
        
    public int crash_command(String [] args, String [] env, File dir, PrintStream ps) {
        return run_regression_test(args, env, dir, new CrashTest(ps));
    }

    public int neg_crash_command(String [] args, String [] env, File dir, PrintStream ps) {
        return run_regression_test(args, env, dir, new NegativeCrashTest(ps));
    }
    
    public int hang_command(String [] args, String [] env, File dir, PrintStream ps) {
        return run_regression_test(args, env, dir, new HangTest(ps));
    }

    public int run_command(String [] args, String [] env, File dir, PrintStream ps) {
        return run_regression_test(args, env, dir, new CrashTest(ps), false);
    }

    public int run_regression_test(String [] args, 
            String [] env, 
            File dir, 
            CrashTest test) {
        return run_regression_test (args, env, dir, test, true);
    }

    public int run_regression_test(String [] args, 
            String [] env, 
            File dir, 
            CrashTest test,
            boolean processArgs) {
        
        long startTime = Util.getCurrentTime();             
        String [] testarr;
        int execStatus;
        
        String cmd = "";
        if (processArgs) {
            testarr = new String[args.length + 1];       
            testarr[0] = cfg.getTestedRuntime();
            for (int i=0; i < args.length; i++) {
                testarr[i + 1] = args[i];
            }
            cmd = getCommand(testarr, test);
        } else {
            cmd = args[0];
            for (int i=1; i < args.length; i++) {
                cmd += " " + args[i];
            }
        }
        
        result.setExecCmd("\n" + cmd);
        execStatus = test.run(cmd);
        result.setExecTime(Util.getCurrentTime() - startTime);
        return execStatus;
    }

    public String getCommand(String [] args, CrashTest test) {
        return test.getCommand(args);
    }

    public int echo_command(String [] args) {
        for(int i = 0; i < args.length; i++)
            result.setOutMsg(args[i]);
        return cfg.getRepPassed()[0];
    }

    public int runSame(String[] args) {
        return cfg.getRepModErr()[0];
    }

    public String[] parseParamSame(String[] args) {
        return null;
    }
    
    protected String [] getCrashArgs(String [] args) {
        String ret [] = new String[args.length + 2];
        ret[0] = args[0];
        ret[1] = "~doNotCheckCrashString";
        ret[2] = "~checkRes";
        for (int i = 1; i < args.length; i++) {
            ret[i + 2] = args[i];
        }
        return ret;
    }
    
    protected String decode(String name) {
        String tmp = decodePluginProperty(name, "ExecuteRegression");
        if (tmp != null) {
            return tmp;
        }
        return super.decode(name);
    }    
}

class THRedirector extends Thread {
    private PipedOutputStream outStream;
    private TResIR res;
    
    public THRedirector(PipedOutputStream stream, TResIR r) {
        outStream = stream;
        res = r;
    }
    
    public void run() {
        try {
            String str;
            InputStreamReader iReader = new InputStreamReader(
                    new PipedInputStream(outStream));
            BufferedReader bReader= new BufferedReader(iReader);

            while ((str = bReader.readLine()) != null) {
                res.setOutMsg(str);
            }    
        } catch(IOException e) {};
    }
}
