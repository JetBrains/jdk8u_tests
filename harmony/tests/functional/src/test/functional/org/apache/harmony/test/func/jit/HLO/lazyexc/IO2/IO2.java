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
package org.apache.harmony.test.func.jit.HLO.lazyexc.IO2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 19.04.2006
 */

public class IO2 extends Test {

    private BufferedWriter out;
    private IOException ioExc;
        
    public static void main(String[] args) {
        System.exit((new IO2()).test(args));
    }
        
    public int test() {
        log.info("Start IO2 lazyexc test...");
        File file = new File("HLO_laxyexc_Test.tmp");
        try {
            out = new BufferedWriter(new PrintWriter(new BufferedWriter(new FileWriter(file))));
            for (int i=0; i<1000; i++) {
                for (int j=0; j<100; j++) {
                    try {
                        //insert some not invariant code
                        throw new TestException();
                    } catch (TestException e) {
                 
                    }
                }
            }
            out.close();
        } catch (IOException e) {
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
        long length = file.length();
        log.info("Test file length = " + length);
        file.delete();
        if (ioExc != null) return fail("TEST FAILED: unexpected " + ioExc);
        if (length == 500000) return pass();
        else return fail("TEST FAILED: test file length != 500000.\n" +
                "Check if TestException object wasn't created, " +
                "i.e. lazyexc was incorrectly performed.");
    }
        
    final class TestException extends Exception {
           
        TestException() { 
            try {
                out.write("test ");
            } catch (IOException e) {
                ioExc = e;
            }
        }
    }
}

