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
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.10 $
 */
package org.apache.harmony.harness;

import java.util.HashMap;

public interface Report {

    /**
     * Generate report of the run in the 'human-readable' form.
     */
    void genReport();

    /**
     * Add result of the test execution to the report data
     * 
     * @param testID testID to identify the result
     * @param status status of execution
     * @return true if the status is known otherwise false
     */
    boolean addResult(String testID, int status);

    /**
     * Get all test results registered in the report as HashMap with testID as
     * key and test execution status as value
     * 
     * @return all tests results
     */
    HashMap getResults();

    /**
     * Clear information about all tests.
     */
    void clear();

    /**
     * Provide information about number of ran tests for moment when this
     * method was called
     * 
     * @return number of tests that report any status
     */
    int allTestCnt();

    /**
     * Provide information about number of passed tests for moment when this
     * method was called
     * 
     * @return number of passed tests
     */
    int passedTestCnt();

    /**
     * Provide information about number of failed tests for moment when this
     * method was called
     * 
     * @return number of failed tests
     */
    int failedTestCnt();

    /**
     * Provide information about number of tests with execution status 'error'
     * for moment when this method was called
     * 
     * @return number of tests with 'error' execution status
     */
    int errorTestCnt();

    /**
     * Provide information about number of tests that report 'skipped' status
     * when this method was called
     * 
     * @return number of skipped tests
     */
    int skippedTestCnt();

    /**
     * Provide information about number of tests that report statuses not
     * recognized by harness for moment when this method was called
     * 
     * @return number of tests with unspecified execution status
     */
    int unspecifiedTestCnt();
}