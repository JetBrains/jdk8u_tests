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

/**
 * Goal: Check various unusual cases of operating with shutdown hooks expecting there will be
 *       no hang, abnormal completion or other unexpected behavior.
 *
 * The test does:
 *
 *      1. Reads parameters:
 *            args[0] is a path to vm to start via Runtime.exec().
 *            args[1] is a path to the directory to specify in -classpath option for 
 *                    the vm started via Runtime.exec() in tests to find test classes.
 *
 *      2. For each of 9 classes (each representing some shutdown hook usage case) runs 
 *         each via Runtime.exec(<vm> -classpath <classes dir> <class> <number of hooks>).
 *
 *      3. 9 cases/classes are:
 *           * Hooks are added in finalize() method.
 *           * Hooks try to add new hooks while running, expect IllegalStateException.
 *           * Hooks try to remove themselves while running, expect IllegalStateException.
 *           * Hooks call System.gc() in its run() method, expecting no crash/hang/etc.
 *           * Hooks call Runtime.runFinalization() in its run() method, expecting no crash/hang/etc.
 *           * Hooks call System.exit() in its run() method, expecting no crash/hang/etc.
 *           * Hooks call Runtime.halt() in its run() method, expecting no crash/hang/etc.
 *           * All hooks but one complete normally, the other calls Runtime.halt() in its run()
 *             method, expecting no crash/hang/etc.
 *           * Hooks create new objects, call runFinalization(). It is expected that all created
 *             objects are finalized.
 *
 *      4. The main class waits for exec()-ed process completion, destroys the process if it 
 *         hung, analizes exit code and process output (if necessary) to check that hooks worked ok.
 */

public class ShtdwnHooksCornerCaseTest extends Test {

    static final int EXIT_CODE = 55;

    static final int HALT_CODE = 44;

    static final int BYTE_ARRAY_SIZE = 2000;

    static final int N_OF_HOOKS = 100;

    static final int SLEEP_TIMEOUT = 50;

    static final int ITERATIONS = 3000;

    static final String ok = "ok";

    static final String delimiter = ":";

    String classpathDir = ".";

    String vmPath = "";

    static final String CLASSPATH_OPT = "-classpath";

    // This must be the same name as package name of this class

    static final String package_name = "org.apache.harmony.test.reliability.api.kernel.hooks.";


    // These are names of the classes to run by java in another process.
    // The classes add various shutdown hooks and exit, thus initiating running hooks.
    // See class descriptions near class declarations.

    static final String AddHookViaFinalizationClass = package_name + "AddHookViaFinalization";
    static final String AddHookWhileHookRunningClass = package_name + "AddHookWhileHookRunning";
    static final String RmHookWhileHookRunningClass = package_name + "RmHookWhileHookRunning";
    static final String RunFinalizationWhileHookRunningClass = package_name + "RunFinalizationWhileHookRunning";
    static final String SysGCWhileHookRunningClass = package_name + "SysGCWhileHookRunning";
    static final String SysExitWhileHookRunningClass = package_name + "SysExitWhileHookRunning";
    static final String HaltWhileHookRunningClass = package_name + "HaltWhileHookRunning";
    static final String SysExitAndHaltWhileHookRunningClass = package_name + "SysExitAndHaltWhileHookRunning";
    static final String ObjectsCreatedInHooksFinalizedClass = package_name + "ObjectsCreatedInHooksFinalized";


    public static void main(String[] args) {
        System.exit(new ShtdwnHooksCornerCaseTest().test(args));
    }


