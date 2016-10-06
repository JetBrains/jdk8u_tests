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
 
package org.apache.harmony.test.func.api.javax.swing.share;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Event;
import java.awt.event.FocusEvent;

public class InstrumentedFocusEvent extends FocusEvent {
    public InstrumentedFocusEvent() {
        this(new InstrumentedJComponent(), FOCUS_GAINED);
    }
    public InstrumentedFocusEvent(Component arg0, int arg1) {
        super(arg0, arg1);
    }
    
    public Component getOppositeComponent() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.getOppositeComponent"} );
        return super.getOppositeComponent();
    }
    public boolean isTemporary() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.isTemporary"} );
        return super.isTemporary();
    }
    public String paramString() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.paramString"} );
        return super.paramString();
    }
    public Component getComponent() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.getComponent"} );
        return super.getComponent();
    }
    public int getID() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.getID"} );
        return super.getID();
    }
    protected void consume() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.consume"} );
        super.consume();
    }
    protected boolean isConsumed() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.isConsumed"} );
        return super.isConsumed();
    }
    public void setSource(Object arg0) {
        InstrumentedUILog.add(new Object[] {"FocusEvent.setSource",  arg0} );
        super.setSource(arg0);
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.toString"} );
        return super.toString();
    }
    public Object getSource() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.getSource"} );
        return super.getSource();
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"FocusEvent.hashCode"} );
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] {"FocusEvent.clone"} );
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"FocusEvent.equals",  arg0} );
        return super.equals(arg0);
    }
}
