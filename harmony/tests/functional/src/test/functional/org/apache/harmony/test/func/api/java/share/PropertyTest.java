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
/**
 */

package org.apache.harmony.test.func.api.java.share;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Hashtable;

import org.apache.harmony.share.DRLLogging;

public class PropertyTest {

    public static class Data {

        public Object[] values;

        public Method get, set, verify;

        public String name;

        public Object defaultValue;

        private boolean isDefaultValueSet = false;

        private static DRLLogging log;

        public static void setLogger(DRLLogging logger) {
            log = logger;
        }

        private String capitalizeFirst(String s) {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }

        private static boolean objectEquals(Object a, Object b) {
            return a != null && b != null && a.equals(b);
        }

        private static Object[] toArray(Object obj) {
            if (obj == null) {
                return new Object[] { null };
            } else if (obj.getClass().isArray()) {
                return (Object[]) obj;
            } else {
                return new Object[] { obj };
            }

        }

        private static boolean arrayEquals(Object a, Object b) {
            Object[] v1;
            Object[] v2;

            // one single object should be packed to object[]
            // to be passed to Method.invoke
            // but array should not be packed - to aviod double packing
            // so the second parameter may be wrapped in the array
            // and the first one is not.
            if (a != null && a.getClass().isArray()) {
                v1 = new Object[] { a };
            } else {
                v1 = toArray(a);
            }

            v2 = toArray(b);
            for (int i = 0; i < v1.length && i < v2.length; i++) {
                if (!objectEquals(v1[i], v2[i])) {
                    return false;
                }
            }
            return true;
        }

        public boolean test(Object target) {

            // not all behaviours that could be considered wrong
            // should cause test result FAILED
            boolean testPassed = false;
            boolean testFailed = false;

            try {
                Object oldValue = get.invoke(target, null);

                if (isDefaultValueSet) {
                    if ((oldValue != null ^ defaultValue != null)
                            || (oldValue != null && !oldValue
                                    .equals(defaultValue))) {
                        testFailed = true;
                        log.info("Default value (" + defaultValue + ") != ("
                                + oldValue + ")");
                    }
                }

                if (set != null) {
                    set.invoke(target, values);

                    Object newValue = get.invoke(target, null);

                    if (!arrayEquals(oldValue, values)) {
                        if (arrayEquals(newValue, values)) {
                            testPassed = true;
                        } else {
                            log.info("Failure. Value was damaged");
                        }
                    } else {
                        log.info("Old and new values are the same value.");
                    }
                } else {
                    log.info("Property " + name + " lacks of setter");
                    testPassed = true;
                }
            } catch (Exception e) {
                // log.info("Exception: " + e.toString());
                e.printStackTrace();
            }

            if (!testPassed || testFailed) {
                try {
                    log.info("" + get.getName());
                } catch (Throwable e) {
                }
                try {
                    log.info("/" + set.getName());
                } catch (Throwable e) {
                }
                try {
                    log.info("/" + verify.getName());
                } catch (Throwable e) {
                }
                log.info("\n");
            }
            return testPassed;
        }

        public Data(Class trg, String name, Object newValue, Object defaultValue) {
            this(trg, name, newValue);
            this.defaultValue = defaultValue;
            isDefaultValueSet = true;
        }

        public Data(Class trg, String name, Object value) {
            this.name = name;
            values = toArray(value);
            name = capitalizeFirst(name);

            try {
                get = trg.getMethod("get" + name, null);
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            }

            if (get == null) {
                try {
                    get = trg.getMethod("is" + name, null);
                } catch (SecurityException e) {
                } catch (NoSuchMethodException e) {
                }
            }

            try {
                Class valueClass;
                if (value.getClass().equals(Boolean.class)) {
                    valueClass = boolean.class;
                } else if (value.getClass().equals(Long.class)) {
                    valueClass = long.class;
                } else if (value.getClass().equals(Integer.class)) {
                    valueClass = int.class;
                } else {
                    valueClass = value.getClass();
                }
                set = trg.getMethod("set" + name, new Class[] { valueClass });
            } catch (SecurityException e1) {
            } catch (NoSuchMethodException e1) {
                Method[] methods = trg.getMethods();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equals("set" + name)) {
                        if (methods[i].getParameterTypes()[0]
                                .isAssignableFrom(value.getClass())) {
                            set = methods[i];
                            break;
                        }
                    }
                }
            } catch (Throwable e) {
            }

