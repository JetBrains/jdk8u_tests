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
 * @author Anton Avtamonov
 */
package javax.swing.plaf.basic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.SwingTestCase;
import javax.swing.text.JTextComponent;

public class BasicComboBoxEditorTest extends SwingTestCase {
    private BasicComboBoxEditor editor;

    public BasicComboBoxEditorTest(final String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        editor = new BasicComboBoxEditor();
    }

    @Override
    protected void tearDown() throws Exception {
        editor = null;
    }

    public void testBasicComboBoxEditor() throws Exception {
        assertNotNull(editor.getEditorComponent());
    }

    public void testAddRemoveActionListener() throws Exception {
        ActionController controller = new ActionController();
        assertEquals(0, ((JTextField )editor.getEditorComponent()).getActionListeners().length);
        editor.addActionListener(controller);
        assertEquals(1, ((JTextField )editor.getEditorComponent()).getActionListeners().length);
        editor.addActionListener(new ActionController());
        assertEquals(2, ((JTextField )editor.getEditorComponent()).getActionListeners().length);
        editor.removeActionListener(controller);
        assertEquals(1, ((JTextField )editor.getEditorComponent()).getActionListeners().length);
    }

    public void testGetEditorComponent() throws Exception {
        //assertNotNull(editor.getEditorComponent());
        //assertEquals(editor.editor, editor.getEditorComponent());
        assertTrue(editor.getEditorComponent() instanceof JTextField);
    }

    public void testGetSetItem() throws Exception {
        assertEquals("", editor.getItem());
        assertEquals("", ((JTextField )editor.getEditorComponent()).getText());
        Object item = "any";
        editor.setItem(item);
        assertEquals(item, editor.getItem());
        assertEquals(item, ((JTextField )editor.getEditorComponent()).getText());
        item = "another";
        ((JTextField )editor.getEditorComponent()).setText(item.toString());
        assertEquals(item, ((JTextField )editor.getEditorComponent()).getText());
        assertEquals(item, editor.getItem());
    }

    public void testSelectAll() throws Exception {
        assertNull(((JTextField )editor.getEditorComponent()).getSelectedText());
        editor.setItem("any");
        assertNull(((JTextField )editor.getEditorComponent()).getSelectedText());
        editor.selectAll();
        assertEquals("any", ((JTextField )editor.getEditorComponent()).getSelectedText());
    }

    private class ActionController implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
        }
    }
    
    /**
     * Regression test for HARMONY-2651 
     * */
    public void testRGetSetItem() {
        editor.selectAll();
        assertEquals("", editor.getItem());

        Object obj1 = new java.util.jar.Attributes.Name("AAAAAAAAAAAAAAA");
        String obj2 = "BBBBBBBBBBBBB";
        JTextComponent editorComponent = (JTextComponent)editor.getEditorComponent();
        editorComponent.setText(obj2);
        editor.setItem(obj1);
        assertSame(obj1, editor.getItem());
        assertEquals(obj1.toString(), editorComponent.getText());
        editor.setItem(null);
        assertEquals("", editor.getItem());
    }

    /**
     * Regression test for HARMONY-2651 
     * */
    public void testGetItem() {
        editor.selectAll();
        Object obj1 = new java.util.jar.Attributes.Name("AAAAAAAAAAAAAAA");
        Object obj2 = "BBBBBBBBBBBBBB";
        editor.setItem(obj1);
        JTextComponent editorComponent = (JTextComponent)editor.getEditorComponent();
        editorComponent.setText((String)obj2);
        assertNotSame(obj2, editor.getItem());
        assertEquals(obj2, editor.getItem());
    }

}
