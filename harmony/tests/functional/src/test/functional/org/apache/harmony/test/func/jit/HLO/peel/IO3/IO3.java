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
package org.apache.harmony.test.func.jit.HLO.peel.IO3;

/**
 */

/*
 * Created on 06.06.2006
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.harmony.share.Test;

public class IO3 extends Test{

    public static void main(String[] args) {
        System.exit(new IO3().test(args));
    }

    public int test() {
        log.info("Start IO3 peel test...");
        boolean result = false;
        File file = new File("HLO_peel_IO3.tmp");
        log.info("Create tmp file: " + file.getAbsolutePath());
        try {
            BufferedWriter out = new BufferedWriter(new PrintWriter(
                    new BufferedWriter(new FileWriter(file))));
            for(int j=0; j<100000; j++) {
                for(int k=0; k<10; k++) {            
                    out.write("test");
                    out.newLine();
                }
            }
            out.close();
        } catch (IOException e) {
            log.add(e);
            return fail("TEST FAILED: unexpected IOException occurred");
        }
        long length = file.length();
        log.info("File length: " + length);
        file.delete();
        String os = System.getProperty("os.name").toLowerCase();
        if ((length == 6000000) && (os.indexOf("windows") >= 0)) return pass();
        else if ((length == 5000000) && (os.indexOf("linux") >= 0)) return pass();
        else return fail("TEST FAILED: check if IO operation was " +
                    "incorrectly hoisted outside a loop");
    }
}
