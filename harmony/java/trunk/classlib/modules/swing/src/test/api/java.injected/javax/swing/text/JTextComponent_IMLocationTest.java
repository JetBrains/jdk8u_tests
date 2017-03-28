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
 * @author Evgeniya G. Maenkova
 */
package javax.swing.text;

import java.awt.event.InputMethodEvent;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingWaitTestCase;

public class JTextComponent_IMLocationTest extends SwingWaitTestCase {
    JTextArea jta;

    JFrame jf;

    InputMethodEvent ime;

    Map<Attribute, Object> map;

    AbstractDocument doc;

    AttributedCharacterIterator iter;

    AttributedString attrString;

    static final AttributedCharacterIterator.Attribute SEGMENT_ATTRIBUTE = AttributedCharacterIterator.Attribute.INPUT_METHOD_SEGMENT;

    static final String initialContent = "IM test";

    boolean bWasException;

    InputMethodRequests imr;

    String message;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        map = new HashMap<Attribute, Object>();
        jf = new JFrame();
        jta = new JTextArea();
        jta.setText(initialContent);
        doc = (AbstractDocument) jta.getDocument();
        imr = jta.getInputMethodRequests();
        bWasException = false;
        message = null;
        jf.getContentPane().add(jta);
        jf.setSize(200, 300);
        jf.setVisible(true);
        component = jf;
    }

    @Override
    protected void tearDown() throws Exception {
        jf.dispose();
        super.tearDown();
    }

    private InputMethodEvent getTextEvent(AttributedCharacterIterator text,
            int committedCharacterCount, TextHitInfo caret, TextHitInfo visiblePosition) {
        return getEvent(InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, text,
                committedCharacterCount, caret, visiblePosition);
    }

    private InputMethodEvent getEvent(final int id, final AttributedCharacterIterator text,
            final int committedCharacterCount, final TextHitInfo caret,
            final TextHitInfo visiblePosition) {
        return new InputMethodEvent(jta, id, text, committedCharacterCount, caret,
                visiblePosition);
    }

    private Map<Attribute, Object> putSegmentAttribute(final Map<Attribute, Object> map, final Object value) {
        map.put(SEGMENT_ATTRIBUTE, value);
        return map;
    }

    private AttributedCharacterIterator getIterator(final String text, final Map<Attribute, Object> map) {
        attrString = new AttributedString(text, map);
        return attrString.getIterator();
    }
}
