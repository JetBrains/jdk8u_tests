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
 * @version $Revision: 1.18 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.util.logging.Level;

public class Config implements Configurator {

    ConfigIR             cfgIR   = new ConfigIR();
    CfgParser            cp      = new CfgParser();
    Logging              log     = Main.getCurCore().getInternalLogger();

    private final String classID = "Config";

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Configurator#clearConfiguration()
     */
    public boolean clearConfiguration() {
        try {
            cfgIR.getProperties().clear();
            cfgIR.setGenTimeout(0);
            cfgIR.setTempStorage("");
            cfgIR.setTestedRuntime("");
            cfgIR.setReferenceRuntime("");
            cfgIR.setTestSuiteRoot("");
            cfgIR.setTestResultRoot("");
            cfgIR.getTestedSubSuites().clear();
            cfgIR.getRemoteRunner().clear();
            cfgIR.getPlugins().clear();
            return true;
        } catch (Exception e) {
            log.add(Level.FINE, MessageInfo.MSG_PREFIX + classID
                + "\tclearConfiguration(): unexpected exception " + e);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Configurator#createConfiguration()
     */
    public boolean createConfiguration(String cfgPath)
        throws ConfigurationException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateConfiguration(): ";
        if (!loadTEConfig(cfgPath) || !checkTEConfig()) {
            return false;
        }
        log.add(Level.FINE, methodLogPrefix + "parse environment OK");
        loadTSConfig();
        checkTSConfig();
        log.add(Level.FINE, methodLogPrefix + "parse suite configuration OK");
        checkConfig();
        Main.getCurCore().setConfigIR(cfgIR);
        return true;
    }

    /**
     * Load test environment configuration and parse it to internal
     * representation
     * 
     * @param teConfPath - the path to the 'cfg_env.xml' file
     * @return true for successful load, otherwise false
     * @throws ConfigurationException
     */
    public boolean loadTEConfig(String teConfPath)
        throws ConfigurationException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tloadTEConfig(): ";
        if (Util.checkExistFile(teConfPath)) {
            try {
                TECfgHandler handler = new TECfgHandler(cfgIR);
                if (teConfPath.indexOf(File.separatorChar) != -1) {
                    teConfPath = teConfPath.replace(File.separatorChar,
                        Constants.INTERNAL_FILE_SEP_C);
                }
                if (teConfPath.indexOf(Constants.INTERNAL_FILE_SEP_C) != -1
                    && Util.checkExistFile(teConfPath.substring(0, teConfPath
                        .lastIndexOf(Constants.INTERNAL_FILE_SEP_C))
                        + Constants.INTERNAL_FILE_SEP_C + "cfg_env.dtd")) {
                    cp = new CfgParser("cfg_env.dtd", teConfPath.substring(0,
                        teConfPath.lastIndexOf(Constants.INTERNAL_FILE_SEP_C))
                        + Constants.INTERNAL_FILE_SEP_C + "cfg_env.dtd");
                    log.add(Level.FINE, methodLogPrefix
                        + "Set cfg_env.dtd as "
                        + teConfPath.substring(0, teConfPath
                            .lastIndexOf(Constants.INTERNAL_FILE_SEP_C))
                        + Constants.INTERNAL_FILE_SEP_C + "cfg_env.dtd");
                } else {
                    cp = new CfgParser();
                    log.add(Level.FINE, methodLogPrefix
                        + "Set link to cfg_env.dtd by default");
                }
                cp.parser(handler, new File(teConfPath));
                return true;
            } catch (Exception e) {
                throw new ConfigurationException(
                    methodLogPrefix
                        + "Can't parse or find the .dtd file for test harness environment configuration (cfg_env.dtd)");
            }
        } else if (Util.checkExistFile(teConfPath + File.separator
            + "cfg_env.xml")) {
            try {
                TECfgHandler handler = new TECfgHandler(cfgIR);
                if (Util.checkExistFile(teConfPath + File.separator
                    + "cfg_env.dtd")) {
                    cp = new CfgParser("cfg_env.dtd", teConfPath
                        + File.separator + "cfg_env.dtd");
                    log.add(Level.FINE, methodLogPrefix + "Set cfg_env.dtd as "
                        + teConfPath + File.separator + "cfg_env.dtd");
                } else {
                    cp = new CfgParser();
                    log.add(Level.FINE, methodLogPrefix
                        + "Set link to cfg_env.dtd by default");
                }
                cp.parser(handler, new File(teConfPath + File.separator
                    + "cfg_env.xml"));
                return true;
            } catch (Exception e) {
                log.add(Level.FINE, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION + e, e);
                throw new ConfigurationException(
                    methodLogPrefix
                        + "Can't parse or find the .dtd file for test harness environment configuration (cfg_env.dtd)");
            }
        } else {
            throw new ConfigurationException(
                methodLogPrefix
                    + "Can't find the file with test harness environment configuration (cfg_env.xml)");
        }
    }

    /**
     * Check test environment
     * 
     * @return true for successful checking, otherwise false
     */
    protected boolean checkTEConfig() {
        return true;
    }

    /**
     * load test suite configuration
     * 
     * @return true for successful load, otherwise false
     */
    public boolean loadTSConfig() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tloadTSConfig(): ";
        try {
            TSCfgHandler handler = new TSCfgHandler(cfgIR);
            if (Util.checkExistFile(cfgIR.getTestSuiteConfigRoot()
                + File.separator + "cfg_ts.dtd")) {
                cp = new CfgParser("cfg_ts.dtd", cfgIR.getTestSuiteConfigRoot()
                    + File.separator + "cfg_ts.dtd");
                log.add(Level.FINE, methodLogPrefix + "Set cfg_ts.dtd as "
                    + cfgIR.getTestSuiteConfigRoot() + File.separator
                    + "cfg_ts.dtd");
            } else {
                cp = new CfgParser();
                log.add(Level.FINE, methodLogPrefix
                    + "Set link to cfg_ts.dtd by default");

            }
            cp.parser(handler, new File(cfgIR.getTestSuiteConfigRoot()
                + File.separator + "cfg_ts.xml"));
            return true;
        } catch (Exception e) {
            log
                .add(
                    Level.SEVERE,
                    methodLogPrefix
                        + "The file with Test suite configuration is missed or have invalid format "
                        + e, e);
            return false;
        }
    }

    /**
     * Check test suite configuration
     * 
     * @return true for successful checking, otherwise false
     */
    protected boolean checkTSConfig() {
        return true;
    }

    /**
     * Check the all available (key,value) for configuration
     * 
     * @return true for successful checking, otherwise false
     */
    protected boolean checkConfig() throws ConfigurationException {
        return true;
    }
}
