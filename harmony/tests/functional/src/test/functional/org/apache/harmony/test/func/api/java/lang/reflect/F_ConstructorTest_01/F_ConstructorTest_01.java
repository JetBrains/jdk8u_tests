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
/*
 * Created on 24.02.2005
 * Last modification G.Seryakova
 * Last modified on 24.02.2005
 * 
 * 
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ConstructorTest_01;

import java.lang.reflect.*;
import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.test.func.api.java.lang.reflect.share.CounterForReflection;
import org.apache.harmony.test.func.api.java.lang.reflect.F_ConstructorTest_01.auxiliary.*;

/**
 */
public class F_ConstructorTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ConstructorTest_01().test(args));
    }

    public int test() {
        StringBuffer desc;
        int modif;
        CounterForReflection counter = new CounterForReflection(10);
        Constructor constr[] = TestObject.class.getDeclaredConstructors();
        AccessibleObject.setAccessible(constr, true);

        int cnt[] = new int[constr.length];
        for (byte i = 0; i < constr.length; i++) {
            cnt[i] = 0;
        }

        for (byte j = 0; j < constr.length; j++) {
            modif = constr[j].getModifiers();
            try {
                if (Modifier.isPublic(modif)) {
                    constr[j].newInstance(new Object[] {});
                } else if (Modifier.isProtected(modif)) {
                    constr[j].newInstance(new Object[] { new Integer(0) });
                } else if (Modifier.isPrivate(modif)) {
                    constr[j].newInstance(new Object[] { new Short("0"),
                            new Short(j) });
                } else {
                    constr[j].newInstance(new Object[] { new Byte(j),
                            new Long(0) });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            counter.put(constr[j]);
            cnt[j]++;
        }

        for (byte i = 1; i < 50; i++) {
            for (byte j = 0; j < constr.length; j++) {
                if (Math.round(Math.random()) == 0) {
                    modif = constr[j].getModifiers();
                    try {
                        Constructor cnst = TestObject.class
                                .getDeclaredConstructor(constr[j]
                                        .getParameterTypes());
                        cnst.setAccessible(true);
                        if (Modifier.isPublic(modif)) {
                            cnst.newInstance(new Object[] {});
                        } else if (Modifier.isProtected(modif)) {
                            cnst.newInstance(new Object[] { new Integer(i) });
                        } else if (Modifier.isPrivate(modif)) {
                            cnst.newInstance(new Object[] { new Short(i),
                                    new Short(j) });
                        } else {
                            cnst.newInstance(new Object[] { new Byte(j),
                                    new Long(i) });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    counter.put(constr[j]);
                    cnt[j]++;
                }
            }
        }

        for (byte i = 0; i < constr.length; i++) {
            desc = new StringBuffer(Modifier.toString(constr[i].getModifiers()));
            desc.append(" " + constr[i].getName() + "(");

            Class parArr[] = constr[i].getParameterTypes();
            for (int j = 0; j < parArr.length; j++) {
                if (j > 0) {
                    desc.append(',');
                }
                desc.append(parArr[j].getName());
            }

            desc.append(')');

            Class excArr[] = constr[i].getExceptionTypes();
            for (int j = 0; j < excArr.length; j++) {
                if (j == 0) {
                    desc.append(" throws ");
                }
                if (j > 0) {
                    desc.append(", ");
                }
                desc.append(excArr[j].getName());
            }

            log.info("Constructor " + i + " of class "
                    + constr[i].getDeclaringClass().getName());
            log.info("generated description - " + desc.toString());
            log.info("toString description - " + constr[i].toString());
            log.info("Number: hash table - " + counter.get(constr[i])
                    + ", counter - " + cnt[i]);
            log.info("");

            if (counter.get(constr[i]) != cnt[i]) {
                return fail("Number of inctances isn't equal for constructor "
                        + constr[i].toString());
            }

            if (!constr[i].toString().trim().equals(desc.toString().trim())) {
                return fail("Description isn't equal for constructor: "
                        + constr[i].toString() + "!=" + desc.toString());
            }
        }

        return pass();
    }
}