            try {
                verify = trg.getMethod("is" + name + "Set", null);
            } catch (SecurityException e2) {
            } catch (NoSuchMethodException e2) {
            }

        }
    }

    /**
     * Used for print out code to ease testing simple properties i.e. properties
     * that do not noticeably affect object behaviour 06.09.2005
     */
    public static class CodeGen {
        private class Property {
            public String name;

            public Method get, set, isSet;

            public Property merge(Property source) {

                if (source != null) {

                    if (!this.name.equals(source.name)) {
                        throw new InvalidParameterException(
                                "Only properties with same "
                                        + "name could be merged. \n\""
                                        + this.name + "\" != \"" + source.name);
                    }

                    if (this.get == null) {
                        this.get = source.get;
                    }
                    if (this.set == null) {
                        this.set = source.set;
                    }
                    if (this.isSet == null) {
                        this.isSet = source.isSet;
                    }
                }
                return this;
            }
        }

        private Property getProperty(Method m) {
            Property p = new Property();
            String title = m.getName();

            if (title.startsWith("is")) {
                if (title.endsWith("Set")) {
                    p.name = title.substring(2, title.length() - 3);
                    p.isSet = m;
                } else {
                    p.name = title.substring(2, title.length());
                    p.get = m;
                }
            } else if (title.startsWith("get")) {
                p.name = title.substring(3);
                p.get = m;
            } else if (title.startsWith("set")) {
                p.name = title.substring(3);
                p.set = m;
            }

            return p.name != null ? p : null;
        }

        private Property[] getProperties(Class cls) {
            Method[] methods = cls.getMethods();

            Hashtable properties = new Hashtable(0);
            for (int i = 0; i < methods.length; i++) {
                Property p = getProperty(methods[i]);
                if (p != null) {
                    properties.put(p.name, p.merge((Property) properties
                            .get(p.name)));
                }
            }
            Object[] raw = properties.values().toArray();
            Property[] result = new Property[raw.length];

            for (int i = 0; i < raw.length; i++) {
                result[i] = (Property) raw[i];
            }

            return result;
        }

        private String objectToCodeString(Object o) {
            return objectToCodeString(o, o.getClass());
        }

        private String objectToCodeString(Object value, Class valueType) {
            if (value == null)
                return "(null)";
            if (valueType.isPrimitive()) {
                if (valueType.equals(byte.class))
                    return "(new Byte(" + value.toString() + "))";
                else if (valueType.equals(int.class))
                    return "(new Integer(" + value.toString() + "))";
                else if (valueType.equals(char.class))
                    return "(new Character('"
                            + value.toString().replaceAll("'", "\\'") + "'))";
                else if (valueType.equals(boolean.class))
                    return "(new Boolean("
                            + (((Boolean) value).booleanValue() ? "true"
                                    : "false") + "))";
                else if (valueType.equals(double.class))
                    return "(new Double(" + value.toString() + "))";
                else if (valueType.equals(float.class))
                    return "(new Float(" + value.toString() + "))";
                else if (valueType.equals(short.class))
                    return "(new Short(" + value.toString() + "))";
                else if (valueType.equals(long.class))
                    return "(new Long(" + value.toString() + "))";

            } else if (valueType.equals(String.class))
                return "\"" + ((String) value).replaceAll("\"", "\\\"") + "\"";

            return value.toString();

        }

        private void printProperty(Property p, Object obj, PrintStream out) {
            out.print("new PropertyTest.Data(");
            out.print("cls, ");
            out.print(objectToCodeString(p.name));
            out.print(", (NULL)");

            if (p.get != null) {
                try {
                    out.print(", "
                            + objectToCodeString(p.get.invoke(obj, null), p.get
                                    .getReturnType()));
                } catch (Exception e) {
                }
            }
            out.print("),\n");
        }

        public void printCode(Object obj, PrintStream out) {
            Class cls = obj.getClass();

            Property[] properties = getProperties(cls);

            for (int i = 0; i < properties.length; i++) {
                printProperty(properties[i], obj, out);
            }
        }
    }
}