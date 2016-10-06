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
 * Created on 24.05.2005
 *  
 */
package org.apache.harmony.test.func.api.javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAction;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedActionListener;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedJTextField;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.Util;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class JTextFieldTest extends MultiCase {
    
    public static void main(String[] args) {
        Document doc = new InstrumentedDocument();
        String text = "Hello!";
        int col = 80;
        System.exit(new JTextFieldTest(doc,text,col).test(args));
    }

    public Result testStaticFields() {
        if (!JTextField.notifyAction.equals("notify-field-accept")) {
            return failed("static field notifyAction has wrong value: " + JTextField.notifyAction);
        }
        
        return passed();
    }

    
    JTextFieldTest(Document doc, String text, int col) {
        new JTextField (doc, text, col);
    }
    
    
    public Result testJTextField0() {

        JTextField ijt = new JTextField();
            
        if (!ijt.getClass().equals(JTextField.class)) {
            return failed ("constructor JTextField() returns a wrong type value");
        }    
    return passed();
    }
    
    public Result testJTextField1() {
        // random columns
        int col = 200;
        
        JTextField ijt = new JTextField(col);
            
        if (!ijt.getClass().equals(JTextField.class)) {
            return failed ("constructor JTextField(int col) returns a wrong type value");
        }
        if (ijt.getColumns() != col) {
            return failed("constructor JTextField(int col) creates an instance with wrong columns: " + ijt.getColumns());    
        }        
    return passed();
    }
    
    public Result testJTextField2() {
        // random params
        Document doc = new InstrumentedDocument();
        String text = "Content";
        int col = 200;
        
        JTextField ijt = new JTextField(doc, text, col);
            
        if (!ijt.getClass().equals(JTextField.class)) {
            return failed ("constructor JTextField(Document doc, String text, int col) returns a wrong type value: " + ijt.getClass());
        }
        if (ijt.getColumns() != col) {
            return failed("constructor JTextField(Document doc, String text, int col) creates an instance with wrong columns: " + ijt.getColumns());    
        }
        if (ijt.getDocument() != doc) {
            return failed("constructor JTextField(Document doc, String text, int col) creates an instance with wrong document: " + ijt.getDocument());    
        }
        if (!ijt.getText().equals(text)) {
            return failed("constructor JTextField(Document doc, String text, int col) creates an instance with wrong text: " + ijt.getText() + text);    
        }
        
    return passed();
    }
    
    
    public Result testgetUIClassID() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        ijt.getUIClassID();
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "JTextField.getUIClassID" }, })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected getUIClassID not to call any additional methods");
        }
        return passed();
    }

    public Result testsetDocument() {
        Document doc = new InstrumentedDocument();
        
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        Document oldDocument = ijt.getDocument(); 
        ijt.setDocument(doc);
        Util.waitQueueEventsProcess();

        if (!InstrumentedUILog
                    .contains( (new Object[] [] { 
                        /* 1 */ { "JTextField.setDocument", doc}, 
                        /* 2 */ { "Document.getDocumentProperties"}, 
                        /* 3 */ { "awt.Component.getComponentOrientation"}, 
                        /* 4 */ { "Document.getDocumentProperties"}, 
                        /* 5 */ { "Document.getDocumentProperties"}, 
                        /* 6 */ { "firePropertyChange", "document", oldDocument,  doc}, 
                        /* 7 */ { "Document.getLength"}, 
                        /* 8 */ { "Document.addDocumentListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 9 */ { "Document.addDocumentListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 10 */ { "Document.getDefaultRootElement"}, 
                        /* 11 */ { "Document.getDocumentProperties"}, 
                        /* 12 */ { "awt.Container.removeAll"}, 
                        /* 13 */ { "revalidate"}, 
                        /* 14 */ { "awt.Component.getParent"}, 
                        /* 15 */ { "awt.Component.repaint"}, 
                        /* 16 */ { "repaint", "0 0 0 0 0"}, 
                        /* 17 */ { "revalidate"}, 
                        /* 18 */ { "awt.Component.getParent"}, 
                        /* 19 */ { "awt.Component.repaint"}, 
                        /* 20 */ { "repaint", "0 0 0 0 0"} } ) )
                        
                        &&
                        !InstrumentedUILog.equals(new Object[][] {
                                /* 1 */ { "JTextField.setDocument", doc}, 
                                /* 2 */ { "Document.getDocumentProperties"}, 
                                /* 3 */ { "awt.Component.getComponentOrientation"}, 
                                /* 4 */ { "Document.getDocumentProperties"}, 
                                /* 5 */ { "Document.getDocumentProperties"}, 
                                /* 6 */ { "firePropertyChange", "document", oldDocument, doc}, 
                                /* 7 */ { "Document.getLength"}, 
                                /* 8 */ { "Document.addDocumentListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                                /* 9 */ { "Document.addDocumentListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                                /* 10 */ { "revalidate"}, 
                                /* 11 */ { "awt.Component.getParent"}, 
                                /* 12 */ { "awt.Component.repaint"}, 
                                /* 13 */ { "repaint", "0 0 0 0 0"}, 
                                /* 14 */ { "Document.getDefaultRootElement"}, 
                                /* 15 */ { "Document.getDocumentProperties"}, 
                                /* 16 */ { "revalidate"}, 
                                /* 17 */ { "awt.Component.getParent"}, 
                                /* 18 */ { "awt.Component.repaint"}, 
                                /* 19 */ { "repaint", "0 0 0 0 0"}, 
                                /* 20 */ { "revalidate"}, 
                                /* 21 */ { "awt.Component.getParent"}, 
                                /* 22 */ { "awt.Component.repaint"}, 
                                /* 23 */ { "repaint", "0 0 0 0 0"}, 
                        })
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setDocument not to call any additional methods");
        }
        return passed();
    }
    
    public Result testisValidateRoot() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        ijt.isValidateRoot();
        Util.waitQueueEventsProcess();
        
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextField.isValidateRoot"}, 
                        /* 2 */ { "awt.Component.getParent"},  })
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected isValidateRoot not to call any additional methods");
        }
        return passed();
    }
    
    public Result testsetHorizontalAlignment() {
        int align [] = {JTextField.LEFT,
                JTextField.CENTER,
                JTextField.RIGHT,
                JTextField.LEADING,
                JTextField.TRAILING
        };
        
        for (int i=0; i<align.length; i++) {
            InstrumentedJTextField ijt = new InstrumentedJTextField();
            InstrumentedUILog.clear();
            ijt.setHorizontalAlignment(align[i]);
            Util.waitQueueEventsProcess();
            if (!InstrumentedUILog.contains(new Object[][] { 
                            /* 1 */ { "JTextField.setHorizontalAlignment", "" + align[i]}})
                                ) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected setHorizontalAlignment not to call any additional methods");
            }
        }
        // set wrong value
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        try {
            ijt.setHorizontalAlignment(22);
            return failed ("IllegalArgumentException doesn't thrown");
        }
        catch (IllegalArgumentException e) {
        }
        
        return passed();
    }

    public Result testgetHorizontalAlignment() {
        int align [] = {JTextField.LEFT,
                JTextField.CENTER,
                JTextField.RIGHT,
                JTextField.LEADING,
                JTextField.TRAILING
        };
        int getAlign;
        for (int i=0; i<align.length; i++) {
            InstrumentedJTextField ijt = new InstrumentedJTextField();
            InstrumentedUILog.clear();
            ijt.setHorizontalAlignment(align[i]);
            getAlign = ijt.getHorizontalAlignment();
            Util.waitQueueEventsProcess();
            if (!InstrumentedUILog
                    .contains(new Object[][] { { "JTextField.setHorizontalAlignment", "" + align[i]}, 
                            { "JTextField.getHorizontalAlignment"} })
            ) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected getHorizontalAlignment not to call any additional methods");
            }
            if (getAlign != align[i]) {
                return failed("getHorizontalAlignment returns " + getAlign + ", but expected " + align[i]);
            }
        }
        return passed();
    }
    
    public Result testcreateDefaultModel() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        if (!ijt.createDefaultModel().getClass().equals(PlainDocument.class)) {
            return failed ("createDefaultModel returns a wrong type value");
        }
        Util.waitQueueEventsProcess();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "JTextField.createDefaultModel" }, })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected createDefaultModel not to call any additional methods");
        }
        
        return passed();
    }
    
    public Result testsetColumns () {
        int col[] = {1,2,3,4,100,100000};
        for (int i=0; i<col.length; i++) {
            InstrumentedJTextField ijt = new InstrumentedJTextField();
            InstrumentedUILog.clear();
            ijt.setColumns(col[i]);
            
            Util.waitQueueEventsProcess();
            if (!InstrumentedUILog
                .equals(new Object[][] { { "JTextField.setColumns", "" + col[i]}, 
                        { "awt.Container.invalidate"}}) 
            &&
            !InstrumentedUILog.equals(new Object[][] {
                    /* 1 */ { "JTextField.setColumns", "" + col[i]}, 
                    /* 2 */ { "awt.Container.invalidate"}, 
                    /* 3 */ { "isPreferredSizeSet"}, 
                    /* 4 */ { "isMinimumSizeSet"}, 
                    /* 5 */ { "isMaximumSizeSet"},         
            })
            
            
            ) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected setColumns not to call any additional methods");
            }
        }
        // wrong value
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        try {
            ijt.setColumns(-1);
            return failed("IllegalArgumentException doesn't thrown");
        }
        catch (IllegalArgumentException e) {}
        return passed();
    }
    
    public Result testgetColumns () {
        int col[] = {1,2,3,4,100,100000};
        for (int i=0; i<col.length; i++) {
            InstrumentedJTextField ijt = new InstrumentedJTextField();
            InstrumentedUILog.clear();
            ijt.setColumns(col[i]);
            if (ijt.getColumns() != col[i]) {
                return failed ("getColumns retuns wrong value: " + ijt.getColumns() + ", expected: " + col[i]);
            }
            Util.waitQueueEventsProcess();
            if (!InstrumentedUILog
                .equals(new Object[][] { 
                        { "JTextField.setColumns", "" + col[i]},
                        { "awt.Container.invalidate"},
                        { "JTextField.getColumns"}} ) 
            &&
            !InstrumentedUILog.equals(new Object[][] {
                    /* 1 */ { "JTextField.setColumns", "" + col[i]}, 
                    /* 2 */ { "awt.Container.invalidate"}, 
                    /* 3 */ { "isPreferredSizeSet"}, 
                    /* 4 */ { "isMinimumSizeSet"}, 
                    /* 5 */ { "isMaximumSizeSet"}, 
                    /* 6 */ { "JTextField.getColumns"},         
            })
            
            ) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected setColumns not to call any additional methods");
            }
        }
        return passed();
    }
    
    public Result testgetColumnWidth() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        Font f = ijt.getFont();
        InstrumentedUILog.clear();
        ijt.getColumnWidth();
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        { "JTextField.getColumnWidth"},
                        { "awt.Component.getFont"}, 
                        { "awt.Component.getFontMetrics", f}
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getColumnWidth not to call any additional methods");    
        }    
    return passed();
    }

    public Result testgetPreferredSize() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        if (!ijt.getPreferredSize().getClass().equals(Dimension.class)) {
            return failed ("getPreferredSize returns a wrong type value");
        }
        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */ { "JTextField.Dimension"}, 
                        /* 2 */ { "Insets"}, 
                        /* 3 */ { "awt.Component.getSize"}, 
                        /* 4 */ { "awt.Component.size"}, 
                        /* 5 */ { "awt.Component.getFont"}, 
                        /* 6 */ { "awt.Component.getFont"}, 
                        /* 7 */ { "awt.Component.getFontMetrics", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 8 */ { "awt.Component.getFont"}, 
                        /* 9 */ { "awt.Component.getFontMetrics", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 10 */ { "awt.Component.getFont"}
                        } )
        &&
        !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "JTextField.Dimension"}, 
                /* 2 */ { "isPreferredSizeSet"}, 
                /* 3 */ { "Insets"}, 
                /* 4 */ { "awt.Component.getSize"}, 
                /* 5 */ { "awt.Component.size"}, 
                /* 6 */ { "awt.Component.getFont"}, 
                /* 7 */ { "awt.Component.getFont"}, 
                /* 8 */ { "awt.Component.getFontMetrics", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                /* 9 */ { "awt.Component.getFont"}, 
                /* 10 */ { "awt.Component.getFontMetrics", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                /* 11 */ { "awt.Component.getFont"},         
        })
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getPreferredSize not to call any additional methods");    
        }    
    return passed();
    }
    
    public Result testsetFont() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        Font of = ijt.getFont(); 
        // random Font
        Font f = new Font(null, 1, 8);
        ijt.setFont(f);
        
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 0 */ { "awt.Component.getFont"}, 
                        /* 1 */ { "JTextField.setFont", f}, 
                        /* 2 */ { "awt.Component.getFont"}, 
                        /* 3 */ { "awt.Component.getFont"}, 
                        /* 4 */ { "firePropertyChange", "font", of, f}, 
                        /* 5 */ { "awt.Container.removeAll"}, 
                        /* 6 */ { "revalidate"}, 
                        /* 7 */ { "awt.Component.getParent"}, 
                        /* 8 */ { "awt.Component.repaint"}, 
                        /* 9 */ { "repaint", "0 0 0 0 0"}, 
                        /* 10 */ { "awt.Component.getFont"}, 
                        /* 11 */ { "revalidate"}, 
                        /* 12 */ { "awt.Component.getParent"}, 
                        /* 13 */ { "awt.Component.repaint"}, 
                        /* 14 */ { "repaint", "0 0 0 0 0"}
                } )
        &&
        !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "awt.Component.getFont"}, 
                /* 2 */ { "JTextField.setFont", f}, 
                /* 3 */ { "awt.Component.getFont"}, 
                /* 4 */ { "awt.Component.getFont"}, 
                /* 5 */ { "firePropertyChange", "font", of, f}, 
                /* 6 */ { "revalidate"}, 
                /* 7 */ { "awt.Component.getParent"}, 
                /* 8 */ { "awt.Component.repaint"}, 
                /* 9 */ { "repaint", "0 0 0 0 0"}, 
                /* 10 */ { "awt.Component.getFont"}, 
                /* 11 */ { "revalidate"}, 
                /* 12 */ { "awt.Component.getParent"}, 
                /* 13 */ { "awt.Component.repaint"}, 
                /* 14 */ { "repaint", "0 0 0 0 0"},         
        })
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setFont not to call any additional methods");    
        }    
    return passed();
    }
    
    public Result testaddActionListener() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        ActionListener l = new InstrumentedActionListener();
        ijt.addActionListener(l);
        ijt.postActionEvent();

        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextField.addActionListener", l}, 
                        /* 2 */ { "JTextField.postActionEvent"}, 
                        /* 3 */ { "JTextField.fireActionPerformed"}, 
                        /* 4 */ { "ActionPerformed", InstrumentedUILog.ANY_NON_NULL_VALUE}        
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected addActionListener not to call any additional methods");    
        }        
    return passed();
    }
    
    public Result testremoveActionListener() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        ActionListener l = new InstrumentedActionListener();
        ijt.addActionListener(l);
        InstrumentedUILog.clear();
        ijt.removeActionListener(l);
        ijt.postActionEvent();
        
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextField.removeActionListener", l}, 
                        /* 2 */ { "JTextField.postActionEvent"}, 
                        /* 3 */ { "JTextField.fireActionPerformed"}
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected removeActionListener not to call any additional methods");    
        }
        
    return passed();
    }
    
    public Result testgetActionListeners() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        ActionListener l = new InstrumentedActionListener();
        ijt.addActionListener(l);
        ActionListener[] list = ijt.getActionListeners();

        if (list.length != 1 || list[0].getClass().equals(ActionListener.class)) {
            return failed ("getActionListeners returns a wrong type value or the array size is wrong");
        }
        ijt.removeActionListener(l);
        list = ijt.getActionListeners();

        if (list.length != 0) {
            return failed ("getActionListeners doesn't return an empty array when there aren't any ActionListeners");
        }
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextField.addActionListener", l}, 
                        /* 2 */ { "JTextField.getActionListeners"}, 
                        /* 3 */ { "JTextField.removeActionListener", l}, 
                        /* 4 */ { "JTextField.getActionListeners"}
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected removeActionListener not to call any additional methods");    
        }
        
    return passed();
    }
    
    public Result testfireActionPerformed() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        ActionListener l = new InstrumentedActionListener();
        ijt.addActionListener(l);
        ijt.fireActionPerformed();

        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextField.addActionListener", l}, 
                        /* 3 */ { "JTextField.fireActionPerformed"}, 
                        /* 4 */ { "ActionPerformed", InstrumentedUILog.ANY_NON_NULL_VALUE}        
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected fireActionPerformed not to call any additional methods");    
        }        
    return passed();
    }
    
    public Result testsetActionCommand() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        InstrumentedActionListener l = new InstrumentedActionListener();
        ijt.addActionListener(l);
        // random command
        String command = "Go-go-go";
        ijt.setActionCommand(command);
        ijt.postActionEvent();
        
        if (!l.getActionEvent().getActionCommand().equals(command)) {
            return failed("setActionCommand and ActionEvent.getActionCommand differ");    
        } 

        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextField.addActionListener", l}, 
                        /* 2 */ { "JTextField.setActionCommand", command}, 
                        /* 3 */ { "JTextField.postActionEvent"}, 
                        /* 4 */ { "JTextField.fireActionPerformed"}, 
                        /* 5 */ { "ActionPerformed", l.getActionEvent()}
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected addActionListener not to call any additional methods");    
        }        
    return passed();
    }
    
    public Result testsetAction() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        Action action = new InstrumentedAction();
        ijt.setAction(action);
        
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextField.setAction", action}, 
                        /* 2 */ { "JTextField.configurePropertiesFromAction", action}, 
                        /* 3 */ { "Action.isEnabled"}, 
                        /* 4 */ { "setEnabled", "false"}, 
                        /* 5 */ { "isEnabled"}, 
                        /* 6 */ { "awt.Component.enable", "false"}, 
                        /* 7 */ { "disable"}, 
                        /* 8 */ { "isEnabled"}, 
                        /* 9 */ { "awt.Component.getParent"}, 
                        /* 10 */ { "awt.Component.isFocusOwner"}, 
                        /* 11 */ { "awt.Component.hasFocus"}, 
                        /* 12 */ { "firePropertyChangeBoolean", "enabled", "true", "false"}, 
                        /* 13 */ { "awt.Component.repaint"}, 
                        /* 14 */ { "repaint", "0 0 0 0 0"}, 
                        /* 15 */ { "Action.getValue", "ShortDescription"}, 
                        /* 16 */ { "setToolTipText", null}, 
                        /* 17 */ { "getToolTipText"}, 
                        /* 18 */ { "awt.Component.removeMouseListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 19 */ { "awt.Component.removeMouseMotionListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 20 */ { "JTextField.addActionListener", action}, 
                        /* 21 */ { "JTextField.createActionPropertyChangeListener", action}, 
                        /* 22 */ { "Action.addPropertyChangeListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 23 */ { "firePropertyChange", "action", null, action}, 
                        /* 24 */ { "revalidate"}, 
                        /* 25 */ { "awt.Component.getParent"}, 
                        /* 26 */ { "awt.Component.repaint"}, 
                        /* 27 */ { "repaint", "0 0 0 0 0"}, 
                        
                } )
        &&
        !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "JTextField.setAction", action}, 
                /* 2 */ { "JTextField.configurePropertiesFromAction", action}, 
                /* 3 */ { "Action.isEnabled"}, 
                /* 4 */ { "setEnabled", "false"}, 
                /* 5 */ { "isEnabled"}, 
                /* 6 */ { "awt.Component.enable", "false"}, 
                /* 7 */ { "disable"}, 
                /* 8 */ { "isEnabled"}, 
                /* 9 */ { "awt.Component.getParent"}, 
                /* 10 */ { "awt.Component.isFocusOwner"}, 
                /* 11 */ { "awt.Component.hasFocus"}, 
                /* 12 */ { "firePropertyChangeBoolean", "enabled", "true", "false"}, 
                /* 13 */ { "awt.Component.isFocusOwner"}, 
                /* 14 */ { "awt.Component.hasFocus"}, 
                /* 15 */ { "awt.Component.getBackground"}, 
                /* 16 */ { "isEnabled"}, 
                /* 17 */ { "awt.Component.getLocale"}, 
                /* 18 */ { "awt.Component.getLocale"}, 
                /* 19 */ { "awt.Component.repaint"}, 
                /* 20 */ { "repaint", "0 0 0 0 0"}, 
                /* 21 */ { "Action.getValue", "ShortDescription"}, 
                /* 22 */ { "setToolTipText", null}, 
                /* 23 */ { "getToolTipText"}, 
                /* 24 */ { "awt.Component.removeMouseListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                /* 25 */ { "awt.Component.removeMouseMotionListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                /* 26 */ { "JTextField.addActionListener", action}, 
                /* 27 */ { "JTextField.createActionPropertyChangeListener", action}, 
                /* 28 */ { "Action.addPropertyChangeListener", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                /* 29 */ { "firePropertyChange", "action", null, action}, 
                /* 30 */ { "revalidate"}, 
                /* 31 */ { "awt.Component.getParent"}, 
                /* 32 */ { "awt.Component.repaint"}, 
                /* 33 */ { "repaint", "0 0 0 0 0"},         
        })
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setAction not to call any additional methods");    
        }        
    return passed();
    }
    
    public Result testgetActions() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();
        if (ijt.getActions() == null) {
            return failed("getActions returns empty array");    
        }
    
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                         { "JTextField.getActions"}, 
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getActions not to call any additional methods");    
        }        
    return passed();
    }
    
    public Result testgetHorizontalVisibility() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();        
        if (!ijt.getHorizontalVisibility().getClass().equals(DefaultBoundedRangeModel.class)) {
            return failed ("getHorizontalVisibility returns a wrong type value");
        }
        InstrumentedUILog.clear();
        if (ijt.getHorizontalVisibility() == null) {
            return failed("getHorizontalVisibility returns null");    
        }
    
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                         { "JTextField.getHorizontalVisibility"}, 
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getHorizontalVisibility not to call any additional methods");    
        }        
    return passed();
    }
    
    public Result testscrollRectToVisible() {
        InstrumentedJTextField ijt = new InstrumentedJTextField();
        InstrumentedUILog.clear();    
        Rectangle r = new Rectangle();
        ijt.scrollRectToVisible(r);
    
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextField.scrollRectToVisible", r}, 
                        /* 2 */ { "Insets"} 
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected scrollRectToVisible not to call any additional methods");    
        }        
    return passed();
    }

}