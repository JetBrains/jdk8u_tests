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
 * Purpose: verify that when a method of listener is invoked, a value of
 * property of target class is set to a value of event property.
 * <p>
 * Step-by-step encoding:
 * <ul>
 * <li>Create event for listener. Event class has reader and writer method of
 * integer property. Set default value of property.
 * <li>Create class which has a writer method of integer property.
 * <li>Invoke create(listener,class from previous item,name of property from
 * item#2,name of property from item#1) method.
 * <li>Verify that create(..) method returns instance of listener.
 * <li>Invoke implemented method of listener.
 * <li>Verify that property from item#2 was set to default value of property of
 * item#1.
 * 
 */
public class SettingPropertyTest extends Test {
    /**
     * See description for invokeByTypeCasting parameter in
     * {@link MakeInvocation#invoke(String, FredEvent, boolean)}method.
     */
    private static boolean invokeByTypeCasting = true;

    public static void main(String[] args) {
        if (args.length != 0) {
            invokeByTypeCasting = Boolean.valueOf(args[1]).booleanValue();
        }
        System.exit(new SettingPropertyTest().test(args));
    }

    public int test() {
        try {
            MakeInvocation makeInvocation = new MakeInvocation("property1", "i",
                null);
            makeInvocation.create();
            makeInvocation.invoke("fireFredEvent", new FredEvent("bean"),
                invokeByTypeCasting);
            if (makeInvocation.target.getProperty1() != FredEvent.getI()) {
                throw new Exception("doesn't invoke method on target");
            }
            return pass();
        } catch (Exception e) {
            e.printStackTrace();
            return fail(e.getMessage());
        }
    }
}