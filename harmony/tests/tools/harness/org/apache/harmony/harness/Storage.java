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
 * @author Yerlan A. Tokpanov
 * @version $Revision: 1.9 $
 */
package org.apache.harmony.harness;

public interface Storage {

    /**
     * Initialize the storage process
     * 
     * @param name of the output stream, file name etc
     * @return true if successful false otherwise
     */
    public boolean init(String name);

    /**
     * Add TResIR object to store
     * 
     * @return true if successful false otherwise
     */
    public boolean add(TResIR test);

    /**
     * Add TResIR object to report only. No external records created.
     * 
     * @return true if successful false otherwise
     */
    public boolean addToReportOnly(TResIR test);

    /**
     * Close the storage process
     * 
     * @return true if successful false otherwise
     */
    public boolean close();

    /**
     * Return the extension for result files or empty string if result stored
     * not in a file system.
     * 
     * @return result files extension or empty string
     */
    public String getResultExtension();
}