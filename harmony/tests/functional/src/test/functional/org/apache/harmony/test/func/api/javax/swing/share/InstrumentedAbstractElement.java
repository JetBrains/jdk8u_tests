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
import java.io.PrintStream;
import java.util.Enumeration;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.AbstractDocument.AbstractElement;
import javax.swing.tree.TreeNode;


public class InstrumentedAbstractElement extends AbstractElement {
    public InstrumentedAbstractElement() {
        this(new InstrumentedAbstractDocument(), new InstrumentedElement(), null);
    }
    
    public InstrumentedAbstractElement(AbstractDocument abstractDocument) {
        this(abstractDocument, null, null);
    }
    
    public InstrumentedAbstractElement(AbstractDocument abstractDocument, Element arg0, AttributeSet arg1) {
        abstractDocument.super(arg0, arg1);
    }

    public int getElementCount() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getElementCount"} );
        return 0;
    }

    public int getEndOffset() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getEndOffset"} );
        return 0;
    }

    public int getStartOffset() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getStartOffset"} );
        return 0;
    }

    public boolean getAllowsChildren() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getAllowsChildren"} );
        return false;
    }

    public boolean isLeaf() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.isLeaf"} );
        return false;
    }

    public int getElementIndex(int arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getElementIndex", "" +  arg0} );
        return 0;
    }

    public Enumeration children() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.children"} );
        return null;
    }

    public Element getElement(int arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getElement", "" +  arg0} );
        return null;
    }
    
    
    public void addAttribute(Object arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.addAttribute",  arg0,  arg1} );
        super.addAttribute(arg0, arg1);
    }
    public void addAttributes(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.addAttributes",  arg0} );
        super.addAttributes(arg0);
    }
    public boolean containsAttribute(Object arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.containsAttribute",  arg0,  arg1} );
        return super.containsAttribute(arg0, arg1);
    }
    public boolean containsAttributes(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.containsAttributes",  arg0} );
        return super.containsAttributes(arg0);
    }
    public AttributeSet copyAttributes() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.copyAttributes"} );
        return super.copyAttributes();
    }
    public void dump(PrintStream arg0, int arg1) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.dump",  arg0, "" +  arg1} );
        super.dump(arg0, arg1);
    }
    public Object getAttribute(Object arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getAttribute",  arg0} );
        return super.getAttribute(arg0);
    }
    public int getAttributeCount() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getAttributeCount"} );
        return super.getAttributeCount();
    }
    public Enumeration getAttributeNames() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getAttributeNames"} );
        return super.getAttributeNames();
    }
    public AttributeSet getAttributes() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getAttributes"} );
        return super.getAttributes();
    }
    public TreeNode getChildAt(int arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getChildAt", "" +  arg0} );
        return super.getChildAt(arg0);
    }
    public int getChildCount() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getChildCount"} );
        return super.getChildCount();
    }
    public Document getDocument() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getDocument"} );
        return super.getDocument();
    }
    public InstrumentedAbstractDocument getInstrumentedAbstractDocument() {
        return (InstrumentedAbstractDocument) super.getDocument();
    }
    public int getIndex(TreeNode arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getIndex",  arg0} );
        return super.getIndex(arg0);
    }
    public String getName() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getName"} );
        return super.getName();
    }
    public TreeNode getParent() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getParent"} );
        return super.getParent();
    }
    public Element getParentElement() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getParentElement"} );
        return super.getParentElement();
    }
    public AttributeSet getResolveParent() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.getResolveParent"} );
        return super.getResolveParent();
    }
    public boolean isDefined(Object arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.isDefined",  arg0} );
        return super.isDefined(arg0);
    }
    public boolean isEqual(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.isEqual",  arg0} );
        return super.isEqual(arg0);
    }
    public void removeAttribute(Object arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.removeAttribute",  arg0} );
        super.removeAttribute(arg0);
    }
    public void removeAttributes(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.removeAttributes",  arg0} );
        super.removeAttributes(arg0);
    }
    public void removeAttributes(Enumeration arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.removeAttributes",  arg0} );
        super.removeAttributes(arg0);
    }
    public void setResolveParent(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.setResolveParent",  arg0} );
        super.setResolveParent(arg0);
    }
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] {"AbstractElement.clone"} );
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"AbstractElement.equals",  arg0} );
        return super.equals(arg0);
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.hashCode"} );
        return super.hashCode();
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"AbstractElement.toString"} );
        return super.toString();
    }
}