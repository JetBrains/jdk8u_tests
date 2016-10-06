/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */
 
package org.apache.harmony.test.func.api.javax.swing.share;

import java.util.Dictionary;
import java.util.Enumeration;


public class InstrumentedDictionary extends Dictionary {
    protected Object clone() throws CloneNotSupportedException {
        InstrumentedUILog.add(new Object[] {"Dictionary.clone"} );
        return super.clone();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"Dictionary.equals",  arg0} );
        return super.equals(arg0);
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"Dictionary.hashCode"} );
        return super.hashCode();
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"Dictionary.toString"} );
        return super.toString();
    }
    public int size() {
        InstrumentedUILog.add(new Object[] {"Dictionary.size"} );
        return 0;
    }

    public boolean isEmpty() {
        InstrumentedUILog.add(new Object[] {"Dictionary.isEmpty"} );
        return false;
    }

    public Enumeration elements() {
        InstrumentedUILog.add(new Object[] {"Dictionary.elements"} );
        return null;
    }

    public Enumeration keys() {
        InstrumentedUILog.add(new Object[] {"Dictionary.keys"} );
        return null;
    }

    public Object get(Object arg0) {
        InstrumentedUILog.add(new Object[] {"Dictionary.get",  arg0} );
        return "i18n".equals(arg0) ? Boolean.TRUE : null;
    }

    public Object remove(Object arg0) {
        InstrumentedUILog.add(new Object[] {"Dictionary.remove",  arg0} );
        return null;
    }

    public Object put(Object arg0, Object arg1) {
        InstrumentedUILog.add(new Object[] {"Dictionary.put",  arg0,  arg1} );
        return null;
    }

}
