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
import java.awt.Point;
import java.awt.event.MouseEvent;


public class InstrumentedMouseEvent extends MouseEvent {

    public InstrumentedMouseEvent() {
        this(MOUSE_PRESSED);
    }

    public InstrumentedMouseEvent(int id) {
        this(new InstrumentedJTextComponent(), id, System.currentTimeMillis(), BUTTON1_MASK, 0, 0, 1, false);
    }

    public InstrumentedMouseEvent(Component arg0, int arg1, long arg2, int arg3, int arg4, int arg5, int arg6, boolean arg7) {
        super(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        
    }
    
    

    public int getButton() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getButton"} );
        return super.getButton();
    }
    public int getClickCount() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getClickCount"} );
        return super.getClickCount();
    }
    public Point getPoint() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getPoint"} );
        return super.getPoint();
    }
    public int getX() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getX"} );
        return super.getX();
    }
    public int getY() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getY"} );
        return super.getY();
    }
    public boolean isPopupTrigger() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.isPopupTrigger"} );
        return super.isPopupTrigger();
    }
    public String paramString() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.paramString"} );
        return super.paramString();
    }
    public synchronized void translatePoint(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] {"MouseEvent.translatePoint", "" +  arg0, "" +  arg1} );
        super.translatePoint(arg0, arg1);
    }
    public int getModifiers() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getModifiers"} );
        return super.getModifiers();
    }
    public int getModifiersEx() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getModifiersEx"} );
        return super.getModifiersEx();
    }
    public long getWhen() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getWhen"} );
        return super.getWhen();
    }
    public void consume() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.consume"} );
        super.consume();
    }
    public boolean isAltDown() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.isAltDown"} );
        return super.isAltDown();
    }
    public boolean isAltGraphDown() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.isAltGraphDown"} );
        return super.isAltGraphDown();
    }
    public boolean isConsumed() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.isConsumed"} );
        return super.isConsumed();
    }
    public boolean isControlDown() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.isControlDown"} );
        return super.isControlDown();
    }
    public boolean isMetaDown() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.isMetaDown"} );
        return super.isMetaDown();
    }
    public boolean isShiftDown() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.isShiftDown"} );
        return super.isShiftDown();
    }
    public Component getComponent() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getComponent"} );
        return super.getComponent();
    }
    public int getID() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getID"} );
        return super.getID();
    }
    public void setSource(Object arg0) {
        InstrumentedUILog.add(new Object[] {"MouseEvent.setSource",  arg0} );
        super.setSource(arg0);
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.toString"} );
        return super.toString();
    }
    public Object getSource() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.getSource"} );
        return super.getSource();
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"MouseEvent.hashCode"} );
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] {"MouseEvent.clone"} );
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"MouseEvent.equals",  arg0} );
        return super.equals(arg0);
    }
}
