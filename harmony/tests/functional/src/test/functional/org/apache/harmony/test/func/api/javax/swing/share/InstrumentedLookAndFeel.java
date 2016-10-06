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
/*
 * Created on 13.04.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.share;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.UIDefaults;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 *
 */
public class InstrumentedLookAndFeel extends BasicLookAndFeel {
    public boolean isNativeLookAndFeel() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 1");
        return false;
    }

    public boolean isSupportedLookAndFeel() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 2");
        return true;
    }

    public String getDescription() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 3");
        return "Instrumented LookAndFeel Description";
    }

    public String getID() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 4");
        return "Instrumented LookAndFeel ID";
    }

    public String getName() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 5");
        return "Instrumented LookAndFeel Name";
    }
    protected Action createAudioAction(Object arg0) {
        InstrumentedUILog.add("InstrumentedLookAndFeel 6");
        return super.createAudioAction(arg0);
    }
    protected ActionMap getAudioActionMap() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 7");
        return super.getAudioActionMap();
    }
    public UIDefaults getDefaults() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 8");
        return super.getDefaults();
    }
    protected void initClassDefaults(UIDefaults arg0) {
        InstrumentedUILog.add("InstrumentedLookAndFeel 9");
        super.initClassDefaults(arg0);
    }
    protected void initComponentDefaults(UIDefaults arg0) {
        InstrumentedUILog.add("InstrumentedLookAndFeel 10");
        super.initComponentDefaults(arg0);
    }
    protected void initSystemColorDefaults(UIDefaults arg0) {
        InstrumentedUILog.add("InstrumentedLookAndFeel 11");
        super.initSystemColorDefaults(arg0);
    }
    protected void loadSystemColors(UIDefaults arg0, String[] arg1, boolean arg2) {
        InstrumentedUILog.add("InstrumentedLookAndFeel 12");
        super.loadSystemColors(arg0, arg1, arg2);
    }
    protected void playSound(Action arg0) {
        InstrumentedUILog.add("InstrumentedLookAndFeel 13");
        super.playSound(arg0);
    }
    public boolean getSupportsWindowDecorations() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 14");
        return super.getSupportsWindowDecorations();
    }
    public void initialize() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 15");
        super.initialize();
    }
    public void provideErrorFeedback(Component arg0) {
        InstrumentedUILog.add("InstrumentedLookAndFeel 16");
        super.provideErrorFeedback(arg0);
    }
    public String toString() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 17");
        return super.toString();
    }
    public void uninitialize() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 18");
        super.uninitialize();
    }
    public int hashCode() {
        InstrumentedUILog.add("InstrumentedLookAndFeel 19");
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        InstrumentedUILog.add("InstrumentedLookAndFeel 20");
        super.finalize();
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add("InstrumentedLookAndFeel 21");
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add("InstrumentedLookAndFeel 22");
        return super.equals(arg0);
    }
}
