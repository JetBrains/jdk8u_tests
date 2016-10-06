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

package org.apache.harmony.test.reliability.api.kernel.hooks;

import org.apache.harmony.test.reliability.share.Test;

import java.io.InputStream;
import java.io.IOException;
import java.util.Random;

 /**
  * Goal: checking that runtime works correctly with shutdown hooks in case of different 
  *        application completion cases, such as via System.exit(), Runtime.halt() or in
  *       case of just normal completion.
  * 
  *      1. Reads parameters:
  *            args[0] is a path to vm to start via Runtime.exec().
  *            args[1] is a path to the directory to specify in -classpath option for 
  *                    the vm started via Runtime.exec() in tests to find test classes.
  *      
  *      2. Runs 5 cases/classes, starting each case/class via 
  *         Runtime.exec(<vm> -classpath <classes dir> <className> <number of hooks>)
  * 
  *         The cases / classes (<className>) are:
  *
  *          * AppNormalCompletion - adds hooks and exits silently (not by System.exit() 
  *            or Runtime.halt()), it is checked that all hooks started/completed.
  *
  *          * AppSysExitCompletion - adds hooks and exits by System.exit(), it is checked
  *            that all hooks started/completed.
  *  
  *          * AppDestroyedProcess - adds hooks and hangs in object.wait() for the main
  *            class to destroy it via Process.destroy(), it is checked that no unexpected results.
  *
  *          * AppHalted - adds hooks and exits by Runtime.halt(), it is checked that no
  *            no hooks started/completed.
  *
  *          * AppNormalCompletionFinishedHooks - adds hooks which are already completed/executed
  *            threads, it is checked that hooks started/completed and no unexpected results.
  *
  *      3. The main class reads output of started processes, analyzes the output and exit value.
  */

public class AddRmShtdwnHooksTest extends Test {

    int N_OF_HOOKS = 200;

    String classpathDir = ".";

    String vmPath = "";

    static final String CLASSPATH_OPT = "-classpath";

    static final String STARTED = " ";

    static int EXIT_CODE = 33;

    static int HALT_CODE = 44;

    static final int NORMAL = 0;

    static final int DESTROY = 1;

    static final int SYS_EXIT = 2;

    static final int HALT = 3;

    static final int SLEEP_TIMEOUT = 200;

    // This must be the same name as package name of this class

    static final String package_name = "org.apache.harmony.test.reliability.api.kernel.hooks.";


    static final String normalCompletionClass = package_name + "AppNormalCompletion";
    static final String sysExitCompletionClass = package_name + "AppSysExitCompletion";
    static final String destroyedProcessClass = package_name + "AppDestroyedProcess";
    static final String haltCompletionClass = package_name + "AppHalted";
    static final String normalCompletionExecutedHooksClass = package_name + "AppNormalCompletionFinishedHooks";



    public static void main(String[] args) {
        System.exit(new AddRmShtdwnHooksTest().test(args));
    }


    public int test(String[] params) {

        parseParams(params);

        boolean passed = true; 
        boolean status = false;

        status = runApp(normalCompletionClass, N_OF_HOOKS, NORMAL);
        passed = passed & status;
        // log.add("Normally completed application (no System.exit() or halt, etc.). Test passed?: " + status + "\n");

        status = runApp(sysExitCompletionClass, N_OF_HOOKS, SYS_EXIT);
        passed = passed & status;
        // log.add("Application exited via System.exit(). Test passed?: " + status + "\n");

        //            status = runApp(destroyedProcessClass, N_OF_HOOKS, DESTROY);
        //            passed = passed & status;
        //            log.add("Application was destroyed. Test passed?: " + status + "\n");

        status = runApp(haltCompletionClass, N_OF_HOOKS, HALT);
        passed = passed & status;
        // log.add("Application exited via Runtime.halt(). Test passed?: " + status + "\n");

        status = runApp(normalCompletionExecutedHooksClass, N_OF_HOOKS, NORMAL);
        passed = passed & status;
        // log.add("Normally completed application, which registered already executed (finished)" +
        //        " threads/hooks. Test passed?: " + status + "\n");

        if (!passed) {
            return fail("Failed");
        }

        return pass("OK");
    }


