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
 * @author Anton Luht
 * @version $Revision: 1.3 $
 */
/*
 * Created on 01.12.2004
 *
 */
package org.apache.harmony.test.stress.api.java.io.share;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.Test;

public class PrepareTestCleanupRunner {
    public static int run(String[] args, PrepareTestCleanup runned)
            throws IOException {

        List argsList = Arrays.asList(args);
        IOMultiCase logger = (IOMultiCase) runned;

        logger.parseArgs(args);

        try {

            if (!argsList.contains("-prepare")
                    && !argsList.contains("-cleanup")
                    && !argsList.contains("-test")) { //launched
                // by
                // Harness
                // -
                // redirect
                File f = File.createTempFile(runned.getClass().getName(), null,
                        new File(Utils.TEMP_STORAGE));
                f.delete();
                f.mkdir();

                try {
                    //PREPARE
                    String[] cmdLines = Utils.constructCommandArguments(false,
                            runned.getClass(), "-prepare", new File[] { f });

                    List fullCmdLines = new ArrayList(Arrays.asList(cmdLines));
                    for (int i = 0; i < args.length; ++i) {
                        fullCmdLines.add(args[i]);
                    }

                    ProcessResult prepareResult = Utils
                            .executeProcess((String[]) fullCmdLines
                                    .toArray(new String[] {}));

                    if (prepareResult.getExitCode() != Result.PASS) {
                        return logger.fail(prepareResult.getOutput());
                    }

                    //TEST

                    cmdLines = Utils.constructCommandArguments(true, runned
                            .getClass(), "-test", new File[] { f });
                    fullCmdLines = new ArrayList(Arrays.asList(cmdLines));
                    for (int i = 0; i < args.length; ++i) {
                        fullCmdLines.add(args[i]);
                    }

                    ProcessResult testResult = Utils
                            .executeProcess((String[]) fullCmdLines
                                    .toArray(new String[] {}));

                    Test.log.info(testResult.getOutput());

                    //CLEANUP
                    cmdLines = Utils.constructCommandArguments(false, runned
                            .getClass(), "-cleanup", new File[] { f });
                    fullCmdLines = new ArrayList(Arrays.asList(cmdLines));
                    for (int i = 0; i < args.length; ++i) {
                        fullCmdLines.add(args[i]);
                    }

                    ProcessResult cleanupResult = Utils
                            .executeProcess((String[]) fullCmdLines
                                    .toArray(new String[] {}));

                    if (testResult.getExitCode() != Result.PASS) {
                        return Result.FAIL;
                    }

                    if (cleanupResult.getExitCode() != Result.PASS) {
                        return logger.fail(cleanupResult.getOutput());
                    }
                    return Result.PASS;
                } finally {
                    //                System.err.println("going to delete " +
                    // f.getAbsolutePath());
                    if (!f.delete() && f.exists()) {
                        System.err.println("failed to delete "
                                + f.getAbsolutePath());
                    }
                }
            } else { //we're in JVM launched by testDispatcher
                if (args[0].equalsIgnoreCase("-prepare")) {
                    Test.log.setLevel(logger.getLogLevel(args));
                    return runned.prepare(new File(args[1]));
                } else if (args[0].equalsIgnoreCase("-cleanup")) {
                    Test.log.setLevel(logger.getLogLevel(args));
                    return runned.cleanup(new File(args[1]));
                } else if (args[0].equalsIgnoreCase("-test")) {
                    runned.setTestDir(new File(args[1]));
                    //strip first two arguments
                    String[] testArgs = new String[args.length - 2];
                    System.arraycopy(args, 2, testArgs, 0, args.length - 2);
                    return MultiThreadRunner.run(logger, testArgs);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return logger.fail(e.getMessage());
        }
        return 0;
    }

}
