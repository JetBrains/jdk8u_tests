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

package org.apache.harmony.test.func.api.java.util.ResourceBundle;

import java.util.ListResourceBundle;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 21, 2006
 */
public class SetParentTest extends MultiCase {

    class ChildBundle extends ListResourceBundle {

        protected Object[][] getContents() {
            return new Object[][] { { "CHILD_KEY", "Child Value" },
                    { "COMMON_KEY", "Common Child Value" } };
        }

        public void setParent(ResourceBundle parent) {
            super.setParent(parent);
        }
    }

    class ParentBundle extends ListResourceBundle {
        protected Object[][] getContents() {
            return new Object[][] { { "PARENT_KEY", "Parent Value" },
                    { "COMMON_KEY", "Common Parent Value" } };
        }
    }

    public Result testSetParent() {
        ChildBundle child = new ChildBundle();
        ParentBundle parent = new ParentBundle();

        child.setParent(parent);

        assertEquals(child.getObject("COMMON_KEY"), "Common Child Value");
        assertEquals(child.getObject("CHILD_KEY"), "Child Value");
        assertEquals(child.getObject("PARENT_KEY"), "Parent Value");

        assertEquals(parent.getObject("PARENT_KEY"), "Parent Value");
        assertEquals(parent.getObject("COMMON_KEY"), "Common Parent Value");

        try {
            parent.getObject("CHILD_KEY");
            assertTrue(false);
        } catch (MissingResourceException e) {

        } 
        
        return result();
    }
    
    public static void main(String[] args) {
        System.exit(new SetParentTest().test(args));
    }

}
