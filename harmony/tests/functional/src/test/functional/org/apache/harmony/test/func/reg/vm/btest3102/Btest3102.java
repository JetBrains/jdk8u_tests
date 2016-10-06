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
package org.apache.harmony.test.func.reg.vm.btest3102;

//XXX: ?????
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JFrame;

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest3102 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest3102().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        final int FRAME_SIZE = 200;

        try {
            JFrame testFrame = new JFrame("test field");
            AbstractButton ab = new AbstractButton() {
                protected void paintComponent(Graphics arg0) {
                    arg0.setColor(Color.BLACK);
                    arg0.drawString("hello world", 30, 50);
                }
            };
            ab.setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));

            testFrame.getContentPane().add(ab);
            testFrame.pack();
            testFrame.setVisible(true);
        } catch (Throwable t) {
            return fail();
        }

        return pass();
    }
}
