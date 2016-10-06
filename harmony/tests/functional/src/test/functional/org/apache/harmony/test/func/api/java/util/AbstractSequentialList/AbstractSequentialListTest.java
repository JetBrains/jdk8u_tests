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

package org.apache.harmony.test.func.api.java.util.AbstractSequentialList;

import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

class MyListIterator implements ListIterator {
    private int index = -1;

    private int fill = 0;

    private Object[] list;

    public MyListIterator(int count) {
        index = count - 1;
        list = new Object[1000];
    }

    public int nextIndex() {
        return index + 1;
    }

    public int previousIndex() {
        return index - 1;
    }

    public void remove() {

    }

    public boolean hasNext() {
        //System.out.println("hasNext" + index + " " + fill);
        return (index + 1 < fill);

    }

    public boolean hasPrevious() {
        return false;
    }

    public Object next() {
        index++;
        //System.out.println("DEBUG" + index + " " + list[index]);
        return list[index];
    }

    public Object previous() {
        return null;
    }

    public void add(Object arg0) {
        list[fill] = arg0;
        //System.out.println("DEBUG" + fill + " " + list[fill]);
        fill++;
    }

    public void set(Object arg0) {
    }

}

class MyAbstractSequentialList extends AbstractSequentialList {
    private ListIterator iter;

    public MyAbstractSequentialList() {
        super();
        iter = new MyListIterator(0);
    }

    public ListIterator listIterator(int arg0) {
        return iter;
    }

    public int size() {
        return 0;
    }
}

public class AbstractSequentialListTest extends MultiCase {

    public static void main(String[] args) {
        System.exit(new AbstractSequentialListTest().test(args));
    }

    public Result testAbstractSequentialList() {
        AbstractSequentialList l = new MyAbstractSequentialList();

        if (l instanceof AbstractSequentialList) {
            return passed();
        } else {
            return failed("Wrong object is created: " + l.getClass().getName());
        }
    }

    public Result testIterator() {
        final Object elems[] = { "0", new Integer(25), "aaa",
                "string with spaces", new Object(),
                new MyAbstractSequentialList(), };

        AbstractSequentialList l = new MyAbstractSequentialList();
        Iterator i = l.iterator();
        for (int j = 0; j < elems.length; j++) {
            l.add(j, elems[j]);
        }

        int k = 0;
        while (i.hasNext()) {
            if (!i.next().equals(elems[k++])) {
                return failed("next returns wrong value: index=" + k
                        + "element=" + elems[k]);
            }
        }

        return passed();

    }
}