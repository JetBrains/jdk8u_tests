/* (C) Copyright 2007, by Andrew Zhang */
package org.punit.reporter;

import java.lang.reflect.*;
import java.util.*;

import org.punit.events.*;
import org.punit.watcher.*;

public class TestResult extends VanillaEventListener {

    private static final long serialVersionUID = -5770657966003459019L;

    private transient int _methodCount;

    private List<Throwable> _failures = new ArrayList<Throwable>();

    public void onMethodEnd(Method method, Object testInstance,
            Object[] params, Throwable t, List<Watcher> Watchers) {
        countMethod();
        if (t != null) {
            addThrowable(t);
        }
    }

    public synchronized void countMethod() {
        ++_methodCount;
    }

    public synchronized void addThrowable(Throwable t) {
        _failures.add(t);
    }

    public int methodCount() {
        return _methodCount;
    }

    public List<Throwable> failures() {
        return _failures;
    }

}
