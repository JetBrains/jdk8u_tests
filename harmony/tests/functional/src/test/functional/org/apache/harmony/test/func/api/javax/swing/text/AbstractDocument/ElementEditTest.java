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
import javax.swing.text.Element;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedElement;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedElementEdit;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class ElementEditTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new ElementEditTest().test(args));
    }

    public Result testConstructor() {
        InstrumentedElement ie = new InstrumentedElement();

        Element[] arg1 = new Element[] { new InstrumentedElement() };
        Element[] arg2 = new Element[] { new InstrumentedElement() };

        InstrumentedUILog.clear();

        InstrumentedElementEdit iee = new InstrumentedElementEdit(ie, 0, arg1,
                arg2);

        if (!InstrumentedUILog.equals(new Object[][] {})) {
            InstrumentedUILog.printLog();
            return failed("expected constructor not to call any more methods");
        }

        return passed();
    }

    public Result testGetElement() {
        InstrumentedElement ie = new InstrumentedElement();

        InstrumentedElementEdit iee = new InstrumentedElementEdit(ie, 0, null,
                null);

        if (iee.getElement() != ie) {
            return failed("expect getElement() to return constructor parameter");
        }

        return passed();
    }

}