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

package org.apache.harmony.test.func.api.java.awt.Component;


import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import java.awt.event.TextEvent;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.harmony.test.func.api.java.awt.share.SimpleComponent;
import org.apache.harmony.share.Test;


public class ListenersTest extends Test {

    public int test() {
        try {

            Component cmp = new SimpleComponent();
            ListenersProxy[] proxies = { new ComponentProxy(cmp),
                    new MouseProxy(cmp), new FocusProxy(cmp),
                    new HierarchyProxy(cmp), new InputMethodProxy(cmp),
                    new KeyProxy(cmp), new MouseWheelProxy(cmp),
                    new MouseMotionProxy(cmp), new HierarchyBoundsProxy(cmp) };
            for (int i = 0; i < proxies.length; i++) {
                test_listeners(proxies[i]);
            }
            test_events();
        } catch (TestException e) {
            return fail(e.toString());
        }
        return pass();
    }

    public abstract class ListenersProxy {
        public abstract Object[] getListeners();

        public abstract Class getListenerClass();

        public abstract void addListener(Object listener);

        public abstract void removeListener(Object listener);

        public ListenersProxy(Component c) {
            this.c = c;
        }

        public ListenersProxy() {
        }

        public Component getComponent() {
            return c;
        }

        protected Component c = null;

    }

    private class ComponentProxy extends ListenersProxy {

        public ComponentProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return ComponentListener.class;
        }