    public int test(String[] params) {

        parseParams(params);

        boolean passed = true; 
        boolean status = false;

        // Run successively 9 different cases of shutdown hooks:
            
        status = runApp(AddHookViaFinalizationClass);
        passed = passed & status;
        // log.add("Application added hooks in finzalize() method. Test passed?: " + status + "\n");

        status = runApp(AddHookWhileHookRunningClass);
        passed = passed & status;
        // log.add("Hooks were added while running hooks, expecting IllegalStateException. Test passed?: " + status + "\n");

        status = runApp(RmHookWhileHookRunningClass);
        passed = passed & status;
        // log.add("Hooks delete themselves while running, expecting IllegalStateException. Test passed?: " + status + "\n");

        status = runApp(RunFinalizationWhileHookRunningClass);
        passed = passed & status;
        // log.add("Runtime.runFinalization() was called while hooks running. Test passed?: " + status + "\n");

        status = runApp(SysGCWhileHookRunningClass);
        passed = passed & status;
        // log.add("System.gc() was called while hooks running. Test passed?: " + status + "\n");

        //          This case is excluded from the test as incorrect, because, due to J2SE 1.5 Runtime.exit() spec
        //          "...If this method is invoked after the virtual machine has begun its shutdown
        //          sequence then if shutdown hooks are being run this method will block indefinitely..."
            
        /*            status = runApp(SysExitWhileHookRunningClass);
         passed = passed & status;
         log.add("System.exit() was called in hooks while hooks running. Test passed?: " + status + "\n");
         */
        status = runApp(HaltWhileHookRunningClass);
        passed = passed & status;
        // log.add("Runtime.halt() was called in hooks while hooks running. Test passed?: " + status + "\n");

        status = runApp(SysExitAndHaltWhileHookRunningClass);
        passed = passed & status;
        // log.add("All hooks completed normally but one called Runtime.halt() while hook running. " +
        //        "Test passed?: " + status + "\n");

        status = runApp(ObjectsCreatedInHooksFinalizedClass);
        passed = passed & status;
        // log.add("Hooks create objects. Were all objects finalized?: " + status + "\n");

        if (!passed) {
            return fail("Failed");
        }

        return pass("OK");
    }



    boolean runApp(String className) {

        int n_of_hooks = N_OF_HOOKS;

        // Run class specified by className via runtime whose path is passed 
        // as test argument, stored in 'vmPath' variable.

        // Command line to run is:
        // <vmPath> -classpath <classes dir> <className> <number of hooks to register>

        String[] cmd = createCmdLine(className, n_of_hooks);

        // log.add("Running: " + cmd[0] + " " + cmd[1] + " " + cmd[2] + " " + cmd[3] + " " + cmd[4]);

        boolean passed = true;

        try {

            Process p = Runtime.getRuntime().exec(cmd);

            // The process is started, experiments show that the process can hang.
            // To manage hanging we wait for ITERATIONS*SLEEP_TIMEOUT milliseconds
            // periodically checking whether the process has finished (ITSE is not 
            // thrown). If the process hangs, we destroy the process.

            int ev = 0;
            boolean process_finished = false;

            for (int i = 0; i < ITERATIONS; ++i) {
                try {
                    Thread.sleep(SLEEP_TIMEOUT);
                } catch (InterruptedException ie){
                }

                try {
                    ev = p.exitValue();
                    process_finished = true;
                    break;
                } catch (IllegalThreadStateException itse){
                }
            }

            if (!process_finished) {
                p.destroy();
                log.add("Process was destroyed after " + (ITERATIONS * SLEEP_TIMEOUT)/1000 + " seconds, hanging?...");
                passed = false;

            } else {

                // The process exited (did not hang), let check its exit value.
                // Should be either 0 (normal completion) or expected for some executed 
                // classes exit value.

                if (ev != 0 && ev != EXIT_CODE && ev != HALT_CODE) {
                    log.add("Process returned " + ev + ", instead of expected 0 or " + EXIT_CODE + " or " + HALT_CODE);
                    passed = false;
                }

                passed &= checkOutput(p, className, n_of_hooks);
            }
        
        } catch (Exception e){
            e.printStackTrace();
            log.add("Unexpected exception while exec()");
            passed = false;
        }

        return passed;
    }

    boolean checkOutput(Process p, String className, int n_of_hooks) {

        // For each of the three classes, which check whether or not some exception is thrown 
        // in the hooks, we check output from the hooks - if it is as expected 
        // then the exception was thrown as expected.

        if (!AddHookWhileHookRunningClass.equals(className) && 
            !RmHookWhileHookRunningClass.equals(className) &&
            !ObjectsCreatedInHooksFinalizedClass.equals(className)) {
            return true;
        }

        InputStream is = p.getInputStream();

        int read_byte = 0;
        int i = 0;

        byte[] b = new byte[n_of_hooks * 5 * 10];

        try {
            while ((read_byte = is.read()) > 0) {
                b[i++] = (byte)read_byte;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            log.add("IOException while reading from process InputStream");
            return false;
        }

        byte[] bb = new byte[i];
        System.arraycopy(b, 0, bb, 0, bb.length);

        // Convert read bytes into string using default encoding:

        String s = new String(bb);

        // log.add(s);

        String[] ss = s.split(delimiter);
    
        // Check that number of "ok:" strings is equal to the number of hooks which 
        // printed these strings as indicator of that checks done in the hooks passed.
 
        if (ss.length != n_of_hooks) {
            if (ObjectsCreatedInHooksFinalizedClass.equals(className)) {
                log.add(ss.length + " objects created in hooks were finalized instead of " + n_of_hooks);
            } else {
                log.add(ss.length + " hooks threw IllegalStateException instead of expected " + n_of_hooks);
            }
            return false;
        }

        return true;
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


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            vmPath = params[0];
        }

        if (params.length >= 2) {
            classpathDir = params[1];
        }
    }

}



    // ---------------------  Case 1: Add hook in finalize() ---------------

    // This class started by VM via main(args) creates some objects (SomeObject) to be finalized, 
    // however, in finalize() methods of the objects we add hooks, so that the hooks 
    // are added when finalization process is running.

