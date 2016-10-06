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
import java.util.Enumeration;
import java.util.Vector;

/**
 * Iterates over an enumeration and returns specified values one by one
 */
public final class Enumerator extends AbstractVariation {
    Vector v = new Vector();

    public Enumeration getVariants() {
        return v.elements();
    }

    public String[] setup(String[] args) throws SetupException {
        ArrayList tail = new ArrayList();
        args = super.setup(args);
        // Everything remaining is mine...
        for (int i = 0; i < args.length; i++) {
            v.add(new ScalarValue(this, args[i], args[i]));
        }
        return Util.toStringArr(tail);
    }
}