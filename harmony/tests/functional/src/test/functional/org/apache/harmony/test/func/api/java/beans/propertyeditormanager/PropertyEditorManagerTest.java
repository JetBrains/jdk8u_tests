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

import org.apache.harmony.test.func.api.java.beans.propertyeditormanager.auxiliary.Sound2Editor;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: PropertyEditorManager, PropertyEditorSupport.
 * <p>
 * Purpose: Verify, that PropertyEditorManager class correctly locates a
 * property editor for any given type name.
 * <p>
 * "Create a editor" means create a class, which extends PropertyEditorSupport
 * class.
 * 
 */
public class PropertyEditorManagerTest extends MultiCase {
    private static final String[] defaultSearchPath = PropertyEditorManager
                                                        .getEditorSearchPath();

    public static void main(String[] args) {
        System.exit(new PropertyEditorManagerTest().test(args));
    }

    /**
     * Verify, that registerEditor method correctly registers an editor class.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a type and editor for this type.</li>
     * <li>Register the editor for this type by registerEditor method.</li>
     * <li>Verify, that findEditor method returns the editor class.</li>
     * </ul>
     */
    public Result testFindRegisteredEditor() {
        Class soundEditorClass = Sound2Editor.class;
        PropertyEditorManager.registerEditor(Sound.class, soundEditorClass);
        assertTrue(PropertyEditorManager.findEditor(Sound.class).getClass()
            .equals(soundEditorClass));
        return result();
    }

    /**
     * Verify, that findEditor method finds an editor class in package of a
     * type.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a type.</li>
     * <li>Create an editor class in package of type with name: name of type
     * plus "Editor".</li>
     * <li>Verify, that findEditor method returns the editor class.</li>
     * </ul>
     */
    public Result testFindImmediateEditor() {
        assertTrue(PropertyEditorManager.findEditor(Sound.class).getClass()
            .equals(SoundEditor.class));
        return result();
    }

    /**
     * Verify, that findEditor method finds an editor class in an editor search
     * path.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a type.</li>
     * <li>Create an editor class in not package of type, with name: name of
     * type plus "Editor".</li>
     * <li>Set editor search path so that it points to the package of editor.
     * </li>
     * <li>Verify, that findEditor method returns the editor class.</li>
     * </ul>
     */
    public Result testFindEditorInSearchPath() {
        String packageName = Sound2Editor.class.getPackage().getName();
        PropertyEditorManager.setEditorSearchPath(new String[] { packageName });
        assertTrue(PropertyEditorManager.findEditor(Sound2.class).getClass()
            .equals(Sound2Editor.class));
        return result();
    }

    /**
     * Verify, that PropertyEditorManager searches an editor in registered
     * editors at first.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a type.</li>
     * <li>Register the editor for given type by registerEditor method.</li>
     * <li>Create an editor class in package of type with name: name of type
     * plus "Editor".</li>
     * <li>Verify, that findEditor method returns registered editor.</li>
     * </ul>
     */
    public Result testVerifyThatFirstSearchInRegisteredEditors() {
        PropertyEditorManager.registerEditor(Sound.class, Sound2Editor.class);
        assertTrue(PropertyEditorManager.findEditor(Sound.class).getClass()
            .equals(Sound2Editor.class));
        return result();
    }

    /**
     * Verify, that PropertyEditorManager searches an editor in package of a
     * type at second. If this test and
     * testVerifyThatFirstSearchInRegisteredEditors test are passed, then
     * PropertyEditorManager searches an editor in an editor search path at
     * third.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a type.</li>
     * <li>Create an editor in not package of the type, with name: name of type
     * plus "Editor".</li>
     * <li>Set editor search path so that it points to the package of editor of
     * item#2.
     * <li>Create an editor in package of type with name: name of the type plus
     * "Editor".</li>
     * <li>Verify, that findEditor method returns the editor from package of
     * the type.</li>
     * </ul>
     */
    public Result testVerifyThatSecondSearchImmediateEditors() {
        PropertyEditorManager
            .setEditorSearchPath(new String[] { org.apache.harmony.test.func.api.java.beans.propertyeditormanager.auxiliary.SoundEditor.class
                .getClass().getPackage().getName() });
        assertTrue(PropertyEditorManager.findEditor(Sound.class).getClass()
            .equals(SoundEditor.class));
        return result();
    }

    /**
     * Verify, that registerEditor method removes registered editor.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a type.</li>
     * <li>Create an editor class in package, which is not package of the type.
     * </li>
     * <li>Register editor for this type by registerEditor method.</li>
     * <li>Remove editor for this type by registerEditor(type, null).</li>
     * <li>Verify, that findEditor method returns null.</li>
     * </ul>
     */
    public Result testRemoveRegisteredEditor() {
        PropertyEditorManager.registerEditor(Sound2.class, SoundEditor.class);
        PropertyEditorManager.registerEditor(Sound2.class, null);
        assertNull(PropertyEditorManager.findEditor(Sound2.class));
        return result();
    }

    protected void tearDown() throws Exception {
        // Deregister editors.
        PropertyEditorManager.registerEditor(Sound.class, null);
        PropertyEditorManager.registerEditor(Sound2.class, null);
        // Set default search path
        PropertyEditorManager.setEditorSearchPath(defaultSearchPath);
    }
}