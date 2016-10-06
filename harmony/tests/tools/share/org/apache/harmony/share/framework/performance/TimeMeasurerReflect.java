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
 * @author Aleksey V Golubitsky
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.share.framework.performance;

import java.util.Vector;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class TimeMeasurerReflect extends Measurer {

    public static final int second    = 1000;
    public static final int minute    = 60 * second;
    public static final int hour      = 60 * minute;

    private Vector          testUnits = new Vector();
    private TMResult[]      results   = null;

    /**
     * Return instance of the default TimeMeasurer
     * 
     * @param className
     * @return default TimeMeasurerReflect
     * @throws NullPointerException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    public static TimeMeasurerReflect getDefaultTimeMeasurer(Class clazz)
        throws NullPointerException, ClassNotFoundException,
        NoSuchMethodException {
        TimeMeasurerReflect defaultTM = new TimeMeasurerReflect();
        TestUnit tu = new TestUnit(clazz);
        tu.setBefore("before");
        tu.setTest("test");
        tu.setAfter("after");
        defaultTM.addTestUnit(tu);
        return defaultTM;
    }

    /**
     * Measure count of tests
     * 
     * @param classInstance
     * @param method
     * @param args
     * @param stopResult
     * @param duration
     * @return result of measurement
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NullPointerException
     */
    public static TMResult measureCount(Object classInstance, Method method,
        Object[] args, Object stopResult, long duration)
        throws IllegalAccessException, InstantiationException,
        InvocationTargetException, NullPointerException {

        if (classInstance == null || method == null) {
            throw new NullPointerException();
        } else if (duration <= 0) {
            throw new IllegalArgumentException();
        }
        Object testResult = null;
        long actualTCount = 0;
        long actualDuration = 0;
        long startTime = System.currentTimeMillis();
        do {
            testResult = method.invoke(classInstance, args);
            actualTCount++;
            if (testResult.equals(stopResult)) {
                break;
            }
            actualDuration = System.currentTimeMillis() - startTime;
        } while (actualDuration < duration);
        return new TMResult(testResult, actualDuration, actualTCount);
    }

    /**
     * Measure duration of tests
     * 
     * @param classInstance
     * @param method
     * @param args
     * @param stopResult
     * @param count
     * @return result of measurement
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NullPointerException
     */
    public static TMResult measureDuration(Object classInstance, Method method,
        Object[] args, Object stopResult, long count)
        throws IllegalAccessException, InstantiationException,
        InvocationTargetException, NullPointerException {

        if (classInstance == null || method == null) {
            throw new NullPointerException();
        } else if (count <= 0) {
            throw new IllegalArgumentException();
        }
        Object testResult = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            testResult = method.invoke(classInstance, args);
            if (testResult.equals(stopResult)) {
                break;
            }
        }
        long actualDuration = System.currentTimeMillis() - startTime;
        return new TMResult(testResult, actualDuration, count);
    }

    /**
     * Measure count and duration of tests
     * 
     * @param classInstance
     * @param method
     * @param args
     * @param stopResult
     * @param duration
     * @param count
     * @return result of measurement
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NullPointerException
     */
    public static TMResult measure(Object classInstance, Method method,
        Object[] args, Object stopResult, long duration, long count)
        throws IllegalAccessException, InstantiationException,
        InvocationTargetException, NullPointerException {

        TMResult result = measureCount(classInstance, method, args, stopResult,
            duration);
        if (result.getActualTestsCount() < count) {
            result = measureDuration(classInstance, method, args, stopResult,
                count);
        }
        return result;
    }

    /**
     * @param duration
     * @param stopResult
     * @return results of measurements
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public TMResult[] measureCount(long duration, Object stopResult)
        throws IllegalAccessException, InstantiationException,
        InvocationTargetException {
        TMResult[] results = new TMResult[testUnits.size()];
        for (int i = 0; i < testUnits.size(); i++) {
            TestUnit tu = (TestUnit)testUnits.elementAt(i);
            Object classInstance = tu.getTestClass().newInstance();
            Method before = tu.getBefore();
            if (before != null) {
                before.invoke(classInstance, tu.getBeforeArgs());
            }
            results[i] = measureCount(classInstance, tu.getTest(), tu
                .getTestArgs(), stopResult, duration);
            Method after = tu.getAfter();
            if (after != null) {
                after.invoke(classInstance, tu.getAfterArgs());
            }
        }
        return results;
    }

    /**
     * @param count
     * @param stopResult
     * @return results of measurements
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public TMResult[] measureDuration(long count, Object stopResult)
        throws IllegalAccessException, InstantiationException,
        InvocationTargetException {
        TMResult[] results = new TMResult[testUnits.size()];
        for (int i = 0; i < testUnits.size(); i++) {
            TestUnit tu = (TestUnit)testUnits.elementAt(i);
            Object classInstance = tu.getTestClass().newInstance();
            Method before = tu.getBefore();
            if (before != null) {
                before.invoke(classInstance, tu.getBeforeArgs());
            }
            results[i] = measureDuration(classInstance, tu.getTest(), tu
                .getTestArgs(), stopResult, count);
            Method after = tu.getAfter();
            if (after != null) {
                after.invoke(classInstance, tu.getAfterArgs());
            }
        }
        return results;
    }

    /**
     * @param duration
     * @param count
     * @param stopResult
     * @return results of measurements
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public TMResult[] measure(long duration, long count, Object stopResult)
        throws IllegalAccessException, InstantiationException,
        InvocationTargetException {
        TMResult[] results = new TMResult[testUnits.size()];
        for (int i = 0; i < testUnits.size(); i++) {
            TestUnit tu = (TestUnit)testUnits.elementAt(i);
            Object classInstance = tu.getTestClass().newInstance();
            Method before = tu.getBefore();
            if (before != null) {
                before.invoke(classInstance, tu.getBeforeArgs());
            }
            results[i] = measure(classInstance, tu.getTest(), tu.getTestArgs(),
                stopResult, duration, count);
            Method after = tu.getAfter();
            if (after != null) {
                after.invoke(classInstance, tu.getAfterArgs());
            }
        }
        return results;
    }

    /**
     * @param tu
     * @throws NullPointerException
     */
    public void addTestUnit(TestUnit tu) throws NullPointerException {
        if (tu == null) {
            throw new NullPointerException();
        }
        testUnits.add(tu);
    }
}