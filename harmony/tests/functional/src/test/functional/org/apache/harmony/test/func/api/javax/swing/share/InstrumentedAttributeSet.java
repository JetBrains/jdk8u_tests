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

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.text.AttributeSet;

public class InstrumentedAttributeSet implements AttributeSet {
    public int getAttributeCount() {
        InstrumentedUILog
                .add(new Object[] { "AttributeSet.getAttributeCount" });
        return 1;
    }

    public boolean isDefined(Object arg0) {
        InstrumentedUILog.add(new Object[] { "AttributeSet.isDefined", arg0 });
        return "AttributeName1".equals(arg0);
    }

    public Enumeration getAttributeNames() {
        InstrumentedUILog
                .add(new Object[] { "AttributeSet.getAttributeNames" });
        Vector v = new Vector();
        v.add("AttributeName1");

        return v.elements();
    }

    public AttributeSet copyAttributes() {
        InstrumentedUILog.add(new Object[] { "AttributeSet.copyAttributes" });
        return this;
    }

    public AttributeSet getResolveParent() {
        InstrumentedUILog.add(new Object[] { "AttributeSet.getResolveParent" });
        return null;
    }

    public boolean containsAttributes(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] { "AttributeSet.containsAttributes",
                arg0 });
        return arg0 == null
                || arg0.getAttributeCount() == 0
                || (arg0.getAttributeCount() == 1 && arg0.containsAttribute(
                        "AttributeName1", "AttributeValue1"));
    }

    public boolean isEqual(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] { "AttributeSet.isEqual", arg0 });
        return arg0 instanceof InstrumentedAttributeSet;
    }

    public Object getAttribute(Object arg0) {
        InstrumentedUILog
                .add(new Object[] { "AttributeSet.getAttribute", arg0 });
        return "AttributeName1".equals(arg0) ? "AttributeValue1" : null;
    }

    public boolean containsAttribute(Object arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] { "AttributeSet.containsAttribute",
                arg0, arg1 });
        return "AttributeName1".equals(arg0) && "AttributeValue1".equals(arg1);
    }
}