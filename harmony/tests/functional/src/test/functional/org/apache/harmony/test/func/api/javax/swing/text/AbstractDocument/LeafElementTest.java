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

import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAbstractDocument;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedAttributeSet;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLeafElement;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedElement;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class LeafElementTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new LeafElementTest().test(args));
    }

    public Result testConstructor() {
        InstrumentedAbstractDocument ad = new InstrumentedAbstractDocument();
        Element e = new InstrumentedElement();
        AttributeSet as = new InstrumentedAttributeSet();

        ad.writeLockExposed();
        InstrumentedUILog.clear();

        try {
            new InstrumentedLeafElement(ad, e, as, 0, 3);
            if (!InstrumentedUILog.equals((new Object[][] {
            /* 1 */{ "LeafElement.addAttributes", as },
            /* 2 */{ "AttributeSet.getAttributeCount" },
            /* 3 */{ "AttributeSet.getAttributeNames" },
            /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" },
            /* 5 */{ "AbstractDocument.createPosition", "0" },
            /* 6 */{ "Content.createPosition", "0" },
            /* 7 */{ "AbstractDocument.createPosition", "3" },
            /* 8 */{ "Content.createPosition", "3" }, }))
            
            && !InstrumentedUILog.equals((new Object[][] {
                    /* 1 */{ "LeafElement.addAttributes", as },
                    /* 3 */{ "AttributeSet.getAttributeNames" },
                    /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" },
                    /* 5 */{ "AbstractDocument.createPosition", "0" },
                    /* 6 */{ "Content.createPosition", "0" },
                    /* 7 */{ "AbstractDocument.createPosition", "3" },
                    /* 8 */{ "Content.createPosition", "3" }, }))
            ) {
                InstrumentedUILog.printLog();
                return failed("expected constructor to call another sequence of events");
            }
        } catch (Throwable ee) {
            InstrumentedUILog.printLogAsArray();
            return failed("got exception");
        } finally {
            ad.writeUnLockExposed();
        }

        return passed();
    }

    public Result testAttributes() {
        InstrumentedLeafElement e = new InstrumentedLeafElement();
        AttributeSet as = new InstrumentedAttributeSet();

        e.getInstrumentedAbstractDocument().writeLockExposed();

        InstrumentedUILog.clear();

        if (e.getAttributes() != e) {
            return failed("expected LeafElement.getAttributes() to return element itself");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "LeafElement.getAttributes" } })) {
            InstrumentedUILog.printLog();
            return failed("expected getAttributes() not to call any additional methods");
        }
        InstrumentedUILog.clear();

        if (e.getAttributeNames().hasMoreElements()) {
            return failed("expected LeafElement.getAttributeNames() to be empty by default");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "LeafElement.getAttributeNames" } })) {
            InstrumentedUILog.printLog();
            return failed("expected getAttributeNames() not to call any additional methods");
        }

        InstrumentedUILog.clear();

        if (e.getAttributeCount() != 0) {
            return failed("expected LeafElement.getAttributeCount() to return 0 by default");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] { { "LeafElement.getAttributeCount" } })) {
            InstrumentedUILog.printLog();
            return failed("expected getAttributeCount() not to call any additional methods");
        }

        InstrumentedUILog.clear();

        try {
            e.addAttributes(as);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "LeafElement.addAttributes", as },
            /* 2 */{ "AttributeSet.getAttributeCount" },
            /* 3 */{ "AttributeSet.getAttributeNames" },
            /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" }, })
            && !InstrumentedUILog.equals(new Object[][] {
                    /* 1 */{ "LeafElement.addAttributes", as },
                    /* 3 */{ "AttributeSet.getAttributeNames" },
                    /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" }, })
            ) {
                InstrumentedUILog.printLog();
                return failed("expected addAttributes() to call another sequence of events");
            }

            as = new InstrumentedAttributeSet() {
                public int getAttributeCount() {
                    super.getAttributeCount();
                    return -1;
                };
            };

            InstrumentedUILog.clear();

            e.addAttributes(as);
            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "LeafElement.addAttributes", as },
            /* 2 */{ "AttributeSet.getAttributeCount" },
            /* 3 */{ "AttributeSet.getAttributeNames" },
            /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" }, })
            
            && !InstrumentedUILog.equals(new Object[][] {
                    /* 1 */{ "LeafElement.addAttributes", as },
                    /* 3 */{ "AttributeSet.getAttributeNames" },
                    /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" }, })
            ) {
                InstrumentedUILog.printLog();
                return failed("expected addAttributes() with negative elements count to call another sequence of events");
            }

            as = new InstrumentedAttributeSet() {
                public int getAttributeCount() {
                    super.getAttributeCount();
                    return 0;
                };
            };

            InstrumentedUILog.clear();

            e.addAttributes(as);

            if (!InstrumentedUILog.equals(new Object[][] {
            /* 1 */{ "LeafElement.addAttributes", as },
            /* 2 */{ "AttributeSet.getAttributeCount" },
            /* 3 */{ "AttributeSet.getAttributeNames" },
            /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" }, })
            && !InstrumentedUILog.equals(new Object[][] {
                    /* 1 */{ "LeafElement.addAttributes", as },
                    /* 3 */{ "AttributeSet.getAttributeNames" },
                    /* 4 */{ "AttributeSet.getAttribute", "AttributeName1" }, })
            ) {
                InstrumentedUILog.printLog();
                return failed("expected addAttributes() with zero elements count to call another sequence of events");
            }

            if (!"AttributeValue1".equals(e.getAttribute("AttributeName1"))) {
                InstrumentedUILog.printLog();
                return failed("expected attribute value to be set");
            }

            if (e.getAttribute("attributename1") != null) {
                return failed("expected attribute value to be case-insensitive");
            }

            try {
                e.getAttribute(null);
                return failed("expected getAttribute(null) to throw NPE");
            } catch (NullPointerException npe) {
            }

            try {
                e.addAttribute(null, "abc");
                return failed("expected setAttribute(null, String) to throw NPE");
            } catch (NullPointerException npe) {
            }

            Enumeration eennuumm = e.getAttributeNames();
            if (!eennuumm.hasMoreElements()
                    || !"AttributeName1".equals(eennuumm.nextElement())
                    || eennuumm.hasMoreElements()) {
                return failed("expected attributeNames to contain only one name");
            }

            AttributeSet attrset = e.getAttributes();
            if (!attrset.isEqual(new InstrumentedAttributeSet())) {
                return failed("expected attributesets to be equal");
            }

        } finally {
            e.getInstrumentedAbstractDocument().writeUnLockExposed();
        }

        return passed();
    }

    public Result testDocument() {
        InstrumentedAbstractDocument d = new InstrumentedAbstractDocument();

        InstrumentedUILog.clear();
        InstrumentedLeafElement e = new InstrumentedLeafElement(d);

        if (e.getDocument() != d) {
            return failed("expected getDocument() to return constructor argument");
        }

        try {
            new InstrumentedLeafElement(null);
            return failed("expected LeafElement constructor with document == null to throw NPE");
        } catch (NullPointerException npe) {
        }

        return passed();
    }

    public Result testElements() {
        InstrumentedAbstractDocument ad = new InstrumentedAbstractDocument();
        Element ie = new InstrumentedElement();
        AttributeSet as = new InstrumentedAttributeSet();

        ad.writeLockExposed();

        try {
            InstrumentedLeafElement be = new InstrumentedLeafElement(ad, ie,
                    as, 0, 3);

            InstrumentedUILog.clear();

            if (be.getElement(1234) != null) {
                return failed("expected getElement(1234) to return null");
            }

            if (!InstrumentedUILog.equals(new Object[][] { {
                    "LeafElement.getElement", "1234" } })) {
                InstrumentedUILog.printLog();
                return failed("expected LeafElement.getElement(1234) no to call any more methods");
            }

            InstrumentedUILog.clear();

            if (be.getElement(-1234) != null) {
                return failed("expected getElement(-1234) to return null");
            }

            if (!InstrumentedUILog.equals(new Object[][] { {
                    "LeafElement.getElement", "-1234" } })) {
                InstrumentedUILog.printLog();
                return failed("expected LeafElement.getElement(-1234) no to call any more methods");
            }

            InstrumentedUILog.clear();

            if (be.getElementCount() != 0) {
                return failed("expected getElementCount() to return 0");
            }

            if (!InstrumentedUILog
                    .equals(new Object[][] { { "LeafElement.getElementCount" } })) {
                InstrumentedUILog.printLog();
                return failed("expected LeafElement.getElementCount() no to call any more methods");
            }
        } finally {
            ad.writeUnLockExposed();
        }

        return passed();
    }

    public Result testGetElementIndex() {
        InstrumentedLeafElement be = new InstrumentedLeafElement() {
            public int getStartOffset() {
                try {
                    super.getStartOffset();
                } catch (NullPointerException e) {
                }
                return 15;
            }
        };
        InstrumentedUILog.clear();

        if (be.getElementIndex(1234) != -1) {
            return failed("expected getElementIndex(1234) to return -1, got "
                    + be.getElementIndex(1234));
        }

        if (!InstrumentedUILog.equals(new Object[][] { {
                "LeafElement.getElementIndex", "1234" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected LeafElement.getElementIndex(1234) not to call any more methods");
        }

        InstrumentedUILog.clear();

        if (be.getElementIndex(-1234) != -1) {
            return failed("expected getElementIndex(-1234) to return -1, got "
                    + be.getElementIndex(1 - 234));
        }

        if (!InstrumentedUILog.equals(new Object[][] { {
                "LeafElement.getElementIndex", "-1234" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected LeafElement.getElementIndex(-1234) no to call any more methods");
        }

        return passed();
    }

    public Result testGetOffset() {
        InstrumentedLeafElement be = new InstrumentedLeafElement() {
            public int getStartOffset() {
                try {
                    super.getStartOffset();
                } catch (NullPointerException e) {
                }
                return 15;
            }

            public int getEndOffset() {
                try {
                    super.getEndOffset();
                } catch (NullPointerException e) {
                }
                return 25;
            }

        };
        InstrumentedUILog.clear();
        be.getStartOffset();

        if (!InstrumentedUILog
                .equals(new Object[][] { { "LeafElement.getStartOffset" } })) {
            InstrumentedUILog.printLog();
            return failed("expected LeafElement.getStartOffset() no to call any more methods");
        }

        InstrumentedUILog.clear();
        be.getEndOffset();
        if (!InstrumentedUILog
                .equals(new Object[][] { { "LeafElement.getEndOffset" } })) {
            InstrumentedUILog.printLog();
            return failed("expected LeafElement.getEndOffset() no to call any more methods");
        }

        InstrumentedUILog.clear();

        return passed();
    }
}