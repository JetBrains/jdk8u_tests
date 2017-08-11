package org.apache.harmony.luni.tests.java.util;

import junit.framework.TestCase;
import org.junit.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by vprovodin on 13/04/2017.
 */
public class FormatterLSTest extends TestCase {

    private String oldSeparator = "";

    protected void setUp() {
        oldSeparator = System.getProperty("line.separator");
    }

    protected void tearDown() {
        System.setProperty("line.separator", oldSeparator);
    }

    /**
     * @tests java.util.Formatter#format(String, Object...) for line sperator
     */
    public void _test_formatLjava_lang_String$Ljava_lang_Object_LineSeparator() {
        Formatter f = null;

        System.setProperty("line.separator", "!\n");

        f = new Formatter(Locale.US);
        f.format("%1$n", 1);
        assertEquals("!\n", f.toString());

        f = new Formatter(Locale.KOREAN);
        f.format("head%1$n%2$n", 1, new Date());
        assertEquals("head!\n!\n", f.toString());

        f = new Formatter(Locale.US);
        f.format("%n%s", "hello");
        assertEquals("!\nhello", f.toString());

        System.setProperty("line.separator", oldSeparator);

        f = new Formatter(Locale.US);
        try {
            f.format("%-n");
            fail("should throw IllegalFormatFlagsException: %-n");
        } catch (IllegalFormatFlagsException e) {
            // expected
        }
        try {
            f.format("%+n");
            fail("should throw IllegalFormatFlagsException: %+n");
        } catch (IllegalFormatFlagsException e) {
            // expected
        }
        try {
            f.format("%#n");
            fail("should throw IllegalFormatFlagsException: %#n");
        } catch (IllegalFormatFlagsException e) {
            // expected
        }
        try {
            f.format("% n");
            fail("should throw IllegalFormatFlagsException: % n");
        } catch (IllegalFormatFlagsException e) {
            // expected
        }
        try {
            f.format("%0n");
            fail("should throw IllegalFormatFlagsException: %0n");
        } catch (IllegalFormatFlagsException e) {
            // expected
        }
        try {
            f.format("%,n");
            fail("should throw IllegalFormatFlagsException: %,n");
        } catch (IllegalFormatFlagsException e) {
            // expected
        }
        try {
            f.format("%(n");
            fail("should throw IllegalFormatFlagsException: %(n");
        } catch (IllegalFormatFlagsException e) {
            // expected
        }

        f = new Formatter(Locale.US);
        try {
            f.format("%4n");
            fail("should throw IllegalFormatWidthException");
        } catch (IllegalFormatWidthException e) {
            // expected
        }

        f = new Formatter(Locale.US);
        try {
            f.format("%-4n");
            fail("should throw IllegalFormatWidthException");
        } catch (IllegalFormatWidthException e) {
            // expected
        }

        f = new Formatter(Locale.US);
        try {
            f.format("%.9n");
            fail("should throw IllegalFormatPrecisionException");
        } catch (IllegalFormatPrecisionException e) {
            // expected
        }

        f = new Formatter(Locale.US);
        try {
            f.format("%5.9n");
            fail("should throw IllegalFormatPrecisionException");
        } catch (IllegalFormatPrecisionException e) {
            // expected
        }

        System.setProperty("line.separator", oldSeparator);
    }

}
