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

package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0009;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.AbstractButton;
import javax.swing.JButton;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

public class writeObjectReadObject0009 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0009().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof JButton)) {
            return fail("read object not a JButton");
        }

        JButton b = (JButton) o;
        if (b.getAlignmentX() == Component.RIGHT_ALIGNMENT
                && b.getAlignmentY() == Component.BOTTOM_ALIGNMENT
                && b.getAutoscrolls() == true && b.isEnabled() == true
                && b.isFocusable() == true && b.getIgnoreRepaint() == false
                && b.getMnemonic() == KeyEvent.VK_DEAD_ABOVEDOT
                && b.getMultiClickThreshhold() == 1234
                && "tops1".equals(b.getName()) &&
                //b.isOpaque() == false && //!!!!!!!!!!!!!!!!!!!!!!
                b.isSelected() == true && "abc".equals(b.getText())
                && "bcd".equals(b.getToolTipText())
                && b.getVerticalAlignment() == AbstractButton.BOTTOM
                && b.getVerticalTextPosition() == AbstractButton.BOTTOM
                && b.getHorizontalAlignment() == AbstractButton.LEFT
                && b.getHorizontalTextPosition() == AbstractButton.LEFT) {

            return pass();
        }
        return fail("button deserialized with different values");

        //        return fail("button deserialized with different values" +
        // b.getAlignmentX() + " (was " + Component.RIGHT_ALIGNMENT + ") " +
        //                b.getAlignmentY() + " (was " + Component.BOTTOM_ALIGNMENT + ")" +
        //                b.getAutoscrolls() + " " + b.isEnabled() + " " + b.isFocusable() + "
        // " + b.getIgnoreRepaint() + " " +
        //                b.getMnemonic() + " threshhold: " + b.getMultiClickThreshhold() + " "
        // + b.getName() + " " + b.isOpaque() + " " +
        //                b.isSelected() + " " + b.getText() + " " + b.getToolTipText() + " " +
        //                b.getVerticalAlignment() + " " + b.getVerticalTextPosition() + " " +
        //                b.getHorizontalAlignment() + " " + b.getHorizontalTextPosition());
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        JButton b = new JButton("tops");
        b.setAlignmentX(Component.RIGHT_ALIGNMENT);
        b.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        b.setAutoscrolls(true);
        b.setEnabled(true);
        b.setFocusable(true);
        b.setIgnoreRepaint(false);
        b.setMnemonic(KeyEvent.VK_DEAD_ABOVEDOT);
        b.setMultiClickThreshhold(1234);
        b.setName("tops1");
        //b.setOpaque(false);
        b.setSelected(true);
        b.setText("abc");
        b.setToolTipText("bcd");
        b.setVerticalAlignment(AbstractButton.BOTTOM);
        b.setVerticalTextPosition(AbstractButton.BOTTOM);
        b.setHorizontalAlignment(AbstractButton.LEFT);
        b.setHorizontalTextPosition(AbstractButton.LEFT);

        waitAtBarrier();
        oos.writeObject(b);

        return pass();
    }
}

