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
/*
 * Created on 03.06.2005
 */
package org.apache.harmony.test.func.api.javax.swing.share.LookAndFeels;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.UIDefaults;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 */
public class BLAFImpl extends BasicLookAndFeel implements ConcreteLAFPublic {

    public String getDescription() {
        return null;
    }

    public String getID() {
        return null;
    }

    public String getName() {
        return null;
    }

    public boolean isNativeLookAndFeel() {
        return false;
    }

    public boolean isSupportedLookAndFeel() {
        return false;
    }

    public void initClassDefaults(UIDefaults arg0) {
        super.initClassDefaults(arg0);
    }

    public void initComponentDefaults(UIDefaults arg0) {
        super.initComponentDefaults(arg0);
    }

    public void initSystemColorDefaults(UIDefaults arg0) {
        super.initSystemColorDefaults(arg0);
    }

    public Action createAudioAction(Object arg0) {
        return super.createAudioAction(arg0);
    }

    public ActionMap getAudioActionMap() {
        return super.getAudioActionMap();
    }

    public void playSound(Action arg0) {
        super.playSound(arg0);
    }
}
