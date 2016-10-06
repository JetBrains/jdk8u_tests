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
 * @version $Revision: 1.7 $
 */
package org.apache.harmony.harness;

import java.util.ArrayList;
import java.util.List;

public class MCPool {

    private ArrayList availMC = new ArrayList();

    public void add(MCoreIR mcore) {
        if (mcore == null) {
            return;
        }
        synchronized (availMC) {
            availMC.add(mcore);
        }
    }

    public MCoreIR remove(int i) {
        if (i < 0) {
            return null;
        }
        synchronized (availMC) {
            if (i < availMC.size()) {
                return (MCoreIR)availMC.remove(i);
            } else {
                return null;
            }
        }
    }

    public void removeAll() {
        synchronized (availMC) {
            availMC.clear();
        }
    }

    public MCoreIR get(int i) {
        if (i < 0) {
            return null;
        }
        synchronized (availMC) {
            if (i < availMC.size()) {
                return (MCoreIR)availMC.get(i);
            } else {
                return null;
            }
        }
    }

    public List getAll() {
        synchronized (availMC) {
            return (ArrayList)availMC.clone();
        }
    }

    public int size() {
        synchronized (availMC) {
            return availMC.size();
        }
    }
}