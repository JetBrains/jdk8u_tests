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

/* ------------------------------------------------------------------------- */

public class RegressionRunner_2005 extends DRLRegressionRunner {

    private String optSwitch = "-Xem opt:";
    private String jetSwitch     = "-Xem jet:";
    private String serverSwitch = "-Xem opt:";
    private String serverStaticSwitch = "-Xem opt:";
    private String clientSwitch = "-Xem opt:";
    protected String optionsSwitch = "-Xjit";

    /* HLO variables: */
    private String skipOffSwitch   = "opt::skip=off";
    private String abcdOnSwitch    = "opt::do_abcd=on";
    private String abcdOffSwitch   = "opt::do_abcd=off";
    private String guardedDevirtOffSwitch = 
            "opt::do_guarded_devirtualization=off";
    private String inlineOffSwitch = "opt::do_inline=off";
    private String osrOffSwitch    = "opt::do_osr=off";

    /* Timer variables: */
    private String timerOnSwitch  = "time=on";

    private String verifyLevelSwitch = "ia32::verify="; 

    boolean flgJitOptions = false;

        /* 
     * ~-parameters analize
     */
    protected boolean setOption(String option, CrashTest test) {
        try {
            if (option.equals("~abcdOff")) {
                addJITOption(abcdOffSwitch);
                return true;
            } else if (option.equals("~abcdOn")) {
                addJITOption(abcdOnSwitch);
                return true;
            } else if (option.equals("~guardedDevirtOff")) {
                addJITOption(guardedDevirtOffSwitch); 
                return true;                
            } else if (option.equals("~inlineOff")) {
                addJITOption(inlineOffSwitch);
                return true;                
            } else if (option.equals("~osrOff")) {
                addJITOption(osrOffSwitch);
                return true;                
            } else if (option.equals("~skipOff")) {
                addJITOption(skipOffSwitch);
                return true;
            } else if (option.equals("~timeOn")) {
                addJITOption(timerOnSwitch);
                return true;
            } else if (option.startsWith("~verifyLevel=")) {
                addJITOption(verifyLevelSwitch + option.substring(13));
                return true;
            } else {
                return super.setOption(option, test);
            }
        } catch (IndexOutOfBoundsException e) {
            result.setOutMsg("Unexpected exception: " + e);
        }
        
        return false;
    }

    /* 
     * Add separate JIT switch to jitrinoOptions variable 
     */
    protected void addJITOption(String str) {
        jitrinoOptions += 
                (flgJitOptions ? "," : " " + optionsSwitch + " ") + str;
        flgJitOptions = true;
    }

    protected void init() {
        flgJitOptions = false;
        super.init();
    }
}
