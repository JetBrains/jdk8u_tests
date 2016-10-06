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
/*
 * Created on 18.03.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.JComponent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedJComponent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.Util;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 *  
 */
public class JComponentAncestorListenerTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new JComponentAncestorListenerTest().test(args));
    }

    public Result testAddRemove() {
        JComponent jc = new JComponent() {
        };

        if (jc.getAncestorListeners().length != 0) {
            return failed("expected 0 ancestorListeners here");
        }

        AncestorListenerImpl al1 = new AncestorListenerImpl(0);
        AncestorListenerImpl al2 = new AncestorListenerImpl(1);

        jc.addAncestorListener(al1);
        if (jc.getAncestorListeners().length != 1) {
            return failed("expected 1 ancestorListener here");
        }

        jc.addAncestorListener(al2);
        if (jc.getAncestorListeners().length != 2) {
            return failed("expected 2 ancestorListeners here");
        }

        jc.addAncestorListener(al1);
        if (jc.getAncestorListeners().length != 3) {
            return failed("expected 3 ancestorListeners here");
        }

        jc.addAncestorListener(null);

        if (jc.getAncestorListeners().length != 3) {
            return failed("expected 3 ancestorListeners here");
        }

        jc.removeAncestorListener(null);
        if (jc.getAncestorListeners().length != 3) {
            return failed("expected 3 ancestorListeners here");
        }

        jc.removeAncestorListener(al1);
        if (jc.getAncestorListeners().length != 2) {
            return failed("expected 2 ancestorListeners here, got: "
                    + jc.getAncestorListeners().length);
        }

        jc.removeAncestorListener(new AncestorListenerImpl(3));
        if (jc.getAncestorListeners().length != 2) {
            return failed("expected 2 ancestorListeners here");
        }

        jc.removeAncestorListener(new AncestorListenerImpl(0));
        if (jc.getAncestorListeners().length != 1) {
            return failed("expected 1 ancestorListener here");
        }

        jc.removeAncestorListener(al2);
        if (jc.getAncestorListeners().length != 0) {
            return failed("expected 0 ancestorListeners here");
        }

        return passed();
    }

    public Result testGetAncestorListeners() {
        JComponent jc = new JComponent() {
        };

        if (jc.getAncestorListeners() == jc.getAncestorListeners()) {
            return failed("expected two calls on getAncestorListeners() return different values");
        }

        return passed();
    }

    public Result testSetLocation() {
        AncestorListenerImpl al1 = new AncestorListenerImpl(0);
        AncestorListenerImpl al2 = new AncestorListenerImpl(0);

        JComponent jc = new JComponent() {
        };

        jc.addAncestorListener(al1);
        jc.addAncestorListener(al2);
        jc.addAncestorListener(al1);

        jc.setLocation(13, 17);

        Util.waitQueueEventsProcess();

        List l1 = al1.getLog();
        List l2 = al2.getLog();

        if (l1.size() != 2 || !"moved".equals(l1.get(0))
                || !"moved".equals(l1.get(1)) || l2.size() != 1
                || !"moved".equals(l2.get(0))) {
            return failed("wrong AncestorListener hooks called on jc.setLocation()");
        }

        jc.setLocation(13, 17);

        Util.waitQueueEventsProcess();

        l1 = al1.getLog();
        l2 = al2.getLog();

        if (l1.size() != 2 || !"moved".equals(l1.get(0))
                || !"moved".equals(l1.get(1)) || l2.size() != 1
                || !"moved".equals(l1.get(0))) {
            return failed("wrong AncestorListener hooks called on second jc.setLocation()");
        }

        return passed();
    }

    public Result testAncestorAdded() {
        AncestorListenerImpl al1 = new AncestorListenerImpl(0);
        AncestorListenerImpl al2 = new AncestorListenerImpl(0);

        JComponent jc = new InstrumentedJComponent();
        JWindow window = new JWindow();

        jc.setVisible(false);

        jc.addAncestorListener(al1);
        jc.addAncestorListener(al2);
        jc.addAncestorListener(al1);

        window.getContentPane().add(jc);
        window.setVisible(true);

        if (al1.getLog().size() != 0 || al1.getLog().size() != 0) {
            return failed("expected no hooks be called by now");
        }

        InstrumentedUILog.clear();
        jc.setVisible(true);

        Util.waitQueueEventsProcess();

        List l1 = al1.getLog();
        List l2 = al2.getLog();

        if (l1.size() != 2 || !"added".equals(l1.get(0))
                || !"added".equals(l1.get(1)) || l2.size() != 1
                || !"added".equals(l2.get(0))) {
            InstrumentedUILog.printLog();
            return failed("wrong AncestorListener hooks called on jc.setVisible(true)");
        }

        jc.setVisible(true);

        Util.waitQueueEventsProcess();

        l1 = al1.getLog();
        l2 = al2.getLog();

        if (l1.size() != 2 || !"added".equals(l1.get(0))
                || !"added".equals(l1.get(1)) || l2.size() != 1
                || !"added".equals(l2.get(0))) {
            return failed("wrong AncestorListener hooks called on second jc.setVisible(true)");
        }

        return passed();
    }

    public Result testAncestorRemoved() {
        AncestorListenerImpl al1 = new AncestorListenerImpl(0);
        AncestorListenerImpl al2 = new AncestorListenerImpl(0);

        JComponent jc = new JComponent() {
        };
        JFrame window = new JFrame("test");

        window.getContentPane().add(jc);
        window.pack();

        window.setVisible(true);

        jc.setVisible(true);

        jc.addAncestorListener(al1);
        jc.addAncestorListener(al2);
        jc.addAncestorListener(al1);

        if (al1.getLog().size() != 0 || al1.getLog().size() != 0) {
            return failed("expected no hooks be called by now");
        }

        jc.setVisible(false);

        Util.waitQueueEventsProcess();

        List l1 = al1.getLog();
        List l2 = al2.getLog();

        if ((l1.size() != 2 || !"removed".equals(l1.get(0))
                || !"removed".equals(l1.get(1)) || l2.size() != 1 || !"removed"
                .equals(l2.get(0)))
                && (l1.size() != 4 || !"removed".equals(l1.get(0))
                        || !"removed".equals(l1.get(1))
                        || !"removed".equals(l1.get(2))
                        || !"removed".equals(l1.get(3))
                        || l2.size() != 2 
                        || !"removed".equals(l2.get(0))
                        || !"removed".equals(l2.get(1))
                        )
                && (l1.size() != 0 && l2.size() != 0)) {
            System.err.println(l1.size());
            System.err.println(l2.size());
            return failed("wrong AncestorListener hooks called on jc.setVisible(false)");
        }

        jc.setVisible(false);

        Util.waitQueueEventsProcess();

        l1 = al1.getLog();
        l2 = al2.getLog();

        if ((l1.size() != 2 || !"removed".equals(l1.get(0))
                || !"removed".equals(l1.get(1)) || l2.size() != 1 || !"removed"
                .equals(l2.get(0)))
                && (l1.size() != 4 || !"removed".equals(l1.get(0))
                        || !"removed".equals(l1.get(1))
                        || !"removed".equals(l1.get(2))
                        || !"removed".equals(l1.get(3))
                        || l2.size() != 2 
                        || !"removed".equals(l2.get(0))
                        || !"removed".equals(l2.get(1))
                        )
                && (l1.size() != 0 && l2.size() != 0)) {
            return failed("wrong AncestorListener hooks called on second jc.setVisible(false)");
        }

        return passed();
    }

}

class AncestorListenerImpl implements AncestorListener {
    List log = Collections.synchronizedList(new LinkedList());

    int code;

    AncestorListenerImpl(int c) {
        code = c;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }
        if (!(arg0 instanceof AncestorListenerImpl)) {
            return false;
        }
        return code == ((AncestorListenerImpl) arg0).code;
    }

    public int hashCode() {
        return code;
    }

    public void ancestorAdded(AncestorEvent arg0) {
        log.add("added");
    }

    public void ancestorMoved(AncestorEvent arg0) {
        log.add("moved");
    }

    public void ancestorRemoved(AncestorEvent arg0) {
        log.add("removed");
    }

    public List getLog() {
        return log;
    }
}
