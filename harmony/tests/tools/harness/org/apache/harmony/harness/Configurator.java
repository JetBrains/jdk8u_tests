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
 * @version $Revision: 1.6 $
 */
package org.apache.harmony.harness;

import java.io.Serializable;

public interface Configurator extends Serializable {

    /**
     * Create internal representation of configuration from external data. Note,
     * the harness and test suite configurations depends on environment
     * configuration.
     * 
     * @param cfgPath path to the configuration files for environment
     *        (cfg_env.xml) and test suite (cfg_ts.xml)
     * @return true if the operation was successful else false
     */
    boolean createConfiguration(String cfgPath) throws ConfigurationException;

    /**
     * Create internal representation of configuration from external data.
     * 
     * @param cfgPath path to the configuration files for environment
     *        (cfg_env.xml)
     * @return true if the operation was successful else false
     */
    boolean loadTEConfig(String cfgPath) throws ConfigurationException;

    /**
     * Create internal representation of configuration from external data. The
     * path to the configuration files for test suite (cfg_ts.xml) is defined in
     * the cfg_env.xml file or through the command line
     * 
     * @return true if the operation was successful else false
     */
    boolean loadTSConfig() throws ConfigurationException;

    /**
     * Remove all 'key' from configuration.
     * 
     * @return true if the operation was successful else false
     */
    boolean clearConfiguration();
}