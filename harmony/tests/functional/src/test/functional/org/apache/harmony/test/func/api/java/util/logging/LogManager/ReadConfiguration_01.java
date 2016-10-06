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

package org.apache.harmony.test.func.api.java.util.logging.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.harmony.share.Test;

/*
 * 02.11.2005
 */
public class ReadConfiguration_01 extends Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.exit(new ReadConfiguration_01().test(args));
    }

    public int test() {
        try {
            String javaHome = System.getProperty("java.home");
            String filePath = new StringBuffer(javaHome).append(
                    File.separatorChar).append("lib")
                    .append(File.separatorChar).append("logging.properties")
                    .toString();

            Properties p = new Properties();

            p.load(new FileInputStream(filePath));

            Utils.getLogManager().readConfiguration();
            return Utils.verifyLogManager(p);

        } catch (Throwable e) {
            return fail("Unhandled message: " + e.getMessage());
        }
    }

}
