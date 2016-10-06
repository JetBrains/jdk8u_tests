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
 * @version $Revision: 1.5 $
 */
package org.apache.harmony.harness.ReportTool;

public interface BugNumberProvider {

    /**
     * Return the known bug numbers for test
     * 
     * @param testName name of the test
     * @return bug numbers separated by "," or null
     */
    String getAllBugNumbers(String testName);

    /**
     * Return the known bug number for test that was run on the os+arch+execMode
     * 
     * @param testName name of the test
     * @param os operating system
     * @param arch architecture
     * @param execMode execution mode
     * @return bug number or null
     */
    String getBugNumber(String testName, String os, String arch, String execMode);
}