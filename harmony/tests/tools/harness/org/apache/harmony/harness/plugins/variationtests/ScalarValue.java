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

/**
 * A wrapper for a scalar value (used in Enumerator and FileSet, for example)
 */
public class ScalarValue implements IValue {
    String     desc;
    Object     val;
    IVariation var;

    public ScalarValue(IVariation var, String desc, Object val) {
        this.var = var;
        this.desc = desc;
        this.val = val;
    }

    public ScalarValue(Object val) {
        this.desc = val.toString();
        this.val = val;
    }

    public Object getValue(String name) {
        if (!this.var.getVar().equalsIgnoreCase(name))
            return null;
        return val;
    }

    public String getDescription() {
        return desc;
    }

    public String toString() {
        return "[VAR:" + var.getVar() + ",DESC:" + desc + ",VALUE:" + val + "]";
    }
}