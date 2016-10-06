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

package org.apache.harmony.test.reliability.vm.exceptions;

import org.apache.harmony.test.reliability.share.Test;
import org.apache.harmony.test.reliability.share.Result;
import java.util.Random;
import java.util.Hashtable;

/**
 * Two scenarios are executed:
 * 
 * 1. There are 5 methods which can call each other recursively
 * up to specified depth. Each method calls randomly selected method
 * and then throws a particular exception which is caught within
 * this method. When recursion depth reaches the specified value,
 * the method doesn't call more methods and only throws and catches
 *
 * 2. There are 8 methods which reproduce 8 situations of try-catch-finally
 * block execution described in JLS14.20.2. The runner method consequently
 * calls each of these methods and checks that there are no exceptions
 * raised when there shouldn't be any exceptions, and there are expected
 * exceptions raised when they should be raised according to specification.
 *
 * Parameters: number of threads, the length of handler's chain (recursion depth)
 */

public class ExceptionsTest extends Test implements Runnable {    

    private static final Random rnd = new Random();

    private static int invCount;
    private static int numThreads;
    private int result = Result.PASS;

    public static void main(String[] args) {
        System.exit(new ExceptionsTest().test(args));
    }

    public int test(String[] args) {
        if(args.length != 2) {
            log.add("2 parameters expected: number of threads and chain length");
            return error("ERROR");
        }
        invCount = Integer.parseInt(args[1]);
        numThreads = Integer.parseInt(args[0]);
        Thread[] th = new Thread[numThreads];

        for (int i = 0; i < th.length; i++) {
            th[i] = new Thread(new ExceptionsTest());
            th[i].start();
        }
        for (int i = 0; i < th.length; i++) {
            try {
                th[i].join();
            } catch (InterruptedException e) {
            }
        }
        if (result == Result.PASS) {
            return pass("PASSED");
        }
        return result;        
    }

    public void run() {
        int res;
        res = new Scenario1().run();
        if (res != Result.PASS) {
            result = res;
            return;
        }
        res = new Scenario3().run();
        if (res != Result.PASS) {
            result = res;
        }
    }


