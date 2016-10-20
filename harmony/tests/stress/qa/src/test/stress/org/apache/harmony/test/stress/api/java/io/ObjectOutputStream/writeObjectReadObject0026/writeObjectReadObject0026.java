/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/** 
 * @author Anton Luht
 * @version $Revision: 1.2 $
 */  
package org.apache.harmony.test.stress.api.java.io.ObjectOutputStream.writeObjectReadObject0026;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.harmony.test.stress.api.java.io.ObjectOutputStream.share.SerializationTestFramework;


class Spaghetti implements Serializable {
    Set set = new HashSet();

    Spaghetti s1, s2;
    {
        s1 = s2 = this;
    }
}

public class writeObjectReadObject0026 extends SerializationTestFramework {
    public static final int SPAGHETTI_NUM = 100;

    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0026().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof Spaghetti[])) {
            return fail("expected another value");
        }

        Spaghetti[] sa = (Spaghetti[]) o;

        if (sa.length != SPAGHETTI_NUM) {
            return fail("wrong number of objects");
        }

        for (int i = 0; i < SPAGHETTI_NUM; i++) {
            Integer ii = new Integer(i);
            if (!(sa[i].s1.s2.s1.set.contains(ii)
                    && sa[i].s2.s2.s1.s1.s1.set.contains(ii)
                    && sa[i].s1.set.contains(ii) && sa[i].s2.s1.s2.s1.set
                    .contains(ii))) {
                return fail("wrong values restored");
            }
        }

        return pass();
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        Spaghetti[] sa = new Spaghetti[SPAGHETTI_NUM];

        //fill array with empty objects
        for (int i = 0; i < SPAGHETTI_NUM; i++) {
            sa[i] = new Spaghetti();
        }

        //mess it up
        for (int i = 0; i < SPAGHETTI_NUM; i++) {
            sa[i].s1 = sa[getRandom().nextInt(SPAGHETTI_NUM)];
            sa[i].s2 = sa[getRandom().nextInt(SPAGHETTI_NUM)];
        }

        //fill values
        for (int i = 0; i < SPAGHETTI_NUM; i++) {
            Integer ii = new Integer(i);
            sa[i].s1.s2.s1.set.add(ii);
            sa[i].s2.s2.s1.s1.s1.set.add(ii);
            sa[i].s1.set.add(ii);
            sa[i].s2.s1.s2.s1.set.add(ii);
        }

        waitAtBarrier();
        oos.writeObject(sa);

        return pass();
    }
}

