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
package org.apache.harmony.test.func.api.java.text.share.util;

 public class BoxedValue {
     public Object value;

     public Class valueType;

     public BoxedValue() {
     }

     public boolean equals(Object arg0) {
         if (arg0 instanceof BoxedValue) {
             BoxedValue bv = (BoxedValue) arg0;
             return value.equals(bv.value) && valueType.equals(bv.valueType);
         } else {
             return value.equals(arg0);
         }
     }

     public String getValueConstructor() {
         if (value == null)
             return null;
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

     public String toJavaConstructor() {
         return "new BoxedValue(" + this.getValueConstructor() + ", "
                 + valueType.getName() + ".class)";
     }

     public BoxedValue(Object value, Class valueType) {
         this.value = value;
         this.valueType = valueType;
     }

     public BoxedValue(int v) {
         this.value = new Integer(v);
         this.valueType = int.class;
     }

     public BoxedValue(char v) {
         this.value = new Character(v);
         this.valueType = char.class;
     }

     public BoxedValue(boolean v) {
         this.value = new Boolean(v);
         this.valueType = boolean.class;
     }

     public BoxedValue(long v) {
         this.value = new Long(v);
         this.valueType = long.class;
     }

     public BoxedValue(short v) {
         this.value = new Short(v);
         this.valueType = short.class;
     }

     public BoxedValue(double v) {
         this.value = new Double(v);
         this.valueType = double.class;
     }

     public BoxedValue(float v) {
         this.value = new Float(v);
         this.valueType = float.class;
     }

     public BoxedValue(byte v) {
         this.value = new Byte(v);
         this.valueType = int.class;
     }

     public BoxedValue(Object v) {
         this.value = v;
         this.valueType = v.getClass();
     }
     
    public String toString() {
        return value.toString();
    }

 }


