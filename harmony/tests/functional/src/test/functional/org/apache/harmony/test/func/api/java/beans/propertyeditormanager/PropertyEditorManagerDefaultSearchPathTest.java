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
package org.apache.harmony.test.func.api.java.beans.propertyeditormanager;

import java.beans.PropertyEditorManager;

import org.apache.harmony.share.Test;

/**
 * Under test: PropertyEditorManager.
 * <p>
 * Verify that getEditorSearchPath() method returns one package.
 * 
 */
public class PropertyEditorManagerDefaultSearchPathTest extends Test {
    public static void main(String[] args) {
        System
            .exit(new PropertyEditorManagerDefaultSearchPathTest().test(args));
    }

    public int test() {
        String[] editorSearchPath = PropertyEditorManager.getEditorSearchPath();
        if (editorSearchPath.length != 1) {
            return fail("There are more than one packages in search path of PropertyEditorManager class");
        } else {
            return pass();
        }
    }
}