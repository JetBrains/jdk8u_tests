/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.assertion;

import org.punit.message.Messages;

public class Assert {
    
    public static void assertSame(Object expected, Object actual) {
        if (expected != actual) {
            fail(expected, actual);
        }
    }
    
    public static void assertNotSame(Object expected, Object actual) {
        if (expected == actual) {
            fail(Messages.getString("assert.notsame") + expected); //$NON-NLS-1$
        }
    }
    
    public static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            fail(expected, actual);
        }
    }
    
    public static void assertEquals(int expected, int actual) {
        assertEquals(new Integer(expected), new Integer(actual));
    }
    
    public static void assertEquals(long expected, long actual) {
        assertEquals(new Long(expected), new Long(actual));
    }
    
    public static void assertEquals(double expected, double actual) {
        assertEquals(new Double(expected), new Double(actual));
    }
    
    public static void assertEquals(double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
        	fail(new Double(expected), new Double(actual));
        }
    }

    public static void assertEquals(float expected, float actual) {
        assertEquals(new Float(expected), new Float(actual));
    }
    
    public static void assertEquals(float expected, float actual, float delta) {
        if (Math.abs(expected - actual) > delta) {
        	fail(new Float(expected), new Float(actual));
        }
    }

    public static void assertTrue(boolean flag) {
        if(!flag) {
            fail(Boolean.TRUE, Boolean.FALSE);
        }
    }
    
    public static void assertFalse(boolean flag) {
        if(flag) {
            fail(Boolean.FALSE, Boolean.TRUE);
        }
    }
    
    public static void assertNull(Object obj) {
        if(obj != null) {
            fail(null, obj);
        }
    }
    
    public static void assertNotNull(Object obj) {
        if(obj == null) {
            fail(obj, null);
        }
    }
    
    public static void assertException(Class <? extends Throwable> throwable, CodeRunner code) {
        Class <? extends Throwable> exceptionClass = null;
        try {
            code.run();
        } catch (Throwable e) {
            exceptionClass = e.getClass();
        }
        assertSame(throwable, exceptionClass);
    }
    
    public static void fail() {
        throw new AssertionError(null);
    }
    
    public static void fail(String string) {
        throw new AssertionError(string);
    }
    
    private static void fail(Object expected, Object actual) {
        fail(Messages.getString("assert.expected") + "\"" + expected + "\"" + Messages.getString("assert.got") + "\"" + actual + "\""); //$NON-NLS-2$
    }
}
