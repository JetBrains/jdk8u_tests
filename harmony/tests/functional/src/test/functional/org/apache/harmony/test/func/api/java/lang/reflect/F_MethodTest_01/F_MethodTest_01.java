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
package org.apache.harmony.test.func.api.java.lang.reflect.F_MethodTest_01;

import java.lang.reflect.*;
import org.apache.harmony.test.func.api.java.lang.reflect.F_MethodTest_01.auxiliary.*;
import org.apache.harmony.test.func.api.java.lang.reflect.share.CounterForReflection;
import org.apache.harmony.test.func.share.ScenarioTest;

public class F_MethodTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_MethodTest_01().test(args));
    }

    public int test() {
        StringBuffer desc;
        String name;
        int modif;
        CounterForReflection counter = new CounterForReflection(20);
        TestObject obj = new TestObject();
        Method mth1[] = obj.getClass().getSuperclass().getDeclaredMethods();
        Method mth2[] = obj.getClass().getDeclaredMethods();
        Method mths[] = new Method[mth1.length + mth2.length];
        for (int i = 0; i < mth1.length; i++) {
            mths[i] = mth1[i];
        }
        for (int i = 0; i < mth2.length; i++) {
            mths[i + mth1.length] = mth2[i];
        }
        AccessibleObject.setAccessible(mths, true);

        int cnt[] = new int[mths.length];
        for (byte i = 0; i < mths.length; i++) {
            cnt[i] = 0;
        }

        for (byte i = 1; i < 50; i++) {
            for (byte j = 0; j < mths.length; j++) {
                name = mths[j].getName();
                if ((!Modifier.isAbstract(mths[j].getModifiers()))
                        && (!Modifier.isNative(mths[j].getModifiers()))
                        && (Math.round(Math.random()) == 0)) {
                    try {
                        Method mth = TestObject.class.getDeclaredMethod(name,
                                mths[j].getParameterTypes());
                        mth.setAccessible(true);
                        if (name.charAt(0) == 'm') {
                            switch (name.charAt(1)) {
                            case '1':
                                mth.invoke(obj, new Object[] {});
                                break;
                            case '2':
                                mth.invoke(obj, new Object[] { new String(
                                        "java.lang.Byte") });
                                break;
                            case '3':
                                mth.invoke(obj, new Object[] {});
                                break;
                            case '4':
                                mth.invoke(obj,
                                        new Object[] { new Object[] {} });
                                break;
                            case '5':
                                mth.invoke(obj, new Object[] {});
                                break;
                            case '6':
                                mth.invoke(obj,
                                        new Object[] { new Boolean(true) });
                                break;
                            }
                        } else {
                            mth.invoke(obj, new Object[] {});
                        }
                        counter.put(mth);
                        cnt[j]++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (byte i = 0; i < mths.length; i++) {
            modif = mths[i].getModifiers();
            desc = new StringBuffer();
            if (Modifier.isPublic(modif)) {
                desc.append("public ");
            } else if (Modifier.isProtected(modif)) {
                desc.append("protected ");
            } else if (Modifier.isPrivate(modif)) {
                desc.append("private ");
            }
            if (Modifier.isAbstract(modif)) {
                desc.append("abstract ");
            }
            if (Modifier.isStatic(modif)) {
                desc.append("static ");
            }
            if (Modifier.isFinal(modif)) {
                desc.append("final ");
            }
            if (Modifier.isSynchronized(modif)) {
                desc.append("synchronized ");
            }
            if (Modifier.isNative(modif)) {
                desc.append("native ");
            }
            if (Modifier.isStrict(modif)) {
                desc.append("strictfp ");
            }

            if (!desc.toString().trim().equals(Modifier.toString(modif).trim())) {
                return fail("modifiers aren't equal: " + desc.toString() + "!="
                        + Modifier.toString(modif));
            }

            if (mths[i].getReturnType().isArray()) {
                desc.append(mths[i].getReturnType().getComponentType()
                        .getName()
                        + "[] ");
            } else {
                desc.append(mths[i].getReturnType().getName() + " ");
            }

            desc.append(mths[i].getDeclaringClass().getName() + "."
                    + mths[i].getName() + "(");

            Class parArr[] = mths[i].getParameterTypes();
            for (int j = 0; j < parArr.length; j++) {
                if (j > 0) {
                    desc.append(',');
                }
                if (parArr[j].isArray()) {
                    desc.append(parArr[j].getComponentType().getName() + "[]");
                } else {
                    desc.append(parArr[j].getName());
                }
            }

            desc.append(')');

            Class excArr[] = mths[i].getExceptionTypes();
            for (int j = 0; j < excArr.length; j++) {
                if (j == 0) {
                    desc.append(" throws ");
                }
                if (j > 0) {
                    desc.append(", ");
                }
                desc.append(excArr[j].getName());
            }
            log.info("Field " + i + " of class "
                    + mths[i].getDeclaringClass().getName());
            log.info("generated description - " + desc.toString());
            log.info("toString description - " + mths[i].toString());
            log.info("Number: hash table - " + counter.get(mths[i])
                    + ", counter - " + cnt[i]);
            log.info("");

            if (counter.get(mths[i]) != cnt[i]) {
                return fail("Number of inctances isn't equal for field "
                        + mths[i].toString());
            }

            if (!mths[i].toString().trim().equals(desc.toString().trim())) {
                return fail("Description isn't equal for field: "
                        + mths[i].toString() + "!=" + desc.toString());
            }
        }
        return pass();
    }
}