    boolean runApp(String className, int n_of_hooks, int mode) {

        // command line is:
        // <vmPath> -classpath <classes dir> <className> <n_of_hooks>

        String[] cmd = createCmdLine(className, n_of_hooks);

        // log.add("Running: " + cmd[0] + " " + cmd[1] + " " + cmd[2] + " " + cmd[3] + " " + cmd[4]);

        boolean passed = true;

        InputStream is = null;

        try {

            Process p = Runtime.getRuntime().exec(cmd);

            is = p.getInputStream();

            if (mode == DESTROY) {

                // wait for the process's printed byte as signal 
                // that it is ready to be destroyed:

                byte b = (byte)is.read();

                try {
                    Thread.sleep(SLEEP_TIMEOUT);
                } catch (InterruptedException ie){
                }

                p.destroy();

            }
 
            // Lets check that if application exited not via Runtime.halt(), then,
            // all hooks started, each hook printed some string as a signal that the hook
            // worked:
 
            if (mode == NORMAL || mode == SYS_EXIT) {
                
                passed = checkAllHooksStarted(getProcessOutput(is, n_of_hooks), Hook.separator, n_of_hooks);

            }

            // If application exited via Runtime.halt(), then, check that no hooks started,
            // despite of hooks were added before halt() - i.e. application printed nothing into stdout:

            if (mode == HALT) {

                passed = checkNoHooksStarted(is);

            }

            p.waitFor();

            // Lets check exit codes. They should be as expected - either 0, or 1 (in case of process
            // destroying) or EXIT_CODE or HALT_CODE.

            int ev = p.exitValue();

            if (mode == NORMAL && ev != 0) {
                log.add("Process with registered hooks (created but not executed or already finished)" +
                    " and completed normally returned status " + ev + " instead of expected 0");
                passed = false;
            }

            if (mode == DESTROY && (ev != 0 && ev != 1)) {
                log.add("Process with registered hooks and destroyed" +
                    " returned status " + ev + " instead of expected 0 or 1");
                passed = false;
            }

            if (mode == SYS_EXIT && ev != EXIT_CODE) {
                log.add("Process with registered hooks and exited via System.exit() returned status " + 
                    ev + " instead of expected " + EXIT_CODE);
                passed = false;
            }

            if (mode == HALT && ev != HALT_CODE) {
                log.add("Process with registered hooks and exited via Runtime.halt(<status>) returned " + 
                    "status " + ev + " instead of expected " + HALT_CODE);
                passed = false;
            }
        
        } catch (Exception e){
            e.printStackTrace();
            log.add("Unexpected exception while exec() or postprocessing of output of " + 
                "application registered with normal hooks");
            passed = false;

        }

        return passed;
    }


    boolean checkNoHooksStarted(InputStream is){

        ProcessOutputReader t = new ProcessOutputReader(is);

        // Start thread which listens process's output:

        t.start();

        // Wait for the thread actually started reading process's output:

        while (!t.started) {
            Thread.yield();
        }

        // Wait either for:
        // * the reader thread read some bytes and completed
        // * read() returned -1 indicating EOF and reader thread completed
        // * reader thread hung in read(), we called yield() 1000 times and 
        //   finally interrupted the reader thread.

        int i = 0;

        while (!t.finished) {

            Thread.yield();

            if (++i == 1000) {
                t.interrupt();
            }
        }

        try {
            t.join();
        } catch (InterruptedException ie) {
        }

        // If reader thread read something from process's stdout, then, looks like 
        // hooks started and printed something into stdout - this is not what is expected, 
        // since halt() does not cause running hooks.

        if (t.read_bytes > 0) {
            log.add("Process did halt and looks like started hooks - something was printed into System.out");
            return false;
        }

        return true;
    }


    String getProcessOutput(InputStream is, int n_of_hooks){
        int read_byte = 0;
        int i = 0;

        // Allocate large array to fill in with process output

        byte[] b = new byte[n_of_hooks * (Hook.separator.length() + 5) * 10];

        try {
            while ((read_byte = is.read()) > 0) {
                b[i++] = (byte)read_byte;
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.add("IOException while reading from process InputStream");
            return "";
        }

        byte[] bb = new byte[i];
        System.arraycopy(b, 0, bb, 0, bb.length);

        // Convert bytes into String using default encoding:

        return new String(bb);
    }


    boolean checkAllHooksStarted(String process_output, String separator, int n_of_hooks){

        // log.add("Process output: " + process_output);

        boolean passed = true;

        String[] hook_names = process_output.split(separator);

        if (hook_names.length != n_of_hooks){
            log.add(hook_names.length + " thread's output found instead of " + n_of_hooks);
            passed = false;
        }

        byte[] found = new byte[n_of_hooks];

        for (int j = 0; j < hook_names.length; ++j){
            int hook_number = Integer.parseInt(hook_names[j]);
            found[hook_number]++;
        }

        for (int i = 0; i < found.length; ++i){
            if (found[i] > 1){
                log.add("Thread/hook " + i + " was executed " + found[i] + " times instead of 1");
                passed = false;
            }

            if (found[i] < 1){
                log.add("Thread/hook " + i + " was not executed - no output from the hook");
                passed = false;
            }
        }

        return passed;
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            vmPath = params[0];
        }

        if (params.length >= 2) {
            classpathDir = params[1];
        }
    }


