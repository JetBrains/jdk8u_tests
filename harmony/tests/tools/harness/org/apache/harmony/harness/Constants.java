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

public class Constants {

    public static final String NO_VALUE_MSG                    = "Unspecified";

    //INTERNAL_TIMEOUT * INTERNAL_MULTIPLYER = millisecond in seconds
    public static final int    INTERNAL_TIMEOUT                = 100;
    public static final int    INTERNAL_MULTIPLYER             = 10;

    /*
     * the timeout for threads to interrupt
     */
    public static final int    WAIT_TIME                       = INTERNAL_TIMEOUT * 10;

    public static final char   INTERNAL_FILE_SEP_C             = '/';

    public static final String INTERNAL_FILE_SEP               = "/";

    public static final char   FILE_SEP_C_TO_SUBS              = '\\';

    /* delimiter between test ID and testcase ID */
    public static final String MULTICASE_TEST_CASE_DELIMITER   = "@";

    /* delimiter between testcases */
    public static final String TEST_CASE_DELIMITER             = ",";

    /* delimiter between plugin name, parameter name and option name */
    public static final String PLUGIN_CMD_LINE_PARAM_SEPARATOR = ":";

    /*
     * start of comments in the include/ exclude files. Only 'one-line' comments
     * are supported
     */
    public static final String START_COMMENT                   = "//";

    /* start of file extension */
    public static final String START_FILE_EXTENSION            = ".";

    /* symbols to decode test params from the current configuration */
    public static final String PARAM_SEPARATOR                 = "$";
    public static final String PARAM_START                     = "{";
    public static final String PARAM_END                       = "}";

    /*
     * name of environment variable for general (common) options of vm to add
     * to all vm to test run. Options are separated by whitespace, tabulation
     * or ','. Defined into cfg_env file or command line.
     */
    public static final String GEN_VM_OPT                      = "generalVMOption";

    /*
     * name of environment variable for boot class path options of vm to add to
     * all vm to test run. Defined into cfg_env file or command line.
     */
    public static final String BOOT_PATH                       = "bootClassPath";

    /*
     * name of environment variable for number of repetition of each test run.
     * Defined into cfg_env file or command line.
     */
    public static final String REPLAY_NUMBER                   = "replayNumber";

    /*
     * name of environment variable for general (common) environment variable
     * that will be added as environment to each vm to test run. Defined into
     * cfg_env file or command line.
     */
    public static final String GEN_ENV                         = "generalEnv";

    /*
     * name of environment variable to inherit system environment variables that
     * will be added as environment to each vm to test run. Inheritance
     * disabled by default. To enable should be set to 'true'/'yes'. Defined
     * into cfg_env file or command line.
     */
    public static final String INHERIT_ENV                     = "inheritEnv";

    /*
     * prefix to add execution option from command line to the option with the
     * same name from the configuration file By default, command line option
     * overwrite the options from configuration files
     */
    public static final String ADDOPTIONVAL                    = "add,";

    /*
     * name of environment variable for performance option. Defined into cfg_env
     * file or command line.
     */
    public static final String PERF_OPT                        = "performance";

    /*
     * name of environment variable for option 'need system info'. When this
     * option set to 'true' some info about memory will be added to test run
     * report. Defined into cfg_env file or command line.
     */
    public static final String SYSTEM_OPT                      = "needsysinfo";

    /*
     * name of environment variable for intertest timeout option. Defined into
     * cfg_env file or command line.
     */
    public static final String INTER_TESTS_TIMEOUT             = "interteststimeout";

    /*
     * name of environment variable for max counter before MCore shutdown.
     * Defined into cfg_env file or command line.
     */
    public static final String MAX_TESTS_SAME_CNT              = "maxtestsinsame";

    /*
     * default value for the output (result) files
     */
    public static final String RESULTS_EXT                     = ".thr";
}