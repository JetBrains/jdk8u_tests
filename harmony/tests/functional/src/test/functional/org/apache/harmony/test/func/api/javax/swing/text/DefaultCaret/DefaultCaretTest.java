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

package org.apache.harmony.test.func.api.javax.swing.text.DefaultCaret;

import java.awt.event.MouseEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.Position;

import org.apache.harmony.test.func.api.javax.swing.share.*;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class DefaultCaretTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new DefaultCaretTest().test(args));
    }

    public Result testConstructor() {
        InstrumentedUILog.clear();

        new InstrumentedDefaultCaret();

        if (!InstrumentedUILog.equals(new Object[][] {})) {
            InstrumentedUILog.printLog();
            return failed("expected constructor no to call any more methods");
        }
        return passed();
    }

    public Result testAdjustVisibility() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        InstrumentedRectangle ir = new InstrumentedRectangle();

        InstrumentedUILog.clear();

        idc.adjustVisibility(ir);

        if (!InstrumentedUILog.equals(new Object[][] { /* 1 */{
                "DefaultCaret.adjustVisibility", ir }, })) {
            return failed("expected adjustVisibility not to call any additional methods");
        }

        InstrumentedUILog.clear();
        idc.adjustVisibility(null);

        if (!InstrumentedUILog.equals(new Object[][] { /* 1 */{
                "DefaultCaret.adjustVisibility", null }, })) {
            return failed("expected adjustVisibility(null) not to call any additional methods");
        }

        return passed();
    }

    public Result testDamage() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        InstrumentedRectangle ir = new InstrumentedRectangle();

        InstrumentedUILog.clear();
        idc.damage(ir);

        if (!InstrumentedUILog.equals(new Object[][] { /* 1 */{
                "DefaultCaret.damage", ir }, })) {
            return failed("expected damage not to call any additional methods");
        }

        InstrumentedUILog.clear();
        idc.damage(null);

        if (!InstrumentedUILog.equals(new Object[][] { /* 1 */{
                "DefaultCaret.damage", null }, })) {
            return failed("expected damage(null) not to call any additional methods");
        }

        return passed();
    }

    public Result testFocusGained() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        InstrumentedFocusEvent ife = new InstrumentedFocusEvent();
        InstrumentedJTextComponent ijtc = new InstrumentedJTextComponent();
        idc.install(ijtc);

        InstrumentedUILog.clear();

        idc.focusGained(ife);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.focusGained", ife },
        /* 2 */{ "JTextComponent.isEnabled" },
        /* 3 */{ "JTextComponent.isEditable" },
        /* 4 */{ "DefaultCaret.setVisible", "true" },
        /* 5 */{ "JTextComponent.getUI" },
        /* 5 */{ "DefaultCaret.setSelectionVisible", "true" },
        /* 5 */{ "JTextComponent.getHighlighter" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected focusGained to call another sequence of events");
        }

        return passed();
    }

    public Result testFocusLost() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        InstrumentedFocusEvent ife = new InstrumentedFocusEvent();

        InstrumentedUILog.clear();
        idc.focusLost(ife);
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.focusLost", ife },
        /* 2 */{ "DefaultCaret.setVisible", "false" },
        /* 3 */{ "FocusEvent.isTemporary" },
        /* 4 */{ "DefaultCaret.setSelectionVisible", "false" }, })) {
            return failed("expected another sequence for focusLost");
        }

        return passed();
    }

    public Result testComponent() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();

        InstrumentedUILog.clear();

        if (idc.getComponentExposed() != null) {
            return failed("expected getComponent() to return null by default");
        }

        InstrumentedJTextComponent ijtc = new InstrumentedJTextComponent();
        InstrumentedUILog.clear();

        idc.install(ijtc);

        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultCaret.install", ijtc },
                /* 2 */{ "JTextComponent.getDocument" },
                /* 3 */{ "JTextComponent.addFocusListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 4 */{ "JTextComponent.addMouseListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 5 */{ "JTextComponent.addMouseMotionListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 6 */{ "JTextComponent.hasFocus" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected another event sequence for install()");

        }

        if (idc.getComponentExposed() != ijtc) {
            return failed("expected getComponent() to return what was set with install()");
        }

        try {
            idc.install(null);
            return failed("expected install(null) to throw NPE");
        } catch (Throwable e) {

        }

        ijtc = new InstrumentedJTextComponent() {
            public boolean hasFocus() {
                super.hasFocus();
                return true;
            }
        };
        InstrumentedUILog.clear();

        idc.install(ijtc);

        if (!InstrumentedUILog.contains(new Object[][] {
                /* 1 */{ "DefaultCaret.install", ijtc },
                /* 2 */{ "JTextComponent.getDocument" },
                /* 3 */{ "JTextComponent.addFocusListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 4 */{ "JTextComponent.addMouseListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 5 */{ "JTextComponent.addMouseMotionListener",
                        InstrumentedUILog.ANY_NON_NULL_VALUE },
                /* 6 */{ "JTextComponent.hasFocus" },
                /* 7 */{ "DefaultCaret.focusGained", null },
                /* 8 */{ "JTextComponent.isEnabled" },
                /* 9 */{ "JTextComponent.isEditable" },
                /* 10 */{ "DefaultCaret.setVisible", "true" },
                /* 11 */{ "JTextComponent.getUI" },
                /* 12 */{ "DefaultCaret.setSelectionVisible", "true" },
                /* 13 */{ "JTextComponent.getHighlighter" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected another event sequence for install() if component is visible");

        }

        return passed();

    }

    public Result testDot() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        idc.install(new InstrumentedJTextComponent());

        InstrumentedUILog.clear();

        if (idc.getDot() != 0) {
            return failed("expected getDot() to return 0 by default, got "
                    + idc.getDot());
        }

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.getDot" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected getDot() not to call any other methods");
        }

        InstrumentedUILog.clear();
        idc.setDot(234);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.setDot", "234" },
        /* 2 */{ "JTextComponent.getNavigationFilter" },
        /* 3 */{ "JTextComponent.getDocument" },
        /* 4 */{ "JTextComponent.repaint", "0", "0", "0", "0" },
        /* 5 */{ "JTextComponent.repaint", "0", "0", "0", "0", "0" },
        /* 6 */{ "JTextComponent.getDocument" },
        /* 7 */{ "DefaultCaret.fireStateChanged" },
        /* 8 */{ "DefaultCaret.setMagicCaretPosition", null },
        /* 9 */{ "JTextComponent.getUI" },
        /* 10 */{ "JTextComponent.getDocument" },
        /* 11 */{ "JTextComponent.getHighlighter" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected setDot() to call another sequence of events");
        }

        if (idc.getDot() != 234) {
            return failed("expected getDot() to return what was set by setDot()");
        }

        InstrumentedUILog.clear();
        try {
            idc.moveDot(123);
        } catch (NullPointerException e) {
        }

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.moveDot", "123" },
        /* 2 */{ "JTextComponent.isEnabled" },
        /* 3 */{ "JTextComponent.getNavigationFilter" },
        /* 4 */{ "JTextComponent.repaint", "0", "0", "0", "0" },
        /* 5 */{ "JTextComponent.repaint", "0", "0", "0", "0", "0" },
        /* 6 */{ "JTextComponent.getDocument" },
        /* 7 */{ "DefaultCaret.fireStateChanged" },
        /* 8 */{ "JTextComponent.getToolkit" },
        /* 9 */{ "DefaultCaret.setMagicCaretPosition", null },
        /* 10 */{ "JTextComponent.getUI" },
        /* 11 */{ "JTextComponent.getDocument" }, })

        && !InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.moveDot", "123" },
        /* 2 */{ "JTextComponent.isEnabled" },
        /* 3 */{ "JTextComponent.getNavigationFilter" },
        /* 4 */{ "JTextComponent.repaint", "0", "0", "0", "0" },
        /* 5 */{ "JTextComponent.repaint", "0", "0", "0", "0", "0" },
        /* 6 */{ "JTextComponent.getDocument" },
        /* 7 */{ "DefaultCaret.fireStateChanged" },
        /* 8 */{ "JTextComponent.getToolkit" },
        /* 8 */{ "JTextComponent.getSelectedText" }, })

        ) {
            InstrumentedUILog.printLog();
            return failed("expected moveDot() to call another sequence of events");
        }

        if (idc.getDot() != 123) {
            return failed("expected getDot() to return what was set by moveDot()");
        }

        return passed();
    }

    public Result testGetMark() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        idc.install(new InstrumentedJTextComponent());

        InstrumentedUILog.clear();

        idc.getMark();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.getMark" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected getMark() not to call any other methods");
        }

        return passed();
    }

    public Result testMouseEvents() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        idc.install(new InstrumentedJTextComponent());
        InstrumentedMouseEvent ime = new InstrumentedMouseEvent(
                MouseEvent.MOUSE_DRAGGED);

        InstrumentedUILog.clear();

        idc.mouseClicked(ime);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.mouseClicked", ime },
        /* 2 */{ "MouseEvent.isConsumed" },
        /* 3 */{ "MouseEvent.getClickCount" },
        /* 4 */{ "MouseEvent.getModifiers" },
        /* 5 */{ "MouseEvent.getClickCount" },
        /* 6 */{ "MouseEvent.getClickCount" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected mouseClicked() to produce another sequence of events");
        }

        InstrumentedUILog.clear();

        idc.mouseDragged(ime);
        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.mouseDragged", ime },
        /* 2 */{ "MouseEvent.isConsumed" },
        /* 3 */{ "MouseEvent.getModifiers" },
        /* 4 */{ "DefaultCaret.moveCaret", ime },
        /* 5 */{ "MouseEvent.getX" },
        /* 6 */{ "MouseEvent.getY" },
        /* 7 */{ "JTextComponent.getUI" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected another sequence of events for mouseDragged()");
        }

        InstrumentedUILog.clear();

        idc.mouseEntered(ime);
        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.mouseEntered", ime }, })) {
            InstrumentedUILog.printLog();
            return failed("expected no events caused by mouseEntered()");
        }

        InstrumentedUILog.clear();

        idc.mouseExited(ime);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.mouseExited", ime }, })) {
            InstrumentedUILog.printLog();
            return failed("expected no events caused by mouseExited()");
        }

        InstrumentedUILog.clear();

        idc.mouseMoved(ime);
        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.mouseMoved", ime }, })) {
            InstrumentedUILog.printLog();
            return failed("expected no events caused by mouseMoved()");
        }

        InstrumentedUILog.clear();
        idc.mousePressed(ime);

        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.mousePressed", ime },
        /* 2 */{ "MouseEvent.getModifiers" },
        /* 3 */{ "MouseEvent.isConsumed" },
        /* 4 */{ "MouseEvent.getModifiers" },
        /* 5 */{ "DefaultCaret.positionCaret", ime },
        /* 6 */{ "MouseEvent.getX" },
        /* 7 */{ "MouseEvent.getY" },
        /* 8 */{ "JTextComponent.getUI" },
        /* 9 */{ "JTextComponent.isEnabled" },
        /* 10 */{ "JTextComponent.isRequestFocusEnabled" },
        /* 11 */{ "JTextComponent.requestFocus" },
        /* 12 */{ "JTextComponent.getVerifyInputWhenFocusTarget" },
        /* 13 */{ "JTextComponent.isFocusable" },
        /* 14 */{ "JTextComponent.isFocusTraversable" },
        /* 15 */{ "JTextComponent.isVisible" }, })
        
        
        ) {
            InstrumentedUILog.printLog();
            return failed("another sequence of events after mousePressed()");
        }

        InstrumentedUILog.clear();

        idc.mouseReleased(ime);
        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.mouseReleased", ime }, })) {
            InstrumentedUILog.printLog();
            return failed("expected no events caused by mouseReleased()");
        }

        InstrumentedUILog.clear();

        idc.positionCaret(ime);
        if (!InstrumentedUILog.contains(new Object[][] {
        /* 1 */{ "DefaultCaret.positionCaret", ime },
        /* 2 */{ "MouseEvent.getX" },
        /* 3 */{ "MouseEvent.getY" },
        /* 4 */{ "JTextComponent.getUI" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected another sequence of events after positionCaret()");
        }

        return passed();
    }

    public Result testPaint() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        idc.setVisible(true);
        idc.install(new InstrumentedJTextComponent());
        InstrumentedGraphics ig = new InstrumentedGraphics();

        InstrumentedUILog.clear();

        idc.paint(ig);

        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultCaret.paint", ig },
                /* 2 */{ "DefaultCaret.isVisible" },
                /* 3 */{ "JTextComponent.getUI" },
                /* 4 */{ "TextUI.modelToView", idc.getComponentExposed(), "0",
                        Position.Bias.Forward },

        })

                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultCaret.paint", ig },
                /* 2 */{ "DefaultCaret.isVisible" }, })

                && !InstrumentedUILog.equals(new Object[][] {
                        /* 1 */{ "DefaultCaret.paint", ig },
                        /* 4 */{ "TextUI.modelToView",
                                idc.getComponentExposed(), "0",
                                Position.Bias.Forward }, })
                                
                                && !InstrumentedUILog.equals(new Object[][] {
                                        { "DefaultCaret.paint", ig },
                                        { "TextUI.modelToView",
                                                idc.getComponentExposed(), "0",
                                                Position.Bias.Forward }, 
                                
                                { "JTextComponent.repaint", "0, 0, 0, 1, 1" },
                                { "JTextComponent.getVisibleRect" },
                                { "JTextComponent.getWidth" },
                                { "JTextComponent.getHeight" },
                                { "JTextComponent.getParent" }, 
                                { "JTextComponent.repaint", "0, 0, 0, 1, 1" },
                                { "JTextComponent.getVisibleRect" },
                                { "JTextComponent.getWidth" },
                                { "JTextComponent.getHeight" },
                                { "JTextComponent.getParent" }, 
                                }
                                
                                )
                                

        ) {
            InstrumentedUILog.printLog();
            return failed("expected paint(Graphics) to call another sequence of events");
        }

        return passed();
    }

    public Result testRepaint() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        idc.setVisible(true);
        idc.install(new InstrumentedJTextComponent());

        InstrumentedUILog.clear();

        idc.repaintExposed();

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "JTextComponent.repaint", "0", "0", "0", "0" },
        /* 2 */{ "JTextComponent.repaint", "0", "0", "0", "0", "0" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "JTextComponent.repaint", "0, 0, 0, 1, 1" },
                        { "JTextComponent.getVisibleRect" },
                        { "JTextComponent.getWidth" },
                        { "JTextComponent.getHeight" },
                        { "JTextComponent.getParent" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected repaint() to call component.repaint()");
        }

        return passed();
    }

    public Result testBlinkRate() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        idc.setVisible(true);
        idc.install(new InstrumentedJTextComponent());

        InstrumentedUILog.clear();

        if (idc.getBlinkRate() != 0) {
            return failed("expected getBlinkRate() to return 0 by default");
        }

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.getBlinkRate" },

        })) {
            InstrumentedUILog.printLog();
            return failed("expected getBlinkRate() not to call any other methods");
        }

        InstrumentedUILog.clear();

        idc.setBlinkRate(1234);
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.setBlinkRate", "1234" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                        { "DefaultCaret.setBlinkRate", "1234" },
                        { "JTextComponent.repaint", "0, 0, 0, 1, 1" },
                        { "JTextComponent.getVisibleRect" },
                        { "JTextComponent.getWidth" },
                        { "JTextComponent.getHeight" },
                        { "JTextComponent.getParent" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected setBlinkRate() to call another sequence of events");
        }

        return passed();
    }

    public Result testVisible() {
        InstrumentedDefaultCaret idc = new InstrumentedDefaultCaret();
        idc.install(new InstrumentedJTextComponent());

        InstrumentedUILog.clear();
        idc.setVisible(false);

        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.setVisible", "false" },
        /* 2 */{ "JTextComponent.getUI" }, })
                && !InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultCaret.setVisible", "false" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected first set setVisible(false) to call another events sequence");
        }

        InstrumentedUILog.clear();
        idc.setVisible(true);
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultCaret.setVisible", "true" },
                /* 2 */{ "JTextComponent.getUI" },
                /* 3 */{ "TextUI.modelToView", idc.getComponentExposed(), "0",
                        Position.Bias.Forward },
                /* 4 */{ "DefaultCaret.damage", null }, })) {
            InstrumentedUILog.printLog();
            return failed("expected first set setVisible(true) to call another events sequence");
        }

        InstrumentedUILog.clear();
        idc.setVisible(true);
        if (!InstrumentedUILog.equals(new Object[][] {
        /* 1 */{ "DefaultCaret.setVisible", "true" },
        /* 2 */{ "JTextComponent.getUI" },

        })) {
            InstrumentedUILog.printLog();
            return failed("expected second set setVisible(true) to call another events sequence");
        }

        InstrumentedUILog.clear();
        idc.setVisible(false);
        if (!InstrumentedUILog.equals(new Object[][] {
                /* 1 */{ "DefaultCaret.setVisible", "false" },
                /* 2 */{ "JTextComponent.getUI" },
                /* 3 */{ "TextUI.modelToView", idc.getComponentExposed(), "0",
                        Position.Bias.Forward },
                /* 4 */{ "DefaultCaret.damage", null },

        })) {
            InstrumentedUILog.printLog();
            return failed("expected second set setVisible(false) to call another events sequence");
        }

        return passed();
    }

}