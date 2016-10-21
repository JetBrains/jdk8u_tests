/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.exception;

import java.io.*;
import java.util.*;

import org.punit.util.*;

/**
 * Exception for concurrent runner. There might be not only one exception thrown
 * by different thread when executing a method concurrently. This exception can
 * record multiple exceptions.
 */
public class ConcurrentException extends PUnitException {

    private static final long serialVersionUID = 6816596410667344664L;

    private Vector<Throwable> _throwables = new Vector<Throwable>();

    public void add(Throwable t) {
        _throwables.add(t);
    }

    public int size() {
        return _throwables.size();
    }

    public void printStackTrace() {
        System.err.println(ConcurrentException.class.getName() + ":"); //$NON-NLS-1$
        Iterator<Throwable> iter = _throwables.iterator();
        while(iter.hasNext()) {
            iter.next().printStackTrace();
        }
    }

    public void printStackTrace(final PrintStream ps) {
        ps.println(ConcurrentException.class.getName() + ":"); //$NON-NLS-1$
        Iterator<Throwable> iter = _throwables.iterator();
        while(iter.hasNext()) {
            iter.next().printStackTrace(ps);
        }
    }

    public void printStackTrace(final PrintWriter pw) {
        pw.println(ConcurrentException.class.getName() + ":"); //$NON-NLS-1$
        Iterator<Throwable> iter = _throwables.iterator();
        while(iter.hasNext()) {
            iter.next().printStackTrace(pw);
        }
    }

    public String getMessage() {
    	StringBuffer sb = new StringBuffer();
    	Iterator<Throwable> iter = _throwables.iterator();
        while(iter.hasNext()) {
        	sb.append(iter.next());
            sb.append(ReporterUtil.LINE_SEPERATOR);
        }
        return sb.toString();
    }
}