        public Object[] getListeners() {

            return c.getComponentListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof ComponentListener)
                c.addComponentListener((ComponentListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof ComponentListener)
                c.removeComponentListener((ComponentListener) listener);
        }
    }

    private class MouseProxy extends ListenersProxy {

        public MouseProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return MouseListener.class;
        }

        public Object[] getListeners() {

            return c.getMouseListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof MouseListener)
                c.addMouseListener((MouseListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof MouseListener)
                c.removeMouseListener((MouseListener) listener);
        }
    }

    private class FocusProxy extends ListenersProxy {

        public FocusProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return FocusListener.class;
        }

        public Object[] getListeners() {

            return c.getFocusListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof FocusListener)
                c.addFocusListener((FocusListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof FocusListener)
                c.removeFocusListener((FocusListener) listener);
        }
    }

    private class HierarchyProxy extends ListenersProxy {

        public HierarchyProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return HierarchyListener.class;
        }

        public Object[] getListeners() {

            return c.getHierarchyListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof HierarchyListener)
                c.addHierarchyListener((HierarchyListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof HierarchyListener)
                c.removeHierarchyListener((HierarchyListener) listener);
        }
    }

    private class InputMethodProxy extends ListenersProxy {

        public InputMethodProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return InputMethodListener.class;
        }

        public Object[] getListeners() {

            return c.getInputMethodListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof InputMethodListener)
                c.addInputMethodListener((InputMethodListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof InputMethodListener)
                c.removeInputMethodListener((InputMethodListener) listener);
        }
    }

    private class KeyProxy extends ListenersProxy {

        public KeyProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return KeyListener.class;
        }

        public Object[] getListeners() {

            return c.getKeyListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof KeyListener)
                c.addKeyListener((KeyListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof KeyListener)
                c.removeKeyListener((KeyListener) listener);
        }
    }

    private class MouseWheelProxy extends ListenersProxy {

        public MouseWheelProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return MouseWheelListener.class;
        }

        public Object[] getListeners() {

            return c.getMouseWheelListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof MouseWheelListener)
                c.addMouseWheelListener((MouseWheelListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof MouseWheelListener)
                c.removeMouseWheelListener((MouseWheelListener) listener);
        }
    }

    private class MouseMotionProxy extends ListenersProxy {

        public MouseMotionProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return MouseMotionListener.class;
        }

        public Object[] getListeners() {

            return c.getMouseMotionListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof MouseMotionListener)
                c.addMouseMotionListener((MouseMotionListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof MouseMotionListener)
                c.removeMouseMotionListener((MouseMotionListener) listener);
        }
    }

    private class HierarchyBoundsProxy extends ListenersProxy {

        public HierarchyBoundsProxy(Component c) {
            this.c = c;
        }

        public Class getListenerClass() {
            return HierarchyBoundsListener.class;
        }

        public Object[] getListeners() {

            return c.getHierarchyBoundsListeners();
        }

        public void addListener(Object listener) {
            if (listener instanceof HierarchyBoundsListener)
                c
                        .addHierarchyBoundsListener((HierarchyBoundsListener) listener);
        }

        public void removeListener(Object listener) {
            if (listener instanceof HierarchyBoundsListener)
                c
                        .removeHierarchyBoundsListener((HierarchyBoundsListener) listener);
        }
    }

    private class TestException extends Exception {
        public TestException(String s) {
            super(s);
        }

        public TestException() {
            super();
        }
    }

    private void check(boolean value) throws TestException {
        if (!value) {
            throw new TestException("Asserion fault!");
        }
    }

    public void test_listeners(ListenersProxy cmp) throws TestException {
        EventTarget target = new EventTarget();
        //Component cmp = new MyComponent();

        int count;

        count = 0;
        cmp.addListener(target);
        Object[] listeners = (Object[]) cmp.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            count += listeners[i].equals(target) ? 1 : 0;
        }

        check(count == 1);

        count = 0;
        cmp.removeListener(target);
        listeners = (Object[]) cmp.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            count += listeners[i].equals(target) ? 1 : 0;
        }

        check(count == 0);

        massAdd_test(cmp, true);
        massAdd_test(cmp, false);

    }

    private void massAdd_test(ListenersProxy cmp, boolean simpleGet)
            throws TestException {

        Object[] listeners = new Object[] {};
        int targetCount = 10;
        int good = 0, bad = 0, lost = 0;
        EventTarget[] targets = new EventTarget[targetCount];

        for (int i = 0; i < targetCount; i++) {
            cmp.addListener(targets[i] = new EventTarget());
        }

        for (int i = 0; i < targetCount / 2; i++) {
            cmp.removeListener(targets[i * 2]);
        }

        if (simpleGet) {
            listeners = (Object[]) cmp.getListeners();
        } else {
            Class listenerClass = cmp.getListenerClass();
            Component component = cmp.getComponent();
            listeners = component.getListeners(listenerClass);
        }

        for (int i = 0; i < listeners.length; i++) {

            boolean found = false;
            for (int j = 0; j < targets.length; j++) {
                if (targets[j].equals(listeners[i])) {
                    found = true;
                    if (j % 2 == 0) {
                        bad++;
                    } else {
                        good++;
                    }
                }
            }
            if (!found) {
                lost++;
            }
            cmp.removeListener(listeners[i]);
        }

        check(good == targets.length / 2 && bad == 0 && lost == 0
                && listeners.length == targets.length / 2);
    }

    public void test_events() throws TestException {

        EventTarget target = new EventTarget();
        Component cmp = new SimpleComponent();

        cmp.addComponentListener(target);
        cmp.addFocusListener(target);
        cmp.addHierarchyBoundsListener(target);
        cmp.addHierarchyListener(target);
        cmp.addInputMethodListener(target);
        cmp.addKeyListener(target);
        cmp.addMouseListener(target);
        cmp.addMouseMotionListener(target);
        cmp.addMouseWheelListener(target);

        Container container = new Frame();
        long when = new Date().getTime();

        EventKey[] evts = {
                new EventKey(new MouseEvent(cmp, MouseEvent.MOUSE_CLICKED,
                        when, 0, 100, 100, 1, false, MouseEvent.BUTTON1),
                        "mouseClicked"),
                new EventKey(new MouseEvent(cmp, MouseEvent.MOUSE_ENTERED,
                        when, 0, 100, 100, 1, false, MouseEvent.BUTTON1),
                        "mouseEntered"),
                new EventKey(new MouseEvent(cmp, MouseEvent.MOUSE_EXITED, when,
                        0, 100, 100, 1, false, MouseEvent.BUTTON1),
                        "mouseExited"),
                new EventKey(new MouseEvent(cmp, MouseEvent.MOUSE_PRESSED,
                        when, 0, 100, 100, 1, false, MouseEvent.BUTTON1),
                        "mousePressed"),
                new EventKey(new MouseEvent(cmp, MouseEvent.MOUSE_RELEASED,
                        when, 0, 100, 100, 1, false, MouseEvent.BUTTON1),
                        "mouseReleased"),
                new EventKey(new MouseEvent(cmp, MouseEvent.MOUSE_DRAGGED,
                        when, 0, 100, 100, 1, false, MouseEvent.BUTTON1),
                        "mouseDragged"),
                new EventKey(new MouseEvent(cmp, MouseEvent.MOUSE_MOVED, when,
                        0, 100, 100, 1, false, MouseEvent.BUTTON1),
                        "mouseMoved"),
                new EventKey(new MouseWheelEvent(cmp, MouseEvent.MOUSE_WHEEL,
                        when, 0, 100, 100, 1, false,
                        MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, 120),
                        "mouseWheelMoved"),
                new EventKey(new TextEvent(cmp, TextEvent.TEXT_VALUE_CHANGED),
                        "textValueChanged"),
                new EventKey(new ComponentEvent(cmp,
                        ComponentEvent.COMPONENT_HIDDEN), "componentHidden"),
                new EventKey(new ComponentEvent(cmp,
                        ComponentEvent.COMPONENT_MOVED), "componentMoved"),
                new EventKey(new ComponentEvent(cmp,
                        ComponentEvent.COMPONENT_RESIZED), "componentResized"),
                new EventKey(new ComponentEvent(cmp,
                        ComponentEvent.COMPONENT_SHOWN), "componentShown"),
                new EventKey(new FocusEvent(cmp, FocusEvent.FOCUS_GAINED),
                        "focusGained"),
                new EventKey(new FocusEvent(cmp, FocusEvent.FOCUS_LOST),
                        "focusLost"),
                new EventKey(new HierarchyEvent(cmp,
                        HierarchyEvent.ANCESTOR_MOVED, cmp, container),
                        "ancestorMoved"),
                new EventKey(new HierarchyEvent(cmp,
                        HierarchyEvent.ANCESTOR_RESIZED, cmp, container),
                        "ancestorResized"),
                new EventKey(new HierarchyEvent(cmp,
                        HierarchyEvent.HIERARCHY_CHANGED, cmp, container),
                        "hierarchyChanged"),
                new EventKey(new HierarchyEvent(cmp,
                        HierarchyEvent.DISPLAYABILITY_CHANGED, cmp, container),
                        "hierarchyChanged"),
                new EventKey(new HierarchyEvent(cmp,
                        HierarchyEvent.PARENT_CHANGED, cmp, container),
                        "hierarchyChanged"),
                new EventKey(new KeyEvent(cmp, KeyEvent.KEY_PRESSED, when, 0,
                        KeyEvent.VK_X, 'X'), "keyPressed"),
                new EventKey(new KeyEvent(cmp, KeyEvent.KEY_RELEASED, when, 0,
                        KeyEvent.VK_X, 'X'), "keyReleased"),
                //new EventKey(new KeyEvent(cmp, KeyEvent.KEY_TYPED,
                //        when, 0, KeyEvent.VK_X, 'X'), "keyTyped"),
                new EventKey(new InputMethodEvent(cmp,
                        InputMethodEvent.CARET_POSITION_CHANGED, null, 0, null,
                        null), "caretPositionChanged"),
                new EventKey(new InputMethodEvent(cmp,
                        InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, null, 0,
                        null, null), "inputMethodTextChanged"),

        };

        cmp.setSize(200, 200);
        cmp.show();

        for (int i = 0; i < evts.length; i++) {
            cmp.dispatchEvent(evts[i].event);
        }

        for (int i = 0; i < evts.length; i++) {
            if (target.getEventMisses(evts[i]) != 0)
                throw new TestException("Event " + evts[i].name + " has missed");
            if (target.getEventHits(evts[i]) == 0)
                log.info("Event " + evts[i].name + " hasn't hit");
        }

    }

    public static void main(String[] args) {
        System.exit(new ListenersTest().test());
    }
}

