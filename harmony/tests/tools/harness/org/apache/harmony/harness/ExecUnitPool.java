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
 * @version $Revision: 1.8 $
 */
package org.apache.harmony.harness;

import java.util.ArrayList;
import java.util.HashMap;

public class ExecUnitPool {

    private int     curCnt     = 0;
    private HashMap exUnitPool = new HashMap();

    synchronized int size() {
        return exUnitPool.size();
    }

    synchronized String[] getNames() {
        Object[] keys = exUnitPool.keySet().toArray();
        String[] retVal = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            retVal[i] = keys[i].toString();
        }
        return retVal;
    }

    synchronized void add(Object name, ExecUnit eu) {
        if (eu != null) {
            if (exUnitPool.containsKey(name)) {
                ArrayList tmp = (ArrayList)exUnitPool.get(name);
                tmp.add(eu);
                exUnitPool.put(name, tmp);
            } else {
                ArrayList tmp = new ArrayList();
                tmp.add(eu);
                exUnitPool.put(name, tmp);
            }
        }
    }

    synchronized ExecUnit get(String name) {
        if (exUnitPool.size() == 0) {
            //			log.add("No available execution units");
            return null;
        }
        if (exUnitPool.containsKey(name)) {
            ArrayList tmp = (ArrayList)exUnitPool.get(name);
            if (tmp.size() > 0) {
                return (ExecUnit)tmp.get(0);
            }
        }
        return null;
    }

    synchronized ExecUnit[] getSet(String name) {
        if (exUnitPool.size() == 0) {
            //			log.add("No available execution units");
            return null;
        }
        if (exUnitPool.containsKey(name)) {
            ArrayList tmp = (ArrayList)exUnitPool.get(name);
            if (tmp.size() > 0) {
                ExecUnit[] eu = new ExecUnit[tmp.size()];
                for (int i = 0; i < eu.length; i++) {
                    eu[i] = (ExecUnit)tmp.get(i);
                }
                return eu;
            }
        }
        return null;
    }

    synchronized void multiplayToConcurency(int concurency) {
        Object[] keys = exUnitPool.keySet().toArray();
        for (int cnt = 0; cnt < keys.length; cnt++) {
            ArrayList tmp = (ArrayList)exUnitPool.get(keys[cnt]);
            if (tmp.size() > 0) {
                for (int i = tmp.size(); i < concurency; i++) {
                    try {
                        tmp.add((ExecUnit)Class.forName(
                            tmp.get(0).getClass().getName()).newInstance());
                    } catch (Exception e1) {
                        break;
                    }
                }
                exUnitPool.put(keys[cnt], tmp);
            }
        }
    }
}