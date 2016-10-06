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

public interface Finder {

    /**
     * Check that the test with the pointed name exist in the current test suite
     * 
     * @param testName - name of the test to check
     * @return true - if test exist, false - otherwise
     */
    public boolean check(String testName);

    /**
     * Find info for all tests from the root (pointed in configuration). This
     * method is equivalent to find(null, null)
     * 
     * @return number of founded records
     * @throws ConfigurationException if root is missed in the configuration
     */
    public int find() throws ConfigurationException;

    /**
     * Find info for all tests from test sub suite. This method is equivalent to
     * find(subsuite, null)
     * 
     * @return number of founded records
     * @throws ConfigurationException if root is missed in the configuration
     */
    public int find(String subsuite) throws ConfigurationException;

    /**
     * Find info for all tests from test sub suite, excluding that started with
     * pointed mask. If the subsuite is null than the Root should be used. If
     * the mask is null than no filtration.
     * 
     * @return number of founded results
     * @throws ConfigurationException if root is missed in the configuration
     */
    public int find(String subsuite, String[] mask)
        throws ConfigurationException;

    /**
     * Get next founded test or null
     * 
     * @return founded test or null if next is inaccessible
     */
    public Object getNext();

    /**
     * Get current number of elements
     * 
     * @return size in element
     */
    public int getCurSize();

    /**
     * Clear information about all tests.
     */
    public void clear();

    /**
     * Set the root to find the data (directory for example to search into
     * directory structure)
     * 
     * @param newValue for root
     * @return old value
     */
    public String setFindRoot(String newValue);

    /**
     * Return true if the process of finding is started but not finished.
     * 
     * @return true for active state, false otherwise
     */
    public boolean isActive();

    /**
     * Stop the current search if finder support this operation. Otherwise do
     * nothing
     */
    public void stop();
}