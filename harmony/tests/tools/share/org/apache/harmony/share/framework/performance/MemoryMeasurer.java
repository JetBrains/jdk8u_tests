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
 * @author Aleksey V Golubitsky
 * @version $Revision: 1.2 $
 */
package org.apache.harmony.share.framework.performance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MemoryMeasurer extends Measurer {

    private long                    maxUsedMem = getUsedMemory();
    private long                    startUsedMem;
    private boolean                 active;
    private long                    waitingTime;
    private SyncObject              syncObj    = new SyncObject();
    private MemoryMeasurementResult result     = null;

    private synchronized long getUsedMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    public MeasurementResult getResult() {
        return result;
    }

    public Object measureMemory(Method method, Object[] args, Class clazz)
        throws NullPointerException, IllegalArgumentException,
        IllegalAccessException, InstantiationException,
        InvocationTargetException {
        Object testResult = null;
        long usedMemoryBefore = getUsedMemory();
        Object classInstance = getClassInstance(clazz);
        method.invoke(classInstance, args);
        long usedMemoryAfter = getUsedMemory();
        if (result == null) {
            result = new MemoryMeasurementResult();
        }
        result.setMaxUsedMemory(usedMemoryBefore - usedMemoryAfter);
        return testResult;
    }
}