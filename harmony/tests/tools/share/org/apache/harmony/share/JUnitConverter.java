/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Yerlan Tokpanov
 * @version $Revision: 1.3 $
 */
package org.apache.harmony.share;

public interface JUnitConverter {

    /**
     * Sets Result "Passed" if expr is true and "Failed" otherwise.
     * 
     * @param expr Boolean expression.
     */
    public void assertTrue(boolean expr);

    /**
     * Sets Result "Passed" if expr is true and "Failed" otherwise. Sets Result
     * "Error" and msg as message of Result if AssertionFailedError is occurred.
     * 
     * @param msg String error message.
     * @param expr Boolean expression.
     */
    public void assertTrue(String msg, boolean expr);

    /**
     * Sets Result "Passed" if expr is false and "Failed" otherwise.
     * 
     * @param expr Boolean expression.
     */
    public void assertFalse(boolean expr);

    /**
     * Sets Result "Passed" if expr is false and "Failed" otherwise. Sets Result
     * "Error" and msg as message of Result if AssertionFailedError is occurred.
     * 
     * @param msg String error message.
     * @param expr Boolean expression.
     */
    public void assertFalse(String msg, boolean expr);

    /**
     * Sets Result "Passed" if obj is null and "Failed" otherwise.
     * 
     * @param obj The reference to the instance of Object.
     */
    public void assertNull(Object obj);

    /**
     * Sets Result "Passed" if obj is null and "Failed" otherwise. Sets Result
     * "Error" and msg as message of Result if AssertionFailedError is occurred.
     * 
     * @param msg String error message.
     * @param obj The reference to the instance of Object.
     */
    public void assertNull(String msg, Object obj);

    /**
     * Sets Result "Passed" if obj is not null and "Failed" otherwise.
     * 
     * @param obj The reference to the instance of Object.
     */
    public void assertNotNull(Object obj);

    /**
     * Sets Result "Passed" if obj is not null and "Failed" otherwise. Sets
     * Result "Error" and msg as message of Result if AssertionFailedError is
     * occurred.
     * 
     * @param msg String error message.
     * @param obj The reference to the instance of Object.
     */
    public void assertNotNull(String msg, Object obj);

    /**
     * Sets Result "Passed" if two objects refer to the same object and "Failed"
     * otherwise.
     * 
     * @param expected The reference to the first instance of Object.
     * @param actual The reference to the second instance of Object.
     */
    public void assertSame(Object expected, Object actual);

    /**
     * Sets Result "Passed" if two objects refer to the same object and "Failed"
     * otherwise. Sets msg as message of Result.
     * 
     * @param msg String message.
     * @param expected The reference to the first instance of Object.
     * @param actual The reference to the second instance of Object.
     */
    public void assertSame(String msg, Object expected, Object actual);

    /**
     * Sets Result "Passed" if two objects do not refer to the same object and
     * "Failed" otherwise.
     * 
     * @param expected The reference to the first instance of Object.
     * @param actual The reference to the second instance of Object.
     */
    public void assertNotSame(Object expected, Object actual);

    /**
     * Sets Result "Passed" if two objects do not refer to the same object and
     * "Failed" otherwise. Sets msg as message of Result.
     * 
     * @param msg String message.
     * @param expected The reference to the first instance of Object.
     * @param actual The reference to the second instance of Object.
     */
    public void assertNotSame(String msg, Object expected, Object actual);

    /**
     * Sets Result "Passed" if two objects are equal.
     * 
     * @param expected
     * @param actual
     */
    public void assertEquals(Object expected, Object actual);

    /**
     * Sets Result "Passed" if two objects are equal. Sets msg as message of
     * Result.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     */
    public void assertEquals(String msg, Object expected, Object actual);

    /**
     * Sets Result "Passed" if two booleans are equal.
     * 
     * @param expected
     * @param actual
     */
    public void assertEquals(boolean expected, boolean actual);

