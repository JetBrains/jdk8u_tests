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
package org.apache.harmony.vmtt.ir.attributes;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.1 $
 */

public class EnclosingMethod extends Attribute {
    
    public static final int CLASS_INDEX     = 2;
    public static final int METHOD_INDEX    = 3;
    
    private short class_index;
    private short method_index;

    public EnclosingMethod() {
        super(4);
    }

    public short getClassIndex() {
        return class_index;
    }
    
    public short getMethodIndex() {
        return method_index;
    }
    
    public void setClassIndex(short index) {
        class_index = index;
        setDefined(CLASS_INDEX);
    }
    
    public void setMethodIndex(short index) {
        method_index = index;
        setDefined(METHOD_INDEX);
    }
}
