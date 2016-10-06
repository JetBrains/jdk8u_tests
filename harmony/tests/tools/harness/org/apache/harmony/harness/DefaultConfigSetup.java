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
 * @version $Revision: 1.12 $
 */
package org.apache.harmony.harness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Set the values from the DefaultConfigIRValues class to the current
 * configuration
 */
public class DefaultConfigSetup {

    private final String classID = "DefaultConfigSetup";

    Logging              log     = Main.getCurCore().getInternalLogger();

    public void setDefaultValues() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tsetDefaultValues(): ";
        ConfigIR cfg = Main.getCurCore().getConfigIR();
        String tmpStr = "";

        if (cfg == null) {
            cfg = new ConfigIR();
            Main.getCurCore().setConfigIR(cfg);
            log.add(Level.WARNING, methodLogPrefix
                + "The configuration is empty. Please, check your settings.");
        }
        if (cfg.getTestSuiteRoot() == null) {
            cfg.setTestSuiteRoot(DefaultConfigIRValues.DEFAULT_TEST_SUITE_ROOT);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The value for test suite root is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.DEFAULT_TEST_SUITE_ROOT);
        }
        if (cfg.getTestedRuntime() == null) {
            cfg.setTestedRuntime(DefaultConfigIRValues.TESTED_RUNTIME);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The value for Tested runtime is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.TESTED_RUNTIME);
        }
        if (cfg.getTestedCompile() == null) {
            cfg.setTestedCompile(DefaultConfigIRValues.TESTED_COMPILE);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The value for Tested compile is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.TESTED_COMPILE);
        }
        if (cfg.getReferenceRuntime() == null) {
            cfg.setReferenceRuntime(DefaultConfigIRValues.REFERENCE_RUNTIME);
            log
                .add(
                    Level.CONFIG,
                    methodLogPrefix
                        + "The value for reference runtime is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.REFERENCE_RUNTIME);
        }
        if (cfg.getReferenceCompile() == null) {
            cfg.setReferenceCompile(DefaultConfigIRValues.TESTED_COMPILE);
            log
                .add(
                    Level.CONFIG,
                    methodLogPrefix
                        + "The value for reference compile is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.TESTED_COMPILE);
        }
        if (cfg.getGenTimeout() == 0) {
            cfg.setGenTimeout(DefaultConfigIRValues.DEFAULT_TIMEOUT);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The value for general timeout is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.DEFAULT_TIMEOUT);
        }
        if (cfg.getTestResultRoot() == null) {
            cfg.setTestResultRoot(DefaultConfigIRValues.RESULT_DIR);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The value to store the test results is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.RESULT_DIR);
        }
        if (cfg.getTempStorage() == null) {
            cfg.setTempStorage(DefaultConfigIRValues.DEFAULT_TMP_DIR);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The value for temporary directory is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.DEFAULT_TMP_DIR);
        }

        //set up default mapping for default runners pairs in the
        // RunnersMapping
        if (cfg.getPlugins().get(DefaultConfigIRValues.EUNIT_PLUGIN_CLASS) == null) {
            HashMap tmpHM = cfg.getPlugins();
            tmpHM
                .put(DefaultConfigIRValues.EUNIT_PLUGIN_CLASS, new ArrayList());
            cfg.setPlugins(tmpHM);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The values for set of execution units are undefined. Please, check your settings.");
        }
        if (((ArrayList)cfg.getPlugins().get(
            DefaultConfigIRValues.EUNIT_PLUGIN_CLASS)).size() <= 1) {
            ArrayList tmpAL = new ArrayList();
            tmpAL.add(DefaultConfigIRValues.EUNIT_CLASS_NAME);
            tmpAL.add(new HashMap());
            cfg.setPluginProperties(DefaultConfigIRValues.EUNIT_PLUGIN_CLASS,
                tmpAL);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The value for set of execution units is empty. Please, check your settings.");
        }
        HashMap mapHM = (HashMap)((ArrayList)cfg.getPlugins().get(
            DefaultConfigIRValues.EUNIT_PLUGIN_CLASS)).get(1);
        for (int i = 0; i < DefaultConfigIRValues.RUNNERS_MAP.length; i++) {
            if (mapHM.containsKey(DefaultConfigIRValues.RUNNERS_MAP[i][0])) {
                HashMap tmp = (HashMap)mapHM
                    .get(DefaultConfigIRValues.RUNNERS_MAP[i][0]);
                if (!tmp.containsKey("map-class")) {
                    ((HashMap)mapHM
                        .get(DefaultConfigIRValues.RUNNERS_MAP[i][0])).put(
                        "map-class", DefaultConfigIRValues.RUNNERS_MAP[i][1]);
                    log.add(Level.INFO, methodLogPrefix
                        + "The class for execution unit "
                        + DefaultConfigIRValues.RUNNERS_MAP[i][0]
                        + " is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.RUNNERS_MAP[i][1]);
                }
            } else {
                HashMap tmp = new HashMap();
                tmp.put("map-class", DefaultConfigIRValues.RUNNERS_MAP[i][1]);
                mapHM.put(DefaultConfigIRValues.RUNNERS_MAP[i][0], tmp);
                log.add(Level.INFO, methodLogPrefix
                    + "The class for execution unit "
                    + DefaultConfigIRValues.RUNNERS_MAP[i][0]
                    + " is undefined. Please, check your settings.\n"
                    + "Use default value "
                    + DefaultConfigIRValues.RUNNERS_MAP[i][1]);
            }
        }
        ArrayList mapAL = new ArrayList();
        mapAL.add(DefaultConfigIRValues.EUNIT_CLASS_NAME);
        mapAL.add(mapHM);
        cfg
            .setPluginProperties(DefaultConfigIRValues.EUNIT_PLUGIN_CLASS,
                mapAL);

        //set up default report codes
        if (cfg.getPlugins().get(DefaultConfigIRValues.REPORTER_PLUGIN_NAME) == null) {
            HashMap tmpHM = cfg.getPlugins();
            tmpHM.put(DefaultConfigIRValues.REPORTER_PLUGIN_NAME,
                new ArrayList());
            cfg.setPlugins(tmpHM);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The value for reporter plugin is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.REPORTER_PLUGIN_NAME);
        }
        if (((ArrayList)cfg.getPlugins().get(
            DefaultConfigIRValues.REPORTER_PLUGIN_NAME)).size() <= 1) {
            ArrayList tmpAL = new ArrayList();
            tmpAL.add(DefaultConfigIRValues.REPORTER_PLUGIN_CLASS);
            tmpAL.add(new HashMap());
            cfg.setPluginProperties(DefaultConfigIRValues.REPORTER_PLUGIN_NAME,
                tmpAL);
            log
                .add(
                    Level.INFO,
                    methodLogPrefix
                        + "The configuration for reporter plugin is empty. Please, check your settings.");
        }
        //get the class name
        tmpStr = (String)((ArrayList)cfg.getPlugins().get(
            DefaultConfigIRValues.REPORTER_PLUGIN_NAME)).get(0);
        //get the parameters
        HashMap repHM = (HashMap)((ArrayList)cfg.getPlugins().get(
            DefaultConfigIRValues.REPORTER_PLUGIN_NAME)).get(1);
        if (!repHM.containsKey("status")) {
            HashMap tmp = new HashMap();
            for (int i = 0; i < DefaultConfigIRValues.DEFAULT_EXEC_STATUSES.length; i++) {
                ArrayList tmpAL = new ArrayList();
                tmpAL.add(DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][1]);
                tmp.put(DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][0],
                    tmpAL);
                log.add(Level.INFO, methodLogPrefix
                    + "The value for reporter status "
                    + DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][0]
                    + " is undefined. Please, check your settings.\n"
                    + "Use default value "
                    + DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][1]);
            }
            repHM.put("status", tmp);
        } else {
            HashMap tmp = (HashMap)repHM.get("status");
            for (int i = 0; i < DefaultConfigIRValues.DEFAULT_EXEC_STATUSES.length; i++) {
                if (!tmp
                    .containsKey(DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][0])) {
                    ArrayList tmpAL = new ArrayList();
                    tmpAL
                        .add(DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][1]);
                    tmp.put(DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][0],
                        tmpAL);
                    log.add(Level.INFO, methodLogPrefix
                        + "The value for reporter status "
                        + DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][0]
                        + " is undefined. Please, check your settings.\n"
                        + "Use default value "
                        + DefaultConfigIRValues.DEFAULT_EXEC_STATUSES[i][1]);
                }
            }
        }
        ArrayList repAL = new ArrayList();
        //if name was defined - OK, else use default
        if (tmpStr.length() > 0) {
            repAL.add(tmpStr);
        } else {
            repAL.add(DefaultConfigIRValues.REPORTER_PLUGIN_CLASS);
        }
        repAL.add(repHM);
        cfg.setPluginProperties(DefaultConfigIRValues.REPORTER_PLUGIN_NAME,
            repAL);
    }

}