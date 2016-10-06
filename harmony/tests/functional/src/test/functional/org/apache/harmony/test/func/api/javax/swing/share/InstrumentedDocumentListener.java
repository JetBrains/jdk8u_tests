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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class InstrumentedDocumentListener implements DocumentListener {
    private int i = 0;

    public InstrumentedDocumentListener() {
    }

    public InstrumentedDocumentListener(int i) {
        this.i = i;
    }

    public void changedUpdate(DocumentEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "DocumentListener" + (i == 0 ? "" : "" + i) + ".changedUpdate",
                arg0 });
    }

    public void insertUpdate(DocumentEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "DocumentListener" + (i == 0 ? "" : "" + i) + ".insertUpdate",
                arg0 });
    }

    public void removeUpdate(DocumentEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "DocumentListener" + (i == 0 ? "" : "" + i) + ".removeUpdate",
                arg0 });
    }
    
    public boolean equals(Object arg0) {
        return (i == ((InstrumentedDocumentListener) arg0).i);
    }
}