class AddHookViaFinalization {

    public static void main(String[] args){
        int n_of_hooks = Integer.parseInt(args[0]);

        for (int i = 0; i < n_of_hooks; ++i) {
            createSomeObjects();

            if (i % 10 == 0) {
                Runtime.getRuntime().runFinalization();
            }
        }
    }

    static void createSomeObjects() {
        new SomeObject();
    }

}


class SomeObject {

    public SomeObject() {

        // This is just to stimulate gc:

        byte[] b = new byte[ShtdwnHooksCornerCaseTest.BYTE_ARRAY_SIZE];
    }

    protected void finalize() {

        // Add some simple hook, expecting no abnormal completion or exceptions:

        Runtime.getRuntime().addShutdownHook(new SomeHook());
    }

}

    // Some hook class - does nothing.

class SomeHook extends Thread {

    public void run() {
    }

}

    // --------------------- Case 2: Add hook while hook running ---------------

    // This class started by Vm via main(args) add hooks which, being running try to add
    // some hook, expecting that ISE is thrown.

class AddHookWhileHookRunning {

    public static void main(String[] args){
        int n_of_hooks = Integer.parseInt(args[0]);
        for (int i = 0; i < n_of_hooks; ++i) {
            Runtime.getRuntime().addShutdownHook(new Hook_AddHookWhileHookRunning());
        }
    }
}

class Hook_AddHookWhileHookRunning extends Thread {

    static Object obj = new Object();

    public void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new SomeHook());

        } catch (IllegalStateException ise){

            // why synchronized? - to avoid a mess when several simultaneously running
            // threads write "OK" status into stdout.

            synchronized (obj) {
                System.out.print(ShtdwnHooksCornerCaseTest.ok + ShtdwnHooksCornerCaseTest.delimiter);
            }
        }
    }
}

    // --------------------- Case 3: Remove hook while hook running ---------------

    // This class started by Vm via main(args) add hooks which, being running try to remove
    // itself, expecting that ISE is thrown.

class RmHookWhileHookRunning {

    public static void main(String[] args){
        int n_of_hooks = Integer.parseInt(args[0]);
        for (int i = 0; i < n_of_hooks; ++i) {
            Runtime.getRuntime().addShutdownHook(new Hook_RmHookWhileHookRunning());
        }
    }
}

class Hook_RmHookWhileHookRunning extends Thread {

    static Object obj = new Object();

    public void run() {
        try {
            Runtime.getRuntime().removeShutdownHook(this);
        } catch (IllegalStateException ise){

            // why synchronized? - to avoid a mess when several simultaneously running
            // threads write "OK" status into stdout.

            synchronized (obj) {
                System.out.print(ShtdwnHooksCornerCaseTest.ok + ShtdwnHooksCornerCaseTest.delimiter);
            }
        }
    }
}

    // --------------------- Case 4: Run Runtime.runFinalization() while hook running ---------------

    // This class adds hooks which call Runtime.runFinalization() method, expecting no 
    // abnormal completion or unexpected behavior.

class RunFinalizationWhileHookRunning {

    public static void main(String[] args){
        int n_of_hooks = Integer.parseInt(args[0]);
        for (int i = 0; i < n_of_hooks; ++i) {
            Runtime.getRuntime().addShutdownHook(new Hook_RunFinalizationWhileHookRunning());
        }
    }
}

class Hook_RunFinalizationWhileHookRunning extends Thread {

