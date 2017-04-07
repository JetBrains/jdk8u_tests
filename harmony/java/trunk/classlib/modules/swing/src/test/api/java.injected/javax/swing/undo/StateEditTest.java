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
package javax.swing.undo;

import java.util.Hashtable;
import javax.swing.SwingTestCase;

@SuppressWarnings("unchecked")
public class StateEditTest extends SwingTestCase {
    SimpleStateEdit se1;

    TestStateEdit se2;

    boolean bWasException;

    SimpleEditable obj;

    class SimpleEditable implements StateEditable {
        boolean wasCallStore = false;

        boolean wasCallRestore = false;

        Hashtable state = null;

        public void storeState(final Hashtable ht) {
            ht.put("store", "state");
            ht.put("into", "this table");
            wasCallStore = true;
            state = ht;
        }

        public void restoreState(final Hashtable ht) {
            wasCallRestore = true;
            state = ht;
        }
    }

    class TestStateEdit extends StateEdit {

        public TestStateEdit(final StateEditable s) {
            super(s);
        }

        public TestStateEdit(final StateEditable s, final String name) {
            super(s, name);
        }

        @Override
        protected void removeRedundantState() {
            super.removeRedundantState();
        }

        Hashtable<Object,Object> getPreState() {
            return preState;
        }

        Hashtable<Object,Object> getPostState() {
            return postState;
        }

        void setPostState(Hashtable<Object,Object> value) {
            postState = value;
        }

        String getUndoRedoName() {
            return undoRedoName;
        };

        public void init(StateEditable anObject, String name) {
            super.init(anObject, name);
        }

        StateEditable getObject() {
            return object;
        }
    }

    class SimpleStateEdit extends TestStateEdit  {
        private static final long serialVersionUID = 1L;

        boolean wasCallRemoveRedudant = false;

        Hashtable state1 = null;

        Hashtable state2 = null;

        void resetDbgInfo() {
            wasCallRemoveRedudant = false;
            state1 = null;
            state2 = null;
        }

        public SimpleStateEdit(final StateEditable s) {
            super(s);
        }

        public SimpleStateEdit(final StateEditable s, final String name) {
            super(s, name);
        }

        @Override
        protected void removeRedundantState() {
            wasCallRemoveRedudant = true;
            state1 = preState;
            state2 = postState;
            super.removeRedundantState();
        }
    }

    @Override
    protected void setUp() throws Exception {
        bWasException = false;
        obj = new SimpleEditable();
        se1 = new SimpleStateEdit(obj);
        se2 = new TestStateEdit(new SimpleEditable(), "presentationName");
        super.setUp();
    }

    public void testGetPresentationName() {
        assertNull(se1.getPresentationName());
        assertEquals("presentationName", se2.getPresentationName());
    }

    public void testUndo() {
        se1.getPreState().put("1", new Integer(1));
        se1.getPreState().put("2", new Integer(2));
        se1.setPostState(new Hashtable());
        se1.getPostState().put("3", new Integer(3));
        se1.getPostState().put("4", new Integer(4));
        Hashtable oldPreState = se1.getPreState();
        Hashtable oldPostState = se1.getPostState();
        se1.undo();
        assertTrue(obj.wasCallRestore);
        assertEquals(obj.state, se1.getPreState());
        assertEquals(oldPreState, se1.getPreState());
        assertEquals(oldPostState, se1.getPostState());
    }

    public void testRedo() {
        try {
            se1.redo();
        } catch (CannotRedoException e) {
            bWasException = true;
        }
        assertTrue("ExpectedException", bWasException);
        se1.undo();
        obj.wasCallRestore = false;
        se1.getPreState().put("1", new Integer(1));
        se1.getPreState().put("2", new Integer(2));
        se1.setPostState(new Hashtable());
        se1.getPostState().put("3", new Integer(3));
        se1.getPostState().put("4", new Integer(4));
        Hashtable oldPreState = se1.getPreState();
        Hashtable oldPostState = se1.getPostState();
        se1.redo();
        assertTrue(obj.wasCallRestore);
        assertEquals(se1.getPostState(), obj.state);
        assertEquals(oldPreState, se1.getPreState());
        assertEquals(oldPostState, se1.getPostState());
    }

