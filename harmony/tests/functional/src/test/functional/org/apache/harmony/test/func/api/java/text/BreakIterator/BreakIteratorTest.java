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

package org.apache.harmony.test.func.api.java.text.BreakIterator;

import java.text.BreakIterator;
import java.util.Locale;

import org.apache.harmony.test.func.api.java.text.share.framework.TextTestFramework;
import org.apache.harmony.test.func.api.java.text.share.util.BoxedValue;
import org.apache.harmony.test.func.api.java.text.share.util.InvokeData;
import org.apache.harmony.test.func.api.java.text.share.util.MethodSignature;
import org.apache.harmony.test.func.api.java.text.share.util.ObjectState;

public class BreakIteratorTest extends TextTestFramework{

    private static String getTestString() {
        return "The Functional test suite is a "
                + "collection of "
                + "micro scenarios for "
                + "testing various functional parts of "
                + "an implementation.";
    }

    private String testStateArray(Class objectType, ObjectState[] state, String className) {
        for (int j = 0; j < state.length; j++) {
            try {
            state[j].verifyState(objectType);
            } catch (Throwable e)
            {
                return className + "." + e.getMessage();
            }
        }
        return null;

    }

    public int test() {
        Locale.setDefault(Locale.US);
        BreakIterator[] iterators = { 
                BreakIterator.getWordInstance(),
                BreakIterator.getSentenceInstance(), 
                BreakIterator.getWordInstance(Locale.US),
                BreakIterator.getSentenceInstance(Locale.US), 
                };
        for (int i = 0; i < iterators.length; i++) {
            iterators[i].setText(getTestString());
        }
        
        ObjectState[][] state = { 
                getWordState(iterators[0]),
                getSentenceState(iterators[1]),
                getWordState(iterators[2]),
                getSentenceState(iterators[3]),
                getWordState((BreakIterator) iterators[0].clone()),
                getSentenceState((BreakIterator) iterators[1].clone()),
                getWordState((BreakIterator) iterators[2].clone()),
                getSentenceState((BreakIterator) iterators[3].clone())
                };

        BreakIterator it = BreakIterator.getWordInstance();
        it.setText(getTestString());
        System.out.println(it.next());
        for (int i = 0; i < 0; i++) {
            String result =
                testStateArray(BreakIterator.class, state[i], i % 2 == 0 ? "BreakIterator.getWordInstance()" :
                    "BreakIterator.getSentenceInstance()");
            if (result != null)
                return fail(result);
        }
        return pass();
    }

    public static void main(String[] args) {

        System.exit(new BreakIteratorTest().test());
    }

    private ObjectState[] getWordState(BreakIterator it) {
        ObjectState[] result = new ObjectState[] {
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(5)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(5)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(9)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(10)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(10)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(14)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(15)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(15)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(22)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(23)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(23)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(26)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(28)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(28)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(33)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(34)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(34)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(39)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(40)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(40)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(45)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(50)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(50)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(55)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(56)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(56)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(60)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(64)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(64)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(66)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(69)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(69)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(73)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(74)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(74)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(79)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(81)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(81)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(86)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(87)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(87)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(90)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(94)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(94)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(97)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(98)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(98)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(102)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(103)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(103)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(106)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(108)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(108)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(115)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(116)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(116)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(118)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(124)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(124)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(126)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(127)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(127)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(130)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(132)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(132)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(137)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(138)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(138)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(142)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(150)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(150)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(152)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(155)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(155)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(160)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(161)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(161)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(166)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(173)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(173)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(175)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(176)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(176)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(180)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(183)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(183)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(189)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(190)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(190)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(197)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(199)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(199)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(204)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(205)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(205)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(209)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(215)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(215)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(219)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(220)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(220)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(227)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(228)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(228)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(230)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(229)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(229)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(228)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(228)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(227)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(227)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(226)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(226)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(220)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(220)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(219)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(219)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(216)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(216)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(215)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(215)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(209)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(209)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(208)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(208)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(205)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(205)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(204)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(204)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(200)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(200)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(199)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(199)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(197)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(197)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(196)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(196)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(190)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(190)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(189)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(189)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(184)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(184)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(183)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(183)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(180)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(180)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(179)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(179)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(176)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(176)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(175)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(175)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(174)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(174)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(173)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(173)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(166)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(166)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(165)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(165)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(161)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(161)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(160)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(160)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(156)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(156)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(155)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(155)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(152)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(152)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(151)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(151)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(150)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(150)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(142)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(142)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(141)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(141)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(138)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(138)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(137)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(137)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(133)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(133)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(132)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(132)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(130)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(130)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(129)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(129)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(127)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(127)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(126)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(126)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(125)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(125)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(124)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(124)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(118)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(118)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(117)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(117)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(116)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(116)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(115)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(115)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(109)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(109)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(108)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(108)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(106)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(106)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(105)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(105)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(103)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(103)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(102)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(102)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(101)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(101)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(98)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(98)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(97)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(97)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(95)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(95)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(94)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(94)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(90)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(90)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(89)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(89)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(87)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(87)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(86)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(86)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(82)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(82)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(81)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(81)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(79)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(79)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(78)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(78)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(74)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(74)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(73)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(73)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(70)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(70)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(69)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(69)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(66)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(66)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(65)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(65)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(64)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(64)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(60)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(60)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(59)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(59)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(56)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(56)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(55)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(55)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(51)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(51)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(50)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(50)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(45)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(45)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(44)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(44)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(40)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(40)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(39)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(39)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(35)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(35)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(34)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(34)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(33)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(33)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(29)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(29)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(28)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(28)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(26)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(26)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(25)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(25)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(23)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(23)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(22)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(22)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(16)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(16)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(15)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(15)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(14)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(14)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(13)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(13)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(10)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(10)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(9)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(9)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(6)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(6)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(5)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(5)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(0)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(0)),
                                int.class) }), };
        return result;
    }

    private ObjectState[] getSentenceState(BreakIterator it) {
        ObjectState[] result = new ObjectState[] {
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(176)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(176)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("next", new Class[] { int.class }),
                        new BoxedValue[] { new BoxedValue((new Integer(2)),
                                int.class) }) },
                        new BoxedValue[] { new BoxedValue((new Integer(-1)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(176)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(176)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("previous", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(0)),
                                int.class) }),
                new ObjectState(it, new InvokeData[] { new InvokeData(
                        new MethodSignature("current", new Class[] {}),
                        new BoxedValue[] {}) },
                        new BoxedValue[] { new BoxedValue((new Integer(0)),
                                int.class) }), };
        return result;
    }
}