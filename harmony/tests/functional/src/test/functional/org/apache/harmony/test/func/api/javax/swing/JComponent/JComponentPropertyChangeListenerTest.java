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
package org.apache.harmony.test.func.api.javax.swing.JComponent;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.apache.harmony.test.func.api.javax.swing.share.Util;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class JComponentPropertyChangeListenerTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new JComponentPropertyChangeListenerTest().test(args));
    }

    public Result testAddRemove() {
        JComponent jc = new JComponent() {
        };

        if (jc.getPropertyChangeListeners().length != 0) {
            return failed("expected 0 propertyChangeListeners here");
        }

        PropertyChangeListenerImpl al1 = new PropertyChangeListenerImpl(0);
        PropertyChangeListenerImpl al2 = new PropertyChangeListenerImpl(1);

        jc.addPropertyChangeListener(al1);
        if (jc.getPropertyChangeListeners().length != 1) {
            return failed("expected 1 propertyChangeListener");
        }

        jc.addPropertyChangeListener(al2);
        if (jc.getPropertyChangeListeners().length != 2) {
            return failed("expected 2 propertyChangeListeners here");
        }

        jc.addPropertyChangeListener(al1);
        if (jc.getPropertyChangeListeners().length != 3) {
            return failed("expected 3 propertyChangeListeners here");
        }

        jc.addPropertyChangeListener(null);

        if (jc.getPropertyChangeListeners().length != 3) {
            return failed("expected 3 propertyChangeListeners here");
        }

        jc.removePropertyChangeListener(null);
        if (jc.getPropertyChangeListeners().length != 3) {
            return failed("expected 3 propertyChangeListeners here");
        }

        jc.removePropertyChangeListener(al1);
        if (jc.getPropertyChangeListeners().length != 2) {
            return failed("expected 2 propertyChangeListeners here, got: "
                    + jc.getPropertyChangeListeners().length);
        }

        jc.removePropertyChangeListener(new PropertyChangeListenerImpl(3));
        if (jc.getPropertyChangeListeners().length != 2) {
            return failed("expected 2 propertyChangeListeners here");
        }

        jc.removePropertyChangeListener(new PropertyChangeListenerImpl(0));
        if (jc.getPropertyChangeListeners().length != 1) {
            return failed("expected 1 propertyChangeListener here, got "
                    + jc.getPropertyChangeListeners().length);
        }

        jc.removePropertyChangeListener(al2);
        if (jc.getPropertyChangeListeners().length != 0) {
            return failed("expected 0 propertyChangeListeners here");
        }

        return passed();
    }

    public Result testGetPropertyChangeListeners() {
        JComponent jc = new JComponent() {
        };

        if (jc.getPropertyChangeListeners() == jc.getPropertyChangeListeners()) {
            return failed("expected two calls on getPropertyChangeListeners() return different values");
        }

        return passed();
    }

    public Result testSetBackground() {
        PropertyChangeListenerImpl al1 = new PropertyChangeListenerImpl(0);
        PropertyChangeListenerImpl al2 = new PropertyChangeListenerImpl(0);

        JComponent jc = new JComponent() {
        };

        jc.addPropertyChangeListener(al1);
        jc.addPropertyChangeListener(al2);
        jc.addPropertyChangeListener(al1);

        jc.setBackground(Color.CYAN);

        Util.waitQueueEventsProcess();

        List l1 = al1.getLog();
        List l2 = al2.getLog();

        if (l1.size() != 2 || !"background".equals(l1.get(0))
                || !"background".equals(l1.get(1)) || l2.size() != 1
                || !"background".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on jc.setBackground()");
        }

        jc.setBackground(Color.CYAN);

        Util.waitQueueEventsProcess();

        l1 = al1.getLog();
        l2 = al2.getLog();

        if (l1.size() != 2 || !"background".equals(l1.get(0))
                || !"background".equals(l1.get(1)) || l2.size() != 1
                || !"background".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on second jc.setBackground()");
        }

        return passed();
    }

    public Result testSpecificPropertyListener() {
        PropertyChangeListenerImpl al1 = new PropertyChangeListenerImpl(0);
        PropertyChangeListenerImpl al2 = new PropertyChangeListenerImpl(0);

        JComponent jc = new JComponent() {
        };

        jc.addPropertyChangeListener("background", al1);
        jc.addPropertyChangeListener("background", al2);
        jc.addPropertyChangeListener("border", al1);

        jc.setBackground(Color.CYAN);

        Util.waitQueueEventsProcess();

        List l1 = al1.getLog();
        List l2 = al2.getLog();

        if (l1.size() != 1 || !"background".equals(l1.get(0)) || l2.size() != 1
                || !"background".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on jc.setBackground()");
        }

        jc.setBackground(Color.CYAN);

        Util.waitQueueEventsProcess();

        l1 = al1.getLog();
        l2 = al2.getLog();

        if (l1.size() != 1 || !"background".equals(l1.get(0)) || l2.size() != 1
                || !"background".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on second  jc.setBackground()");
        }

        jc.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));

        Util.waitQueueEventsProcess();

        l1 = al1.getLog();
        l2 = al2.getLog();

        if (l1.size() != 2 || !"background".equals(l1.get(0))
                || !"border".equals(l1.get(1)) || l2.size() != 1
                || !"background".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on second jc.setBorder()");
        }

        return passed();
    }

    public Result testFirePropertyChangeInt() {
        PropertyChangeListenerImpl al1 = new PropertyChangeListenerImpl(0);
        PropertyChangeListenerImpl al2 = new PropertyChangeListenerImpl(0);
        List l1 = al1.getLog();
        List l2 = al2.getLog();

        JComponent jc = new JComponent() {
        };

        jc.addPropertyChangeListener(al1);
        jc.addPropertyChangeListener("qwerty", al2);

        jc.firePropertyChange("qwerty", 1, 2);

        Util.waitQueueEventsProcess();

        if (l1.size() != 1 || !"qwerty".equals(l1.get(0)) || l2.size() != 1
                || !"qwerty".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on jc.firePropertyChange(qwerty, 1, 2)");
        }

        jc.firePropertyChange("qwerty", 1, 1);

        Util.waitQueueEventsProcess();

        if (l1.size() != 1 || !"qwerty".equals(l1.get(0)) || l2.size() != 1
                || !"qwerty".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on jc.firePropertyChange(qwerty, 1, 1) "
                    + "(expected no message)");
        }

        jc.firePropertyChange("QWERTY", 1, 2);

        Util.waitQueueEventsProcess();

        if (l1.size() != 2 || !"qwerty".equals(l1.get(0))
                || !"QWERTY".equals(l1.get(1)) || l2.size() != 1
                || !"qwerty".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on second jc.firePropertyChange(QWERTY, 1, 1)");
        }

        jc.firePropertyChange(null, 1, 2);

        Util.waitQueueEventsProcess();

        if (l1.size() != 3 || !"qwerty".equals(l1.get(0))
                || !"QWERTY".equals(l1.get(1)) || l1.get(2) != null
                || l2.size() != 1 || !"qwerty".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on second jc.firePropertyChange(null, 1, 2)");
        }

        return passed();
    }

    public Result testFirePropertyChangeBooolean() {
        PropertyChangeListenerImpl al1 = new PropertyChangeListenerImpl(0);
        PropertyChangeListenerImpl al2 = new PropertyChangeListenerImpl(0);
        List l1 = al1.getLog();
        List l2 = al2.getLog();

        JComponent jc = new JComponent() {
        };

        jc.addPropertyChangeListener(al1);
        jc.addPropertyChangeListener("qwerty", al2);

        jc.firePropertyChange("qwerty", true, false);

        Util.waitQueueEventsProcess();

        if (l1.size() != 1 || !"qwerty".equals(l1.get(0)) || l2.size() != 1
                || !"qwerty".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on jc.firePropertyChange(qwerty, true, false)");
        }

        jc.firePropertyChange("qwerty", true, true);

        Util.waitQueueEventsProcess();

        if (l1.size() != 1 || !"qwerty".equals(l1.get(0)) || l2.size() != 1
                || !"qwerty".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on jc.firePropertyChange(qwerty, true, true) "
                    + "(expected no message)");
        }

        jc.firePropertyChange("QWERTY", true, false);

        Util.waitQueueEventsProcess();

        if (l1.size() != 2 || !"qwerty".equals(l1.get(0))
                || !"QWERTY".equals(l1.get(1)) || l2.size() != 1
                || !"qwerty".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on second jc.firePropertyChange(QWERTY, true, false)");
        }

        jc.firePropertyChange(null, true, false);

        Util.waitQueueEventsProcess();

        if (l1.size() != 3 || !"qwerty".equals(l1.get(0))
                || !"QWERTY".equals(l1.get(1)) || l1.get(2) != null
                || l2.size() != 1 || !"qwerty".equals(l2.get(0))) {
            return failed("wrong PropertyChangeListener hooks called on second jc.firePropertyChange(null, true, false)");
        }

        return passed();
    }

    public Result testFirePropertyChangeObject() {
        class TestedJComponent extends JComponent {
            public Result test() {
                PropertyChangeListenerImpl al1 = new PropertyChangeListenerImpl(
                        0);
                PropertyChangeListenerImpl al2 = new PropertyChangeListenerImpl(
                        0);
                List l1 = al1.getLog();
                List l2 = al2.getLog();

                addPropertyChangeListener(al1);
                addPropertyChangeListener("qwerty", al2);

                firePropertyChange("qwerty", "ab", al1);

                Util.waitQueueEventsProcess();

                if (l1.size() != 1 || !"qwerty".equals(l1.get(0))
                        || l2.size() != 1 || !"qwerty".equals(l2.get(0))) {
                    return failed("wrong PropertyChangeListener hooks called on jc.firePropertyChange(qwerty, String, Object)");
                }

                firePropertyChange("qwerty", al1, al2);

                Util.waitQueueEventsProcess();

                if (l1.size() != 1 || !"qwerty".equals(l1.get(0))
                        || l2.size() != 1 || !"qwerty".equals(l2.get(0))) {
                    return failed("wrong PropertyChangeListener hooks called on jc.firePropertyChange(qwerty, Object, Object)  "
                            + "(Objects are equal - expected no message)");
                }

                firePropertyChange("QWERTY", "ab", "bc");

                Util.waitQueueEventsProcess();

                if (l1.size() != 2 || !"qwerty".equals(l1.get(0))
                        || !"QWERTY".equals(l1.get(1)) || l2.size() != 1
                        || !"qwerty".equals(l2.get(0))) {
                    return failed("wrong PropertyChangeListener hooks called on second jc.firePropertyChange(QWERTY, 'ab', 'bc')");
                }

                firePropertyChange(null, "ab", "bc");

                Util.waitQueueEventsProcess();

                if (l1.size() != 3 || !"qwerty".equals(l1.get(0))
                        || !"QWERTY".equals(l1.get(1)) || l1.get(2) != null
                        || l2.size() != 1 || !"qwerty".equals(l2.get(0))) {
                    return failed("wrong PropertyChangeListener hooks called on second jc.firePropertyChange(null, 'ab', 'bc')");
                }
                return passed();
            }
        }

        return new TestedJComponent().test();
    }

    public Result testFireVetoablePropertyChange() {
        class TestedJComponent extends JComponent {
            public Result test() throws PropertyVetoException {
                VetoableChangeListenerImpl al1 = new VetoableChangeListenerImpl(
                        0);
                List l1 = al1.getLog();

                addVetoableChangeListener(al1);
                addVetoableChangeListener(al1);

                fireVetoableChange("qwerty", "ab", al1);

                Util.waitQueueEventsProcess();

                if (l1.size() != 2 || !"qwerty".equals(l1.get(0))
                        || !"qwerty".equals(l1.get(1))) {
                    return failed("wrong VetoableChangeListener hooks called on jc.fireVetoableChange(qwerty, String, Object)");
                }

                fireVetoableChange("qwerty", al1,
                        new VetoableChangeListenerImpl(0));

                Util.waitQueueEventsProcess();

                if (l1.size() != 2 || !"qwerty".equals(l1.get(0))
                        || !"qwerty".equals(l1.get(1))) {
                    return failed("wrong VetoableChangeListener hooks called on jc.fireVetoableChange(qwerty, Object, Object)  "
                            + "(Objects are equal - expected no message)");
                }

                fireVetoableChange(null, "ab", "bc");

                Util.waitQueueEventsProcess();

                if (l1.size() != 4 || !"qwerty".equals(l1.get(0))
                        || !"qwerty".equals(l1.get(0)) || l1.get(2) != null
                        || l1.get(3) != null) {
                    return failed("wrong VetoableChangeListener hooks called on second jc.fireVetoableChange(null, 'ab', 'bc')");
                }
                return passed();
            }
        }

        try {
            return new TestedJComponent().test();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            return failed("got PropertyVetoException : " + e.getMessage());
        }
    }

}

class PropertyChangeListenerImpl implements PropertyChangeListener {
    List log = Collections.synchronizedList(new LinkedList());

    int code;

    PropertyChangeListenerImpl(int c) {
        code = c;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }
        if (!(arg0 instanceof PropertyChangeListenerImpl)) {
            return false;
        }
        return code == ((PropertyChangeListenerImpl) arg0).code;
    }

    public List getLog() {
        return log;
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        log.add(arg0.getPropertyName());
    }

}

class VetoableChangeListenerImpl implements VetoableChangeListener {
    List log = Collections.synchronizedList(new LinkedList());

    int code;

    VetoableChangeListenerImpl(int c) {
        code = c;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }
        if (!(arg0 instanceof VetoableChangeListenerImpl)) {
            return false;
        }
        return code == ((VetoableChangeListenerImpl) arg0).code;
    }

    public List getLog() {
        return log;
    }

    public void vetoableChange(PropertyChangeEvent arg0) {
        log.add(arg0.getPropertyName());
    }

}