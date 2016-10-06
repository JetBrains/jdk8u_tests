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

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentEvent.ElementChange;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.Position.Bias;

public class InstrumentedView extends View {
    public InstrumentedView() {
        this(new InstrumentedElement());
    }

    public InstrumentedView(Element arg0) {
        super(arg0);

    }

    public float getPreferredSpan(int arg0) {
        InstrumentedUILog
                .add(new Object[] { "View.getPreferredSpan", "" + arg0 });
        return 0;
    }

    public void paint(Graphics arg0, Shape arg1) {
        InstrumentedUILog.add(new Object[] { "View.paint", arg0, arg1 });
    }

    public int viewToModel(float arg0, float arg1, Shape arg2, Bias[] arg3) {
        InstrumentedUILog.add(new Object[] { "View.viewToModel", "" + arg0,
                "" + arg1, arg2, arg3 });
        return 0;
    }

    public Shape modelToView(int arg0, Shape arg1, Bias arg2)
            throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "View.modelToView", "" + arg0,
                arg1, arg2 });
        return new InstrumentedShape();
    }

    public void append(View arg0) {
        InstrumentedUILog.add(new Object[] { "View.append", arg0 });
        super.append(arg0);
    }

    public View breakView(int arg0, int arg1, float arg2, float arg3) {
        InstrumentedUILog.add(new Object[] { "View.breakView", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        return super.breakView(arg0, arg1, arg2, arg3);
    }

    public void changedUpdate(DocumentEvent arg0, Shape arg1, ViewFactory arg2) {
        InstrumentedUILog.add(new Object[] { "View.changedUpdate", arg0, arg1,
                arg2 });
        super.changedUpdate(arg0, arg1, arg2);
    }

    public View createFragment(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "View.createFragment", "" + arg0,
                "" + arg1 });
        return super.createFragment(arg0, arg1);
    }

    protected void forwardUpdate(ElementChange arg0, DocumentEvent arg1,
            Shape arg2, ViewFactory arg3) {
        super.forwardUpdate(arg0, arg1, arg2, arg3);
    }

    protected void forwardUpdateToView(View arg0, DocumentEvent arg1,
            Shape arg2, ViewFactory arg3) {
        super.forwardUpdateToView(arg0, arg1, arg2, arg3);
    }

    public float getAlignment(int arg0) {
        InstrumentedUILog.add(new Object[] { "View.getAlignment", "" + arg0 });
        return super.getAlignment(arg0);
    }

    public AttributeSet getAttributes() {
        InstrumentedUILog.add(new Object[] { "View.getAttributes" });
        return super.getAttributes();
    }

    public int getBreakWeight(int arg0, float arg1, float arg2) {
        InstrumentedUILog.add(new Object[] { "View.getBreakWeight", "" + arg0,
                "" + arg1, "" + arg2 });
        return super.getBreakWeight(arg0, arg1, arg2);
    }

    public Shape getChildAllocation(int arg0, Shape arg1) {
        InstrumentedUILog.add(new Object[] { "View.getChildAllocation",
                "" + arg0, arg1 });
        return super.getChildAllocation(arg0, arg1);
    }

    public Container getContainer() {
        InstrumentedUILog.add(new Object[] { "View.getContainer" });
        return super.getContainer();
    }

    public Document getDocument() {
        InstrumentedUILog.add(new Object[] { "View.getDocument" });
        return super.getDocument();
    }

    public Element getElement() {
        InstrumentedUILog.add(new Object[] { "View.getElement" });
        return super.getElement();
    }

    public int getEndOffset() {
        InstrumentedUILog.add(new Object[] { "View.getEndOffset" });
        return super.getEndOffset();
    }

    public Graphics getGraphics() {
        InstrumentedUILog.add(new Object[] { "View.getGraphics" });
        return super.getGraphics();
    }

    public float getMaximumSpan(int arg0) {
        InstrumentedUILog
                .add(new Object[] { "View.getMaximumSpan", "" + arg0 });
        return super.getMaximumSpan(arg0);
    }

    public float getMinimumSpan(int arg0) {
        InstrumentedUILog
                .add(new Object[] { "View.getMinimumSpan", "" + arg0 });
        return super.getMinimumSpan(arg0);
    }

    public int getNextVisualPositionFrom(int arg0, Bias arg1, Shape arg2,
            int arg3, Bias[] arg4) throws BadLocationException {
        return super.getNextVisualPositionFrom(arg0, arg1, arg2, arg3, arg4);
    }

    public View getParent() {
        InstrumentedUILog.add(new Object[] { "View.getParent" });
        return super.getParent();
    }

    public int getResizeWeight(int arg0) {
        InstrumentedUILog
                .add(new Object[] { "View.getResizeWeight", "" + arg0 });
        return super.getResizeWeight(arg0);
    }

    public int getStartOffset() {
        InstrumentedUILog.add(new Object[] { "View.getStartOffset" });
        return super.getStartOffset();
    }

    public String getToolTipText(float arg0, float arg1, Shape arg2) {
        InstrumentedUILog.add(new Object[] { "View.getToolTipText", "" + arg0,
                "" + arg1, arg2 });
        return super.getToolTipText(arg0, arg1, arg2);
    }

    public View getView(int arg0) {
        InstrumentedUILog.add(new Object[] { "View.getView", "" + arg0 });
        return super.getView(arg0);
    }

    public int getViewCount() {
        InstrumentedUILog.add(new Object[] { "View.getViewCount" });
        return super.getViewCount();
    }

    public ViewFactory getViewFactory() {
        InstrumentedUILog.add(new Object[] { "View.getViewFactory" });
        return super.getViewFactory();
    }

    public int getViewIndex(float arg0, float arg1, Shape arg2) {
        InstrumentedUILog.add(new Object[] { "View.getViewIndex", "" + arg0,
                "" + arg1, arg2 });
        return super.getViewIndex(arg0, arg1, arg2);
    }

    public int getViewIndex(int arg0, Bias arg1) {
        InstrumentedUILog.add(new Object[] { "View.getViewIndex", "" + arg0,
                arg1 });
        return super.getViewIndex(arg0, arg1);
    }

    public void insert(int arg0, View arg1) {
        InstrumentedUILog.add(new Object[] { "View.insert", "" + arg0, arg1 });
        super.insert(arg0, arg1);
    }

    public void insertUpdate(DocumentEvent arg0, Shape arg1, ViewFactory arg2) {
        InstrumentedUILog.add(new Object[] { "View.insertUpdate", arg0, arg1,
                arg2 });
        super.insertUpdate(arg0, arg1, arg2);
    }

    public boolean isVisible() {
        InstrumentedUILog.add(new Object[] { "View.isVisible" });
        return super.isVisible();
    }

    public Shape modelToView(int arg0, Bias arg1, int arg2, Bias arg3,
            Shape arg4) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "View.modelToView", "" + arg0,
                arg1, "" + arg2, arg3, arg4 });
        try {
            return super.modelToView(arg0, arg1, arg2, arg3, arg4);
        } catch (NullPointerException e) {
            return new InstrumentedShape();
        }
    }

    public Shape modelToView(int arg0, Shape arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "View.modelToView", "" + arg0,
                arg1 });
        return super.modelToView(arg0, arg1);
    }

    public void preferenceChanged(View arg0, boolean arg1, boolean arg2) {
        InstrumentedUILog.add(new Object[] { "View.preferenceChanged", arg0,
                "" + arg1, "" + arg2 });
        super.preferenceChanged(arg0, arg1, arg2);
    }

    public void remove(int arg0) {
        InstrumentedUILog.add(new Object[] { "View.remove", "" + arg0 });
        super.remove(arg0);
    }

    public void removeAll() {
        InstrumentedUILog.add(new Object[] { "View.removeAll" });
        super.removeAll();
    }

    public void removeUpdate(DocumentEvent arg0, Shape arg1, ViewFactory arg2) {
        InstrumentedUILog.add(new Object[] { "View.removeUpdate", arg0, arg1,
                arg2 });
        super.removeUpdate(arg0, arg1, arg2);
    }

    public void replace(int arg0, int arg1, View[] arg2) {
        InstrumentedUILog.add(new Object[] { "View.replace", "" + arg0,
                "" + arg1, arg2 });
        super.replace(arg0, arg1, arg2);
    }

    public void setParent(View arg0) {
        InstrumentedUILog.add(new Object[] { "View.setParent", arg0 });
        super.setParent(arg0);
    }

    public void setSize(float arg0, float arg1) {
        InstrumentedUILog.add(new Object[] { "View.setSize", "" + arg0,
                "" + arg1 });
        super.setSize(arg0, arg1);
    }

    protected boolean updateChildren(ElementChange arg0, DocumentEvent arg1,
            ViewFactory arg2) {
        return super.updateChildren(arg0, arg1, arg2);
    }

    protected void updateLayout(ElementChange arg0, DocumentEvent arg1,
            Shape arg2) {
        super.updateLayout(arg0, arg1, arg2);
    }

    public int viewToModel(float arg0, float arg1, Shape arg2) {
        InstrumentedUILog.add(new Object[] { "View.viewToModel", "" + arg0,
                "" + arg1, arg2 });
        return super.viewToModel(arg0, arg1, arg2);
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "View.hashCode" });
        return super.hashCode();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "View.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "View.equals", arg0 });
        return super.equals(arg0);
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "View.toString" });
        return super.toString();
    }
}