    String[] createCmdLine(String className, int n_of_hooks_to_start) {
        String[] s = new String[5];
        s[0] = vmPath;
        s[1] = CLASSPATH_OPT;
        s[2] = classpathDir;
        s[3] = className;
        s[4] = "" + n_of_hooks_to_start;
        return s;
    }

}


    // The class adds hooks and completes normally (not via System.exit() or Runtime.halt()).

class AppNormalCompletion {

    public static void main(String[] args){

        Thread[] hooks = createHookThreads(Integer.parseInt(args[0]));

        for (int i = 0; i < hooks.length; ++ i){
            try{
                Runtime.getRuntime().addShutdownHook(hooks[i]);
            } catch (IllegalArgumentException iae){
                // Ignore
            }
        }

    }

    static Thread[] createHookThreads(int n_of_hooks_to_start) {

        Thread[] t = new Thread[n_of_hooks_to_start];

        for (int i = 0; i < t.length; ++i){
            t[i] = new SimpleHook("" + i);
        }

        return t;
    }
}


    // The class adds hooks which are already executed/finished threads.

class AppNormalCompletionFinishedHooks {

    public static void main(String[] args){

        Thread[] hooks = createExecutedHookThreads(Integer.parseInt(args[0]));

        for (int i = 0; i < hooks.length; ++ i){
            try{
                Runtime.getRuntime().addShutdownHook(hooks[i]);
            } catch (IllegalArgumentException iae){
                // Ignore
            }
        }

    }

    static Thread[] createExecutedHookThreads(int n_of_hooks_to_start) {

        Thread[] t = new Thread[n_of_hooks_to_start];

        for (int i = 0; i < t.length; ++i){
            t[i] = new SimpleHook("" + i);
            t[i].start();
            try {
                t[i].join();
            } catch (InterruptedException ie){
            }
        }
        return t;
    }
}


    // The class adds hooks and exits via System.exit().

class AppSysExitCompletion {

    public static void main(String[] args){

        AppNormalCompletion.main(args);

        System.exit(AddRmShtdwnHooksTest.EXIT_CODE);
    }
}


    // The class adds hooks and exits via Runtime.halt().

class AppHalted {

    public static void main(String[] args){

        AppNormalCompletion.main(args);

        Runtime.getRuntime().halt(AddRmShtdwnHooksTest.HALT_CODE);
    }
}


    // The class adds hooks and hangs in Object.wait(), expecting that the process
    // (running class) will be destroyed.

class AppDestroyedProcess {

    static Object obj = new Object();
 
    public static void main(String[] args){
          
        AppNormalCompletion.main(args);

        synchronized(obj){

            // This print() is to indicate that the process started and is ready 
            // to be destroyed. This is done to avoid destroying being started VM.

            System.out.print(AddRmShtdwnHooksTest.STARTED);

            // wait() forever waiting for being destroyed.

            while(true){
                try {
                    obj.wait();
                } catch (InterruptedException ie){
                    return;
                }
            }

        }   
    }

}


    // This class represents all hooks. Each hook prints into stdout ":ok" as a 
    // signal that the hook works.

class SimpleHook extends Hook {

    String name = "";

    static Random r = new Random(1);

    static Object obj = new Object();

    public SimpleHook(String name){
        this.name = name;
    }

    public void run() {

        int x = r.nextInt(50);

        try {

            Thread.sleep(x);

        } catch (InterruptedException ie){
        }

        // why synchronized? - to avoid simultaneous writing of strings by
        // executed in parallel threads.

        synchronized(SimpleHook.obj) {
            System.out.print(name + Hook.separator);

        }
    }
}


class Hook extends Thread {

    public static String separator = ":ok";

}


    // The class is a Thread which reads from the Process, hanging in read() until 
    // read() returns -1 as a signal that application halted or until IOException. 
    // Both cases signal that nothing was printed by the Process into stdout. 
    // This is expected result, since halt() should not cause hooks running (hooks 
    // print into stdout). 

class ProcessOutputReader extends Thread {

    volatile boolean started = false;
    volatile boolean finished = false;
    volatile int read_bytes = 0;

    InputStream is;

    public ProcessOutputReader(InputStream is){
        this.is = is;
    }


    public void run() {
        try {
            started = true;

            while(is.read() > 0) {
                ++read_bytes;
            }

            finished = true;

        } catch (IOException ioe) {
            finished = true;
            return;
        }
    }
}





