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

package org.apache.harmony.test.func.reg.jit.btest3459;

import org.apache.harmony.test.share.reg.RegressionTest;

import java.awt.AWTKeyStroke;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

public class Btest3459 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest3459().test(args));
    }
    
    public int test(String [] args) {
        boolean ret = false;
        
        myAWTKeyStroke s = new myAWTKeyStroke(); 
        
        System.err.println(
            "s.getKeyChar() = " + (int) s.getKeyChar() + " (65535 expected).");      
        System.err.println("s.getKeyChar() == KeyEvent.CHAR_UNDEFINED: " 
            + (s.getKeyChar() == KeyEvent.CHAR_UNDEFINED));
        System.err.println("s.getKeyCode() == KeyEvent.VK_UNDEFINED: "
            + (s.getKeyCode() == KeyEvent.VK_UNDEFINED));
        System.err.println(
            "s.getModifiers() = " + s.getModifiers() + " (0 expected)");
        System.err.println(
            "s.isOnKeyRelease() = " + s.isOnKeyRelease() + " (false expected)");
        
        return ((int) s.getKeyChar() == 65535) 
                && (s.getKeyChar() == KeyEvent.CHAR_UNDEFINED)
                && (s.getKeyCode() == KeyEvent.VK_UNDEFINED)
                && (s.getModifiers() == 0) 
                && (!s.isOnKeyRelease())
            ? pass() : fail();

    }    
}

class myAWTKeyStroke extends AWTKeyStroke 
{ 
    myAWTKeyStroke() {
        super();
    }
}