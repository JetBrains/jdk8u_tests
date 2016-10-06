/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
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
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

package org.apache.harmony.test.reliability.vm.stack;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;

/*
 * Goal: check that stack is unwinded properly when exception is thrown from jitted code. The idea 
 * is that exception is thrown from differently compiled methods: compiled, optimized, inlined. 
 * Accepted parameters:
 * param[0] - number of threads to launch
 * param[1] - number of methods to call iterativly by every thread

 * The test does:
 * 1. Creates 3 MethodNClass objects
 * 2. Launches TestHotInline to hint JIT to recompile TestHotInline function with optimizer
 *    and inline InlinedMethod in Method3Class 
 * 3. Starts param[0] number of threads, every thread does:
 *    a. makes some simple operations
 *    b. calls StackUnwindTest.intersectMethodCall() function 
 *    c. intersectMethodCall function calls 1 of 4 functions of 1 of instantiated classes MethodNClass 
 *       chosen randomly
 *    d. when number of functions called is equal to numberOfIterations, an exception is thrown,
 *       it to be catched by Thread.
 *
 * No hangs, crashes or fails are expected.
 * 
 * Note: TestHotInline supposes that when function is called with a very big internal cycle, then it is to be recompiled 
 *       with optimizing JIT. Thereto boolean flag hotMode is used. 
 *
 */

public class StackUnwindTest extends Test {
    volatile static boolean failed = false;
    final static int NUMBER_OF_THREADS = 100;
    static int numberOfThreads = NUMBER_OF_THREADS;
    static Random rm = new Random();
    static volatile boolean hotMode = false; 
    static int NUMBER_OF_CYCLE_RUNS_TO_MAKE_METHOD_HOT = 151000;
    static int NUMBER_OF_ITERATIONS = 1000;
    static int numberOfIterations = NUMBER_OF_ITERATIONS; 
    static MethodBase[] mBase = new MethodBase[3];
    static public Thread mainthread = null;
    
    public static void setFailed(){
        failed = true;
    }
    
    public static void commenterror(String msg){
        log.add(msg);
    }
    
    public static void main(String[] params){
        System.exit(new StackUnwindTest().test(params));
    }
    
    public int test(String []params){
        parseParams(params);
        mainthread = Thread.currentThread();

        // create objects to work on
        mBase[0] = new Method1Class();
        mBase[1] = new Method2Class();
        mBase[2] = new Method3Class();
        
        // "burn" methods - make JIT recompile these methods with optimization
        // hotMode flag is used in methods to run special code to make JIT recompile 
        // this method with optimizer
        hotMode = true;
        mBase[0].TestHotInline();
        mBase[1].TestHotInline();
        mBase[2].TestHotInline();
        hotMode = false;

        Thread thrds[] = new Thread[numberOfThreads];
        for (int i=0; i< numberOfThreads; i++){
            thrds[i] = new ThreadExecutor(rm.nextInt(NUMBER_OF_ITERATIONS) + 1);
            thrds[i].start();
        }

        for (int i=0; i< numberOfThreads; i++){
            try {
                thrds[i].join();
            } catch (InterruptedException e) {
                failed = true;
                log.add("Thread " + thrds[i].getId() + " was interrupted");
            }
        }

        if (failed){
            return fail("FAILED");
        }
        
        return pass("OK");
    }
    
    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            numberOfIterations = Integer.parseInt(params[1]);
        }
        
    }

    public static void intersectMethodCall(){
        MethodBase obj = null;
        switch (rm.nextInt(3)){
            case 0:
                obj = mBase[0];
                break;
            case 1:
                obj = mBase[1];
                break;
            case 2:
                obj = mBase[2];
                break;
        }
        
        switch (rm.nextInt(4)){
            case 0:
                obj.Test1();
                break;
            case 1:
                obj.Test2();
                break;
            case 2:
                obj.Test3();
                break;
            case 3:
                obj.TestHotInline();
                break;
        }
        
    }
}


    // Methods for manipulations on stack

interface MethodBase{
    public void Test1();
    public void Test2();
    public void Test3();
    public void TestHotInline();
}

    // extend the class of Exception to avoid inlining 
class Method1Class extends Exception implements MethodBase{
    public void Test1(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }
    
    public void Test2(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }

    public void Test3(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }
    
    public void TestHotInline(){
        if (StackUnwindTest.hotMode){
            int i = StackUnwindTest.NUMBER_OF_CYCLE_RUNS_TO_MAKE_METHOD_HOT;
            while (i > 0){
                // do something lightweight
                if (i == StackUnwindTest.NUMBER_OF_CYCLE_RUNS_TO_MAKE_METHOD_HOT/2){
                    i--;
                    continue;
                }
                i--;
            }
            return;
        }
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }

}

    //extend the class of Exception to avoid inlining
class Method2Class extends Exception implements MethodBase{
    public void Test1(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }
    
    public void Test2(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }

    public void Test3(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }
    public void TestHotInline(){
        if (StackUnwindTest.hotMode){
            int i = StackUnwindTest.NUMBER_OF_CYCLE_RUNS_TO_MAKE_METHOD_HOT;
            while (i > 0){
                // do something lightweight
                if (i == StackUnwindTest.NUMBER_OF_CYCLE_RUNS_TO_MAKE_METHOD_HOT/2){
                    i--;
                    continue;
                }
                i--;
            }
            return;
        }
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }
    
}

class Method3Class implements MethodBase{
    public void Test1(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        Test2();
    }
    
    public void Test2(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        Test3();
    }

    public void Test3(){
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        // do something
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }
    
    public void TestHotInline(){
        if (StackUnwindTest.hotMode){
            int i = StackUnwindTest.NUMBER_OF_CYCLE_RUNS_TO_MAKE_METHOD_HOT;
            while (i > 0){
                // try to hink JIT to compile optimized and inline InlinedMethod 
                InlinedMethod();
                i--;
            }
            return;
        }
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;        
        StackUnwindTest.intersectMethodCall();
    }
    
    void InlinedMethod(){
        // simple actions
        int a = 10;
        int b = 20;
        int c = 50;
        
        c = a+b;
        c -= a;
        
        double h = Math.cos(c);

        c = a;
        
        if (Thread.currentThread() == StackUnwindTest.mainthread){
            return;
        }
        ((ThreadExecutor)(Thread.currentThread())).stopIterationNumber--;
        
        if (((ThreadExecutor)(Thread.currentThread())).stopIterationNumber < 0){
            // force exception here
            c = (a-b)/(c-a);
        }
        
        b *= a*b*c*h; 
        
        StackUnwindTest.intersectMethodCall();
    }

}

class ThreadExecutor extends Thread{
    int stopIterationNumber = 1;
    ThreadExecutor(int i){
        stopIterationNumber = i;
    }
    public void run(){
        try{
            StackUnwindTest.intersectMethodCall();
        }catch(Throwable e){
            // Expected
            //e.printStackTrace();
            StackTraceElement[] ste = e.getStackTrace();
            for (int i = 0; i<ste.length; i++){
                ste[i].getMethodName();
                ste[i].getLineNumber();
                ste[i].getClassName();
            }
        }
    }
}