    private static class Scenario1 {
        private int count = 0;        
        public int run() {
            int next = rnd.nextInt(5);
            int res = Result.FAIL;
            switch (next) {
                case 0:
                    res = methNPE();
                    break;
                case 1:
                    res = methAE();
                    break;
                case 2:
                    res = methAIOOBE();
                    break;
                case 3:
                    res = methULE();
                    break;
                case 4:
                    res = methUserException();
                    break;
            }            
            return res;
        }
        private int methNPE() {
            int next = -1;
            try {
                count++;
                if (count == invCount) {
                    return Result.PASS;
                }                
                next = rnd.nextInt(5);
                int res = Result.FAIL;
                switch (next) {
                    case 0:
                        res = methNPE();
                        break;
                    case 1:
                        res = methAE();
                        break;
                    case 2:
                        res = methAIOOBE();
                        break;
                    case 3:
                        res = methULE();
                        break;
                    case 4:
                        res = methUserException();
                        break;
                }
                if (res != Result.PASS) {
                    log.add("in methNPE(): invocation of " + next + " resulted \"FAILED\"");
                    return res;
                }

                // Cause a NPE
                new Hashtable().get(new Object()).toString();
            } catch (NullPointerException e) {
                return Result.PASS;
            } catch (Throwable t) {
                log.add("in methNPE(): invocation of " + next + " caused an unexpected exception " + t.getClass().getName());
                return Result.FAIL;
            }
            log.add("in methNPE(): NPE wasn't thrown");
            return Result.FAIL;
        }
        private int methAE() {
            int next = -1;
            try {
                count++;
                if (count == invCount) {
                    return Result.PASS;
                }
                next = rnd.nextInt(5);
                int res = Result.FAIL;
                switch (next) {
                    case 0:
                        res = methNPE();
                        break;
                    case 1:
                        res = methAE();
                        break;
                    case 2:
                        res = methAIOOBE();
                        break;
                    case 3:
                        res = methULE();
                        break;
                    case 4:
                        res = methUserException();
                        break;
                }
                if (res != Result.PASS) {
                    log.add("in methAE(): invocation of " + next + " resulted \"FAILED\"");
                    return res;
                }

                // Cause an AE
                int i = count / (count / (2 * invCount));
            } catch (ArithmeticException e) {
                return Result.PASS;
            } catch (Throwable t) {
                log.add("in methAE(): invocation of " + next + " caused an unexpected exception " + t.getClass().getName());
                return Result.FAIL;
            }
            log.add("in methAE: AE wasn't thrown");
            return Result.FAIL;
        }
        private int methAIOOBE() {
            int next = -1;
            try {
                count++;
                if (count == invCount) {
                    return Result.PASS;
                }
                next = rnd.nextInt(5);
                int res = Result.FAIL;
                switch (next) {
                    case 0:
                        res = methNPE();
                        break;
                    case 1:
                        res = methAE();
                        break;
                    case 2:
                        res = methAIOOBE();
                        break;
                    case 3:
                        res = methULE();
                        break;
                    case 4:
                        res = methUserException();
                        break;
                }
                if (res != Result.PASS) {
                    log.add("in methAIOOBE(): invocation of " + next + " resulted \"FAILED\"");
                    return res;
                }

                // Cause an AIOOBE
                int[] arr = new int[count];
                int a = arr[invCount];
            } catch (ArrayIndexOutOfBoundsException e) {
                return Result.PASS;
            } catch (Throwable t) {
                log.add("in methAIOOBE(): invocation of " + next + " caused an unexpected exception " + t.getClass().getName());
                return Result.FAIL;
            }
            log.add("in methAIOOBE: AIOOBE wasn't thrown");
            return Result.FAIL;
        }
        private int methULE() {
            int next = -1;
            try {
                count++;
                if (count == invCount) {
                    return Result.PASS;
                }
                next = rnd.nextInt(5);
                int res = Result.FAIL;
                switch (next) {
                    case 0:
                        res = methNPE();
                        break;
                    case 1:
                        res = methAE();
                        break;
                    case 2:
                        res = methAIOOBE();
                        break;
                    case 3:
                        res = methULE();
                        break;
                    case 4:
                        res = methUserException();
                        break;
                }
                if (res != Result.PASS) {
                    log.add("in methULE(): invocation of " + next + " resulted \"FAILED\"");
                    return res;
                }

                // Cause an ULE
                nativeMethod();
            } catch (UnsatisfiedLinkError e) {
                return Result.PASS;
            } catch (Throwable t) {
                log.add("in methULE(): invocation of " + next + " caused an unexpected exception " + t.getClass().getName());
                return Result.FAIL;
            }
            log.add("in methULE: ULE wasn't thrown");
            return Result.FAIL;
        }
        private int methUserException() {
            int next = -1;
            class UserException extends Exception {
                public UserException() {
                    super("User exception");
                }
            }
            try {
                count++;
                if (count == invCount) {
                    return Result.PASS;
                }
                next = rnd.nextInt(5);
                int res = Result.FAIL;
                switch (next) {
                    case 0:
                        res = methNPE();
                        break;
                    case 1:
                        res = methAE();
                        break;
                    case 2:
                        res = methAIOOBE();
                        break;
                    case 3:
                        res = methULE();
                        break;
                    case 4:
                        res = methUserException();
                        break;
                }
                if (res != Result.PASS) {
                    log.add("in methUserException(): invocation of " + next + " resulted \"FAILED\"");
                    return res;
                }

                // Cause a UserException
                throw new UserException();
            } catch (UserException e) {
                return Result.PASS;
            } catch (Throwable t) {
                log.add("in methUserException(): invocation of " + next + " caused an unexpected exception " + t.getClass().getName());
                return Result.FAIL;
            }
        }
    }

