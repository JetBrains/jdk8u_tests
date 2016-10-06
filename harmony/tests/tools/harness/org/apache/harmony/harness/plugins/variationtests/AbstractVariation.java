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

import java.util.ArrayList;

/**
 * Base class for Enumerator and FileSet variations
 */
public abstract class AbstractVariation implements IVariation {
    protected String title = null;
    protected String var   = null;

    public String getTitle() {
        return title;
    }

    public String getVar() {
        return var;
    }

    /**
     * This method remembers variation title and variation parameter name
     * 
     * @see org.apache.harmony.harness.plugins.variationtests.IPluggable#setup(java.lang.String[])
     */
    public String[] setup(String[] args) throws SetupException {
        ArrayList tail = new ArrayList();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-title")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -title without value");
                }
                title = args[i];
            } else if (args[i].equalsIgnoreCase("-var")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -var without value");
                }
                var = args[i];
            } else {
                tail.add(args[i]);
            }
        }
        if (title == null) {
            throw new SetupException("-title is not specified");
        }
        if (var == null) {
            throw new SetupException("-var is not specified");
        }
        return Util.toStringArr(tail);
    }
}