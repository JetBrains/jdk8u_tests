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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.ViewFactory;


public class InstrumentedDefaultEditorKit extends DefaultEditorKit {

    
    public Caret createCaret() {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.createCaret"} );
        return super.createCaret();
    }
    public Document createDefaultDocument() {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.createDefaultDocument"} );
        return super.createDefaultDocument();
    }
    public Action[] getActions() {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.getActions"} );
        return super.getActions();
    }
    public String getContentType() {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.getContentType"} );
        return super.getContentType();
    }
    public ViewFactory getViewFactory() {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.getViewFactory"} );
        return super.getViewFactory();
    }
    public void read(InputStream arg0, Document arg1, int arg2)
            throws IOException, BadLocationException {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.read",  arg0,  arg1, "" +  arg2} );
        super.read(arg0, arg1, arg2);
    }
    public void read(Reader arg0, Document arg1, int arg2) throws IOException,
            BadLocationException {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.read",  arg0,  arg1, "" +  arg2} );
        super.read(arg0, arg1, arg2);
    }
    public void write(OutputStream arg0, Document arg1, int arg2, int arg3)
            throws IOException, BadLocationException {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.write",  arg0,  arg1, "" +  arg2, "" +  arg3} );
        super.write(arg0, arg1, arg2, arg3);
    }
    public void write(Writer arg0, Document arg1, int arg2, int arg3)
            throws IOException, BadLocationException {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.write",  arg0,  arg1, "" +  arg2, "" +  arg3} );
        super.write(arg0, arg1, arg2, arg3);
    }
    public Object clone() {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.clone"} );
        return super.clone();
    }
    public void deinstall(JEditorPane arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.deinstall",  arg0} );
        super.deinstall(arg0);
    }
    public void install(JEditorPane arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.install",  arg0} );
        super.install(arg0);
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.hashCode"} );
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.equals",  arg0} );
        return super.equals(arg0);
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"DefaultEditorKit.toString"} );
        return super.toString();
    }
}
