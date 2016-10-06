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

package org.apache.harmony.test.func.api.java.awt.Component;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.util.Locale;

import org.apache.harmony.test.func.api.java.awt.share.SimpleComponent;
import org.apache.harmony.share.Result;

public class ComponentOrientationTest extends org.apache.harmony.share.Test {

    private interface ComponentOrientationSetter {
        void set(Component c, ComponentOrientation co);
    }

    private class Set implements ComponentOrientationSetter {
        public void set(Component c, ComponentOrientation co) {
            c.setComponentOrientation(co);
        }
    }

    private class Apply implements ComponentOrientationSetter {
        public void set(Component c, ComponentOrientation co) {
            c.applyComponentOrientation(co);
        }
    }

    public int test(ComponentOrientationSetter set) {
        Component component = new SimpleComponent();
        ComponentOrientation cn = ComponentOrientation
                .getOrientation(Locale.JAPAN);
        set.set(component, cn);

        ComponentOrientation cs = component.getComponentOrientation();
        if (!cs.equals(cn)) {
            return fail("ComponentOrientation was not applied properly");
        }
        component.setLocale(Locale.UK);
        ComponentOrientation cu = component.getComponentOrientation();
        if (!(cu.isHorizontal() && cu.isLeftToRight())) {
            return fail("setLocale(Locale) hasn't caused "
                    + "ComponentOrientation to change");
        }
        return pass();
    }

    public static void main(String[] args) {
        ComponentOrientationTest test = new ComponentOrientationTest();
        System.exit(test.test());
    }

    public int test() {
        if (Result.FAIL != test(new Set())) {
            return test(new Apply());
        } else {
            return Result.FAIL;
        }
    }
}