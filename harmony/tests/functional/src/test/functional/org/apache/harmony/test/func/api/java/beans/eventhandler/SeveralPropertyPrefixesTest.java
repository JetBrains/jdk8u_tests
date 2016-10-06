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
package org.apache.harmony.test.func.api.java.beans.eventhandler;

import org.apache.harmony.share.Test;

/**
 * Under test: EventHadler.
 * <p>
 * Purpose: verify installation of property on target class when event property
 * name is qualified with several property prefixes.
 * <p>
 * Step-by-step encoding:
 * <ul>
 * <li>2.Create class which is type#1 class.
 * <li>3.Create type#2 class which has a reader and writer methods of type#1
 * property.
 * <li>4.Create type#3 class which has a reader and writer methods of type#2
 * property.
 * <li>5.Create event for listener. Event class has reader and writer method of
 * type#3 property. Set default value of property created in event class.
 * <li>6.Create class which has writer and reader methods of type#1 property.
 * <li>7.Invoke create(listener,class from item#5,name of property from
 * item#5,eventProperyName) method, where eventProperyName is qualified with 3
 * property prefixes, delimited with the "." symbol.
 * <li>8. Invoke implemented method of listener.
 * <li>9. Verify that property from item#5 was set.
 * 
 */
public class SeveralPropertyPrefixesTest extends Test {
    /**
     * See description for invokeByTypeCasting parameter in
     * {@link MakeInvocation#invoke(String, FredEvent, boolean)}method.
     */
    private static boolean invokeByTypeCasting = true;

    public static void main(String[] args) {
        if (args.length != 0) {
            invokeByTypeCasting = Boolean.valueOf(args[1]).booleanValue();
        }
        System.exit(new SeveralPropertyPrefixesTest().test(args));
    }

    public int test() {
        try {
            MakeInvocation makeInvocation = new MakeInvocation("classType1",
                "classType3.classType2.classType1", null);
            makeInvocation.create();
            makeInvocation.invoke("fireFredEvent", new FredEvent("bean"),
                invokeByTypeCasting);
            if (makeInvocation.target.getClassType1().i != FredEvent.getI()) {
                throw new Exception("doesn't invoke method on target");
            }
            return pass();
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e.getMessage());
        }
    }
}