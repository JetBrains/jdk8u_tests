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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

public class InstrumentedGraphics extends Graphics {

    public void dispose() {
        InstrumentedUILog.add(new Object[] { "Graphics.dispose" });
    }

    public void setPaintMode() {
        InstrumentedUILog.add(new Object[] { "Graphics.setPaintMode" });
    }

    public void translate(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "Graphics.translate", "" + arg0,
                "" + arg1 });
    }

    public void clearRect(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.clearRect", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
    }

    public void clipRect(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.clipRect", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
    }

    public void drawLine(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawLine", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
    }

    public void drawOval(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawOval", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
    }

    public void fillOval(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.fillOval", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
    }

    public void fillRect(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.fillRect", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
    }

    public void setClip(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.setClip", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
    }

    public void copyArea(int arg0, int arg1, int arg2, int arg3, int arg4,
            int arg5) {
        InstrumentedUILog.add(new Object[] { "Graphics.copyArea", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4, "" + arg5 });
    }

    public void drawArc(int arg0, int arg1, int arg2, int arg3, int arg4,
            int arg5) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawArc", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4, "" + arg5 });
    }

    public void drawRoundRect(int arg0, int arg1, int arg2, int arg3, int arg4,
            int arg5) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawRoundRect",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3, "" + arg4,
                "" + arg5 });
    }

    public void fillArc(int arg0, int arg1, int arg2, int arg3, int arg4,
            int arg5) {
        InstrumentedUILog.add(new Object[] { "Graphics.fillArc", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4, "" + arg5 });
    }

    public void fillRoundRect(int arg0, int arg1, int arg2, int arg3, int arg4,
            int arg5) {
        InstrumentedUILog.add(new Object[] { "Graphics.fillRoundRect",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3, "" + arg4,
                "" + arg5 });
    }

    public void drawPolygon(int[] arg0, int[] arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawPolygon", "" + arg0,
                "" + arg1, "" + arg2 });
    }

    public void drawPolyline(int[] arg0, int[] arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawPolyline",
                "" + arg0, "" + arg1, "" + arg2 });
    }

    public void fillPolygon(int[] arg0, int[] arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "Graphics.fillPolygon", "" + arg0,
                "" + arg1, "" + arg2 });
    }

    public Color getColor() {
        InstrumentedUILog.add(new Object[] { "Graphics.getColor" });
        return null;
    }

    public void setColor(Color arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.setColor", arg0 });
    }

    public void setXORMode(Color arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.setXORMode", arg0 });
    }

    public Font getFont() {
        InstrumentedUILog.add(new Object[] { "Graphics.getFont" });
        return null;
    }

    public void setFont(Font arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.setFont", arg0 });
    }

    public Graphics create() {
        InstrumentedUILog.add(new Object[] { "Graphics.create" });
        return null;
    }

    public Rectangle getClipBounds() {
        InstrumentedUILog.add(new Object[] { "Graphics.getClipBounds" });
        return null;
    }

    public Shape getClip() {
        InstrumentedUILog.add(new Object[] { "Graphics.getClip" });
        return null;
    }

    public void setClip(Shape arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.setClip", arg0 });
    }

    public void drawString(String arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawString", arg0,
                "" + arg1, "" + arg2 });
    }

    public void drawString(AttributedCharacterIterator arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawString", arg0,
                "" + arg1, "" + arg2 });
    }

    public FontMetrics getFontMetrics(Font arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.getFontMetrics", arg0 });
        return null;
    }

    public boolean drawImage(Image arg0, int arg1, int arg2, int arg3,
            int arg4, int arg5, int arg6, int arg7, int arg8, ImageObserver arg9) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawImage", arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4, "" + arg5,
                "" + arg6, "" + arg7, "" + arg8, arg9 });
        return false;
    }

    public boolean drawImage(Image arg0, int arg1, int arg2, int arg3,
            int arg4, ImageObserver arg5) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawImage", arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4, arg5 });
        return false;
    }

    public boolean drawImage(Image arg0, int arg1, int arg2, ImageObserver arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawImage", arg0,
                "" + arg1, "" + arg2, arg3 });
        return false;
    }

    public boolean drawImage(Image arg0, int arg1, int arg2, int arg3,
            int arg4, int arg5, int arg6, int arg7, int arg8, Color arg9,
            ImageObserver arg10) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawImage", arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4, "" + arg5,
                "" + arg6, "" + arg7, "" + arg8, arg9, arg10 });
        return false;
    }

    public boolean drawImage(Image arg0, int arg1, int arg2, int arg3,
            int arg4, Color arg5, ImageObserver arg6) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawImage", arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4, arg5, arg6 });
        return false;
    }

    public boolean drawImage(Image arg0, int arg1, int arg2, Color arg3,
            ImageObserver arg4) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawImage", arg0,
                "" + arg1, "" + arg2, arg3, arg4 });
        return false;
    }

    public Graphics create(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.create", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        return super.create(arg0, arg1, arg2, arg3);
    }

    public void draw3DRect(int arg0, int arg1, int arg2, int arg3, boolean arg4) {
        InstrumentedUILog.add(new Object[] { "Graphics.draw3DRect", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4 });
        super.draw3DRect(arg0, arg1, arg2, arg3, arg4);
    }

    public void drawBytes(byte[] arg0, int arg1, int arg2, int arg3, int arg4) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawBytes", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4 });
        super.drawBytes(arg0, arg1, arg2, arg3, arg4);
    }

    public void drawChars(char[] arg0, int arg1, int arg2, int arg3, int arg4) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawChars", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4 });
        super.drawChars(arg0, arg1, arg2, arg3, arg4);
    }

    public void drawPolygon(Polygon arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawPolygon", arg0 });
        super.drawPolygon(arg0);
    }

    public void drawRect(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.drawRect", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        super.drawRect(arg0, arg1, arg2, arg3);
    }

    public void fill3DRect(int arg0, int arg1, int arg2, int arg3, boolean arg4) {
        InstrumentedUILog.add(new Object[] { "Graphics.fill3DRect", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3, "" + arg4 });
        super.fill3DRect(arg0, arg1, arg2, arg3, arg4);
    }

    public void fillPolygon(Polygon arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.fillPolygon", arg0 });
        super.fillPolygon(arg0);
    }

    public void finalize() {
        super.finalize();
    }

    public Rectangle getClipBounds(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.getClipBounds", arg0 });
        return super.getClipBounds(arg0);
    }

    public Rectangle getClipRect() {
        InstrumentedUILog.add(new Object[] { "Graphics.getClipRect" });
        return super.getClipRect();
    }

    public FontMetrics getFontMetrics() {
        InstrumentedUILog.add(new Object[] { "Graphics.getFontMetrics" });
        return super.getFontMetrics();
    }

    public boolean hitClip(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "Graphics.hitClip", "" + arg0,
                "" + arg1, "" + arg2, "" + arg3 });
        return super.hitClip(arg0, arg1, arg2, arg3);
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "Graphics.toString" });
        return super.toString();
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "Graphics.hashCode" });
        return super.hashCode();
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "Graphics.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "Graphics.equals", arg0 });
        return super.equals(arg0);
    }
}