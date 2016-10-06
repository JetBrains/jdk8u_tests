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

package org.apache.harmony.test.share.reg;

import java.util.HashMap;

/* ------------------------------------------------------------------------- */

public class DRLRegressionRunner extends RegressionRunner {

    protected String xemSwitch = decode("XemSwitch");
    protected String startLine = (xemSwitch.length() > 0 ? xemSwitch + " " : xemSwitch);
    
    private String optSwitch = startLine + decode("OptSwitch");
    private String jetSwitch = startLine + decode("JetSwitch");
    private String serverSwitch = startLine + decode("ServerSwitch");
    private String serverStaticSwitch = startLine + decode("ServerStaticSwitch");
    private String clientSwitch = startLine + decode("ClientSwitch");
    protected String intSwitch = decode("IntSwitch");

    String HLOConf="-Djit.CS_OPT.path.optimizer=ssa,devirt,inline,purge,simplify,uce,dce,abcd-,lazyexc,memopt,simplify,uce,dce,lower,dessa,statprof,markglobals";
    
    String [] HLOParams = new String[] {"abcd", "guardedDevirt", "inline", "osr", "skip", "time"};
    String [] HLOSwitches = new String[] {"abcd", "devirt", "inline", "", "", ""};
    
    SwitchesMap HLOMap = new SwitchesMap(HLOParams, HLOSwitches);
    
    protected boolean showOptions = true;
    protected boolean addCP = true;
    
    String jitrinoOptions, HLOOptions;
    
    public String getCommand(String [] args, CrashTest test) {
        if (args.length == 0) {
            result.setOutMsg("WARNING: Test command is not set!");
        }
    
        init();
        
        /*
         * result = "<runtime path> -cp <classpath>"
         * Please, note: working directory for tests is not the root TH 
         * directory; workdir is TH temporary directory instead. So, if we want
         * to use relative path to the tested java, we need to take in 
         * attention this fact.
         */
        String sep = System.getProperty("file.separator");
        String ret = (args.length == 0) ? "java" : args[0];

        /*
         * result = "<runtime path> -cp <classpath> <Jitrino switches>"
         * Process input arguments and combine used JIT switches if necessary 
         */
        int  i;
        for (i = 1; i < args.length; i++) {
            if ((args[i] != null) && (args[i].startsWith("~"))) {
                setOption(args[i], test);
            } else {
                break;
            }
        }
        
        /* Add Jitrino options */
        if (jitrinoOptions.length() > 0) {
            ret += jitrinoOptions;
        }

        if (! HLOOptions.equals(HLOConf)) {
            ret += " " + HLOOptions;
        }
        
        if (showOptions) {
            ret += " " + generalVMOptions;
        }

        if (addCP) {
            ret += " -classpath " + test.getClassPath();
        }
        
        /* Add any arguments but ~-parameters */
        for ( ; i < args.length; i++) {
            ret += " " + args[i];
        }
            
        /* Add test name at the end of command line if this is necessary */
        if (test.needTestName) {
            ret += " " + test.testName;
        }

        return ret;
    }

    /* 
     * ~-parameters analize
     */
    protected boolean setOption(String option, CrashTest test) {
           try {
               if (testMode == HANG_MODE) {
                   ((HangTest) test).timeout =
                           Integer.parseInt(testTimeout) * ((HangTest) test).hangTimeoutFactor;
               }

               if (test.setOption(option, false)) {
                return true;
            } else if (HLOMap.containsKey((Object) HLOMap.getKey(option))) {
                HLOOptions = HLOMap.modifyParams(HLOOptions, option);
                return true;
            } else if (option.startsWith("~verifyLevel=")) {
                return true;
            } else if (option.equals("~jet")) {
                jitrinoOptions += " " + jetSwitch;
                return true;
            } else if (option.equals("~opt")) {
                jitrinoOptions += " " + optSwitch;
                return true;
            } else if (option.equals("~server")) {
                jitrinoOptions += " " + serverSwitch;
                return true;
            } else if (option.equals("~server_static")) {
                jitrinoOptions += " " + serverStaticSwitch;
                return true;
            } else if (option.equals("~client")) {
                jitrinoOptions += " " + clientSwitch;
                return true;
            } else if (option.equals("~int")) {
                jitrinoOptions += " " + intSwitch;
                return true;
            } else if (option.equals("~noVersion")) {
                showOptions = false;
                return true;
            } else if (option.equals("~noCP")) {
                addCP = false;
                return true;
            } else {
                   result.setOutMsg("WARNING! " + option + " is unknown option!");
            }
        } catch (IndexOutOfBoundsException e) {
               result.setOutMsg("Unexpected exception: " + e);
        }
        
        return false;
    }

    protected void init() {
        jitrinoOptions = "";
        HLOOptions=HLOConf;
        showOptions = true;
        addCP = true;
        testTimeout = decode("TestTimeout");
    }
    
    /* 
     * Returns current classpath 
     */
    protected String getClassPath() {
        return System.getProperty("java.class.path");
    }

    class SwitchesMap extends HashMap {
        public SwitchesMap(String [] params, String [] switches) {
            if ((switches.length > 0) && 
                    (switches.length >= params.length)) {
                for (int i=0; i < switches.length; i++) {
                    put(params[i], switches[i]);
                }
            }
        }

        public String modifyParams(String s, String key) {
            String value = (String) get((Object) getKey(key));
            boolean on = isOn(key);
        
            if (value.equals("")) {
                return s;
            } else {
                if (s.contains(value + "-")) {
                    return on ? s.replace(value + "-", value) : s;
                } else if (s.contains(value + "+")) {
                    return on ? s : s.replace(value + "+", value + "-");
                } else if (s.endsWith(value)) {
                    return on ? s : s.replace(value, value + "-");
                } else {
                    return s;
                }
            }
        }
    
        public String getKey(String s) {
            return ((s.replaceFirst("~", "")).
                replaceFirst("On", "")).
                replaceFirst("Off",""); 
        }
    
        boolean isOn(String s) {
            return s.endsWith("On");
        }
    }

}