    public void _testStateEditStateEditableString() {
    }

    public void _testStateEditStateEditable() {
    }

    public void testEnd() {
        SimpleStateEdit stEdit = (SimpleStateEdit) se1;
        se1.getPreState().put("1", new Integer(1));
        se1.getPreState().put("2", new Integer(2));
        se1.setPostState(new Hashtable());
        se1.getPostState().put("3", new Integer(3));
        se1.getPostState().put("4", new Integer(4));
        Hashtable oldPreState = se1.getPreState();
        stEdit.resetDbgInfo();
        se1.end();
        assertTrue(obj.wasCallStore);
        assertEquals(stEdit.state2, obj.state);
        assertEquals(stEdit.state1, oldPreState);
        assertEquals(stEdit.state2, obj.state);
        assertTrue(stEdit.wasCallRemoveRedudant);
    }

    public void testRemoveRedundantState() {
        assertNotNull(se1.getPreState());
        assertNull(se1.getPostState());
        assertEquals(2, se1.getPreState().size());
        se1.getPreState().remove("store");
        se1.getPreState().remove("into");
        se1.setPostState(new Hashtable());
        se1.getPreState().put("1", new Integer(1));
        se1.getPreState().put("2", new Integer(2));
        se1.getPreState().put("3", new Integer(3));
        se1.getPreState().put("4", new Integer(4));
        se1.getPreState().put("5", new Integer(5));
        se1.getPostState().put("1", new Integer(44));
        se1.getPostState().put("2x", new Integer(2));
        se1.getPostState().put("3x", new Integer(3));
        se1.getPostState().put("4x", new Integer(4));
        se1.getPostState().put("5", new Integer(5));
        se1.removeRedundantState();
        Hashtable preState = se1.getPreState();
        Hashtable postState = se1.getPostState();
        assertEquals(4, se1.getPreState().size());
        assertEquals(new Integer(1), preState.get("1"));
        assertEquals(new Integer(2), preState.get("2"));
        assertEquals(new Integer(3), preState.get("3"));
        assertEquals(new Integer(4), preState.get("4"));
        assertEquals(4, se1.getPostState().size());
        assertEquals(new Integer(44), postState.get("1"));
        assertEquals(new Integer(2), postState.get("2x"));
        assertEquals(new Integer(3), postState.get("3x"));
        assertEquals(new Integer(4), postState.get("4x"));
    }

    Hashtable getState(final StateEditable editable) {
        Hashtable ht = new Hashtable();
        editable.storeState(ht);
        return ht;
    }

    public void testInit() {
        SimpleEditable newObj = new SimpleEditable();
        obj.wasCallStore = false;
        obj.state = null;
        assertNull(se1.getUndoRedoName());
        assertEquals("presentationName", se2.getUndoRedoName());
        se1.init(newObj, "name");
        assertEquals(newObj, se1.getObject());
        assertEquals("name", se1.getPresentationName());
        assertTrue(newObj.wasCallStore);
        assertEquals(newObj.state, se1.getPreState());
        assertNull(se1.getPostState());
        assertEquals(getState(newObj), se1.getPreState());
        assertEquals("name", se1.getUndoRedoName());

        try { // Regression test for HARMONY-2536
            new StateEdit(null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try { // Regression test for HARMONY-2536
            new StateEdit(null, "str");
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Regression test for HARMONY-2844
    public void testInitNull() {
        TestStateEdit se = new TestStateEdit(new SimpleEditable());
        try {
            se.init(null, "test");
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    public void testConstants() {
        assertEquals("$Id: StateEdit.java,v 1.6 1997/10" + "/01 20:05:51 sandipc Exp $",
                StateEdit.RCSID);
        assertEquals("$Id: StateEditable.java,v 1.2 1997/09" + "/08 19:39:08 marklin Exp $",
                StateEditable.RCSID);
    }
}
