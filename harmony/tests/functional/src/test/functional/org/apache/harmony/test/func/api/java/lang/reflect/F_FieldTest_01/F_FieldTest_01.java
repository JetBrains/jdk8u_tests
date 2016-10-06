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
 * Created on 25.02.2005
 * Last modification G.Seryakova
 * Last modified on 25.02.2005
 * 
 * 
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_FieldTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.reflect.*;
import org.apache.harmony.test.func.api.java.lang.reflect.share.CounterForReflection;
import org.apache.harmony.test.func.api.java.lang.reflect.F_FieldTest_01.auxiliary.TestObject;

/**
 */
public class F_FieldTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_FieldTest_01().test(args));
    }

    public int test() {
        StringBuffer desc;
        String name;
        int modif;
        CounterForReflection counter = new CounterForReflection(20);
        TestObject obj = new TestObject();
        Field flds[] = obj.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(flds, true);

        int cnt[] = new int[flds.length];
        for (byte i = 0; i < flds.length; i++) {
            cnt[i] = 0;
        }

        for (byte i = 1; i < 10; i++) {
            for (byte j = 0; j < flds.length; j++) {
                if (Math.round(Math.random()) == 0) {
                    name = flds[j].getName();
                    try {
                        Field fld = obj.getClass().getDeclaredField(name);
                        fld.setAccessible(true);
                        if (name.equals("F")) {
                            if (fld.getInt(obj) != 1) {
                                return fail("field F != 1");
                            }
                        } else if (name.equals("c1")) {
                            if (fld.getChar(obj) != 'a') {
                                return fail("field c1 != a");
                            }
                        } else if (name.equals("c2")) {
                            if (fld.getInt(obj) != 2) {
                                return fail("field c2 != 2");
                            }
                        } else if (name.equals("f1")) {
                            fld.setInt(obj, flds[j].getInt(obj) + 1);
                        } else if (name.equals("f2")) {
                            fld.setBoolean(obj, !flds[j].getBoolean(obj));
                        } else if (name.equals("f3")) {
                            fld.setByte(obj,
                                    (byte) (flds[j].getByte(obj) + 1));
                        } else if (name.equals("f4")) {
                            fld.setChar(obj,
                                    (char) (flds[j].getChar(obj) + 1));
                        } else if (name.equals("f5")) {
                            fld.setDouble(obj, flds[j].getDouble(obj) + 1);
                        } else if (name.equals("f6")) {
                            fld.setFloat(obj, flds[j].getFloat(obj) + 1);
                        } else if (name.equals("f7")) {
                            fld.setLong(obj, flds[j].getLong(obj) + 1);
                        } else if (name.equals("f8")) {
                            fld.setShort(obj, (short) (flds[j]
                                    .getShort(obj) + 1));
                        } else if (name.equals("f9")) {
                            fld.set(obj, flds[j].get(obj));
                        }
                        counter.put(fld);
                        cnt[j]++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        for (byte i = 0; i < flds.length; i++) {
            modif = flds[i].getModifiers();
            desc = new StringBuffer();
            if (Modifier.isPublic(modif)) {
                desc.append("public ");
            } else if (Modifier.isProtected(modif)) {
                desc.append("protected ");
            } else if (Modifier.isPrivate(modif)) {
                desc.append("private ");
            }
            if (Modifier.isStatic(modif)) {
                desc.append("static ");
            }
            if (Modifier.isFinal(modif)) {
                desc.append("final ");
            }
            if (Modifier.isTransient(modif)) {
                desc.append("transient ");
            }
            if (Modifier.isVolatile(modif)) {
                desc.append("volatile ");
            }
            
            if (!desc.toString().trim().equals(Modifier.toString(modif).trim())) {
                return fail("modifiers aren't equal: " + desc.toString() + "!="
                        + Modifier.toString(modif));
            }

            desc.append(flds[i].getType().getName() + " ");
            desc.append(flds[i].getDeclaringClass().getName() + "." + flds[i].getName()
                    + " ");

            log.info("Field " + i + " of class "
                    + flds[i].getDeclaringClass().getName());
            log.info("generated description - " + desc.toString());
            log.info("toString description - " + flds[i].toString());
            log.info("Number: hash table - " + counter.get(flds[i])
                    + ", counter - " + cnt[i]);
            log.info("");

            if (counter.get(flds[i]) != cnt[i]) {
                return fail("Number of inctances isn't equal for field "
                        + flds[i].toString());
            }

            if (!flds[i].toString().trim().equals(desc.toString().trim())) {
                return fail("Description isn't equal for field: "
                        + flds[i].toString() + "!=" + desc.toString());
            }
        }

        return pass();
    }
}