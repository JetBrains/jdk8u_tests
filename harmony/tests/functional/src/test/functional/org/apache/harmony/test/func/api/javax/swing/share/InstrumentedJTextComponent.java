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

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.EventListener;
import java.util.Locale;
import java.util.Set;

import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.InputVerifier;
import javax.swing.JRootPane;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.NavigationFilter;

public class InstrumentedJTextComponent extends JTextComponent {
     static private TextUI textUI = new InstrumentedTextUI();
    
    public void addCaretListener(CaretListener arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.addCaretListener",
                arg0 });
        super.addCaretListener(arg0);
    }

    public void addInputMethodListener(InputMethodListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.addInputMethodListener", arg0 });
        super.addInputMethodListener(arg0);
    }

    public void copy() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.copy" });
        super.copy();
    }

    public void cut() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.cut" });
        super.cut();
    }

    protected void fireCaretUpdate(CaretEvent arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.fireCaretUpdate",
                arg0 });
        super.fireCaretUpdate(arg0);
    }

    public AccessibleContext getAccessibleContext() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getAccessibleContext" });
        return super.getAccessibleContext();
    }

    public Action[] getActions() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getActions" });
        return super.getActions();
    }

    public Caret getCaret() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getCaret" });
        return super.getCaret();
    }

    public Color getCaretColor() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getCaretColor" });
        return super.getCaretColor();
    }

    public CaretListener[] getCaretListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getCaretListeners" });
        return super.getCaretListeners();
    }

    public int getCaretPosition() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getCaretPosition" });
        return super.getCaretPosition();
    }

    public Color getDisabledTextColor() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getDisabledTextColor" });
        return super.getDisabledTextColor();
    }

    public Document getDocument() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getDocument" });
        return super.getDocument();
    }

    public boolean getDragEnabled() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getDragEnabled" });
        return super.getDragEnabled();
    }

    public char getFocusAccelerator() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getFocusAccelerator" });
        return super.getFocusAccelerator();
    }

    public Highlighter getHighlighter() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getHighlighter" });
        return super.getHighlighter();
    }

    public InputMethodRequests getInputMethodRequests() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getInputMethodRequests" });
        return super.getInputMethodRequests();
    }

    public Keymap getKeymap() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getKeymap" });
        return super.getKeymap();
    }

    public Insets getMargin() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getMargin" });
        return super.getMargin();
    }

    public NavigationFilter getNavigationFilter() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getNavigationFilter" });
        return super.getNavigationFilter();
    }

    public Dimension getPreferredScrollableViewportSize() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getPreferredScrollableViewportSize" });
        return super.getPreferredScrollableViewportSize();
    }

    public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.getScrollableBlockIncrement", arg0, "" + arg1,
                "" + arg2 });
        return super.getScrollableBlockIncrement(arg0, arg1, arg2);
    }

    public boolean getScrollableTracksViewportHeight() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getScrollableTracksViewportHeight" });
        return super.getScrollableTracksViewportHeight();
    }

    public boolean getScrollableTracksViewportWidth() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getScrollableTracksViewportWidth" });
        return super.getScrollableTracksViewportWidth();
    }

    public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.getScrollableUnitIncrement", arg0, "" + arg1,
                "" + arg2 });
        return super.getScrollableUnitIncrement(arg0, arg1, arg2);
    }

    public String getSelectedText() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getSelectedText" });
        return super.getSelectedText();
    }

    public Color getSelectedTextColor() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getSelectedTextColor" });
        return super.getSelectedTextColor();
    }

    public Color getSelectionColor() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getSelectionColor" });
        return super.getSelectionColor();
    }

    public int getSelectionEnd() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getSelectionEnd" });
        return super.getSelectionEnd();
    }

    public int getSelectionStart() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getSelectionStart" });
        return super.getSelectionStart();
    }

    public String getText() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getText" });
        return super.getText();
    }

    public String getText(int arg0, int arg1) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getText",
                "" + arg0, "" + arg1 });
        return super.getText(arg0, arg1);
    }

    public String getToolTipText(MouseEvent arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getToolTipText",
                arg0 });
        return super.getToolTipText(arg0);
    }

    public TextUI getUI() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getUI" });
        return textUI;
    }

    public boolean isEditable() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isEditable" });
        return super.isEditable();
    }

    public Rectangle modelToView(int arg0) throws BadLocationException {
        InstrumentedUILog.add(new Object[] { "JTextComponent.modelToView",
                "" + arg0 });
        return super.modelToView(arg0);
    }

    public void moveCaretPosition(int arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.moveCaretPosition", "" + arg0 });
        super.moveCaretPosition(arg0);
    }

    protected String paramString() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paramString" });
        return super.paramString();
    }

    public void paste() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paste" });
        super.paste();
    }

    protected void processInputMethodEvent(InputMethodEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processInputMethodEvent", arg0 });
        super.processInputMethodEvent(arg0);
    }

    public void read(Reader arg0, Object arg1) throws IOException {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.read", arg0, arg1 });
        super.read(arg0, arg1);
    }

    public void removeCaretListener(CaretListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeCaretListener", arg0 });
        super.removeCaretListener(arg0);
    }

    public void removeNotify() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.removeNotify" });
        super.removeNotify();
    }

    public void replaceSelection(String arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.replaceSelection",
                arg0 });
        super.replaceSelection(arg0);
    }

    public void select(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.select",
                "" + arg0, "" + arg1 });
        super.select(arg0, arg1);
    }

    public void selectAll() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.selectAll" });
        super.selectAll();
    }

    public void setCaret(Caret arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setCaret", arg0 });
        super.setCaret(arg0);
    }

    public void setCaretColor(Color arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setCaretColor",
                arg0 });
        super.setCaretColor(arg0);
    }

    public void setCaretPosition(int arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setCaretPosition",
                "" + arg0 });
        super.setCaretPosition(arg0);
    }

    public void setComponentOrientation(ComponentOrientation arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setComponentOrientation", arg0 });
        super.setComponentOrientation(arg0);
    }

    public void setDisabledTextColor(Color arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setDisabledTextColor", arg0 });
        super.setDisabledTextColor(arg0);
    }

    public void setDocument(Document arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setDocument", arg0 });
        super.setDocument(arg0);
    }

    public void setDragEnabled(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setDragEnabled",
                "" + arg0 });
        super.setDragEnabled(arg0);
    }

    public void setEditable(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setEditable",
                "" + arg0 });
        super.setEditable(arg0);
    }

    public void setFocusAccelerator(char arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setFocusAccelerator", "" + arg0 });
        super.setFocusAccelerator(arg0);
    }

    public void setHighlighter(Highlighter arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setHighlighter",
                arg0 });
        super.setHighlighter(arg0);
    }

    public void setKeymap(Keymap arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setKeymap", arg0 });
        super.setKeymap(arg0);
    }

    public void setMargin(Insets arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setMargin", arg0 });
        super.setMargin(arg0);
    }

    public void setNavigationFilter(NavigationFilter arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setNavigationFilter", arg0 });
        super.setNavigationFilter(arg0);
    }

    public void setSelectedTextColor(Color arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setSelectedTextColor", arg0 });
        super.setSelectedTextColor(arg0);
    }

    public void setSelectionColor(Color arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setSelectionColor", arg0 });
        super.setSelectionColor(arg0);
    }

    public void setSelectionEnd(int arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setSelectionEnd",
                "" + arg0 });
        super.setSelectionEnd(arg0);
    }

    public void setSelectionStart(int arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setSelectionStart", "" + arg0 });
        super.setSelectionStart(arg0);
    }

    public void setText(String arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setText", arg0 });
        super.setText(arg0);
    }

    public void setUI(TextUI arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setUI", arg0 });
        super.setUI(arg0);
    }

    public void updateUI() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.updateUI" });
        try {
            super.updateUI();
        } catch (Throwable e) {
        }
    }

    public int viewToModel(Point arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.viewToModel", arg0 });
        return super.viewToModel(arg0);
    }

    public void write(Writer arg0) throws IOException {
        InstrumentedUILog.add(new Object[] { "JTextComponent.write", arg0 });
        super.write(arg0);
    }

    public float getAlignmentX() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getAlignmentX" });
        return super.getAlignmentX();
    }

    public float getAlignmentY() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getAlignmentY" });
        return super.getAlignmentY();
    }

    public int getDebugGraphicsOptions() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getDebugGraphicsOptions" });
        return super.getDebugGraphicsOptions();
    }

    public int getHeight() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getHeight" });
        return super.getHeight();
    }

    public int getWidth() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getWidth" });
        return super.getWidth();
    }

    public int getX() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getX" });
        return super.getX();
    }

    public int getY() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getY" });
        return super.getY();
    }

    public void addNotify() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.addNotify" });
        super.addNotify();
    }

    public void disable() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.disable" });
        super.disable();
    }

    public void enable() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.enable" });
        super.enable();
    }

    public void grabFocus() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.grabFocus" });
        super.grabFocus();
    }

    public void requestFocus() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.requestFocus" });
        super.requestFocus();
    }

    public void resetKeyboardActions() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.resetKeyboardActions" });
        super.resetKeyboardActions();
    }

    public void revalidate() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.revalidate" });
        super.revalidate();
    }

    public boolean getAutoscrolls() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getAutoscrolls" });
        return super.getAutoscrolls();
    }

    public boolean getVerifyInputWhenFocusTarget() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getVerifyInputWhenFocusTarget" });
        return super.getVerifyInputWhenFocusTarget();
    }

    public boolean isDoubleBuffered() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isDoubleBuffered" });
        return super.isDoubleBuffered();
    }

    public boolean isManagingFocus() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isManagingFocus" });
        return super.isManagingFocus();
    }

    public boolean isMaximumSizeSet() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isMaximumSizeSet" });
        return super.isMaximumSizeSet();
    }

    public boolean isMinimumSizeSet() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isMinimumSizeSet" });
        return super.isMinimumSizeSet();
    }

    public boolean isOpaque() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isOpaque" });
        return super.isOpaque();
    }

    public boolean isOptimizedDrawingEnabled() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isOptimizedDrawingEnabled" });
        return super.isOptimizedDrawingEnabled();
    }

    public boolean isPaintingTile() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isPaintingTile" });
        return super.isPaintingTile();
    }

    public boolean isPreferredSizeSet() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isPreferredSizeSet" });
        return super.isPreferredSizeSet();
    }

    public boolean isRequestFocusEnabled() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isRequestFocusEnabled" });
        return super.isRequestFocusEnabled();
    }

    public boolean isValidateRoot() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isValidateRoot" });
        return super.isValidateRoot();
    }

    public boolean requestDefaultFocus() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.requestDefaultFocus" });
        return super.requestDefaultFocus();
    }

    public boolean requestFocusInWindow() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.requestFocusInWindow" });
        return super.requestFocusInWindow();
    }

    public void setAlignmentX(float arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setAlignmentX",
                "" + arg0 });
        super.setAlignmentX(arg0);
    }

    public void setAlignmentY(float arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setAlignmentY",
                "" + arg0 });
        super.setAlignmentY(arg0);
    }

    public void setDebugGraphicsOptions(int arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setDebugGraphicsOptions", "" + arg0 });
        super.setDebugGraphicsOptions(arg0);
    }

    public boolean contains(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.contains",
                "" + arg0, "" + arg1 });
        return super.contains(arg0, arg1);
    }

    public void paintImmediately(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paintImmediately",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        super.paintImmediately(arg0, arg1, arg2, arg3);
    }

    public void reshape(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.reshape",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        super.reshape(arg0, arg1, arg2, arg3);
    }

    public void repaint(long arg0, int arg1, int arg2, int arg3, int arg4) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.repaint",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3, "" + arg4 });
        super.repaint(arg0, arg1, arg2, arg3, arg4);
    }

    public void setAutoscrolls(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setAutoscrolls",
                "" + arg0 });
        super.setAutoscrolls(arg0);
    }

    public void setDoubleBuffered(boolean arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setDoubleBuffered", "" + arg0 });
        super.setDoubleBuffered(arg0);
    }

    public void setEnabled(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setEnabled",
                "" + arg0 });
        super.setEnabled(arg0);
    }

    public void setOpaque(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setOpaque",
                "" + arg0 });
        super.setOpaque(arg0);
    }

    public void setRequestFocusEnabled(boolean arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setRequestFocusEnabled", "" + arg0 });
        super.setRequestFocusEnabled(arg0);
    }

    public void setVerifyInputWhenFocusTarget(boolean arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setVerifyInputWhenFocusTarget", "" + arg0 });
        super.setVerifyInputWhenFocusTarget(arg0);
    }

    public void setVisible(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setVisible",
                "" + arg0 });
        super.setVisible(arg0);
    }

    public boolean requestFocus(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.requestFocus",
                "" + arg0 });
        return super.requestFocus(arg0);
    }

    protected boolean requestFocusInWindow(boolean arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.requestFocusInWindow", "" + arg0 });
        return super.requestFocusInWindow(arg0);
    }

    public void setBackground(Color arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setBackground",
                arg0 });
        super.setBackground(arg0);
    }

    public void setForeground(Color arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setForeground",
                arg0 });
        super.setForeground(arg0);
    }

    public Component getNextFocusableComponent() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getNextFocusableComponent" });
        return super.getNextFocusableComponent();
    }

    public void setNextFocusableComponent(Component arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setNextFocusableComponent", arg0 });
        super.setNextFocusableComponent(arg0);
    }

    public Container getTopLevelAncestor() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getTopLevelAncestor" });
        return super.getTopLevelAncestor();
    }

    public Dimension getMaximumSize() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getMaximumSize" });
        return super.getMaximumSize();
    }

    public Dimension getMinimumSize() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getMinimumSize" });
        return super.getMinimumSize();
    }

    public Dimension getPreferredSize() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getPreferredSize" });
        return super.getPreferredSize();
    }

    public void setMaximumSize(Dimension arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setMaximumSize",
                arg0 });
        super.setMaximumSize(arg0);
    }

    public void setMinimumSize(Dimension arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setMinimumSize",
                arg0 });
        super.setMinimumSize(arg0);
    }

    public void setPreferredSize(Dimension arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setPreferredSize",
                arg0 });
        super.setPreferredSize(arg0);
    }

    public void setFont(Font arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setFont", arg0 });
        super.setFont(arg0);
    }

    public Graphics getGraphics() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getGraphics" });
        return super.getGraphics();
    }

    public void paint(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paint", arg0 });
        super.paint(arg0);
    }

    protected void paintBorder(Graphics arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.paintBorder", arg0 });
        super.paintBorder(arg0);
    }

    protected void paintChildren(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paintChildren",
                arg0 });
        super.paintChildren(arg0);
    }

    protected void paintComponent(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paintComponent",
                arg0 });
        super.paintComponent(arg0);
    }

    public void print(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.print", arg0 });
        super.print(arg0);
    }

    public void printAll(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.printAll", arg0 });
        super.printAll(arg0);
    }

    protected void printBorder(Graphics arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.printBorder", arg0 });
        super.printBorder(arg0);
    }

    protected void printChildren(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.printChildren",
                arg0 });
        super.printChildren(arg0);
    }

    protected void printComponent(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.printComponent",
                arg0 });
        super.printComponent(arg0);
    }

    public void update(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.update", arg0 });
        super.update(arg0);
    }

    public Insets getInsets() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getInsets" });
        return super.getInsets();
    }

    public Rectangle getVisibleRect() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getVisibleRect" });
        return super.getVisibleRect();
    }

    public void computeVisibleRect(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.computeVisibleRect", arg0 });
        super.computeVisibleRect(arg0);
    }

    public void paintImmediately(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paintImmediately",
                arg0 });
        super.paintImmediately(arg0);
    }

    public void repaint(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.repaint", arg0 });
        super.repaint(arg0);
    }

    public void scrollRectToVisible(Rectangle arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.scrollRectToVisible", arg0 });
        super.scrollRectToVisible(arg0);
    }

    protected void processComponentKeyEvent(KeyEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processComponentKeyEvent", arg0 });
        super.processComponentKeyEvent(arg0);
    }

    protected void processKeyEvent(KeyEvent arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.processKeyEvent",
                arg0 });
        super.processKeyEvent(arg0);
    }

    protected void processMouseMotionEvent(MouseEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processMouseMotionEvent", arg0 });
        super.processMouseMotionEvent(arg0);
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getPropertyChangeListeners" });
        return super.getPropertyChangeListeners();
    }

    public synchronized void addPropertyChangeListener(
            PropertyChangeListener arg0) {
        super.addPropertyChangeListener(arg0);
    }

    public synchronized void removePropertyChangeListener(
            PropertyChangeListener arg0) {
        super.removePropertyChangeListener(arg0);
    }

    public synchronized VetoableChangeListener[] getVetoableChangeListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getVetoableChangeListeners" });
        return super.getVetoableChangeListeners();
    }

    public synchronized void addVetoableChangeListener(
            VetoableChangeListener arg0) {
        super.addVetoableChangeListener(arg0);
    }

    public synchronized void removeVetoableChangeListener(
            VetoableChangeListener arg0) {
        super.removeVetoableChangeListener(arg0);
    }

    public String getToolTipText() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getToolTipText" });
        return super.getToolTipText();
    }

    public String getUIClassID() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getUIClassID" });
        return super.getUIClassID();
    }

    public void setToolTipText(String arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setToolTipText",
                arg0 });
        super.setToolTipText(arg0);
    }

    public void firePropertyChange(String arg0, byte arg1, byte arg2) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.firePropertyChange", arg0,
                        "" + arg1, "" + arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public void firePropertyChange(String arg0, char arg1, char arg2) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.firePropertyChange", arg0,
                        "" + arg1, "" + arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public void firePropertyChange(String arg0, double arg1, double arg2) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.firePropertyChange", arg0,
                        "" + arg1, "" + arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public void firePropertyChange(String arg0, float arg1, float arg2) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.firePropertyChange", arg0,
                        "" + arg1, "" + arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public void firePropertyChange(String arg0, int arg1, int arg2) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.firePropertyChange", arg0,
                        "" + arg1, "" + arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public void firePropertyChange(String arg0, long arg1, long arg2) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.firePropertyChange", arg0,
                        "" + arg1, "" + arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public void firePropertyChange(String arg0, short arg1, short arg2) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.firePropertyChange", arg0,
                        "" + arg1, "" + arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public void firePropertyChange(String arg0, boolean arg1, boolean arg2) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.firePropertyChange", arg0,
                        "" + arg1, "" + arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    public InputVerifier getInputVerifier() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getInputVerifier" });
        return super.getInputVerifier();
    }

    public void setInputVerifier(InputVerifier arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setInputVerifier",
                arg0 });
        super.setInputVerifier(arg0);
    }

    public JRootPane getRootPane() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getRootPane" });
        return super.getRootPane();
    }

    public JToolTip createToolTip() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.createToolTip" });
        return super.createToolTip();
    }

    public KeyStroke[] getRegisteredKeyStrokes() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getRegisteredKeyStrokes" });
        return super.getRegisteredKeyStrokes();
    }

    public int getConditionForKeyStroke(KeyStroke arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.getConditionForKeyStroke", arg0 });
        return super.getConditionForKeyStroke(arg0);
    }

    public void unregisterKeyboardAction(KeyStroke arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.unregisterKeyboardAction", arg0 });
        super.unregisterKeyboardAction(arg0);
    }

    public TransferHandler getTransferHandler() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getTransferHandler" });
        return super.getTransferHandler();
    }

    public void setTransferHandler(TransferHandler arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setTransferHandler", arg0 });
        super.setTransferHandler(arg0);
    }

    public Border getBorder() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getBorder" });
        return super.getBorder();
    }

    public void setBorder(Border arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setBorder", arg0 });
        super.setBorder(arg0);
    }

    public AncestorListener[] getAncestorListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getAncestorListeners" });
        return super.getAncestorListeners();
    }

    public void addAncestorListener(AncestorListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.addAncestorListener", arg0 });
        super.addAncestorListener(arg0);
    }

    public void removeAncestorListener(AncestorListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeAncestorListener", arg0 });
        super.removeAncestorListener(arg0);
    }

    protected void setUI(ComponentUI arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setUI", arg0 });
        super.setUI(arg0);
    }

    public Dimension getSize(Dimension arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getSize", arg0 });
        return super.getSize(arg0);
    }

    protected Graphics getComponentGraphics(Graphics arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.getComponentGraphics", arg0 });
        return super.getComponentGraphics(arg0);
    }

    public Insets getInsets(Insets arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getInsets", arg0 });
        return super.getInsets(arg0);
    }

    public Point getLocation(Point arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getLocation", arg0 });
        return super.getLocation(arg0);
    }

    public Point getToolTipLocation(MouseEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.getToolTipLocation", arg0 });
        return super.getToolTipLocation(arg0);
    }

    public Rectangle getBounds(Rectangle arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getBounds", arg0 });
        return super.getBounds(arg0);
    }

    public ActionListener getActionForKeyStroke(KeyStroke arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.getActionForKeyStroke", arg0 });
        return super.getActionForKeyStroke(arg0);
    }

    protected boolean processKeyBinding(KeyStroke arg0, KeyEvent arg1,
            int arg2, boolean arg3) {
        return super.processKeyBinding(arg0, arg1, arg2, arg3);
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners(
            String arg0) {
        return super.getPropertyChangeListeners(arg0);
    }

    public synchronized void addPropertyChangeListener(String arg0,
            PropertyChangeListener arg1) {
        super.addPropertyChangeListener(arg0, arg1);
    }

    public synchronized void removePropertyChangeListener(String arg0,
            PropertyChangeListener arg1) {
        super.removePropertyChangeListener(arg0, arg1);
    }

    public EventListener[] getListeners(Class arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getListeners",
                arg0 });
        return super.getListeners(arg0);
    }

    public void registerKeyboardAction(ActionListener arg0, KeyStroke arg1,
            int arg2) {
        super.registerKeyboardAction(arg0, arg1, arg2);
    }

    protected void firePropertyChange(String arg0, Object arg1, Object arg2) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.firePropertyChange", arg0, arg1, arg2 });
        super.firePropertyChange(arg0, arg1, arg2);
    }

    protected void fireVetoableChange(String arg0, Object arg1, Object arg2)
            throws PropertyVetoException {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.fireVetoableChange", arg0, arg1, arg2 });
        super.fireVetoableChange(arg0, arg1, arg2);
    }

    public void registerKeyboardAction(ActionListener arg0, String arg1,
            KeyStroke arg2, int arg3) {
        super.registerKeyboardAction(arg0, arg1, arg2, arg3);
    }

    public int countComponents() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.countComponents" });
        return super.countComponents();
    }

    public int getComponentCount() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getComponentCount" });
        return super.getComponentCount();
    }

    public void doLayout() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.doLayout" });
        super.doLayout();
    }

    public void invalidate() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.invalidate" });
        super.invalidate();
    }

    public void layout() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.layout" });
        super.layout();
    }

    public void removeAll() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.removeAll" });
        super.removeAll();
    }

    public void transferFocusBackward() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.transferFocusBackward" });
        super.transferFocusBackward();
    }

    public void transferFocusDownCycle() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.transferFocusDownCycle" });
        super.transferFocusDownCycle();
    }

    public void validate() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.validate" });
        super.validate();
    }

    protected void validateTree() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.validateTree" });
        super.validateTree();
    }

    public boolean isFocusCycleRoot() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isFocusCycleRoot" });
        return super.isFocusCycleRoot();
    }

    public boolean isFocusTraversalPolicySet() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isFocusTraversalPolicySet" });
        return super.isFocusTraversalPolicySet();
    }

    public void remove(int arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.remove", "" + arg0 });
        super.remove(arg0);
    }

    public boolean areFocusTraversalKeysSet(int arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.areFocusTraversalKeysSet", "" + arg0 });
        return super.areFocusTraversalKeysSet(arg0);
    }

    public void setFocusCycleRoot(boolean arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setFocusCycleRoot", "" + arg0 });
        super.setFocusCycleRoot(arg0);
    }

    protected void processEvent(AWTEvent arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.processEvent",
                arg0 });
        super.processEvent(arg0);
    }

    public Component[] getComponents() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getComponents" });
        return super.getComponents();
    }

    public Component getComponent(int arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getComponent",
                "" + arg0 });
        return super.getComponent(arg0);
    }

    public Component findComponentAt(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.findComponentAt",
                "" + arg0, "" + arg1 });
        return super.findComponentAt(arg0, arg1);
    }

    public Component getComponentAt(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getComponentAt",
                "" + arg0, "" + arg1 });
        return super.getComponentAt(arg0, arg1);
    }

    public Component locate(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.locate",
                "" + arg0, "" + arg1 });
        return super.locate(arg0, arg1);
    }

    public void remove(Component arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.remove", arg0 });
        super.remove(arg0);
    }

    public boolean isAncestorOf(Component arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isAncestorOf",
                arg0 });
        return super.isAncestorOf(arg0);
    }

    public void applyComponentOrientation(ComponentOrientation arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.applyComponentOrientation", arg0 });
        super.applyComponentOrientation(arg0);
    }

    public boolean isFocusCycleRoot(Container arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isFocusCycleRoot",
                arg0 });
        return super.isFocusCycleRoot(arg0);
    }

    public Dimension minimumSize() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.minimumSize" });
        return super.minimumSize();
    }

    public Dimension preferredSize() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.preferredSize" });
        return super.preferredSize();
    }

    public void deliverEvent(Event arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.deliverEvent",
                arg0 });
        super.deliverEvent(arg0);
    }

    public FocusTraversalPolicy getFocusTraversalPolicy() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getFocusTraversalPolicy" });
        return super.getFocusTraversalPolicy();
    }

    public void setFocusTraversalPolicy(FocusTraversalPolicy arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setFocusTraversalPolicy", arg0 });
        super.setFocusTraversalPolicy(arg0);
    }

    public void paintComponents(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paintComponents",
                arg0 });
        super.paintComponents(arg0);
    }

    public void printComponents(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.printComponents",
                arg0 });
        super.printComponents(arg0);
    }

    public Insets insets() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.insets" });
        return super.insets();
    }

    public LayoutManager getLayout() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getLayout" });
        return super.getLayout();
    }

    public void setLayout(LayoutManager arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setLayout", arg0 });
        super.setLayout(arg0);
    }

    protected void processContainerEvent(ContainerEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processContainerEvent", arg0 });
        super.processContainerEvent(arg0);
    }

    public synchronized ContainerListener[] getContainerListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getContainerListeners" });
        return super.getContainerListeners();
    }

    public synchronized void addContainerListener(ContainerListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.addContainerListener", arg0 });
        super.addContainerListener(arg0);
    }

    public synchronized void removeContainerListener(ContainerListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeContainerListener", arg0 });
        super.removeContainerListener(arg0);
    }

    public void list(PrintStream arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.list", arg0,
                "" + arg1 });
        super.list(arg0, arg1);
    }

    public void list(PrintWriter arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.list", arg0,
                "" + arg1 });
        super.list(arg0, arg1);
    }

    public Set getFocusTraversalKeys(int arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.getFocusTraversalKeys", "" + arg0 });
        return super.getFocusTraversalKeys(arg0);
    }

    public void setFocusTraversalKeys(int arg0, Set arg1) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setFocusTraversalKeys", "" + arg0, arg1 });
        super.setFocusTraversalKeys(arg0, arg1);
    }

    public Component add(Component arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.add", arg0 });
        return super.add(arg0);
    }

    public Component add(Component arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.add", arg0,
                "" + arg1 });
        return super.add(arg0, arg1);
    }

    public Component findComponentAt(Point arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.findComponentAt",
                arg0 });
        return super.findComponentAt(arg0);
    }

    public Component getComponentAt(Point arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getComponentAt",
                arg0 });
        return super.getComponentAt(arg0);
    }

    public void add(Component arg0, Object arg1) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.add", arg0, arg1 });
        super.add(arg0, arg1);
    }

    public void add(Component arg0, Object arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.add", arg0, arg1,
                "" + arg2 });
        super.add(arg0, arg1, arg2);
    }

    protected void addImpl(Component arg0, Object arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.addImpl", arg0,
                arg1, "" + arg2 });
        super.addImpl(arg0, arg1, arg2);
    }

    public Component add(String arg0, Component arg1) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.add", arg0, arg1 });
        return super.add(arg0, arg1);
    }

    public void hide() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.hide" });
        super.hide();
    }

    public void list() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.list" });
        super.list();
    }

    public void nextFocus() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.nextFocus" });
        super.nextFocus();
    }

    public void repaint() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.repaint" });
        super.repaint();
    }

    public void show() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.show" });
        super.show();
    }

    public void transferFocus() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.transferFocus" });
        super.transferFocus();
    }

    public void transferFocusUpCycle() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.transferFocusUpCycle" });
        super.transferFocusUpCycle();
    }

    public boolean getFocusTraversalKeysEnabled() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getFocusTraversalKeysEnabled" });
        return super.getFocusTraversalKeysEnabled();
    }

    public boolean getIgnoreRepaint() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getIgnoreRepaint" });
        return super.getIgnoreRepaint();
    }

    public boolean hasFocus() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.hasFocus" });
        return super.hasFocus();
    }

    public boolean isBackgroundSet() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isBackgroundSet" });
        return super.isBackgroundSet();
    }

    public boolean isCursorSet() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isCursorSet" });
        return super.isCursorSet();
    }

    public boolean isDisplayable() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isDisplayable" });
        return super.isDisplayable();
    }

    public boolean isEnabled() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isEnabled" });
        return super.isEnabled();
    }

    public boolean isFocusOwner() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isFocusOwner" });
        return super.isFocusOwner();
    }

    public boolean isFocusTraversable() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isFocusTraversable" });
        return super.isFocusTraversable();
    }

    public boolean isFocusable() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isFocusable" });
        return super.isFocusable();
    }

    public boolean isFontSet() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isFontSet" });
        return super.isFontSet();
    }

    public boolean isForegroundSet() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.isForegroundSet" });
        return super.isForegroundSet();
    }

    public boolean isLightweight() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isLightweight" });
        return super.isLightweight();
    }

    public boolean isShowing() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isShowing" });
        return super.isShowing();
    }

    public boolean isValid() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isValid" });
        return super.isValid();
    }

    public boolean isVisible() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.isVisible" });
        return super.isVisible();
    }

    public void move(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.move", "" + arg0,
                "" + arg1 });
        super.move(arg0, arg1);
    }

    public void resize(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.resize",
                "" + arg0, "" + arg1 });
        super.resize(arg0, arg1);
    }

    public void setLocation(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setLocation",
                "" + arg0, "" + arg1 });
        super.setLocation(arg0, arg1);
    }

    public void setSize(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setSize",
                "" + arg0, "" + arg1 });
        super.setSize(arg0, arg1);
    }

    public boolean inside(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.inside",
                "" + arg0, "" + arg1 });
        return super.inside(arg0, arg1);
    }

    public void repaint(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.repaint",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        super.repaint(arg0, arg1, arg2, arg3);
    }

    public void setBounds(int arg0, int arg1, int arg2, int arg3) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setBounds",
                "" + arg0, "" + arg1, "" + arg2, "" + arg3 });
        super.setBounds(arg0, arg1, arg2, arg3);
    }

    public void repaint(long arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.repaint",
                "" + arg0 });
        super.repaint(arg0);
    }

    public void enable(boolean arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.enable", "" + arg0 });
        super.enable(arg0);
    }

    public void enableInputMethods(boolean arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.enableInputMethods", "" + arg0 });
        super.enableInputMethods(arg0);
    }

    public void setFocusTraversalKeysEnabled(boolean arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.setFocusTraversalKeysEnabled", "" + arg0 });
        super.setFocusTraversalKeysEnabled(arg0);
    }

    public void setFocusable(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setFocusable",
                "" + arg0 });
        super.setFocusable(arg0);
    }

    public void setIgnoreRepaint(boolean arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setIgnoreRepaint",
                "" + arg0 });
        super.setIgnoreRepaint(arg0);
    }

    public void show(boolean arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.show", "" + arg0 });
        super.show(arg0);
    }

    public Color getBackground() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getBackground" });
        return super.getBackground();
    }

    public Color getForeground() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getForeground" });
        return super.getForeground();
    }

    public ComponentOrientation getComponentOrientation() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getComponentOrientation" });
        return super.getComponentOrientation();
    }

    public Container getFocusCycleRootAncestor() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getFocusCycleRootAncestor" });
        return super.getFocusCycleRootAncestor();
    }

    public Container getParent() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getParent" });
        return super.getParent();
    }

    public Cursor getCursor() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getCursor" });
        return super.getCursor();
    }

    public void setCursor(Cursor arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setCursor", arg0 });
        super.setCursor(arg0);
    }

    public Dimension getSize() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getSize" });
        return super.getSize();
    }

    public Dimension size() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.size" });
        return super.size();
    }

    public void resize(Dimension arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.resize", arg0 });
        super.resize(arg0);
    }

    public void setSize(Dimension arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setSize", arg0 });
        super.setSize(arg0);
    }

    public boolean handleEvent(Event arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.handleEvent", arg0 });
        return super.handleEvent(arg0);
    }

    public boolean postEvent(Event arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.postEvent", arg0 });
        return super.postEvent(arg0);
    }

    public boolean keyDown(Event arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.keyDown", arg0,
                "" + arg1 });
        return super.keyDown(arg0, arg1);
    }

    public boolean keyUp(Event arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.keyUp", arg0,
                "" + arg1 });
        return super.keyUp(arg0, arg1);
    }

    public boolean mouseDown(Event arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.mouseDown", arg0,
                "" + arg1, "" + arg2 });
        return super.mouseDown(arg0, arg1, arg2);
    }

    public boolean mouseDrag(Event arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.mouseDrag", arg0,
                "" + arg1, "" + arg2 });
        return super.mouseDrag(arg0, arg1, arg2);
    }

    public boolean mouseEnter(Event arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.mouseEnter", arg0,
                "" + arg1, "" + arg2 });
        return super.mouseEnter(arg0, arg1, arg2);
    }

    public boolean mouseExit(Event arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.mouseExit", arg0,
                "" + arg1, "" + arg2 });
        return super.mouseExit(arg0, arg1, arg2);
    }

    public boolean mouseMove(Event arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.mouseMove", arg0,
                "" + arg1, "" + arg2 });
        return super.mouseMove(arg0, arg1, arg2);
    }

    public boolean mouseUp(Event arg0, int arg1, int arg2) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.mouseUp", arg0,
                "" + arg1, "" + arg2 });
        return super.mouseUp(arg0, arg1, arg2);
    }

    public Font getFont() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getFont" });
        return super.getFont();
    }

    public void paintAll(Graphics arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.paintAll", arg0 });
        super.paintAll(arg0);
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getGraphicsConfiguration" });
        return super.getGraphicsConfiguration();
    }

    public Image createImage(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.createImage",
                "" + arg0, "" + arg1 });
        return super.createImage(arg0, arg1);
    }

    public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
            int arg4, int arg5) {
        return super.imageUpdate(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    public synchronized void remove(MenuComponent arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.remove", arg0 });
        super.remove(arg0);
    }

    public Point getLocation() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getLocation" });
        return super.getLocation();
    }

    public Point getLocationOnScreen() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getLocationOnScreen" });
        return super.getLocationOnScreen();
    }

    public Point location() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.location" });
        return super.location();
    }

    public void setLocation(Point arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setLocation", arg0 });
        super.setLocation(arg0);
    }

    public boolean contains(Point arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.contains", arg0 });
        return super.contains(arg0);
    }

    public synchronized void add(PopupMenu arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.add", arg0 });
        super.add(arg0);
    }

    public Rectangle bounds() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.bounds" });
        return super.bounds();
    }

    public Rectangle getBounds() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getBounds" });
        return super.getBounds();
    }

    public void setBounds(Rectangle arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setBounds", arg0 });
        super.setBounds(arg0);
    }

    public Toolkit getToolkit() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getToolkit" });
        return super.getToolkit();
    }

    public synchronized DropTarget getDropTarget() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getDropTarget" });
        return super.getDropTarget();
    }

    public synchronized void setDropTarget(DropTarget arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setDropTarget",
                arg0 });
        super.setDropTarget(arg0);
    }

    protected void processComponentEvent(ComponentEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processComponentEvent", arg0 });
        super.processComponentEvent(arg0);
    }

    public synchronized ComponentListener[] getComponentListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getComponentListeners" });
        return super.getComponentListeners();
    }

    public synchronized void addComponentListener(ComponentListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.addComponentListener", arg0 });
        super.addComponentListener(arg0);
    }

    public synchronized void removeComponentListener(ComponentListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeComponentListener", arg0 });
        super.removeComponentListener(arg0);
    }

    protected void processFocusEvent(FocusEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processFocusEvent", arg0 });
        super.processFocusEvent(arg0);
    }

    public synchronized FocusListener[] getFocusListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getFocusListeners" });
        return super.getFocusListeners();
    }

    public synchronized void addFocusListener(FocusListener arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.addFocusListener",
                arg0 });
        super.addFocusListener(arg0);
    }

    public synchronized void removeFocusListener(FocusListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeFocusListener", arg0 });
        super.removeFocusListener(arg0);
    }

    public synchronized HierarchyBoundsListener[] getHierarchyBoundsListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getHierarchyBoundsListeners" });
        return super.getHierarchyBoundsListeners();
    }

    public void addHierarchyBoundsListener(HierarchyBoundsListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.addHierarchyBoundsListener", arg0 });
        super.addHierarchyBoundsListener(arg0);
    }

    public void removeHierarchyBoundsListener(HierarchyBoundsListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeHierarchyBoundsListener", arg0 });
        super.removeHierarchyBoundsListener(arg0);
    }

    protected void processHierarchyBoundsEvent(HierarchyEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processHierarchyBoundsEvent", arg0 });
        super.processHierarchyBoundsEvent(arg0);
    }

    protected void processHierarchyEvent(HierarchyEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processHierarchyEvent", arg0 });
        super.processHierarchyEvent(arg0);
    }

    public synchronized HierarchyListener[] getHierarchyListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getHierarchyListeners" });
        return super.getHierarchyListeners();
    }

    public void addHierarchyListener(HierarchyListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.addHierarchyListener", arg0 });
        super.addHierarchyListener(arg0);
    }

    public void removeHierarchyListener(HierarchyListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeHierarchyListener", arg0 });
        super.removeHierarchyListener(arg0);
    }

    public synchronized InputMethodListener[] getInputMethodListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getInputMethodListeners" });
        return super.getInputMethodListeners();
    }

    public synchronized void removeInputMethodListener(InputMethodListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeInputMethodListener", arg0 });
        super.removeInputMethodListener(arg0);
    }

    public synchronized KeyListener[] getKeyListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getKeyListeners" });
        return super.getKeyListeners();
    }

    public synchronized void addKeyListener(KeyListener arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.addKeyListener",
                arg0 });
        super.addKeyListener(arg0);
    }

    public synchronized void removeKeyListener(KeyListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeKeyListener", arg0 });
        super.removeKeyListener(arg0);
    }

    protected void processMouseEvent(MouseEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processMouseEvent", arg0 });
        super.processMouseEvent(arg0);
    }

    public synchronized MouseListener[] getMouseListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getMouseListeners" });
        return super.getMouseListeners();
    }

    public synchronized void addMouseListener(MouseListener arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.addMouseListener",
                arg0 });
        super.addMouseListener(arg0);
    }

    public synchronized void removeMouseListener(MouseListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeMouseListener", arg0 });
        super.removeMouseListener(arg0);
    }

    public synchronized MouseMotionListener[] getMouseMotionListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getMouseMotionListeners" });
        return super.getMouseMotionListeners();
    }

    public synchronized void addMouseMotionListener(MouseMotionListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.addMouseMotionListener", arg0 });
        super.addMouseMotionListener(arg0);
    }

    public synchronized void removeMouseMotionListener(MouseMotionListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeMouseMotionListener", arg0 });
        super.removeMouseMotionListener(arg0);
    }

    protected void processMouseWheelEvent(MouseWheelEvent arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.processMouseWheelEvent", arg0 });
        super.processMouseWheelEvent(arg0);
    }

    public synchronized MouseWheelListener[] getMouseWheelListeners() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getMouseWheelListeners" });
        return super.getMouseWheelListeners();
    }

    public synchronized void addMouseWheelListener(MouseWheelListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.addMouseWheelListener", arg0 });
        super.addMouseWheelListener(arg0);
    }

    public synchronized void removeMouseWheelListener(MouseWheelListener arg0) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.removeMouseWheelListener", arg0 });
        super.removeMouseWheelListener(arg0);
    }

    public InputContext getInputContext() {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.getInputContext" });
        return super.getInputContext();
    }

    public ColorModel getColorModel() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getColorModel" });
        return super.getColorModel();
    }

    public VolatileImage createVolatileImage(int arg0, int arg1) {
        InstrumentedUILog.add(new Object[] {
                "JTextComponent.createVolatileImage", "" + arg0, "" + arg1 });
        return super.createVolatileImage(arg0, arg1);
    }

    public ComponentPeer getPeer() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getPeer" });
        return super.getPeer();
    }

    public void list(PrintStream arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.list", arg0 });
        super.list(arg0);
    }

    public void list(PrintWriter arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.list", arg0 });
        super.list(arg0);
    }

    public String getName() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getName" });
        return super.getName();
    }

    public String toString() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.toString" });
        return super.toString();
    }

    public void setName(String arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.setName", arg0 });
        super.setName(arg0);
    }

    public Locale getLocale() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getLocale" });
        return super.getLocale();
    }

    public void setLocale(Locale arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.setLocale", arg0 });
        super.setLocale(arg0);
    }

    public FontMetrics getFontMetrics(Font arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.getFontMetrics",
                arg0 });
        return super.getFontMetrics(arg0);
    }

    public Image createImage(ImageProducer arg0) {
        InstrumentedUILog
                .add(new Object[] { "JTextComponent.createImage", arg0 });
        return super.createImage(arg0);
    }

    public int checkImage(Image arg0, int arg1, int arg2, ImageObserver arg3) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.checkImage", arg0,
                "" + arg1, "" + arg2, arg3 });
        return super.checkImage(arg0, arg1, arg2, arg3);
    }

    public boolean prepareImage(Image arg0, int arg1, int arg2,
            ImageObserver arg3) {
        return super.prepareImage(arg0, arg1, arg2, arg3);
    }

    public int checkImage(Image arg0, ImageObserver arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.checkImage", arg0,
                arg1 });
        return super.checkImage(arg0, arg1);
    }

    public boolean prepareImage(Image arg0, ImageObserver arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.prepareImage",
                arg0, arg1 });
        return super.prepareImage(arg0, arg1);
    }

    public VolatileImage createVolatileImage(int arg0, int arg1,
            ImageCapabilities arg2) throws AWTException {
        return super.createVolatileImage(arg0, arg1, arg2);
    }

    public boolean action(Event arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.action", arg0,
                arg1 });
        return super.action(arg0, arg1);
    }

    public boolean gotFocus(Event arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.gotFocus", arg0,
                arg1 });
        return super.gotFocus(arg0, arg1);
    }

    public boolean lostFocus(Event arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.lostFocus", arg0,
                arg1 });
        return super.lostFocus(arg0, arg1);
    }

    protected AWTEvent coalesceEvents(AWTEvent arg0, AWTEvent arg1) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.coalesceEvents",
                arg0, arg1 });
        return super.coalesceEvents(arg0, arg1);
    }

    public int hashCode() {
        InstrumentedUILog.add(new Object[] { "JTextComponent.hashCode" });
        return super.hashCode();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] { "JTextComponent.clone" });
        return super.clone();
    }

    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] { "JTextComponent.equals", arg0 });
        return super.equals(arg0);
    }
}