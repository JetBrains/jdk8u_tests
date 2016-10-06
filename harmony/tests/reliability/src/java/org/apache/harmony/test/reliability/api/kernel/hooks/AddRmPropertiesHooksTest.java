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

package org.apache.harmony.test.reliability.api.kernel.hooks;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Properties;

/**
 * Goal: test and find incorrectness in add/set/remove operations with shutdown 
 *       hooks and properties.
 *
 * The test does:
 *
 *     1. Reads parameters:
 *         param[0] - number of properties to play with
 *         param[1] - number of hooks to play with
 *
 *     2. Plays with properties:
 *        a. Adds param[0] properties via setProperty(), checks the properties are set
 *           removes properties.
 *        b. Adds param[0] properties via setProperties(), checks the properties are set,
 *           removes properties.
 *
 *     3. Plays with hooks:
 *        a. Creates, adds, removes param[1] hooks which are non-started Threads.
 *        b. Creates, adds, removes param[1] hooks which are finished Threads.
 *        c. Creates, adds, removes param[1] hooks which are being running Threads.
 */

public class AddRmPropertiesHooksTest extends Test {

        int N_OF_PROPERTIES = 1000;

        int N_OF_HOOKS = 200;

	public static void main(String[] args) {
            System.exit(new AddRmPropertiesHooksTest().test(args));
	}


        public int test(String[] args) {

            boolean prop_status = true;
            boolean hooks_status = true;

            parseParams(args);

            prop_status = playWithProperties();

            hooks_status = playWithShutdownHooks();

            if (prop_status != true || hooks_status != true) {
                return fail("Failed");
            }
            
            return pass("OK");
	}

        boolean playWithShutdownHooks() {

            boolean passed = true;

            Thread[] hooks = createNormalHooks();

            passed = addNormalHooks(hooks);

            removeNormalHooks(hooks);

            addRemoveExecutedHooks(createExecutedHooks());

            passed = passed & addRemoveBeingRunningHooks();

            return passed;
        }

        boolean addRemoveBeingRunningHooks() {

            boolean passed = true;

            Thread[] t = createRunningThreads();

            for (int i = 0; i < t.length; ++i) {

                try {

                    Runtime.getRuntime().addShutdownHook(t[i]);
                    passed = false;

                    // looks like hook was unexpectedly added, lets remove added hook

                    Runtime.getRuntime().removeShutdownHook(t[i]); 

                } catch (IllegalArgumentException iae){
                }
            }

            waitForRunningThreads(t);

            if (!passed){
                log.add("addShutdownHook(running thread) does not throw IllegalArgumentException");
            }
            return passed;
        }


        Thread[] createRunningThreads() {

            RunningThread[] t = new RunningThread[N_OF_HOOKS];

            for (int i = 0; i < t.length; ++i) {

                t[i] = new RunningThread();
                t[i].start();

                while (!t[i].started) {
                    Thread.yield();
                }
            }
            return t;
        }


        void waitForRunningThreads(Thread[] t){

            for (int i = 0; i < t.length; ++i){

                synchronized (((RunningThread)t[i]).obj) {

                    ((RunningThread)t[i]).may_wakeup = true;

                    ((RunningThread)t[i]).obj.notify();

                }

                while(!((RunningThread)t[i]).ended){

                    Thread.yield();

                }

                try {
                    t[i].join();
                } catch (InterruptedException ie){
                }

            }
        }

        
        Thread[] createExecutedHooks() {

            Thread[] hooks = new Thread[N_OF_HOOKS];

            for (int i = 0; i < hooks.length; ++i){
                Thread t = new NormalHook();
                t.start();
                Thread.yield();
                try {
                    t.join();
                } catch (InterruptedException ie){
                }
                hooks[i] = t;
            }

            return hooks;
        }

        void addRemoveExecutedHooks(Thread[] hooks) {
            for (int i = 0; i < hooks.length; ++i) {
                boolean remShHook = true;
                try{
                    Runtime.getRuntime().addShutdownHook(hooks[i]);
                } catch (IllegalArgumentException iae){
                	remShHook = false;
                }
                if (remShHook){
                    Runtime.getRuntime().removeShutdownHook(hooks[i]);
                }
            }
        }

        Thread[] createNormalHooks() {

            Thread[] hooks = new Thread[N_OF_HOOKS];

            for (int i = 0; i < hooks.length; ++i){
                hooks[i] = new NormalHook();
            }

            return hooks;
        }

