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
 * Created on 31.05.2005
 *  
 */
package org.apache.harmony.test.func.api.javax.swing.JTextArea;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedJTextArea;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class JTextAreaTest extends MultiCase {
    
    public static void main(String[] args) {
        Document doc = new InstrumentedDocument();
        String text = "Hello!";
        int rows = 24;
        int col = 80;
        System.exit(new JTextAreaTest(doc,text,rows,col).test(args));
    }
    
    JTextAreaTest(Document doc, String text, int rows, int col) {
        new JTextArea (doc, text, rows, col);
    }
    
    
    public Result testJTextArea0() {

        JTextArea ijt = new JTextArea();
            
        if (!ijt.getClass().equals(JTextArea.class)) {
            return failed ("constructor JTextArea() returns a wrong type value");
        }    
    return passed();
    }
    
    public Result testJTextArea1() {
        // random params
        Document doc = new InstrumentedDocument();
        String text = "Content";
        int rows = 200;
        int col = 200;
        
        JTextArea ijt = new JTextArea(doc, text, rows, col);
            
        if (!ijt.getClass().equals(JTextArea.class)) {
            return failed ("constructor JtextArea(Document doc, String text, int rows, int col) returns a wrong type value: " + ijt.getClass());
        }
        if (ijt.getColumns() != col) {
            return failed("constructor JTextArea(Document doc, String text, int rows, int col) creates an instance with wrong columns: " + ijt.getColumns());    
        }
        if (ijt.getRows() != rows) {
            return failed("constructor JTextArea(Document doc, String text, int rows, int col) creates an instance with wrong rows: " + ijt.getRows());    
        }
        if (ijt.getDocument() != doc) {
            return failed("constructor JTextArea(Document doc, String text, int rows, int col) creates an instance with wrong document: " + ijt.getDocument());    
        }
        if (!ijt.getText().equals(text)) {
            return failed("constructor JTextArea(Document doc, String text, int rows, int col) creates an instance with wrong text: " + ijt.getText() + text);    
        }
        
    return passed();
    }
    
    
    public Result testgetUIClassID() {
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
        InstrumentedUILog.clear();
        ijt.getUIClassID();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "JTextArea.getUIClassID" }, })
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getUIClassID not to call any additional methods");
        }
        return passed();
    }

    public Result testcreateDefaultModel() {
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
        InstrumentedUILog.clear();
        if (!ijt.createDefaultModel().getClass().equals(PlainDocument.class)) {
            return failed ("createDefaultModel returns a wrong type value");
        }
        if (!InstrumentedUILog
                .equals(new Object[][] { { "JTextArea.createDefaultModel" }, })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected createDefaultModel not to call any additional methods");
        }
        
        return passed();
    }
    
    public Result testsetColumns () {
        int col[] = {1,2,3,4,100,100000};
        for (int i=0; i<col.length; i++) {
            InstrumentedJTextArea ijt = new InstrumentedJTextArea();
            InstrumentedUILog.clear();
            ijt.setColumns(col[i]);
            
            if (!InstrumentedUILog
                .equals(new Object[][] { { "JTextArea.setColumns", "" + col[i]}, 
                        { "awt.Container.invalidate"}})
                &&
                !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "JTextArea.setColumns", "" + col[i]}, 
                /* 2 */ { "awt.Container.invalidate"}, 
                /* 3 */ { "isPreferredSizeSet"}, 
                /* 4 */ { "isMinimumSizeSet"}, 
                /* 5 */ { "isMaximumSizeSet"}, 
                }
                ) 
            ) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected setColumns not to call any additional methods");
            }
        }
        // wrong value
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
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
            InstrumentedJTextArea ijt = new InstrumentedJTextArea();
            InstrumentedUILog.clear();
            ijt.setColumns(col[i]);
            if (ijt.getColumns() != col[i]) {
                return failed ("getColumns retuns wrong value: " + ijt.getColumns() + ", expected: " + col[i]);
            }
            if (!InstrumentedUILog
                .equals(new Object[][] { 
                        { "JTextArea.setColumns", "" + col[i]},
                        { "awt.Container.invalidate"},
                        { "JTextArea.getColumns"}} )
                        &&
                        !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "JTextArea.setColumns", "" + col[i]}, 
                /* 2 */ { "awt.Container.invalidate"}, 
                /* 3 */ { "isPreferredSizeSet"}, 
                /* 4 */ { "isMinimumSizeSet"}, 
                /* 5 */ { "isMaximumSizeSet"}, 
                /* 6 */ { "JTextArea.getColumns"}, 
            })
            
            ) {
                InstrumentedUILog.printLogAsArray();
                return failed("expected getColumns not to call any additional methods");
            }
        }
        return passed();
    }
    
    public Result testgetColumnWidth() {
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
        Font f = ijt.getFont();
        InstrumentedUILog.clear();
        ijt.getColumnWidth();
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        { "JTextArea.getColumnWidth"}, 
                        { "awt.Component.getFont"}, 
                        { "awt.Component.getFontMetrics", f}
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getColumnWidth not to call any additional methods");    
        }    
    return passed();
    }

    public Result testgetPreferredSize() {
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
        InstrumentedUILog.clear();
        if (!ijt.getPreferredSize().getClass().equals(Dimension.class)) {
            return failed ("getPreferredSize returns a wrong type value");
        }
        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */ { "JTextArea.getPreferredSize"}, 
                        /* 2 */ { "Insets"}, 
                        /* 3 */ { "awt.Component.getSize"}, 
                        /* 4 */ { "awt.Component.size"}, 
                        /* 5 */ { "awt.Component.getFont"}, 
                        /* 6 */ { "awt.Component.getFont"}, 
                        /* 7 */ { "awt.Component.getFontMetrics", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 8 */ { "awt.Component.getFont"}, 
                        /* 9 */ { "awt.Component.getFont"},
                        /* 10 */ {"Insets"}
                        } )
        &&
        !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "JTextArea.getPreferredSize"}, 
                /* 2 */ { "isPreferredSizeSet"}, 
                /* 3 */ { "Insets"}, 
                /* 4 */ { "awt.Component.getSize"}, 
                /* 5 */ { "awt.Component.size"}, 
                /* 6 */ { "awt.Component.getFont"}, 
                /* 7 */ { "awt.Component.getFont"}, 
                /* 8 */ { "awt.Component.getFontMetrics", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                /* 9 */ { "awt.Component.getFont"}, 
                /* 10 */ { "awt.Component.getFont"}, 
                /* 11 */ { "Insets"},         
        })
        
        
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getPreferredSize not to call any additional methods");    
        }    
    return passed();
    }
    
    public Result testsetFont() {
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
        InstrumentedUILog.clear();
        Font of = ijt.getFont(); 
        // random Font
        Font f = new Font(null, 1, 8);
        ijt.setFont(f);
        
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "awt.Component.getFont"}, 
                        /* 2 */ { "JTextArea.setFont", f}, 
                        /* 3 */ { "awt.Component.getFont"}, 
                        /* 4 */ { "awt.Component.getFont"}, 
                        /* 5 */ { "firePropertyChange", "font", of, f}, 
                        /* 6 */ { "JTextArea.getLineWrap"}, 
                        /* 7 */ { "awt.Container.removeAll"}, 
                        /* 8 */ { "revalidate"}, 
                        /* 9 */ { "awt.Component.getParent"}, 
                        /* 10 */ { "awt.Component.repaint"}, 
                        /* 11 */ { "repaint", "0 0 0 0 0"}, 
                        /* 12 */ { "awt.Component.getFont"}, 
                        /* 13 */ { "revalidate"}, 
                        /* 14 */ { "awt.Component.getParent"}, 
                        /* 15 */ { "awt.Component.repaint"}, 
                        /* 16 */ { "repaint", "0 0 0 0 0"},                         
                } )
        &&
        !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "awt.Component.getFont"}, 
                /* 2 */ { "JTextArea.setFont", f}, 
                /* 3 */ { "awt.Component.getFont"}, 
                /* 4 */ { "awt.Component.getFont"}, 
                /* 5 */ { "firePropertyChange", "font", of, f}, 
                /* 6 */ { "JTextArea.getLineWrap"}, 
                /* 7 */ { "revalidate"}, 
                /* 8 */ { "awt.Component.getParent"}, 
                /* 9 */ { "awt.Component.repaint"}, 
                /* 10 */ { "repaint", "0 0 0 0 0"}, 
                /* 11 */ { "awt.Component.getFont"}, 
                /* 12 */ { "revalidate"}, 
                /* 13 */ { "awt.Component.getParent"}, 
                /* 14 */ { "awt.Component.repaint"}, 
                /* 15 */ { "repaint", "0 0 0 0 0"},         
        })
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected setFont not to call any additional methods");    
        }    
    return passed();
    }


    
    public Result testgetLineWrap() {
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
        InstrumentedUILog.clear();
        if (ijt.getLineWrap() == true) {
            return failed("getLineWrap returns true (false is default value)");    
        }
        
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextArea.getLineWrap"},         
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getLineWrap not to call any additional methods");    
        }
        ijt.setLineWrap(true);
        InstrumentedUILog.clear();
        if (ijt.getLineWrap() != true) {
            return failed("getLineWrap returns false (true has set by setLineWrap)");    
        }
        return passed();
    }
    
    public Result testgetPreferredScrollableViewportSize() {
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
        InstrumentedUILog.clear();
        if (!ijt.getPreferredScrollableViewportSize().getClass().equals(Dimension.class)) {
            return failed ("getPreferredScrollableViewportSize returns a wrong type value");
        }
        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */ { "JTextArea.getPreferredScrollableViewportSize"}, 
                        /* 2 */ { "JTextArea.getPreferredSize"}, 
                        /* 3 */ { "Insets"}, 
                        /* 4 */ { "awt.Component.getSize"}, 
                        /* 5 */ { "awt.Component.size"}, 
                        /* 6 */ { "awt.Component.getFont"}, 
                        /* 7 */ { "awt.Component.getFont"}, 
                        /* 8 */ { "awt.Component.getFontMetrics", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                        /* 9 */ { "awt.Component.getFont"}, 
                        /* 10 */ { "awt.Component.getFont"}, 
                        /* 11 */ { "Insets"}, 
                        } )
        &&
        !InstrumentedUILog.equals(new Object[][] {
                /* 1 */ { "JTextArea.getPreferredScrollableViewportSize"}, 
                /* 2 */ { "JTextArea.getPreferredSize"}, 
                /* 3 */ { "isPreferredSizeSet"}, 
                /* 4 */ { "Insets"}, 
                /* 5 */ { "awt.Component.getSize"}, 
                /* 6 */ { "awt.Component.size"}, 
                /* 7 */ { "awt.Component.getFont"}, 
                /* 8 */ { "awt.Component.getFont"}, 
                /* 9 */ { "awt.Component.getFontMetrics", InstrumentedUILog.ANY_NON_NULL_VALUE}, 
                /* 10 */ { "awt.Component.getFont"}, 
                /* 11 */ { "awt.Component.getFont"}, 
                /* 12 */ { "Insets"},     
        })
            
        ) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getPreferredScrollableViewportSize not to call any additional methods");    
        }    
        return passed();
    }

    public Result testgetWrapStyleWord() {
        InstrumentedJTextArea ijt = new InstrumentedJTextArea();
        InstrumentedUILog.clear();
        if (ijt.getWrapStyleWord() == true) {
            return failed("getWrapStyleWord returns true (false is default value)");    
        }
        
        if (!InstrumentedUILog
                .equals(new Object[][] { 
                        /* 1 */ { "JTextArea.getWrapStyleWord"},         
                } )) {
            InstrumentedUILog.printLogAsArray();
            return failed("expected getWrapStyleWord not to call any additional methods");    
        }
        ijt.setWrapStyleWord(true);
        InstrumentedUILog.clear();
        if (ijt.getWrapStyleWord() != true) {
            return failed("getWrapStyleWord returns false (true has set by setWrapStyleWord)");    
        }
        return passed();
    }

}