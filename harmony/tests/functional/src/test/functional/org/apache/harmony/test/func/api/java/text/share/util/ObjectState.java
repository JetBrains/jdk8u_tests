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

   public class ObjectState {
       public Object object;
       
       public InvokeData[] testedMethods;

       public BoxedValue[] currentState;

       public boolean verifyState(Class objectType) throws Throwable {
           for (int i = 0; i < testedMethods.length; i++) {
               Object result = testedMethods[i].getResult(object, objectType);
               if (!result.equals(currentState[i]))
               {
                   throw new Throwable (
                           testedMethods[i].method.methodName + " returns incorrect value\n"
                           +"Expected:'" + currentState[i].toString() 
                           + "'\nReturned:'" + result.toString() +"'");
               }
           }
           return true;
       }

       public void setState(Class objectType) {
           currentState = new BoxedValue[testedMethods.length];
           for (int i = 0; i < testedMethods.length; i++) {
               currentState[i] = testedMethods[i].getResult(object, objectType);
           }
       }

       public String toJavaConstructor(String objectName) {
           String invokeData = "new InvokeData[] {";
           for (int i = 0; i < testedMethods.length; i++) {
               invokeData += testedMethods[i].toJavaConstructor();
               if (i < testedMethods.length - 1)
                   invokeData += ", ";
           }
           invokeData += "}";

           String state = "new BoxedValue[] {";
           if (currentState != null) {
               for (int i = 0; i < currentState.length; i++) {
                   state += currentState[i].toJavaConstructor();
                   if (i < currentState.length - 1)
                       state += ", ";
               }
               state += "}";
           } else {
               state = "null";
           }
           Object obj = null;
           obj = new ObjectState(obj,
                   new InvokeData[] {
                           new InvokeData(new MethodSignature("isBoundary",
                                   new Class[] { int.class }), null),
                           new InvokeData(new MethodSignature("preceding",
                                   new Class[] { int.class }), null),
                           new InvokeData(new MethodSignature("next",
                                   new Class[] { int.class }), null),
                           new InvokeData(new MethodSignature("following",
                                   new Class[] { int.class }), null),
                           new InvokeData(new MethodSignature("previous",
                                   new Class[] {}), null),
                           new InvokeData(new MethodSignature("next",
                                   new Class[] {}), null),
                           new InvokeData(new MethodSignature("last",
                                   new Class[] {}), null),
                           new InvokeData(new MethodSignature("first",
                                   new Class[] {}), null),
                           new InvokeData(new MethodSignature("current",
                                   new Class[] {}), null) }, null);

           return "new ObjectState(" + objectName + ", " + invokeData + ", "
                   + state + ")";

       }

       public ObjectState(Object object, InvokeData[] testedMethods,
               BoxedValue[] currentState) {
           this.object = object;
           this.testedMethods = testedMethods;
           this.currentState = currentState;
       }

       public ObjectState() {
       }
   }
