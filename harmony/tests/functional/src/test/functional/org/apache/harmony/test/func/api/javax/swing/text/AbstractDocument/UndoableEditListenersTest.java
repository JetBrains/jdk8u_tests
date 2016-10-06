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

package org.apache.harmony.test.func.api.javax.swing.text.AbstractDocument;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAbstractDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUndoableEditEvent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUndoableEditListener;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class UndoableEditListenersTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new UndoableEditListenersTest().test(args));
    }

    public Result testAddRemove() {
        AbstractDocument ad = new InstrumentedAbstractDocument();

        if (ad.getUndoableEditListeners().length != 0) {
            return failed("expected 0 UndoableEditListeners here");
        }

        UndoableEditListener dl1 = new InstrumentedUndoableEditListener(0);
        UndoableEditListener dl2 = new InstrumentedUndoableEditListener(1);

        ad.addUndoableEditListener(dl1);
        if (ad.getUndoableEditListeners().length != 1) {
            return failed("expected 1 UndoableEditListener");
        }

        ad.addUndoableEditListener(dl2);
        if (ad.getUndoableEditListeners().length != 2) {
            return failed("expected 2 UndoableEditListeners here");
        }

        ad.addUndoableEditListener(dl1);
        if (ad.getUndoableEditListeners().length != 3) {
            return failed("expected 3 UndoableEditListeners here");
        }

        ad.addUndoableEditListener(null);

        if (ad.getUndoableEditListeners().length != 3) {
            return failed("expected 3 UndoableEditListeners here");
        }

        ad.removeUndoableEditListener(null);
        if (ad.getUndoableEditListeners().length != 3) {
            return failed("expected 3 UndoableEditListeners here");
        }

        ad.removeUndoableEditListener(dl1);
        if (ad.getUndoableEditListeners().length != 2) {
            return failed("expected 2 UndoableEditListeners here, got: "
                    + ad.getUndoableEditListeners().length);
        }

        ad.removeUndoableEditListener(new InstrumentedUndoableEditListener(3));
        if (ad.getUndoableEditListeners().length != 2) {
            return failed("expected 2 UndoableEditListeners here");
        }

        ad.removeUndoableEditListener(new InstrumentedUndoableEditListener(0));
        if (ad.getUndoableEditListeners().length != 1) {
            return failed("expected 1 UndoableEditListener here, got "
                    + ad.getUndoableEditListeners().length);
        }

        ad.removeUndoableEditListener(dl2);
        if (ad.getUndoableEditListeners().length != 0) {
            return failed("expected 0 UndoableEditListeners here");
        }

        return passed();
    }

    public Result testGetUndoableEditListeners() {
        AbstractDocument ad = new InstrumentedAbstractDocument();

        if (ad.getUndoableEditListeners() == ad.getUndoableEditListeners()) {
            return failed("expected two calls on getUndoableEditListeners() return different values");
        }

        return passed();
    }

    public Result testFire() {
        InstrumentedAbstractDocument ad = new InstrumentedAbstractDocument();

        UndoableEditListener dl1 = new InstrumentedUndoableEditListener(1);
        UndoableEditListener dl2 = new InstrumentedUndoableEditListener(2);

        ad.addUndoableEditListener(dl1);
        ad.addUndoableEditListener(dl2);
        ad.addUndoableEditListener(dl1);

        UndoableEditEvent uee = new InstrumentedUndoableEditEvent();

        InstrumentedUILog.clear();

        ad.fireUndoableEditUpdate(uee);

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "AbstractDocument.fireUndoableEditUpdate", uee },
        /* 2 */{ "UndoableEditListener1.undoableEditHappened", uee },
        /* 3 */{ "UndoableEditListener2.undoableEditHappened", uee },
        /* 4 */{ "UndoableEditListener1.undoableEditHappened", uee }, })

                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "AbstractDocument.fireUndoableEditUpdate",
                                uee },
                        { "AbstractDocument.getUndoableEditListeners" },
                        /* 2 */{ "UndoableEditListener1.undoableEditHappened",
                                uee },
                        /* 3 */{ "UndoableEditListener2.undoableEditHappened",
                                uee },
                        /* 4 */{ "UndoableEditListener1.undoableEditHappened",
                                uee }, })) {
            InstrumentedUILog.printLog();
            return failed("expected fireChangedUpdate to call another sequence of events");
        }

        return passed();
    }
}