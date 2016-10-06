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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAbstractDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDefaultDocumentEvent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDocumentListener;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class DocumentListenersTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new DocumentListenersTest().test(args));
    }

    public Result testAddRemove() {
        AbstractDocument ad = new InstrumentedAbstractDocument();

        if (ad.getDocumentListeners().length != 0) {
            return failed("expected 0 documentListeners here");
        }

        DocumentListener dl1 = new InstrumentedDocumentListener(0);
        DocumentListener dl2 = new InstrumentedDocumentListener(1);

        ad.addDocumentListener(dl1);
        if (ad.getDocumentListeners().length != 1) {
            return failed("expected 1 documentListener");
        }

        ad.addDocumentListener(dl2);
        if (ad.getDocumentListeners().length != 2) {
            return failed("expected 2 documentListeners here");
        }

        ad.addDocumentListener(dl1);
        if (ad.getDocumentListeners().length != 3) {
            return failed("expected 3 documentListeners here");
        }

        ad.addDocumentListener(null);

        if (ad.getDocumentListeners().length != 3) {
            return failed("expected 3 documentListeners here");
        }

        ad.removeDocumentListener(null);
        if (ad.getDocumentListeners().length != 3) {
            return failed("expected 3 documentListeners here");
        }

        ad.removeDocumentListener(dl1);
        if (ad.getDocumentListeners().length != 2) {
            return failed("expected 2 documentListeners here, got: "
                    + ad.getDocumentListeners().length);
        }

        ad.removeDocumentListener(new InstrumentedDocumentListener(3));
        if (ad.getDocumentListeners().length != 2) {
            return failed("expected 2 documentListeners here");
        }

        ad.removeDocumentListener(new InstrumentedDocumentListener(0));
        if (ad.getDocumentListeners().length != 1) {
            return failed("expected 1 documentListener here, got "
                    + ad.getDocumentListeners().length);
        }

        ad.removeDocumentListener(dl2);
        if (ad.getDocumentListeners().length != 0) {
            return failed("expected 0 documentListeners here");
        }

        return passed();
    }

    public Result testGetdocumentListeners() {
        AbstractDocument ad = new InstrumentedAbstractDocument();

        if (ad.getDocumentListeners() == ad.getDocumentListeners()) {
            return failed("expected two calls on getDocumentListeners() return different values");
        }

        return passed();
    }

    public Result testFire() {
        InstrumentedAbstractDocument ad = new InstrumentedAbstractDocument();

        DocumentListener dl1 = new InstrumentedDocumentListener(1);
        DocumentListener dl2 = new InstrumentedDocumentListener(2);

        ad.addDocumentListener(dl1);
        ad.addDocumentListener(dl2);
        ad.addDocumentListener(dl1);

        DocumentEvent de = new InstrumentedDefaultDocumentEvent();

        InstrumentedUILog.clear();

        ad.fireChangedUpdate(de);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractDocument.fireChangedUpdate", de },
        /* 2 */{ "DocumentListener1.changedUpdate", de },
        /* 3 */{ "DocumentListener2.changedUpdate", de },
        /* 4 */{ "DocumentListener1.changedUpdate", de }, })
        ) {
            InstrumentedUILog.printLog();
            return failed("expected fireChangedUpdate to call another sequence of events");
        }

        InstrumentedUILog.clear();

        ad.fireInsertUpdate(de);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractDocument.fireInsertUpdate", de },
        /* 2 */{ "DocumentListener1.insertUpdate", de },
        /* 3 */{ "DocumentListener2.insertUpdate", de },
        /* 4 */{ "DocumentListener1.insertUpdate", de }, })) {
            InstrumentedUILog.printLog();
            return failed("expected fireInsertedUpdate to call another sequence of events");
        }

        InstrumentedUILog.clear();

        ad.fireRemoveUpdate(de);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "AbstractDocument.fireRemoveUpdate", de },
        /* 2 */{ "DocumentListener1.removeUpdate", de },
        /* 3 */{ "DocumentListener2.removeUpdate", de },
        /* 4 */{ "DocumentListener1.removeUpdate", de }, })) {
            InstrumentedUILog.printLog();
            return failed("expected fireRemoveUpdate to call another sequence of events");
        }
        
        return passed();
    }
}