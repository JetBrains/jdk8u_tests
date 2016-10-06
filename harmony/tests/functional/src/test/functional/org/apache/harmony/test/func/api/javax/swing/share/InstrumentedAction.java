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
 * Created on 27.05.2005
 *  
 */

package org.apache.harmony.test.func.api.javax.swing.share;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public class InstrumentedAction implements Action {

    public boolean isEnabled() {
        InstrumentedUILog.add(new Object[] {"Action.isEnabled"});
        return false;
    }

    public void setEnabled(boolean arg0) {
        InstrumentedUILog.add(new Object[] {"Action.setEnebled"});
    }

    public void addPropertyChangeListener(PropertyChangeListener arg0) {
        InstrumentedUILog.add(new Object[] {"Action.addPropertyChangeListener", arg0});
    }

    public void removePropertyChangeListener(PropertyChangeListener arg0) {
        InstrumentedUILog.add(new Object[] {"Action.removePropertyChangeListener", arg0});
    }

    public Object getValue(String arg0) {
        InstrumentedUILog.add(new Object[] {"Action.getValue", arg0});
        return null;
    }

    public void putValue(String arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] {"Action.putValue", arg0});
    }

    public void actionPerformed(ActionEvent arg0) {
        InstrumentedUILog.add(new Object[] {"Action.actionPerformed", arg0});
    }

}