    private static class Scenario3 {
        int res = Result.FAIL;
        public int run() {            
            res = doTry11();            
            if (res != Result.PASS) {
                log.add("doTry11() failed");
                return res;
            }

            try {
                doTry12();
                log.add("Expected exception was not thrown in doTry12()");
                return Result.FAIL;
            } catch (NullPointerException e) {
            } catch (Throwable t) {
                log.add("Unexpected exception thrown in doTry12()");
                return Result.FAIL;
            }

            res = doTry2111();
            if (res != Result.PASS) {
                log.add("doTry2111() failed");
                return res;
            }

            try {
                doTry2112();
            } catch (NullPointerException e) {
            } catch (Throwable t) {
                log.add("Unexpected exception thrown in doTry2112()");
                return Result.FAIL;
            }

            try {
                doTry2121();
            } catch (NullPointerException e) {
            } catch (Throwable t) {
                log.add("Unexpected exception thrown in doTry2121()");
                return Result.FAIL;
            }

            try {
                doTry2122();
            } catch (NullPointerException e) {
            } catch (Throwable t) {
                log.add("Unexpected exception thrown in doTry2122()");
                return Result.FAIL;
            }

            try {
                doTry221();
            } catch (NullPointerException e) {
            } catch (Throwable t) {
                log.add("Unexpected exception thrown in doTry221()");
                return Result.FAIL;
            }

            try {
                doTry222();
            } catch (NullPointerException e) {
            } catch (Throwable t) {
                log.add("Unexpected exception thrown in doTry222()");
                return Result.FAIL;
            }
            return Result.PASS;
        }

        private int doTry11() {
            try {
            } catch (Throwable t) {
                log.add("Unexpected exception caught in doTry11()");
                return Result.FAIL;
            } finally {
            }
            return Result.PASS;
        }
        private void doTry12() throws MyException {
            try {
            } catch (Throwable t) {
                log.add("Unexpected exception caught in doTry12()");
                throw new MyException("Unexpected exception caught");
            } finally {
                new Hashtable().get(new Object()).toString();
            }
        }
        private int doTry2111() {
            try {
                throw new MyException("MyException thrown");
            } catch (MyException e) {
            } catch (Throwable t) {
                log.add("Unexpected exception caught in doTry2111()");
                return Result.FAIL;
            } finally {
            }
            return Result.PASS;
        }
        private void doTry2112() throws MyException {
            try {
                throw new MyException("MyException thrown");
            } catch (MyException e) {
            } catch (Throwable t) {
                log.add("Unexpected exception caught in doTry2112()");
                throw new MyException("Unexpected exception caught");
            } finally {
                new Hashtable().get(new Object()).toString();
            }
        }
        private void doTry2121() throws MyException {
            try {
                throw new MyException("MyException thrown");
            } catch (MyException e) {
                new Hashtable().get(new Object()).toString();
            } catch (Throwable t) {
                log.add("Unexpected exception caught in doTry2121()");
                throw new MyException("Unexpected exception caught");
            } finally {                
            }
        }
        private void doTry2122() throws MyException {
            try {
                throw new MyException("MyException thrown");
            } catch (MyException e) {
                throw new MyException("MyException caught");
            } catch (Throwable t) {
                log.add("Unexpected exception caught in doTry2122()");
                throw new MyException("Unexpected exception caught");
            } finally {
                new Hashtable().get(new Object()).toString();
            }
        }
        private void doTry221() throws MyException {
            try {
                new Hashtable().get(new Object()).toString();
            } catch (ClassCircularityError e) {
                log.add("Unexpected exception caught in doTry221()");
                throw new MyException("Unexpected OOME caught");
            } finally {              
            }
        }
        private void doTry222() throws MyException {
            try {
                throw new MyException("MyException thrown");
            } catch (NullPointerException e) {
                log.add("Unexpected exception caught in doTry222()");
                throw new MyException("Unexpected NPE caught");
            } finally {
                new Hashtable().get(new Object()).toString();
            }
        }
    }

    private static native void nativeMethod();

    private static class MyException extends Exception {
        public MyException(String msg) {
            super(msg);
        }
    }
}
