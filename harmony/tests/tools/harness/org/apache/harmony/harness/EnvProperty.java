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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class EnvProperty {

    public static final String KEY_VAL_SEPARATOR = "=";
    public static final String PROP_SEPARATOR    = "\n";
    protected static final int TIMEOUT           = Constants.INTERNAL_TIMEOUT;

    /* initialization flag */
    private static boolean     initialize        = false;

    private static boolean     inheritSystemEnv  = false;

    /* array of common environment */
    private static String[]    env               = new String[0];

    /* hash map of common environment to optimize add/ remove operation */
    private static HashMap     envHM             = new HashMap();

    /* hash map of run unique environment to optimize add/ remove operation */
    private HashMap            envUniqHM         = new HashMap();

    /* list of property to remove from common environment for this uniq run */
    private HashMap            envUniqRemove     = new HashMap();

    public boolean setInheritSystemEnv(boolean inherit) {
        boolean prevVal = inheritSystemEnv;
        inheritSystemEnv = inherit;
        return prevVal;
    }

    /**
     * Get the array of environment variables to use in the program. Each
     * element is record 'KEY=VALUE'
     * 
     * @return array of environment variables or null if used default
     *         environments
     */
    public synchronized String[] getEnv() {
        if (!initialize && envUniqHM.size() == 0 && envUniqRemove.size() == 0) {
            return null;
        }
        return createUniq();
    }

    /**
     * Get the environment variable with the specified name
     * 
     * @param name variable name
     * @return variable value
     */
    public synchronized String getEnv(String name) {
        if (name == null) {
            return null;
        }
        if (envUniqHM.containsKey(name)) {
            return envUniqHM.get(name).toString();
        }
        return getCommonEnv(name);
    }

    /**
     * Add env variable to the environment. Redefine old value if exist
     * 
     * @param name variable name
     * @param value variable value
     */
    public synchronized void addEnv(String name, String value) {
        if (name != null) {
            envUniqHM.put(name, value);
        }
    }

    /**
     * Add environment variable to the environment. Redefine old value if exist
     * 
     * @param nameAndvalue - variable name and value as 'name=value'
     */
    public synchronized void addEnv(String nameAndvalue) {
        if (nameAndvalue != null) {
            int separatorIndex = nameAndvalue.indexOf(KEY_VAL_SEPARATOR);
            if (separatorIndex != -1) {
                envUniqHM.put(nameAndvalue.substring(0, separatorIndex),
                    nameAndvalue.substring(separatorIndex + 1));
            } else {
                envUniqHM.put(nameAndvalue, null);
            }
        }
    }

    /**
     * Remove the environment variable from environment. Return the value of
     * variable or 'null' if it not set.
     * 
     * @param name - variable name
     * @return variable value or null
     */
    public synchronized String removeEnv(String name) {
        if (name == null) {
            return null;
        }
        if (envUniqHM.containsKey(name)) {
            return (String)envUniqHM.remove(name);
        } else {
            envUniqRemove.put(name, null);
        }
        return null;
    }

    /**
     * Get the array of common environment variables to use in the program.
     * Each element is record 'KEY=VALUE'
     * 
     * @return array of environment variables or null if used default
     *         environments
     */
    public static synchronized String[] getCommonEnv() {
        if (!initialize) {
            init();
        }
        if (env.length != envHM.size()) {
            redefineCommonEnv();
        }
        return env;
    }

    /**
     * Get the specified of common environment variable.
     * 
     * @param name variable name
     * @return value for specified variables or null
     */
    public static synchronized String getCommonEnv(String name) {
        if (name == null) {
            return "";
        }
        if (!initialize) {
            init();
        }
        return (String)envHM.get(name);
    }

    /**
     * Add env variable to the common environment. Redefine old value if exist
     * 
     * @param name - variable name
     * @param value - variable value
     * @return previous value for specified key, or null if no value
     */
    public static synchronized String addCommonEnv(String name, String value) {
        if (name == null) {
            return null;
        }
        if (!initialize) {
            init();
        }
        return (String)envHM.put(name, value);
    }

    /**
     * Remove the environment variable from environment for all. Return the value of
     * variable or 'null' if it not set.
     * 
     * @param name - variable name
     * @return variable value or null
     */
    public static synchronized String removeCommonEnv(String name) {
        if (name == null) {
            return null;
        }
        if (!initialize) {
            init();
        }
        if (envHM.containsKey(name)) {
            return (String)envHM.remove(name);
        }
        return null;
    }

    /**
     * Clear information about common variable
     */
    public static synchronized void clear() {
        initialize = false;
        env = new String[0];
        envHM.clear();
    }

    /**
     * Clear information about run specific environment
     */
    public synchronized void clearEnv() {
        envUniqHM.clear();
    }

    private static byte[] resizeBuf(byte[] data) {
        byte[] retVal = new byte[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            retVal[i] = data[i];
        }
        return retVal;
    }

    private static void init() {
        try {
            Process p;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.indexOf("windows") != -1) {
                p = java.lang.Runtime.getRuntime().exec("cmd /c set");
            } else {
                p = java.lang.Runtime.getRuntime().exec("env");
            }
            InputStream is = p.getInputStream();
            byte[] buf = new byte[8192];
            int validEnvBufLength = 0;
            //wait for environment not more than 100 Timeout's
            for (int i = 0; i < 100; i++) {
                try {
                    p.exitValue();
                    if (is.available() > 0) {
                        int toRead = is.available();
                        while ((validEnvBufLength + toRead) > buf.length) {
                            buf = resizeBuf(buf);
                        }
                        validEnvBufLength = validEnvBufLength
                            + is.read(buf, validEnvBufLength, toRead);
                    }
                    break;
                } catch (IllegalThreadStateException ie) {
                    Thread.sleep(TIMEOUT / 10); //wait for process
                    int cnt = 0;
                    while (is.available() > 0) {
                        int toRead = is.available();
                        while ((validEnvBufLength + toRead) > buf.length) {
                            buf = resizeBuf(buf);
                        }
                        validEnvBufLength = validEnvBufLength
                            + is.read(buf, validEnvBufLength, toRead);
                        if (cnt++ > TIMEOUT / 10) { //no infinite cycles
                            break;
                        }
                        Thread.sleep(TIMEOUT / 10); //wait for process
                    }
                }
            }
            StringTokenizer st = new StringTokenizer(new String(buf, 0,
                validEnvBufLength), PROP_SEPARATOR);
            int iter = st.countTokens();
            env = new String[iter];
            for (int cnt = 0; cnt < iter; cnt++) {
                String tmp = st.nextToken().trim();
                if (tmp.indexOf(KEY_VAL_SEPARATOR) != -1) {
                    envHM.put(tmp.substring(0, tmp.indexOf(KEY_VAL_SEPARATOR)),
                        tmp.substring(tmp.indexOf(KEY_VAL_SEPARATOR) + 1, tmp
                            .length()));
                }
            }
            redefineCommonEnv();
        } catch (Throwable e) {
            //just do nothing
        }
    }

    private static void redefineCommonEnv() {
        if (inheritSystemEnv) {
            env = new String[envHM.size()];
            int num = envHM.size();
            Iterator iter = envHM.keySet().iterator();
            for (int i = 0; i < num; i++) {
                String curKey = (String)iter.next();
                env[i] = curKey + KEY_VAL_SEPARATOR + envHM.get(curKey);
            }
        }
    }

    private String[] createUniq() {
        int num;
        Iterator iter;
        if (!initialize) {
            init();
        }
        if (env.length != envHM.size()) {
            redefineCommonEnv();
        }
        if (envUniqHM.size() > 0 || envUniqRemove.size() > 0) {
            //add all values that unique for this request
            HashMap tmp = (HashMap)envUniqHM.clone();

            //copy common environment. unique settings are stored unchanged
            if (inheritSystemEnv) {
                num = envHM.size();
                iter = envHM.keySet().iterator();
                for (int i = 0; i < num; i++) {
                    String curKey = (String)iter.next();
                    if (!tmp.containsKey(curKey)
                        && !envUniqRemove.containsKey(curKey)) {
                        tmp.put(curKey, envHM.get(curKey));
                    }
                }
            }

            //create return array
            num = tmp.size();
            iter = tmp.keySet().iterator();
            String[] retVal = new String[num];
            for (int i = 0; i < num; i++) {
                String curKey = (String)iter.next();
                retVal[i] = curKey + KEY_VAL_SEPARATOR + tmp.get(curKey);
            }
            return retVal;
        }
        return env;
    }
}