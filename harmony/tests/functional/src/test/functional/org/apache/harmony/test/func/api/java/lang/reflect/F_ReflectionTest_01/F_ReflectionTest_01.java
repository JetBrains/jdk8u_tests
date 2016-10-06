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
 * Created on 1.12.2004
 * Last modification G.Seryakova
 * Last modified on 2.12.2004
 * 
 * Extract metadata of classes.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ReflectionTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.reflect.*;

/**
 * Extracting metadata of classes.
 * 
 */
public class F_ReflectionTest_01 extends ScenarioTest {
    private static String ls  = System.getProperty("line.separator");
    private static String tb1 = "    ";

    public static void main(String[] args) {
        System.exit(new F_ReflectionTest_01().test(args));
    }

    public int test() {
        Class cls;
        try {
            cls = Class.forName("java.lang.Class");
        } catch (ClassNotFoundException e) {
            return fail("ClassNotFoundException");
        }

        //        log.info(getClassDesc(void.class, ""));
        //        log.info(getClassDesc(int.class, ""));
        //        log.info(getClassDesc(Object[].class, ""));
        //        log.info(getClassDesc(cls, ""));

        getClassDesc(void.class, "");
        getClassDesc(int.class, "");
        getClassDesc(Object[].class, "");
        getClassDesc(cls, "");

        return pass();
    }

    private String getClassDesc(Class cls, String tb) {
        if (cls.isPrimitive()) {
            return extractName(cls.getName());
        }
        if (cls.isArray()) {
            return extractName(cls.getComponentType().getName()) + "[]";
        }

        StringBuffer result = new StringBuffer(1024);
        int modif = cls.getModifiers();

        result.append(getModifiersString(modif));

        if (cls.isInterface()) {
            result.append("interface ");
        } else {
            result.append("class ");
        }

        result.append(extractName(cls.getName()) + " extends ");

        result.append(extractName(cls.getSuperclass().getName()));

        Class intfArr[] = cls.getInterfaces();
        for (int i = 0; i < intfArr.length; i++) {
            if (i == 0) {
                result.append(" implements ");
            }
            if (i > 0) {
                result.append(", ");
            }
            result.append(extractName(intfArr[i].getName()));
        }
        result.append(" {" + ls);

        Field fldArr[] = cls.getDeclaredFields();
        for (int i = 0; i < fldArr.length; i++) {
            result.append(tb + tb1);
            result.append(getFieldDesc(fldArr[i]));
            result.append(ls);
        }

        Constructor cnstArr[] = cls.getDeclaredConstructors();
        for (int i = 0; i < cnstArr.length; i++) {
            if (i == 0) {
                result.append(ls);
            }
            result.append(tb + tb1);
            result.append(getConstrDesc(cnstArr[i]));
            result.append(ls);
        }

        Method mthdArr[] = cls.getDeclaredMethods();
        for (int i = 0; i < mthdArr.length; i++) {
            if (i == 0) {
                result.append(ls);
            }
            result.append(tb + tb1);
            result.append(getMethodDesc(mthdArr[i]));
            result.append(ls);
        }

        Class clsArr[] = cls.getDeclaredClasses();
        for (int i = 0; i < clsArr.length; i++) {
            if (i == 0) {
                result.append(ls);
            }
            result.append(tb + tb1);
            result.append(getClassDesc(clsArr[i], tb + tb1));
            result.append(ls);
        }

        result.append(tb + "}");

        return result.toString();
    }

    private String getConstrDesc(Constructor cnstr) {
        StringBuffer result = new StringBuffer(256);

        result.append(getModifiersString(cnstr.getModifiers()));
        result.append(extractName(cnstr.getName()) + "(");

        Class parArr[] = cnstr.getParameterTypes();
        for (int i = 0; i < parArr.length; i++) {
            if (i > 0) {
                result.append(';');
            }
            if (parArr[i].isArray()) {
                result.append(extractName(parArr[i].getComponentType()
                    .getName())
                    + "[]");
            } else {
                result.append(extractName(parArr[i].getName()));
            }
        }

        result.append(')');

        Class excArr[] = cnstr.getExceptionTypes();
        for (int i = 0; i < excArr.length; i++) {
            if (i == 0) {
                result.append(" throws ");
            }
            if (i > 0) {
                result.append(", ");
            }
            result.append(extractName(excArr[i].getName()));
        }
        result.append(" {}");

        return result.toString();
    }

    private String getMethodDesc(Method mthd) {
        StringBuffer result = new StringBuffer(256);

        result.append(getModifiersString(mthd.getModifiers()));

        Class retType = mthd.getReturnType();
        if (retType.isArray()) {
            result.append(extractName(retType.getComponentType().getName())
                + "[] ");
        } else {
            result.append(extractName(retType.getName()) + " ");
        }

        result.append(mthd.getName() + "(");

        Class parArr[] = mthd.getParameterTypes();
        for (int i = 0; i < parArr.length; i++) {
            if (i > 0) {
                result.append(';');
            }
            if (parArr[i].isArray()) {
                result.append(extractName(parArr[i].getComponentType()
                    .getName())
                    + "[]");
            } else {
                result.append(extractName(parArr[i].getName()));
            }
        }

        result.append(')');

        Class excArr[] = mthd.getExceptionTypes();
        for (int i = 0; i < excArr.length; i++) {
            if (i == 0) {
                result.append(" throws ");
            }
            if (i > 0) {
                result.append(", ");
            }
            result.append(extractName(excArr[i].getName()));
        }
        result.append(" {}");

        return result.toString();
    }

    private String getFieldDesc(Field fld) {
        StringBuffer result = new StringBuffer(256);
        int modif = fld.getModifiers();

        result.append(getModifiersString(modif));

        Class tp = fld.getType();
        if (tp.isArray()) {
            result.append(extractName(tp.getComponentType().getName()) + "[] ");
        } else {
            result.append(extractName(tp.getName()) + " ");
        }

        result.append(fld.getName() + ";");

        return result.toString();
    }

    private String extractName(String name) {
        String str = name.substring(name.lastIndexOf(".") + 1);
        return str.substring(str.lastIndexOf("$") + 1);
    }

    private String getModifiersString(int m) {
        StringBuffer result = new StringBuffer(50);
        if (Modifier.isPublic(m)) {
            result.append("public ");
        } else if (Modifier.isPrivate(m)) {
            result.append("private ");
        } else if (Modifier.isProtected(m)) {
            result.append("protected ");
        }

        if (Modifier.isStatic(m)) {
            result.append("static ");
        }
        if (Modifier.isAbstract(m)) {
            result.append("abstract ");
        }
        if (Modifier.isFinal(m)) {
            result.append("final ");
        }

        return result.toString();
    }
}