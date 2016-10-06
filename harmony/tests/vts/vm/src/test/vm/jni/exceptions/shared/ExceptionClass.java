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
 * @author Gregory Shimansky, Petr Ivanov
 * @version $Revision: 1.1.1.1 $
 */  
/*
 * Created on 12.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.exceptions;

/**
 * @author Gregory Shimansky
 *
 * Class to be used in exception tests
 */
public class ExceptionClass extends Exception {
    private int field = 0;
    private String msg = null;

    public ExceptionClass() {
        field = 0;
        msg = null;
    }

    public ExceptionClass(String msg) {
        this.msg = msg;
        field = 0;
    }

    public ExceptionClass(int val) {
        field = val;
        msg = null;
    }

    public int getField() {
        return field;
    }
    
    public String getMessage() {
        return msg;
    }
}
