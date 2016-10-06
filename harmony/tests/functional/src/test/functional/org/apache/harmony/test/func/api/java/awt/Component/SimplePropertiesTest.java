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


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.lang.reflect.Method;

import org.apache.harmony.test.func.api.java.awt.share.SimpleComponent;
import org.apache.harmony.test.func.api.java.share.PropertyTest;
import org.apache.harmony.share.Test;


public class SimplePropertiesTest extends Test {

    public int test() {

        Color clr = new Color((int) 0xff7f3f1f);
        Cursor cur = new Cursor(Cursor.CROSSHAIR_CURSOR);
        Font fnt = new Font("Arial", 0, 14);
        String str = "Some string";
        Component cmp = new SimpleComponent();
        Class cls = Component.class;

        PropertyTest.Data[] simple = {
                new PropertyTest.Data(cls, "Background", clr),
                new PropertyTest.Data(cls, "Cursor", cur),
                new PropertyTest.Data(cls, "Enabled", Boolean.FALSE),
                new PropertyTest.Data(cls, "Focusable", Boolean.FALSE),
                new PropertyTest.Data(cls, "FocusTraversalKeysEnabled",
                        Boolean.FALSE), new PropertyTest.Data(cls, "Font", fnt),
                new PropertyTest.Data(cls, "Foreground", clr),
                new PropertyTest.Data(cls, "IgnoreRepaint", Boolean.TRUE),
                new PropertyTest.Data(cls, "Name", str),
                new PropertyTest.Data(cls, "Visible", Boolean.FALSE),

        };

        int failCount = 0;
        for (int i = 0; i < simple.length; i++) {
            if (!simple[i].test(cmp))
            {
                this.error(simple[i].name + " property have invalid behaviour");
                failCount++;
            }
        }
        if (failCount > 0) {
            return fail(failCount + " properties failed");
        }
        else
        {
            return pass();
        }
        
    }

    public static void main(String[] args) {
        PropertyTest.Data.setLogger(log);
        System.exit(new SimplePropertiesTest().test());
    }
}