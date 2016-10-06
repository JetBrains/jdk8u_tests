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
package org.apache.harmony.test.func.api.javax.swing.text.DefaultCaret;

import java.awt.Color;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import org.apache.harmony.test.func.api.javax.swing.share.Util;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 *  
 */
public class DefaultCaretChangeListenerTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new DefaultCaretChangeListenerTest().test(args));
    }

    public Result testAddRemove() {
        javax.swing.text.DefaultCaret dk = new javax.swing.text.DefaultCaret() {
        };

        if (dk.getChangeListeners().length != 0) {
            return failed("expected 0 changeListeners here");
        }

        ChangeListenerImpl al1 = new ChangeListenerImpl(0);
        ChangeListenerImpl al2 = new ChangeListenerImpl(1);

        dk.addChangeListener(al1);
        if (dk.getChangeListeners().length != 1) {
            return failed("expected 1 changeListener");
        }

        dk.addChangeListener(al2);
        if (dk.getChangeListeners().length != 2) {
            return failed("expected 2 changeListeners here");
        }

        dk.addChangeListener(al1);
        if (dk.getChangeListeners().length != 3) {
            return failed("expected 3 changeListeners here");
        }

        dk.addChangeListener(null);

        if (dk.getChangeListeners().length != 3) {
            return failed("expected 3 changeListeners here");
        }

        dk.removeChangeListener(null);
        if (dk.getChangeListeners().length != 3) {
            return failed("expected 3 changeListeners here");
        }

        dk.removeChangeListener(al1);
        if (dk.getChangeListeners().length != 2) {
            return failed("expected 2 changeListeners here, got: "
                    + dk.getChangeListeners().length);
        }

        dk.removeChangeListener(new ChangeListenerImpl(3));
        if (dk.getChangeListeners().length != 2) {
            return failed("expected 2 changeListeners here");
        }

        dk.removeChangeListener(new ChangeListenerImpl(0));
        if (dk.getChangeListeners().length != 1) {
            return failed("expected 1 changeListener here, got "
                    + dk.getChangeListeners().length);
        }

        dk.removeChangeListener(al2);
        if (dk.getChangeListeners().length != 0) {
            return failed("expected 0 changeListeners here");
        }

        return passed();
    }

    public Result testGetChangeListeners() {
        javax.swing.text.DefaultCaret dk = new javax.swing.text.DefaultCaret() {
        };

        if (dk.getChangeListeners() == dk.getChangeListeners()) {
            return failed("expected two calls on getChangeListeners() return different values");
        }

        return passed();
    }

    public Result testSetVisible() {
        ChangeListenerImpl al1 = new ChangeListenerImpl(0);
        ChangeListenerImpl al2 = new ChangeListenerImpl(0);

        javax.swing.text.DefaultCaret dk = new javax.swing.text.DefaultCaret() {
        };

        dk.addChangeListener(al1);
        dk.addChangeListener(al2);
        dk.addChangeListener(al1);

        dk.setVisible(true);

        Util.waitQueueEventsProcess();

        List l1 = al1.getLog();
        List l2 = al2.getLog();

        if (l1.size() != 0 || l2.size() != 0) {
            return failed("wrong ChangeListener hooks called on dk.setVisible(true)");
        }

        return passed();
    }


    public Result testFireStateChanged() {
        ChangeListenerImpl al1 = new ChangeListenerImpl(0);
        ChangeListenerImpl al2 = new ChangeListenerImpl(0);
        List l1 = al1.getLog();
        List l2 = al2.getLog();

        DefaultCaretPublicMethods jc = new DefaultCaretPublicMethods();

        jc.addChangeListener(al1);
        jc.addChangeListener(al2);

        jc.fireStateChanged();

        Util.waitQueueEventsProcess();

        if (l1.size() != 1 ||  l2.size() != 1) {
            return failed("wrong ChangeListener hooks called on jc.fireStateChanged()");
        }

        jc.fireStateChanged();

        Util.waitQueueEventsProcess();
        
        if (l1.size() != 2 ||  l2.size() != 2) {
            return failed("wrong ChangeListener hooks called on second jc.fireStateChanged()");
        }


        return passed();
    }
}


class ChangeListenerImpl implements ChangeListener {
    List log = Collections.synchronizedList(new LinkedList());

    int code;

    ChangeListenerImpl(int c) {
        code = c;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }
        if (!(arg0 instanceof ChangeListenerImpl)) {
            return false;
        }
        return code == ((ChangeListenerImpl) arg0).code;
    }

    public List getLog() {
        return log;
    }

    public void stateChanged(ChangeEvent arg0) {
        log.add(arg0.toString());
    }
}

class DefaultCaretPublicMethods extends DefaultCaret {
    public void fireStateChanged() {
        super.fireStateChanged();
    }
}

