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
import javax.swing.text.MutableAttributeSet;

public class InstrumentedMutableAttributeSet implements MutableAttributeSet {
    public void removeAttribute(Object arg0) {
    InstrumentedUILog.add(new Object[] {"MutableAttributeSet.removeAttribute",  arg0} );
    }

    public void removeAttributes(Enumeration arg0) {
    InstrumentedUILog.add(new Object[] {"MutableAttributeSet.removeAttributes",  arg0} );
    }

    public void addAttributes(AttributeSet arg0) {
    InstrumentedUILog.add(new Object[] {"MutableAttributeSet.addAttributes",  arg0} );
    }

    public void removeAttributes(AttributeSet arg0) {
    InstrumentedUILog.add(new Object[] {"MutableAttributeSet.removeAttributes",  arg0} );
    }

    public void setResolveParent(AttributeSet arg0) {
    InstrumentedUILog.add(new Object[] {"MutableAttributeSet.setResolveParent",  arg0} );
    }

    public void addAttribute(Object arg0, Object arg1) {
    InstrumentedUILog.add(new Object[] {"MutableAttributeSet.addAttribute",  arg0,  arg1} );
    }

    public int getAttributeCount() {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.getAttributeCount"} );
        return 0;
    }

    public boolean isDefined(Object arg0) {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.isDefined",  arg0} );
        return false;
    }

    public Enumeration getAttributeNames() {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.getAttributeNames"} );
        return null;
    }

    public AttributeSet copyAttributes() {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.copyAttributes"} );
        return null;
    }

    public AttributeSet getResolveParent() {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.getResolveParent"} );
        return null;
    }

    public boolean containsAttributes(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.containsAttributes",  arg0} );
        return false;
    }

    public boolean isEqual(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.isEqual",  arg0} );
        return false;
    }

    public Object getAttribute(Object arg0) {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.getAttribute",  arg0} );
        return null;
    }

    public boolean containsAttribute(Object arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] {"MutableAttributeSet.containsAttribute",  arg0,  arg1} );
        return false;
    }
}
