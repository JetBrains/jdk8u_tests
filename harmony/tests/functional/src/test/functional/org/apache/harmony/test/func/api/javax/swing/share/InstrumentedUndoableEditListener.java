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

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

public class InstrumentedUndoableEditListener implements UndoableEditListener {
    private int i = 0;

    public InstrumentedUndoableEditListener() {
    }

    public InstrumentedUndoableEditListener(int i) {
        this.i = i;
    }

    public void undoableEditHappened(UndoableEditEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "UndoableEditListener" + (i == 0 ? "" : "" + i)
                        + ".undoableEditHappened", arg0 });
    }
    
    
    public boolean equals(Object arg0) {
        return (i == ((InstrumentedUndoableEditListener) arg0).i);
    }
}