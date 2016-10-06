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
 * Created on 20.05.2005
 */
package org.apache.harmony.test.func.api.javax.swing.KeyStroke;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


public class KeyStrokeTest extends MultiCase {
    
    private void checkKeyStroke(KeyStroke keyStroke) {
        checkKeyStroke( keyStroke, "Null keystroke found");
    }
    
    private void checkKeyStroke(KeyStroke keyStroke, String mes) {
        if (keyStroke == null) {
            throw new RuntimeException(mes);
        }
    }
    
    /* getKeyStroke(int, int)*/
    public Result testGetKeyStroke0001() {
        checkKeyStroke(KeyStroke.getKeyStroke(0, 0));
        checkKeyStroke(KeyStroke.getKeyStroke((1 << 17), (1 << 17) ));
        return passed();
    }
    
    /* getKeyStroke(int, int, boolean)*/
    public Result testGetKeyStroke0002() {
        checkKeyStroke(KeyStroke.getKeyStroke(0, 0, false));
        checkKeyStroke(KeyStroke.getKeyStroke((1 << 17), (1 << 17), true ));
        return passed();
    }
    
    /* getKeyStroke(KeyEvent)*/
    public Result testGetKeyStroke0003() {
//        Component component = new Component(){};
//        KeyEvent event = new KeyEvent(component, AWTEvent.RESERVED_ID_MAX + 1, 0, 0, 0, 'a');
//        checkKeyStroke(KeyStroke.getKeyStrokeForEvent(event));
        
        try {
            try {
                checkKeyStroke(KeyStroke.getKeyStrokeForEvent(null));
                return failed("NPE excpected");
            } catch (NullPointerException e ) {}
        } catch (RuntimeException e) {
            return failed("Unexcpected exception of " + e.getClass());
        }
        KeyStroke stroke = KeyStroke.getKeyStrokeForEvent(new KeyEvent(new Button(), KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_UNDEFINED, '1'));
        if (stroke.getKeyChar() != '1') {
            return failed("wrong event parsing");
        }
        return passed();
    }
    
    static final String[] parsingOk = {"INSERT", "control DELETE", "alt control shift meta altGraph ctrl X", "alt shift released X", "typed a"};
    static final String[] parsingInvalid = {"SDFA", "DELETE control", "shift pressed released", "x", "x released", "x x", "shift", "alt x"};
    
    
    /* getKeyStroke(String)*/
    public Result testGetKeyStroke0004() {
        if (KeyStroke.getKeyStroke("") != null || KeyStroke.getKeyStroke(null) != null) {
            return failed("Null object expected");
        }
        for (int i = 0; i < parsingOk.length; i++) {
            try {
                if (KeyStroke.getKeyStroke(parsingOk[i]) == null) {
                    return failed("Can't parse '" + parsingOk[i] + "'");
                }
            } catch (Exception e ) {
                return failed("Unexcpected exception: " + e.getClass().toString().substring("class".length(), e.getClass().toString().length()) );
            }
        }
        for (int i = 0; i < parsingInvalid.length; i++) {
            try {
                if (KeyStroke.getKeyStroke(parsingInvalid[i]) != null) {
                    return failed("Invalid parsing '" + parsingInvalid[i] + "'");
                }
            } catch (Exception e) {
                return failed("Unexcpected exception: "
                        + e.getClass().toString().substring("class".length(),
                                e.getClass().toString().length()));
            }
        }
        return passed();
    }

    public static void main(String[] args) {
        System.exit( new KeyStrokeTest().test(args));
    }
}
