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
 * @version $Revision: 1.6 $
 */
package org.apache.harmony.harness.plugins.variationtests;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;

import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;

/**
 * This class is responsible for producing all possible variable combinations
 * from a given set of variations.
 */
public class Product {
    private final String classID    = "Product";

    protected Logging    log        = Main.getCurCore().getInternalLogger();
    ArrayList            a          = new ArrayList();
    ArrayList            variations = new ArrayList();
    Object[]             cur;
    boolean              eof        = false;

    public int getSize() {
        return a.size();
    }

    public String getVarName(int i) {
        return ((Data)a.get(i)).name;
    }

    public String getVarTitle(int i) {
        return ((Data)a.get(i)).title;
    }

    public void add(IVariation seq) {
        a.add(new Data(seq));
    }

    public void init() throws SetupException {
        cur = new Object[a.size()];
        for (int i = 0; i < a.size(); i++) {
            Data d = (Data)a.get(i);
            if (d.arr.length == 0) {
                throw new SetupException("Variation '" + d.var.getTitle()
                    + "' yields no variants.");
            }
            d.pos = 0;
        }
        eof = false;
    }

    public boolean next() {
        int i = 0;
        for (i = 0; i < a.size(); i++) {
            Data d = (Data)a.get(i);
            if (d.pos < d.arr.length - 1) {
                d.pos++;
                break;
            } else {
                d.pos = 0;
            }
        }
        if (i == a.size()) {
            return false;
        }
        return true;
    }

    public Object get(String name) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tget(): ";
        for (int i = 0; i < a.size(); i++) {
            Data d = (Data)a.get(i);
            Object o = ((IValue)d.arr[d.pos]).getValue(name);
            if (o != null) {
                return d.arr[d.pos];
            }
        }
        log.add(Level.INFO, methodLogPrefix + "UNRESOLVED " + name);
        return null;
    }

    public Object get(int num) {
        Data d = (Data)a.get(num);
        return d.arr[d.pos];
    }

    public void dump() {
        for (int i = 0; i < a.size(); i++) {
            Data d = (Data)a.get(i);
            log.add(Level.CONFIG, classID + ": [" + i + "] " + d);
        }
    }

    public String subst(String s) {
        String w = s;
        for (int i = 0; i < a.size(); i++) {
            Data d = (Data)a.get(i);
            w = w.replaceAll("@" + d.name, d.arr[d.pos].toString());
        }
        return w;
    }

    class Data {
        String     name;
        String     title;
        int        pos;
        Object[]   arr;
        IVariation var;

        Data(IVariation var) {
            this.var = var;
            this.name = var.getVar();
            this.title = var.getTitle();
            ArrayList arrl = new ArrayList();
            for (Enumeration seq = var.getVariants(); seq.hasMoreElements();) {
                arrl.add(seq.nextElement());
            }
            this.arr = arrl.toArray();
            this.pos = 0;
        }

        public String toString() {
            return "Name:" + name + ",pos:" + pos + ",cur:" + arr[pos];
        }
    }
}