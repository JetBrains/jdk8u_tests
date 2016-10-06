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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class InstrumentedRectangle extends Rectangle {

    public void add(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.add", "" + arg0,
                "" + arg1 });
        super.add(arg0, arg1);
    }

    public void add(Point arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.add", arg0 });
        super.add(arg0);
    }

    public void add(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.add", arg0 });
        super.add(arg0);
    }

    public boolean contains(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Rectangle.contains", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        return super.contains(arg0, arg1, arg2, arg3);
    }

    public boolean contains(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.contains", "" + arg0,
                "" + arg1 });
        return super.contains(arg0, arg1);
    }

    public boolean contains(Point arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.contains", arg0 });
        return super.contains(arg0);
    }

    public boolean contains(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.contains", arg0 });
        return super.contains(arg0);
    }

    public Rectangle2D createIntersection(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.createIntersection",
                arg0 });
        return super.createIntersection(arg0);
    }

    public Rectangle2D createUnion(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.createUnion", arg0 });
        return super.createUnion(arg0);
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.equals", arg0 });
        return super.equals(arg0);
    }

    public Rectangle getBounds() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getBounds" });
        return super.getBounds();
    }

    public Rectangle2D getBounds2D() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getBounds2D" });
        return super.getBounds2D();
    }

    public double getHeight() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getHeight" });
        return super.getHeight();
    }

    public Point getLocation() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getLocation" });
        return super.getLocation();
    }

    public Dimension getSize() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getSize" });
        return super.getSize();
    }

    public double getWidth() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getWidth" });
        return super.getWidth();
    }

    public double getX() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getX" });
        return super.getX();
    }

    public double getY() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getY" });
        return super.getY();
    }

    public void grow(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.grow", "" + arg0,
                "" + arg1 });
        super.grow(arg0, arg1);
    }

    public boolean inside(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.inside", "" + arg0,
                "" + arg1 });
        return super.inside(arg0, arg1);
    }

    public Rectangle intersection(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.intersection", arg0 });
        return super.intersection(arg0);
    }

    public boolean intersects(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.intersects", arg0 });
        return super.intersects(arg0);
    }

    public boolean isEmpty() {
        InstrumentedUILog.add(new Object[] { "Rectangle.isEmpty" });
        return super.isEmpty();
    }

    public void move(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.move", "" + arg0,
                "" + arg1 });
        super.move(arg0, arg1);
    }

    public int outcode(double arg0, double arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.outcode", "" + arg0,
                "" + arg1 });
        return super.outcode(arg0, arg1);
    }

    public void reshape(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Rectangle.reshape", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        super.reshape(arg0, arg1, arg2, arg3);
    }

    public void resize(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.resize", "" + arg0,
                "" + arg1 });
        super.resize(arg0, arg1);
    }

    public void setBounds(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setBounds", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        super.setBounds(arg0, arg1, arg2, arg3);
    }

    public void setBounds(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setBounds", arg0 });
        super.setBounds(arg0);
    }

    public void setLocation(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setLocation",
                "" + arg0, "" + arg1 });
        super.setLocation(arg0, arg1);
    }

    public void setLocation(Point arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setLocation", arg0 });
        super.setLocation(arg0);
    }

    public void setRect(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setRect", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        super.setRect(arg0, arg1, arg2, arg3);
    }

    public void setSize(Dimension arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setSize", arg0 });
        super.setSize(arg0);
    }

    public void setSize(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setSize", "" + arg0,
                "" + arg1 });
        super.setSize(arg0, arg1);
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "Rectangle.toString" });
        return super.toString();
    }

    public void translate(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.translate", "" + arg0,
                "" + arg1 });
        super.translate(arg0, arg1);
    }

    public Rectangle union(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.union", arg0 });
        return super.union(arg0);
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "Rectangle.hashCode" });
        return super.hashCode();
    }

    public void add(double arg0, double arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.add", "" + arg0,
                "" + arg1 });
        super.add(arg0, arg1);
    }

    public boolean contains(double arg0, double arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.contains", "" + arg0,
                "" + arg1 });
        return super.contains(arg0, arg1);
    }

    public void setFrame(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setFrame", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        super.setFrame(arg0, arg1, arg2, arg3);
    }

    public boolean contains(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] { "Rectangle.contains", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        return super.contains(arg0, arg1, arg2, arg3);
    }

    public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] { "Rectangle.intersects", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        return super.intersects(arg0, arg1, arg2, arg3);
    }

    public boolean intersectsLine(double arg0, double arg1, double arg2,
            double arg3) {
        return super.intersectsLine(arg0, arg1, arg2, arg3);
    }

    public boolean intersectsLine(Line2D arg0) {
        InstrumentedUILog
                .add(new Object[] { "Rectangle.intersectsLine", arg0 });
        return super.intersectsLine(arg0);
    }

    public int outcode(Point2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.outcode", arg0 });
        return super.outcode(arg0);
    }

    public void add(Point2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.add", arg0 });
        super.add(arg0);
    }

    public void add(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.add", arg0 });
        super.add(arg0);
    }

    public void setRect(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setRect", arg0 });
        super.setRect(arg0);
    }

    public PathIterator getPathIterator(AffineTransform arg0) {
        InstrumentedUILog
                .add(new Object[] { "Rectangle.getPathIterator", arg0 });
        return super.getPathIterator(arg0);
    }

    public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.getPathIterator", arg0,
                "" + arg1 });
        return super.getPathIterator(arg0, arg1);
    }

    public double getCenterX() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getCenterX" });
        return super.getCenterX();
    }

    public double getCenterY() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getCenterY" });
        return super.getCenterY();
    }

    public double getMaxX() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getMaxX" });
        return super.getMaxX();
    }

    public double getMaxY() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getMaxY" });
        return super.getMaxY();
    }

    public double getMinX() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getMinX" });
        return super.getMinX();
    }

    public double getMinY() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getMinY" });
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
        InstrumentedUILog.add(new Object[] { "Rectangle.contains", arg0 });
        return super.contains(arg0);
    }

    public Rectangle2D getFrame() {
        InstrumentedUILog.add(new Object[] { "Rectangle.getFrame" });
        return super.getFrame();
    }

    public void setFrame(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setFrame", arg0 });
        super.setFrame(arg0);
    }

    public boolean contains(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.contains", arg0 });
        return super.contains(arg0);
    }

    public boolean intersects(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] { "Rectangle.intersects", arg0 });
        return super.intersects(arg0);
    }

    public Object clone() {
        InstrumentedUILog.add(new Object[] { "Rectangle.clone" });
        return super.clone();
    }

    public void setFrame(Point2D arg0, Dimension2D arg1) {
        InstrumentedUILog
                .add(new Object[] { "Rectangle.setFrame", arg0, arg1 });
        super.setFrame(arg0, arg1);
    }

    public void setFrameFromCenter(Point2D arg0, Point2D arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setFrameFromCenter",
                arg0, arg1 });
        super.setFrameFromCenter(arg0, arg1);
    }

    public void setFrameFromDiagonal(Point2D arg0, Point2D arg1) {
        InstrumentedUILog.add(new Object[] { "Rectangle.setFrameFromDiagonal",
                arg0, arg1 });
        super.setFrameFromDiagonal(arg0, arg1);
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }
}