/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.14 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.util.logging.Level;

/**
 * Default values for main parameters
 */
public class DefaultConfigIRValues {

    /**
     * Constant for test status passed
     */
    public static final String     R_PASSED                = "104";
    /**
     * Constant for test status failed
     */
    public static final String     R_FAILED                = "105";
    /**
     * Constant for test status error
     */
    public static final String     R_ERROR                 = "106";
    /**
     * Constant for test status skipped
     */
    public static final String     R_SKIPPED               = "107";

    public static final String     REPORTER_PLUGIN_NAME    = "Reporter";
    public static final String     REPORTER_PLUGIN_CLASS   = "org.apache.harmony.harness.plugins.Reporter";
    public static final String[][] DEFAULT_EXEC_STATUSES   = {
        { "passed", R_PASSED }, { "failed", R_FAILED }, { "error", R_ERROR },
        { "moderror", R_SKIPPED }                         };

    /**
     * Constant for default logging level
     */
    public static final Level      DEFAULT_LOG_LEVEL       = Level.INFO;
    /**
     * Constant for default timeout (in seconds)
     */
    public static final int        DEFAULT_TIMEOUT         = 600;
    /**
     * Constant for default execution mode (each test in new vm)
     */
    public static final boolean    DEFAULT_EXEC_MODE       = Main.OTHER;
    /**
     * Constant for default execution location (on current computer)
     */
    public static final boolean    DEFAULT_EXEC_LOCATION   = Main.LOCAL;
    /**
     * Constant for default competition rate (number of test which can be run
     * simultaneously)
     */
    public static final int        DEFAULT_COMPETITION     = java.lang.Runtime
                                                               .getRuntime()
                                                               .availableProcessors();

    /**
     * Constant for test suite root
     */
    public static final String     DEFAULT_TEST_SUITE_ROOT = System
                                                               .getProperty("user.dir");
    /**
     * Constant for temporary directory
     */
    public static final String     DEFAULT_TMP_DIR         = System
                                                               .getProperty("user.dir")
                                                               + File.separator
                                                               + "toDelete";
    /**
     * Constant for results root
     */
    public static final String     RESULT_DIR              = System
                                                               .getProperty("user.dir")
                                                               + File.separator
                                                               + "results";
    /**
     * Constant for tested runtime
     */
    public static final String     TESTED_RUNTIME          = "java";
    /**
     * Constant for tested compile
     */
    public static final String     TESTED_COMPILE          = "javac";
    /**
     * Constant for reference runtime
     */
    public static final String     REFERENCE_RUNTIME       = "java";
    public static final String     EUNIT_PLUGIN_CLASS      = "ExecUnit";
    public static final String     EUNIT_CLASS_NAME        = "org.apache.harmony.harness.ExecUnit";
    public static final String[][] RUNNERS_MAP             = {
        { "Runtime", "org.apache.harmony.harness.plugins.Run" },
        { "RuntimeNegative", "org.apache.harmony.harness.plugins.RunNeg" },
        { "CompileNegative", "org.apache.harmony.harness.plugins.Comp" },
        { "CompileRuntime", "org.apache.harmony.harness.plugins.CompRun" },
        { "ExecuteNegative", "org.apache.harmony.harness.plugins.Exec" },
        { "Execute", "org.apache.harmony.harness.plugins.ExecRun" },
        { "MultiCase", "org.apache.harmony.harness.plugins.MultiCase" },
        { "JPDARunner", "org.apache.harmony.harness.plugins.JPDARunner" },
        { "DistributedRunner",
        "org.apache.harmony.harness.plugins.DistributedRunner" },
        { "JUExec", "org.apache.harmony.harness.plugins.JUExec" },
        { "GroupRunner", "org.apache.harmony.harness.plugins.RunGroupInThreads" } };

}