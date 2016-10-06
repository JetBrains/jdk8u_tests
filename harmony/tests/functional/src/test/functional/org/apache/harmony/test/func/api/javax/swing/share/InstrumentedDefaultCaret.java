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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.EventListener;

import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.Highlighter.HighlightPainter;

public class InstrumentedDefaultCaret extends DefaultCaret {
    public void addChangeListener(ChangeListener arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.addChangeListener",
                arg0 });
        super.addChangeListener(arg0);
    }

    public void adjustVisibility(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.adjustVisibility",
                arg0 });
        super.adjustVisibility(arg0);
    }

    public void damage(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.damage", arg0 });
        super.damage(arg0);
    }

    public void deinstall(JTextComponent arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.deinstall", arg0 });
        super.deinstall(arg0);
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.equals", arg0 });
        return super.equals(arg0);
    }

    protected void fireStateChanged() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.fireStateChanged" });
        super.fireStateChanged();
    }

    public void focusGained(FocusEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.focusGained", arg0 });
        super.focusGained(arg0);
    }

    public void focusLost(FocusEvent arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.focusLost", arg0 });
        super.focusLost(arg0);
    }

    public int getBlinkRate() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getBlinkRate" });
        return super.getBlinkRate();
    }

    public ChangeListener[] getChangeListeners() {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.getChangeListeners" });
        return super.getChangeListeners();
    }

    public int getDot() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getDot" });
        return super.getDot();
    }

    public EventListener[] getListeners(Class arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.getListeners", arg0 });
        return super.getListeners(arg0);
    }

    public Point getMagicCaretPosition() {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.getMagicCaretPosition" });
        return super.getMagicCaretPosition();
    }

    public int getMark() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getMark" });
        return super.getMark();
    }

    protected HighlightPainter getSelectionPainter() {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.getSelectionPainter" });
        return super.getSelectionPainter();
    }

    public void install(JTextComponent arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.install", arg0 });
        super.install(arg0);
    }

    public boolean isSelectionVisible() {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.isSelectionVisible" });
        return super.isSelectionVisible();
    }

    public boolean isVisible() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.isVisible" });
        return super.isVisible();
    }

    public void mouseClicked(MouseEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.mouseClicked", arg0 });
        super.mouseClicked(arg0);
    }

    public void mouseDragged(MouseEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.mouseDragged", arg0 });
        super.mouseDragged(arg0);
    }

    public void mouseEntered(MouseEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.mouseEntered", arg0 });
        super.mouseEntered(arg0);
    }

    public void mouseExited(MouseEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.mouseExited", arg0 });
        super.mouseExited(arg0);
    }

    public void mouseMoved(MouseEvent arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.mouseMoved", arg0 });
        super.mouseMoved(arg0);
    }

    public void mousePressed(MouseEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.mousePressed", arg0 });
        super.mousePressed(arg0);
    }

    public void mouseReleased(MouseEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.mouseReleased", arg0 });
        super.mouseReleased(arg0);
    }

    protected void moveCaret(MouseEvent arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.moveCaret", arg0 });
        try {
            super.moveCaret(arg0);
        } catch (NullPointerException e) {
        }
    }

    public void moveDot(int arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.moveDot", "" + arg0 });
        super.moveDot(arg0);
    }

    public void paint(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.paint", arg0 });
        super.paint(arg0);
    }

    public void positionCaret(MouseEvent arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.positionCaret", arg0 });
        try {
            super.positionCaret(arg0);
        } catch (NullPointerException e) {
        }
    }

    public void removeChangeListener(ChangeListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "DefaultCaret.removeChangeListener", arg0 });
        super.removeChangeListener(arg0);
    }

    public void setBlinkRate(int arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setBlinkRate",
                "" + arg0 });
        super.setBlinkRate(arg0);
    }

    public void setDot(int arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.setDot", "" + arg0 });
        super.setDot(arg0);
    }

    public void setMagicCaretPosition(Point arg0) {
        InstrumentedUILog.add(new Object[] {
                "DefaultCaret.setMagicCaretPosition", arg0 });
        super.setMagicCaretPosition(arg0);
    }

    public void setSelectionVisible(boolean arg0) {
        InstrumentedUILog.add(new Object[] {
                "DefaultCaret.setSelectionVisible", "" + arg0 });
        super.setSelectionVisible(arg0);
    }

    public void setVisible(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setVisible",
                "" + arg0 });
        try {
            super.setVisible(arg0);
        } catch (NullPointerException e) {
        }
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.toString" });
        return super.toString();
    }

    public double getHeight() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getHeight" });
        return super.getHeight();
    }

    public double getWidth() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getWidth" });
        return super.getWidth();
    }

    public double getX() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getX" });
        return super.getX();
    }

    public double getY() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getY" });
        return super.getY();
    }

    public boolean isEmpty() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.isEmpty" });
        return super.isEmpty();
    }

    public int outcode(double arg0, double arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.outcode", "" + arg0,
                "" + arg1 });
        return super.outcode(arg0, arg1);
    }

    public void setRect(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setRect", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        super.setRect(arg0, arg1, arg2, arg3);
    }

    public void add(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.add", "" + arg0,
                "" + arg1 });
        super.add(arg0, arg1);
    }

    public void grow(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.grow", "" + arg0,
                "" + arg1 });
        super.grow(arg0, arg1);
    }

    public void move(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.move", "" + arg0,
                "" + arg1 });
        super.move(arg0, arg1);
    }

    public void resize(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.resize", "" + arg0,
                "" + arg1 });
        super.resize(arg0, arg1);
    }

    public void setLocation(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setLocation",
                "" + arg0, "" + arg1 });
        super.setLocation(arg0, arg1);
    }

    public void setSize(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setSize", "" + arg0,
                "" + arg1 });
        super.setSize(arg0, arg1);
    }

    public void translate(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.translate",
                "" + arg0, "" + arg1 });
        super.translate(arg0, arg1);
    }

    public boolean contains(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.contains",
                "" + arg0, "" + arg1 });
        return super.contains(arg0, arg1);
    }

    public boolean inside(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.inside", "" + arg0,
                "" + arg1 });
        return super.inside(arg0, arg1);
    }

    public void reshape(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.reshape", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        super.reshape(arg0, arg1, arg2, arg3);
    }

    public void setBounds(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setBounds",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        super.setBounds(arg0, arg1, arg2, arg3);
    }

    public boolean contains(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.contains",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        return super.contains(arg0, arg1, arg2, arg3);
    }

    public Dimension getSize() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getSize" });
        return super.getSize();
    }

    public void setSize(Dimension arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setSize", arg0 });
        super.setSize(arg0);
    }

    public Point getLocation() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getLocation" });
        return super.getLocation();
    }

    public void add(Point arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.add", arg0 });
        super.add(arg0);
    }

    public void setLocation(Point arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.setLocation", arg0 });
        super.setLocation(arg0);
    }

    public boolean contains(Point arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.contains", arg0 });
        return super.contains(arg0);
    }

    public Rectangle getBounds() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getBounds" });
        return super.getBounds();
    }

    public void add(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.add", arg0 });
        super.add(arg0);
    }

    public void setBounds(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setBounds", arg0 });
        super.setBounds(arg0);
    }

    public boolean contains(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.contains", arg0 });
        return super.contains(arg0);
    }

    public boolean intersects(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.intersects", arg0 });
        return super.intersects(arg0);
    }

    public Rectangle2D getBounds2D() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getBounds2D" });
        return super.getBounds2D();
    }

    public Rectangle intersection(Rectangle arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.intersection", arg0 });
        return super.intersection(arg0);
    }

    public Rectangle union(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.union", arg0 });
        return super.union(arg0);
    }

    public Rectangle2D createIntersection(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.createIntersection",
                arg0 });
        return super.createIntersection(arg0);
    }

    public Rectangle2D createUnion(Rectangle2D arg0) {
        InstrumentedUILog
                .add(new Object[] { "DefaultCaret.createUnion", arg0 });
        return super.createUnion(arg0);
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.hashCode" });
        return super.hashCode();
    }

    public void add(double arg0, double arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.add", "" + arg0,
                "" + arg1 });
        super.add(arg0, arg1);
    }

    public boolean contains(double arg0, double arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.contains",
                "" + arg0, "" + arg1 });
        return super.contains(arg0, arg1);
    }

    public void setFrame(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setFrame",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        super.setFrame(arg0, arg1, arg2, arg3);
    }

    public boolean contains(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.contains",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        return super.contains(arg0, arg1, arg2, arg3);
    }

    public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.intersects",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        return super.intersects(arg0, arg1, arg2, arg3);
    }

    public boolean intersectsLine(double arg0, double arg1, double arg2,
            double arg3) {
        return super.intersectsLine(arg0, arg1, arg2, arg3);
    }

    public boolean intersectsLine(Line2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.intersectsLine",
                arg0 });
        return super.intersectsLine(arg0);
    }

    public int outcode(Point2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.outcode", arg0 });
        return super.outcode(arg0);
    }

    public void add(Point2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.add", arg0 });
        super.add(arg0);
    }

    public void add(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.add", arg0 });
        super.add(arg0);
    }

    public void setRect(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setRect", arg0 });
        super.setRect(arg0);
    }

    public PathIterator getPathIterator(AffineTransform arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getPathIterator",
                arg0 });
        return super.getPathIterator(arg0);
    }

    public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getPathIterator",
                arg0, "" + arg1 });
        return super.getPathIterator(arg0, arg1);
    }

    public double getCenterX() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getCenterX" });
        return super.getCenterX();
    }

    public double getCenterY() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getCenterY" });
        return super.getCenterY();
    }

    public double getMaxX() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getMaxX" });
        return super.getMaxX();
    }

    public double getMaxY() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getMaxY" });
        return super.getMaxY();
    }

    public double getMinX() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getMinX" });
        return super.getMinX();
    }

    public double getMinY() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getMinY" });
        return super.getMinY();
    }

    public void setFrameFromCenter(double arg0, double arg1, double arg2,
            double arg3) {
        super.setFrameFromCenter(arg0, arg1, arg2, arg3);
    }

    public void setFrameFromDiagonal(double arg0, double arg1, double arg2,
            double arg3) {
        super.setFrameFromDiagonal(arg0, arg1, arg2, arg3);
    }

    public boolean contains(Point2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.contains", arg0 });
        return super.contains(arg0);
    }

    public Rectangle2D getFrame() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.getFrame" });
        return super.getFrame();
    }

    public void setFrame(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setFrame", arg0 });
        super.setFrame(arg0);
    }

    public boolean contains(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.contains", arg0 });
        return super.contains(arg0);
    }

    public boolean intersects(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.intersects", arg0 });
        return super.intersects(arg0);
    }

    public Object clone() {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.clone" });
        return super.clone();
    }

    public void setFrame(Point2D arg0, Dimension2D arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setFrame", arg0,
                arg1 });
        super.setFrame(arg0, arg1);
    }

    public void setFrameFromCenter(Point2D arg0, Point2D arg1) {
        InstrumentedUILog.add(new Object[] { "DefaultCaret.setFrameFromCenter",
                arg0, arg1 });
        super.setFrameFromCenter(arg0, arg1);
    }

    public void setFrameFromDiagonal(Point2D arg0, Point2D arg1) {
        InstrumentedUILog.add(new Object[] {
                "DefaultCaret.setFrameFromDiagonal", arg0, arg1 });
        super.setFrameFromDiagonal(arg0, arg1);
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void repaintExposed() {
        repaint();
    }

    public JTextComponent getComponentExposed() {
        return getComponent();
    }
}