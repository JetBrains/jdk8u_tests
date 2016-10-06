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
package org.apache.harmony.test.func.api.javax.swing.text.JTextComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.harmony.test.func.api.share.InteractiveTest;
import org.apache.harmony.share.Result;

public class JTextComponentInteractiveTest extends InteractiveTest {
    protected Frame frm;

    protected boolean butActionListenerChanged = false;

    protected final int WIN_WIDTH = 420;

    protected final int WIN_HEIGHT = 240;

    protected final int WIN_X = 150;

    protected final int WIN_Y = 150;

    protected JTextComponent jtc1;

    protected JTextComponent jtc2;

    protected JTextComponent jtc3;

    protected Dimension winDimension = new Dimension(WIN_WIDTH, WIN_HEIGHT);

    protected Dimension smallWinDimension = new Dimension(WIN_WIDTH / 4,
            WIN_HEIGHT / 4);

    protected Point winLocation = new Point(WIN_X, WIN_Y);

    protected void setUp() {
        frm = new Frame(
                "javax.swing.JTextComponent.cut()/copy()/paste() testing window.");
        jtc1 = new JTextField();
        jtc2 = new JTextField();
        jtc3 = new JTextField();

        /* WM_DESTROY is posted when button is clicked */
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String action = ae.getActionCommand();
                if (action.equals("Close")) {
                    frm.dispose();
                    butActionListenerChanged = true;
                } else {
                    System.out.println(action);
                }
            }
        };
        jtc1.setBounds(30, 60, 80, 22);
        jtc2.setBounds(120, 60, 80, 22);
        jtc3.setBounds(210, 60, 80, 22);

        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setSize(winDimension);
        panel.add(jtc1);
        panel.add(jtc2);
        panel.add(jtc3);

        frm.add(panel);

        /* Handler for WM_DESTROY message */
        frm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frm.dispose();
                //System.exit(0);
            }
        });

        frm.setSize(winDimension);
        frm.setLocation(winLocation);

    }

    protected void tearDown() {
        frm.dispose();
    }

    public static void main(String[] args) {
        System.exit(new JTextComponentInteractiveTest().test(args));
    }

    public Result testJTextComponent_cut_copy_paste() {

        frm.setBackground(Color.BLACK);
        frm.setVisible(true);

        setDescription("Do you see the black window with three TextFields?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(""), new StringSelection(""));

        jtc1.cut();
        jtc1.paste();
        jtc2.copy();
        jtc2.paste();
        jtc3.paste();

        setDescription("No text in TextField's?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        jtc1.setText("Test text");

        setDescription("Do you see 'Test text' in left TextField?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(""), new StringSelection(""));

        jtc1.setSelectionStart(0);
        jtc1.setSelectionEnd(9);
        jtc1.cut();
        jtc3.paste();

        setDescription("Text cut from left TextField and pasted "
                + "into right TextField?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(""), new StringSelection(""));

        jtc3.setSelectionStart(0);
        jtc3.setSelectionEnd(9);
        jtc3.cut();
        jtc2.paste();

        setDescription("Text cut from right TextField and pasted "
                + "into middle TextField?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(""), new StringSelection(""));

        jtc1.setCaretPosition(0);
        jtc2.setSelectionStart(0);
        jtc2.setSelectionEnd(9);
        jtc2.copy();

        jtc1.paste();

        setDescription("Text copied from middle TextField and pasted "
                + "into left TextField?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        jtc3.setCaretPosition(0);
        jtc3.paste();

        setDescription("Text in all three TextField's?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(""), new StringSelection(""));

        jtc1.setSelectionStart(0);
        jtc1.setSelectionEnd(9);
        jtc1.cut();
        jtc2.setSelectionStart(0);
        jtc2.setSelectionEnd(9);
        jtc2.cut();
        jtc3.setSelectionStart(0);
        jtc3.setSelectionEnd(9);
        jtc3.cut();

        setDescription("TextField's are empty?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
      
        jtc1.setCaretPosition(0);
        jtc1.setText("New String");
        setDescription("Do you see 'New String' in the left TextField?'");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }        
        jtc1.setSelectionStart(0);
        jtc1.setSelectionEnd(10);
        jtc1.cut();
        jtc2.setCaretPosition(0);
        jtc2.paste();
        
        setDescription("Is 'New String' in the middle TextField?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection("Test text"), new StringSelection("Test text"));
 
        jtc2.setCaretPosition(0);
        jtc2.setSelectionStart(0);
        jtc2.setSelectionEnd(10);
        jtc2.paste();
     
        setDescription("Do you see 'Test text' in the middle TextField?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
        
        jtc2.setCaretPosition(0);
        jtc2.paste();
        
        setDescription("Do you see 'Test textTest Text' in the middle TextField?");
        if (Asserting() == false) {
            return failed(getFailureReason());
        }
                
        return passed();
    }
}
