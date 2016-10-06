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
import javax.swing.text.AbstractDocument.BranchElement;
import javax.swing.tree.TreeNode;

public class InstrumentedBranchElement extends BranchElement {
    public InstrumentedBranchElement() {
        this(new InstrumentedAbstractDocument(), new InstrumentedElement(), null);
    }

    public InstrumentedBranchElement(AbstractDocument abstractDocument) {
        this(abstractDocument, null, null);
    }

    public InstrumentedBranchElement(AbstractDocument abstractDocument,
            Element arg0, AttributeSet arg1) {
        abstractDocument.super(arg0, arg1);
    }
    
    public InstrumentedAbstractDocument getInstrumentedAbstractDocument() {
        return (InstrumentedAbstractDocument) super.getDocument();
    }

    public Enumeration children() {
        InstrumentedUILog.add(new Object[] { "BranchElement.children" });
        return super.children();
    }

    public boolean getAllowsChildren() {
        InstrumentedUILog
                .add(new Object[] { "BranchElement.getAllowsChildren" });
        return super.getAllowsChildren();
    }

    public Element getElement(int arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.getElement",
                "" + arg0 });
        return super.getElement(arg0);
    }

    public int getElementCount() {
        InstrumentedUILog.add(new Object[] { "BranchElement.getElementCount" });
        return super.getElementCount();
    }

    public int getElementIndex(int arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.getElementIndex",
                "" + arg0 });
        return super.getElementIndex(arg0);
    }

    public int getEndOffset() {
        InstrumentedUILog.add(new Object[] { "BranchElement.getEndOffset" });
        return super.getEndOffset();
    }

    public String getName() {
        InstrumentedUILog.add(new Object[] { "BranchElement.getName" });
        return super.getName();
    }

    public int getStartOffset() {
        InstrumentedUILog.add(new Object[] { "BranchElement.getStartOffset" });
        return super.getStartOffset();
    }

    public boolean isLeaf() {
        InstrumentedUILog.add(new Object[] { "BranchElement.isLeaf" });
        return super.isLeaf();
    }

    public Element positionToElement(int arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.positionToElement",
                "" + arg0 });
        return super.positionToElement(arg0);
    }

    public void replace(int arg0, int arg1, Element[] arg2) {
        InstrumentedUILog.add(new Object[] { "BranchElement.replace",
                "" + arg0, "" + arg1, arg2 });
        super.replace(arg0, arg1, arg2);
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "BranchElement.toString" });
        return super.toString();
    }

    public int getAttributeCount() {
        InstrumentedUILog
                .add(new Object[] { "BranchElement.getAttributeCount" });
        return super.getAttributeCount();
    }

    public int getChildCount() {
        InstrumentedUILog.add(new Object[] { "BranchElement.getChildCount" });
        return super.getChildCount();
    }

    public void dump(PrintStream arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "BranchElement.dump", arg0,
                "" + arg1 });
        super.dump(arg0, arg1);
    }

    public void removeAttribute(Object arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.removeAttribute",
                arg0 });
        super.removeAttribute(arg0);
    }

    public boolean isDefined(Object arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.isDefined", arg0 });
        return super.isDefined(arg0);
    }

    public Enumeration getAttributeNames() {
        InstrumentedUILog
                .add(new Object[] { "BranchElement.getAttributeNames" });
        return super.getAttributeNames();
    }

    public void removeAttributes(Enumeration arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.removeAttributes",
                arg0 });
        super.removeAttributes(arg0);
    }

    public AttributeSet copyAttributes() {
        InstrumentedUILog.add(new Object[] { "BranchElement.copyAttributes" });
        return super.copyAttributes();
    }

    public AttributeSet getAttributes() {
        InstrumentedUILog.add(new Object[] { "BranchElement.getAttributes" });
        return super.getAttributes();
    }

    public AttributeSet getResolveParent() {
        InstrumentedUILog
                .add(new Object[] { "BranchElement.getResolveParent" });
        return super.getResolveParent();
    }

    public void addAttributes(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.addAttributes",
                arg0 });
        super.addAttributes(arg0);
    }

    public void removeAttributes(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.removeAttributes",
                arg0 });
        super.removeAttributes(arg0);
    }

    public void setResolveParent(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.setResolveParent",
                arg0 });
        super.setResolveParent(arg0);
    }

    public boolean containsAttributes(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] {
                "BranchElement.containsAttributes", arg0 });
        return super.containsAttributes(arg0);
    }

    public boolean isEqual(AttributeSet arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.isEqual", arg0 });
        return super.isEqual(arg0);
    }

    public Document getDocument() {
        InstrumentedUILog.add(new Object[] { "BranchElement.getDocument" });
        return super.getDocument();
    }

    public Element getParentElement() {
        InstrumentedUILog
                .add(new Object[] { "BranchElement.getParentElement" });
        return super.getParentElement();
    }

    public TreeNode getParent() {
        InstrumentedUILog.add(new Object[] { "BranchElement.getParent" });
        return super.getParent();
    }

    public TreeNode getChildAt(int arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.getChildAt",
                "" + arg0 });
        return super.getChildAt(arg0);
    }

    public int getIndex(TreeNode arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.getIndex", arg0 });
        return super.getIndex(arg0);
    }

    public Object getAttribute(Object arg0) {
        InstrumentedUILog
                .add(new Object[] { "BranchElement.getAttribute", arg0 });
        return super.getAttribute(arg0);
    }

    public void addAttribute(Object arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] { "BranchElement.addAttribute",
                arg0, arg1 });
        super.addAttribute(arg0, arg1);
    }

    public boolean containsAttribute(Object arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] { "BranchElement.containsAttribute",
                arg0, arg1 });
        return super.containsAttribute(arg0, arg1);
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "BranchElement.hashCode" });
        return super.hashCode();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "BranchElement.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "BranchElement.equals", arg0 });
        return super.equals(arg0);
    }
}