    /**
     * Sets Result "Passed" if two booleans are equal. Sets msg as message of
     * Result.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     */
    public void assertEquals(String msg, boolean expected, boolean actual);

    /**
     * Sets Result "Passed" if two bytes are equal.
     * 
     * @param expected
     * @param actual
     */
    public void assertEquals(byte expected, byte actual);

    /**
     * Sets Result "Passed" if two bytes are equal. Sets msg as message of
     * Result.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     */
    public void assertEquals(String msg, byte expected, byte actual);

    /**
     * Sets Result "Passed" if two chars are equal.
     * 
     * @param expected
     * @param actual
     */
    public void assertEquals(char expected, char actual);

    /**
     * Sets Result "Passed" if two chars are equal. Sets msg as message of
     * Result.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     */
    public void assertEquals(String msg, char expected, char actual);

    /**
     * Sets Result "Passed" if two int values are equal.
     * 
     * @param expected
     * @param actual
     */
    public void assertEquals(int expected, int actual);

    /**
     * Sets Result "Passed" if two int values are equal. Sets msg as message of
     * Result.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     */
    public void assertEquals(String msg, int expected, int actual);

    /**
     * Sets Result "Passed" if two shorts are equal.
     * 
     * @param expected
     * @param actual
     */
    public void assertEquals(short expected, short actual);

    /**
     * Sets Result "Passed" if two shorts are equal. Sets msg as message of
     * Result.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     */
    public void assertEquals(String msg, short expected, short actual);

    /**
     * Sets Result "Passed" if two longs are equal.
     * 
     * @param expected
     * @param actual
     */
    public void assertEquals(long expected, long actual);

    /**
     * Sets Result "Passed" if two longs are equal. Sets msg as message of
     * Result.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     */
    public void assertEquals(String msg, long expected, long actual);

    /**
     * Sets Result "Passed" if two doubles are equal concerning a delta. If the
     * expected value is infinity then the delta value is ignored.
     * 
     * @param expected
     * @param actual
     * @param delta
     */
    public void assertEquals(double expected, double actual, double delta);

    /**
     * Sets Result "Passed" if two doubles are equal concerning a delta. Sets
     * msg as message of Result. If the expected value is infinity then the
     * delta value is ignored.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     * @param delta
     */
    public void assertEquals(String msg, double expected, double actual,
        double delta);

    /**
     * Sets Result "Passed" if two float values are equal concerning a delta. If the
     * expected value is infinity then the delta value is ignored.
     * 
     * @param expected
     * @param actual
     * @param delta
     */
    public void assertEquals(float expected, float actual, float delta);

    /**
     * Sets Result "Passed" if two float values are equal concerning a delta. Sets
     * msg as message of Result. If the expected value is infinity then the
     * delta value is ignored.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     * @param delta
     */
    public void assertEquals(String msg, float expected, float actual,
        float delta);

    /**
     * Sets Result "Passed" if two strings are equal.
     * 
     * @param expected
     * @param actual
     */
    public void assertEquals(String expected, String actual);

    /**
     * Sets Result "Passed" if two strings are equal. Sets msg as message of
     * Result.
     * 
     * @param msg String message.
     * @param expected
     * @param actual
     */
    public void assertEquals(String msg, String expected, String actual);

    /**
     * Sets Result "Failed".
     */
    public void jufail();

    /**
     * Sets Result "Failed". Sets msg as message of Result.
     * 
     * @param msg String message.
     */
    public void jufail(String msg);

    /**
     * Sets Result "Passed".
     */
    public void jupass();

    /**
     * Sets Result "Passed". Sets msg as message of Result.
     * 
     * @param msg String message.
     */

    public void jupass(String msg);

    /**
     * Sets Result "Error".
     */

    public void juerror();

    /**
     * Sets Result "Error". Sets msg as message of Result.
     * 
     * @param msg String message.
     */
    public void juerror(String msg);
}