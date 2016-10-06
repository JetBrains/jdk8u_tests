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
 * @author Valery Shakurov
 * @version $Revision: 1.5 $
 */
package org.apache.harmony.harness.plugins.variationtests;

import java.io.File;

/**
 * This is a wrapper over standard File class which returns name with slashes
 * instead of backslashes (necessary if you intend to pass the name as an
 * argument for a shell script
 */
public final class FileWrapper {
    protected File file;

    public FileWrapper(File f) {
        file = f;
    }

    public FileWrapper(String s) {
        file = new File(s);
    }

    public String toString() {
        return file.toString().replace('\\', '/');
    }
}