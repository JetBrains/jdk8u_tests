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

package org.apache.harmony.test.stress.misc;

import java.io.File;
import java.io.IOException;

/**
 * @author Vladimir Nenashev
 * @version $Revision: 1.4 $
 */

public class TempFilesInHookTest {

    /**
     * Number of temporary files to be created in a shutdown hook
     */
    private static final int NUMBER_OF_FILES = 65535;

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new TempFilesInHookThread());
        System.exit(0);
    }

    /**
     * Class implementing shutdown hook functionality
     */
    private static class TempFilesInHookThread extends Thread {
        public void run() {

            /* java.io.File object representing current directory */
            File currentDir = new File(".");

            /*
             * Create temporary files and request to delete them when VM exits
             */
            for (int i = 0; i < NUMBER_OF_FILES; i++) {

                if (i % 1000 == 0) {
                    System.out.println("i=" + i);
                }

                try {
                    File file = File.createTempFile("temp", "temp", currentDir);
                } catch (IOException e) {
                    System.err.println("Cannot create temporary file");
                    break;
                }
            }
        }
    }
}