        boolean addNormalHooks(Thread[] hooks) {
            boolean passed = true;
            for (int i = 0; i < hooks.length; ++i) {
                Runtime.getRuntime().addShutdownHook(hooks[i]);
                try {
                    Runtime.getRuntime().addShutdownHook(hooks[i]);
                    log.add("No IllegalArgumentException in addShutdownHook is the same hook is added twice");
                    passed = false;
                } catch (IllegalArgumentException iae){
                }
            }
            return passed;
        }

        void removeNormalHooks(Thread[] hooks){

            for (int i = 0; i < hooks.length; ++i) {

                Runtime.getRuntime().removeShutdownHook(hooks[i]);

                // intentionslly remove the same hook, should be no surprises

                Runtime.getRuntime().removeShutdownHook(hooks[i]);
            }
        }

        boolean playWithProperties() {

            boolean passed = true;

            addProperty();

            if (!checkProperty()) {
                passed = false;
            }

            removeProperty();

            if (!checkPropertyRemoved()) {
                passed = false;
            }

            addProperties();

            if (!checkProperties()) {
                passed = false;
            }

            removeProperties();

            if (!checkPropertiesRemoved()) {
                passed = false;
            }

            return passed;
        }



        public void addProperty(){

            for (int i = 0; i < N_OF_PROPERTIES; ++i) {
                System.setProperty("" + i, "" + i);
                System.setProperty("" + i, "" + i);
            }
        }

        boolean checkProperty() {

             boolean passed = true;

             for (int i = 0; i < N_OF_PROPERTIES; ++i){

                 if (!("" + i).equals(System.getProperty("" + i))){
                      log.add("System.getProperty(\"" + i + "\") returns: " + System.getProperty("" + i) + 
                              " while expected \"" + i + "\"");
                      passed = false;
                 }
             }

             return passed;
        }

        public void removeProperty(){

            for (int i = 0; i < N_OF_PROPERTIES; ++i) {

                System.clearProperty("" + i);
            }
        }


        boolean checkPropertyRemoved() {
        
             boolean passed = true;

             for (int i = 0; i < N_OF_PROPERTIES; ++i){

                 if (System.getProperty("" + i) != null){
                      log.add("System.getProperty(\"" + i + "\") returns: " + System.getProperty("" + i) + 
                              " while expected null (no such property), property removal failed?");
                      passed = false;
                 }
             }

             return passed;

        }


        public void addProperties(){

            Properties p = new Properties();

            for (int i = 0; i < N_OF_PROPERTIES; ++i) {

                p.setProperty("" + i + "_" + i, "" + i + "_" + i);

            }

            System.setProperties(p);
        }


        boolean checkProperties() {

             boolean passed = true;

             for (int i = 0; i < N_OF_PROPERTIES; ++i){

                 if (!("" + i + "_" + i).equals(System.getProperty("" + i + "_" + i))){
                      log.add("System.getProperty(\"" + i + "_" + i + "\") returns: " + System.getProperty("" + i + "_" + i) + 
                              " while expected \"" + i + "_" + i + "\"");
                      passed = false;
                 }
             }

             return passed;
        }


        public void removeProperties(){

                System.setProperties(null);

        }


        boolean checkPropertiesRemoved() {

             boolean passed = true;

             for (int i = 0; i < N_OF_PROPERTIES; ++i){

                 if (System.getProperty("" + i + "_" + i) != null){
                      log.add("System.getProperty(\"" + i + "_" + i + "\") returns: " + System.getProperty("" + i + "_" + i) + 
                              " while expected null, properties removal failed?");
                      passed = false;
                 }
             }

             return passed;
        }


        public void parseParams(String[] params) {

		if (params.length >= 1) {
		    N_OF_PROPERTIES = Integer.parseInt(params[0]);
		}		

		if (params.length >= 2) {
		    N_OF_HOOKS = Integer.parseInt(params[1]);
		}		
        }
}


class NormalHook extends Thread {
    public void run() {
        return;
    }
}


class RunningThread extends Thread {

    volatile boolean started = false;
    volatile boolean ended = false;
    volatile boolean may_wakeup = false;
    Object obj = new Object();

    public void run() {

        synchronized (obj) {

            while (!may_wakeup) {

                try {

                    started = true;
                    obj.wait();

                } catch (InterruptedException ie){
                }

            }
        }

        ended = true;
    }
}