class EventKey {
    public String name;

    public AWTEvent event;

    public EventKey(AWTEvent event, String name) {
        this.name = name;
        this.event = event;
    }

    public int hashCode() {
        return name.hashCode() * 31 + event.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof EventKey)) {
            return false;
        }
        EventKey k = (EventKey) o;
        return k.name.equals(name) && k.event.equals(event);
    }
}

class EventTarget implements MouseListener, ComponentListener, FocusListener,
        HierarchyBoundsListener, HierarchyListener, MouseMotionListener,
        MouseWheelListener, KeyListener, InputMethodListener {

    private Hashtable events = new Hashtable();

    private static int instancesCounter = 0;
    private int instanceID;

    public EventTarget() {
        instanceID = ++instancesCounter;
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof EventTarget))
            return false;
        EventTarget t = (EventTarget) o;
        boolean eq = this.instanceID == t.instanceID;
        return eq;
    }

    private class EventEntry {
        public AWTEvent event;

        public String name;

        public int hitCount = 0;

        public int hashCode() {
            return event.hashCode();
        }

        public void addTo(Hashtable ht) {
            if (ht.containsKey(event)) {
                ((EventEntry) ht.get(event)).hitCount++;
            } else {
                ht.put(new EventKey(event, name), this);
                this.hitCount = 1;
            }
        }
    }

    private void addEvent(AWTEvent event, String methodName) {
        EventEntry e = new EventEntry();
        e.event = event;
        e.name = methodName;
        e.addTo(events);
    }

    public int getEventHits(EventKey eventKey) {
        EventEntry entry = (EventEntry) events.get(eventKey);
        return entry != null ? entry.hitCount : 0;
    }

    public int getEventMisses(EventKey eventKey) {
        Enumeration it = events.keys();
        int count = 0;
        while (it.hasMoreElements()) {
            EventKey e = (EventKey) it.nextElement();
            if (e.equals(eventKey)) {
                count += ((EventEntry) events.get(e)).hitCount;
            }
        }
        return count - getEventHits(eventKey);
    }

    public void clear() {
        events.clear();
    }

    //        ComponentListener methods
    public void componentHidden(ComponentEvent event) {
        addEvent(event, "componentHidden");
    }

    public void componentMoved(ComponentEvent event) {
        addEvent(event, "componentMoved");
    }

    public void componentResized(ComponentEvent event) {
        addEvent(event, "componentResized");
    }

    public void componentShown(ComponentEvent event) {
        addEvent(event, "componentShown");
    }

    //FocusListener methods
    public void focusGained(FocusEvent event) {
        addEvent(event, "focusGained");
    }

    public void focusLost(FocusEvent event) {
        addEvent(event, "focusLost");
    }

    //HierarchyBoundsListener methods
    public void ancestorMoved(HierarchyEvent event) {
        addEvent(event, "ancestorMoved");
    }

    public void ancestorResized(HierarchyEvent event) {
        addEvent(event, "ancestorResized");
    }

    //HierarchyListener methods
    public void hierarchyChanged(HierarchyEvent event) {
        addEvent(event, "hierarchyChanged");
    }

    //InputMethodListener methods
    public void caretPositionChanged(InputMethodEvent event) {
        addEvent(event, "caretPositionChanged");
    }

    public void inputMethodTextChanged(InputMethodEvent event) {
        addEvent(event, "inputMethodTextChanged");
    }

    //KeyListener
    public void keyPressed(KeyEvent event) {
        addEvent(event, "keyPressed");
    }

    public void keyReleased(KeyEvent event) {
        addEvent(event, "keyReleased");
    }

    public void keyTyped(KeyEvent event) {
        addEvent(event, "keyTyped");
    }

    //MouseListener
    public void mouseClicked(MouseEvent event) {
        addEvent(event, "mouseClicked");
    }

    public void mouseEntered(MouseEvent event) {
        addEvent(event, "mouseEntered");
    }

    public void mouseExited(MouseEvent event) {
        addEvent(event, "mouseExited");
    }

    public void mousePressed(MouseEvent event) {
        addEvent(event, "mousePressed");
    }

    public void mouseReleased(MouseEvent event) {
        addEvent(event, "mouseReleased");
    }

    //MouseMotionListener
    public void mouseDragged(MouseEvent event) {
        addEvent(event, "mouseDragged");
    }

    public void mouseMoved(MouseEvent event) {
        addEvent(event, "mouseMoved");
    }

    //MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent event) {
        addEvent(event, "mouseWheelMoved");
    }
}