    public void run() {
        Runtime.getRuntime().runFinalization();
    }

}


    // --------------------- Case 5: Run System.gc() while hook running ---------------

    // This class adds hooks which call System.gc() method, expecting no 
    // abnormal completion or unexpected behavior.

class SysGCWhileHookRunning {

    public static void main(String[] args){
        int n_of_hooks = Integer.parseInt(args[0]);
        for (int i = 0; i < n_of_hooks; ++i) {
            Runtime.getRuntime().addShutdownHook(new Hook_SysGCWhileHookRunning());
        }
    }
}

class Hook_SysGCWhileHookRunning extends Thread {

    public void run() {
        System.gc();
    }

}


    // --------------------- Case 6: Run System.exit() while hook running ---------------

    // This class adds hooks which call System.exit(), expecting no hanging or 
    // abnormal completion or other unexpected behavior.
    // This case is excluded from the test as incorrect, because, due to J2SE 1.5 Runtime.exit() spec
    //  "...If this method is invoked after the virtual machine has begun its shutdown
    //  sequence then if shutdown hooks are being run this method will block indefinitely..."

    /*
     class SysExitWhileHookRunning {

     public static void main(String[] args){
     int n_of_hooks = Integer.parseInt(args[0]);
     for (int i = 0; i < n_of_hooks; ++i) {
     Runtime.getRuntime().addShutdownHook(new Hook_SysExitWhileHookRunning());
     }
     }
     }

     class Hook_SysExitWhileHookRunning extends Thread {

     public void run() {
     System.exit(ShtdwnHooksCornerCaseTest.EXIT_CODE);
     }

     }
     */

    // --------------------- Case 7: Run Runtime.halt() while hook running ---------------

    // This class adds hooks which call Runtime.halt(), expecting no hanging or 
    // abnormal completion or other unexpected behavior.

class HaltWhileHookRunning {

    public static void main(String[] args){
        int n_of_hooks = Integer.parseInt(args[0]);
        for (int i = 0; i < n_of_hooks; ++i) {
            Runtime.getRuntime().addShutdownHook(new Hook_HaltWhileHookRunning());
        }
    }
}

class Hook_HaltWhileHookRunning extends Thread {

    public void run() {
        Runtime.getRuntime().halt(ShtdwnHooksCornerCaseTest.HALT_CODE);
    }

}


    // --------------------- Case 8: All hooks complete normally, but one calls Runtime.halt() ---------------

    // This class adds hooks, half of them call System.exit() another half - Runtime.halt(), 
    // expecting no hanging or abnormal completion or other unexpected behavior.

class SysExitAndHaltWhileHookRunning {

    public static void main(String[] args){

        int n_of_hooks = Integer.parseInt(args[0]);

        for (int i = 0; i < n_of_hooks; ++i) {
            if (i == n_of_hooks / 2){
                Runtime.getRuntime().addShutdownHook(new Hook_HaltWhileHookRunning());
            } else {
                Runtime.getRuntime().addShutdownHook(new SomeSleepingHook());
            }
        }
    }
}


    // Some hook class - does nothing.

class SomeSleepingHook extends Thread {

    public void run() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException ie){
            }
        }
    }

}


    // --------------------- Case 9: Check finalization is called for objects created by hooks ---------------

    // This class adds hooks which create new objects. It is expected that the objects
    // will be sooner or later all finalized before VM actually exits (each object prints 
    // OK status in its finalize() method).

class ObjectsCreatedInHooksFinalized {

    public static void main(String[] args){
        int n_of_hooks = Integer.parseInt(args[0]);
        for (int i = 0; i < n_of_hooks; ++i) {
            Runtime.getRuntime().addShutdownHook(new Hook_ObjectsCreatedInHooksFinalized());
        }
        Runtime.getRuntime().runFinalizersOnExit(true);
    }
}

class Hook_ObjectsCreatedInHooksFinalized extends Thread {

    public void run() {
        m();
        Thread.yield();
        Runtime.getRuntime().runFinalization();
    }

    void m() {
        new ObjectToFinalize();
    }

}


class ObjectToFinalize {

    static Object obj = new Object();

    public ObjectToFinalize() {
        byte[] b = new byte[ShtdwnHooksCornerCaseTest.BYTE_ARRAY_SIZE];
    }

    protected void finalize() {
        synchronized(obj){
            System.out.print(ShtdwnHooksCornerCaseTest.ok + ShtdwnHooksCornerCaseTest.delimiter);
        }
    }
}






