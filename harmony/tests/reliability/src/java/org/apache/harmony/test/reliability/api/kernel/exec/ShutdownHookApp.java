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
 * @author Nikolay V. Bannikov
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.kernel.exec;

public class ShutdownHookApp {

    private static int CNT = 10;

    final static int sleep = 10;

    private String argument = "";

    private final static String longstring = "STARTssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "ssssssssssssssssssssssssssssssssssssssssssssssssssssss"
        + "sssssssssssssssssssssssssssssssssssssssssssssssssssEND";

    public static String getLongString() {
        return longstring;
    }

    public static void systemExit() {
        Runtime.getRuntime().exit(100);
    }

    void ShutdownHookApp() {
        Thread thread = (Thread) new ThreadShutdownHookApp();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(thread);
        return;
    }

    void prnLongString() {
        System.out.println(longstring);
    }

    void prnError() {
        System.err.println(argument);
    }

    void prnCurrentThread() {
        System.out.println("Current Thread : "
            + Thread.currentThread().getName());
    }

    class ThreadShutdownHookApp extends Thread {
        ThreadShutdownHookApp() {
            super();
        }

        public void run() {
            prnLongString();
            prnError();
            prnCurrentThread();
        }
    }

    public int test(String args[]) {
        if (args.length > 0) {
            argument = args[0];
        }
        if (args.length > 1) {
            CNT = Integer.parseInt(args[1]);
        }

        for (int i = 0; i < CNT; i++) {
            try {
                ShutdownHookApp();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.currentThread().sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return 104;
    }

    public static void main(String args[]) {
        System.exit(new ShutdownHookApp().test(args));
    }
}

