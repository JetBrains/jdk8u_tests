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

import javax.swing.text.AttributeSet;
import javax.swing.text.AbstractDocument.AttributeContext;

public class InstrumentedAttributeContext implements AttributeContext {
    public AttributeSet getEmptySet() {
        InstrumentedUILog.add(new Object[] { "AttributeContext.getEmptySet" });
        return null;
    }

    public void reclaim(AttributeSet arg0) {
        InstrumentedUILog
                .add(new Object[] { "AttributeContext.reclaim", arg0 });
    }

    public AttributeSet removeAttribute(AttributeSet arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] {
                "AttributeContext.removeAttribute", arg0, arg1 });
        return null;
    }

    public AttributeSet removeAttributes(AttributeSet arg0, Enumeration arg1) {
        InstrumentedUILog.add(new Object[] {
                "AttributeContext.removeAttributes", arg0, arg1 });
        return null;
    }

    public AttributeSet addAttributes(AttributeSet arg0, AttributeSet arg1) {
        InstrumentedUILog.add(new Object[] { "AttributeContext.addAttributes",
                arg0, arg1 });
        return null;
    }

    public AttributeSet removeAttributes(AttributeSet arg0, AttributeSet arg1) {
        InstrumentedUILog.add(new Object[] {
                "AttributeContext.removeAttributes", arg0, arg1 });
        return null;
    }

    public AttributeSet addAttribute(AttributeSet arg0, Object arg1, Object arg2) {
        InstrumentedUILog.add(new Object[] { "AttributeContext.addAttribute",
                arg0, arg1, arg2 });
        return null;
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "AttributeContext.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "AttributeContext.equals", arg0 });
        return super.equals(arg0);
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "AttributeContext.hashCode" });
        return super.hashCode();
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "AttributeContext.toString" });
        return super.toString();
    }
}