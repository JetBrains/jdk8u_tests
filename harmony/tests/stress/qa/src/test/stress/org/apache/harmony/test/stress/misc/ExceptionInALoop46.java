/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;
import java.util.Random;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class ExceptionInALoop46 extends TestCase {
    final static int INTERATIONS = 2048;

    final static int OUTPUT_FREQUENCY = 500;

    final static int DEEPNESS = 255;

    public static Random randomizer;

    public void test() {
        long seed = 0;

        seed = Long.parseLong(System.getProperty("org.apache.harmony.test."
                + "stress.misc." + "ExceptionInALoop46.seed"));

        if (seed == 0) {
            ExceptionInALoop46.randomizer = new Random();
        } else {
            ExceptionInALoop46.randomizer = new Random(seed);
        }

        for (int i = 0; i < INTERATIONS; i++) {
            // throwing Exception from public method
            try {
                thrower(ExceptionInALoop46.randomizer.nextInt(DEEPNESS - 1) + 1);
            } catch (NullPointerException NPE) { // NullPointerException
                if (i % OUTPUT_FREQUENCY == 0)
                    ReliabilityRunner
                            .debug(NPE.toString() + " has been thrown");
            } catch (Exception2 ex2) { // Empty user-def exception
                if (i % OUTPUT_FREQUENCY == 0)
                    ReliabilityRunner
                            .debug(ex2.toString() + " has been thrown");
            } catch (Exception3 ex3) { // Non-empty user-def exception
                if (i % OUTPUT_FREQUENCY == 0)
                    ReliabilityRunner
                            .debug(ex3.toString() + " has been thrown");
            } catch (VerifyError er) { // VerifyError
                if (i % OUTPUT_FREQUENCY == 0)
                    ReliabilityRunner.debug(er.toString() + " has been thrown");
            } catch (Throwable thr) {
                ReliabilityRunner.debug(thr.toString());
                ReliabilityRunner.debug("Test fail. Random seed was: " + seed);
                ReliabilityRunner.mainTest.addError(this, thr);
            }
        }
    }

    // main thrower
    public void thrower(int DEEPNESS) throws Throwable {
        switch (ExceptionInALoop46.randomizer.nextInt(6)) {
        case 0:
            catcher11(ExceptionInALoop46.randomizer.nextInt(DEEPNESS),
                    ExceptionInALoop46.randomizer.nextInt(DEEPNESS)); // public-public
        case 1:
            catcher12(ExceptionInALoop46.randomizer.nextInt(DEEPNESS),
                    ExceptionInALoop46.randomizer.nextInt(DEEPNESS)); // protected-protected
        case 2:
            catcher13(ExceptionInALoop46.randomizer.nextInt(DEEPNESS),
                    ExceptionInALoop46.randomizer.nextInt(DEEPNESS)); // private-private
        case 3:
            catcher14(ExceptionInALoop46.randomizer.nextInt(DEEPNESS),
                    ExceptionInALoop46.randomizer.nextInt(DEEPNESS)); // static:
        // public-public
        case 4:
            catcher15(ExceptionInALoop46.randomizer.nextInt(DEEPNESS),
                    ExceptionInALoop46.randomizer.nextInt(DEEPNESS)); // static:
        // protected-protected
        case 5:
            catcher16(ExceptionInALoop46.randomizer.nextInt(DEEPNESS),
                    ExceptionInALoop46.randomizer.nextInt(DEEPNESS)); // static:
        // private-private
        }
    }

    // catchers
    public void catcher11(int rec1, int rec2) throws Throwable {
        try {
            if (rec2 > 0) {
                if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                    catcher11(rec1, rec2 - 1);
                } else
                    catcher11k(rec1, rec2 - 1);
            } else
                throwSome();
        } catch (Throwable thr) {
            throw thr;
        }
    }

    public void catcher11k(int rec1, int rec2) throws Throwable {
        if (rec2 > 0) {
            if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                catcher11(rec1, rec2 - 1);
            } else
                catcher11k(rec1, rec2 - 1);
        } else {
            if (rec1 > 0) {
                catcher11k(rec1 - 1, rec2);
            } else
                throwSome();
        }
    }

    protected void catcher12(int rec1, int rec2) throws Throwable {
        try {
            if (rec2 > 0) {
                if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                    catcher12(rec1, rec2 - 1);
                } else
                    catcher12k(rec1, rec2 - 1);
            } else
                throwSome();
        } catch (Throwable thr) {
            throw thr;
        }
    }

    protected void catcher12k(int rec1, int rec2) throws Throwable {
        if (rec2 > 0) {
            if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                catcher12(rec1, rec2 - 1);
            } else
                catcher12k(rec1, rec2 - 1);
        } else {
            if (rec1 > 0) {
                catcher12k(rec1 - 1, rec2);
            } else
                throwSome();
        }
    }

    private void catcher13(int rec1, int rec2) throws Throwable {
        try {
            if (rec2 > 0) {
                if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                    catcher13(rec1, rec2 - 1);
                } else
                    catcher13k(rec1, rec2 - 1);
            } else
                throwSome();
        } catch (Throwable thr) {
            throw thr;
        }
    }

    private void catcher13k(int rec1, int rec2) throws Throwable {
        if (rec2 > 0) {
            if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                catcher13(rec1, rec2 - 1);
            } else
                catcher13k(rec1, rec2 - 1);
        } else {
            if (rec1 > 0) {
                catcher13k(rec1 - 1, rec2);
            } else
                throwSome();
        }
    }

    public static void catcher14(int rec1, int rec2) throws Throwable {
        try {
            if (rec2 > 0) {
                if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                    catcher14(rec1, rec2 - 1);
                } else
                    catcher14k(rec1, rec2 - 1);
            } else
                throwSomeStatic();
        } catch (Throwable thr) {
            throw thr;
        }
    }

    public static void catcher14k(int rec1, int rec2) throws Throwable {
        if (rec2 > 0) {
            if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                catcher14(rec1, rec2 - 1);
            } else
                catcher14k(rec1, rec2 - 1);
        } else {
            if (rec1 > 0) {
                catcher14k(rec1 - 1, rec2);
            } else
                throwSomeStatic();
        }
    }

    protected static void catcher15(int rec1, int rec2) throws Throwable {
        try {
            if (rec2 > 0) {
                if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                    catcher15(rec1, rec2 - 1);
                } else
                    catcher15k(rec1, rec2 - 1);
            } else
                throwSomeStatic();
        } catch (Throwable thr) {
            throw thr;
        }
    }

    protected static void catcher15k(int rec1, int rec2) throws Throwable {
        if (rec2 > 0) {
            if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                catcher15(rec1, rec2 - 1);
            } else
                catcher15k(rec1, rec2 - 1);
        } else {
            if (rec1 > 0) {
                catcher15k(rec1 - 1, rec2);
            } else
                throwSomeStatic();
        }
    }

    private static void catcher16(int rec1, int rec2) throws Throwable {
        try {
            if (rec2 > 0) {
                if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                    catcher16(rec1, rec2 - 1);
                } else
                    catcher16k(rec1, rec2 - 1);
            } else
                throwSomeStatic();
        } catch (Throwable thr) {
            throw thr;
        }
    }

    private static void catcher16k(int rec1, int rec2) throws Throwable {
        if (rec2 > 0) {
            if (ExceptionInALoop46.randomizer.nextInt(2) == 1) {
                catcher16(rec1, rec2 - 1);
            } else
                catcher16k(rec1, rec2 - 1);
        } else {
            if (rec1 > 0) {
                catcher16k(rec1 - 1, rec2);
            } else
                throwSomeStatic();
        }
    }

    // throwers
    //
    public void throwSome() throws Throwable {
        switch (ExceptionInALoop46.randomizer.nextInt(28)) {
        case 0:
            thrower11(); // public method, NPE
        case 1:
            thrower12(); // protected method, NPE
        case 2:
            thrower13(); // private method, NPE
        case 3:
            thrower21(); // public method, user-def empty exception
        case 4:
            thrower22(); // protected method, user-def empty exception
        case 5:
            thrower23(); // private method, user-def empty exception
        case 6:
            thrower31(); // public method, user-def non-empty exception
        case 7:
            thrower32(); // protected method, user-def non-empty exception
        case 8:
            thrower33(); // private method, user-def non-empty exception
        case 9:
            thrower41(); // public method, VerifyError
        case 10:
            thrower42(); // protected method, VerifyError
        case 11:
            thrower43(); // private method, VerifyError
        case 12:
            thrower51(); // public static method, NPE
        case 13:
            thrower52(); // protected static method, NPE
        case 14:
            thrower53(); // private static method, NPE
        case 15:
            thrower61(); // public static method, user-def empty exception
        case 16:
            thrower62(); // protected static method, user-def empty exception
        case 17:
            thrower63(); // private static method, user-def empty exception
        case 18:
            thrower71(); // public static method, user-def non-empty
        // exception
        case 19:
            thrower72(); // protected static method, user-def non-empty
        // exception
        case 20:
            thrower73(); // private static method, user-def non-empty
        // exception
        case 21:
            thrower81(); // public static method, VerifyError
        case 22:
            thrower82(); // protected static method, VerifyError
        case 23:
            thrower83(); // private static method, VerifyError
        case 24:
            thrower91(); // constructor, NPE
        case 25:
            thrower101(); // constructor, user-def empty exception
        case 26:
            thrower111(); // constructor, user-def non-empty exception
        case 27:
            thrower121(); // constructor, VerifyError
        }
    }

    public static void throwSomeStatic() throws Throwable {
        switch (ExceptionInALoop46.randomizer.nextInt(16)) {
        case 0:
            thrower51(); // public static method, NPE
        case 1:
            thrower52(); // protected static method, NPE
        case 2:
            thrower53(); // private static method, NPE
        case 3:
            thrower61(); // public static method, user-def empty exception
        case 4:
            thrower62(); // protected static method, user-def empty exception
        case 5:
            thrower63(); // private static method, user-def empty exception
        case 6:
            thrower71(); // public static method, user-def non-empty
        // exception
        case 7:
            thrower72(); // protected static method, user-def non-empty
        // exception
        case 8:
            thrower73(); // private static method, user-def non-empty
        // exception
        case 9:
            thrower81(); // public static method, VerifyError
        case 10:
            thrower82(); // protected static method, VerifyError
        case 11:
            thrower83(); // private static method, VerifyError
        case 12:
            thrower91s(); // constructor, NPE
        case 13:
            thrower101s(); // constructor, user-def empty exception
        case 14:
            thrower111s(); // constructor, user-def non-empty exception
        case 15:
            thrower121s(); // constructor, VerifyError
        }
    }

    public void thrower11() throws NullPointerException {
        throw new NullPointerException();
    }

    protected void thrower12() throws NullPointerException {
        throw new NullPointerException();
    }

    private void thrower13() throws NullPointerException {
        throw new NullPointerException();
    }

    public void thrower21() throws Exception2 {
        throw new Exception2();
    }

    protected void thrower22() throws Exception2 {
        throw new Exception2();
    }

    private void thrower23() throws Exception2 {
        throw new Exception2();
    }

    public void thrower31() throws Exception3 {
        throw new Exception3();
    }

    protected void thrower32() throws Exception3 {
        throw new Exception3();
    }

    private void thrower33() throws Exception3 {
        throw new Exception3();
    }

    public void thrower41() throws VerifyError {
        throw new VerifyError();
    }

    protected void thrower42() throws VerifyError {
        throw new VerifyError();
    }

    private void thrower43() throws VerifyError {
        throw new VerifyError();
    }

    public static void thrower51() throws NullPointerException {
        throw new NullPointerException();
    }

    protected static void thrower52() throws NullPointerException {
        throw new NullPointerException();
    }

    private static void thrower53() throws NullPointerException {
        throw new NullPointerException();
    }

    public static void thrower61() throws Exception2 {
        throw new Exception2();
    }

    protected static void thrower62() throws Exception2 {
        throw new Exception2();
    }

    private static void thrower63() throws Exception2 {
        throw new Exception2();
    }

    public static void thrower71() throws Exception3 {
        throw new Exception3();
    }

    protected static void thrower72() throws Exception3 {
        throw new Exception3();
    }

    private static void thrower73() throws Exception3 {
        throw new Exception3();
    }

    public static void thrower81() throws VerifyError {
        throw new VerifyError();
    }

    protected static void thrower82() throws VerifyError {
        throw new VerifyError();
    }

    private static void thrower83() throws VerifyError {
        throw new VerifyError();
    }

    public void thrower91() throws NullPointerException {
        new Thrower9();
    }

    public void thrower101() throws Exception2 {
        new Thrower10();
    }

    public void thrower111() throws Exception3 {
        new Thrower11();
    }

    public void thrower121() throws VerifyError {
        new Thrower12();
    }

    public static void thrower91s() throws NullPointerException {
        new Thrower9();
    }

    public static void thrower101s() throws Exception2 {
        new Thrower10();
    }

    public static void thrower111s() throws Exception3 {
        new Thrower11();
    }

    public static void thrower121s() throws VerifyError {
        new Thrower12();
    }
}

// exceptions and classes with exceptions in constructor
class Exception2 extends Exception {
}

class Exception3 extends Exception {
    public static int count = 0;

    public Exception3() {
        Exception3.count++;
        int i = Exception3.count;
    }
}

class Thrower9 {
    public Thrower9() throws NullPointerException {
        throw new NullPointerException();
    }
}

class Thrower10 {
    public Thrower10() throws Exception2 {
        throw new Exception2();
    }
}

class Thrower11 {
    public Thrower11() throws Exception3 {
        throw new Exception3();
    }
}

class Thrower12 {
    public Thrower12() throws VerifyError {
        throw new VerifyError();
    }
}
