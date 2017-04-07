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
 * @author Dennis Ushakov
 */
package javax.swing.colorchooser;

import java.awt.Color;
import java.util.Arrays;
import javax.swing.BasicSwingTestCase;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class DefaultColorSelectionModelTest extends BasicSwingTestCase {
    ChangeController changeController;

    MyDefaultColorSelectionModel model;

    class MyDefaultColorSelectionModel extends DefaultColorSelectionModel {
        public ChangeEvent getChangeEvent() {
            return changeEvent;
        }

        public void setListenerList(EventListenerList value) {
            listenerList = value;
        }

        public void fireStateChanged() {
            super.fireStateChanged();
        }
    }

    @Override
    public void setUp() throws Exception {
        changeController = new ChangeController();
        model = new MyDefaultColorSelectionModel();
    }

    @Override
    public void tearDown() throws Exception {
        model = null;
        changeController = null;
    }

    public void testDefaultColorSelectionModel() {
        assertSame(Color.WHITE, model.getSelectedColor());
        assertNull(model.getChangeEvent());
        assertNotNull(model.getChangeListeners());
    }

    public void testGetSetSelectedColor() {
        model.addChangeListener(changeController);
        model.setSelectedColor(Color.GREEN);
        assertTrue(changeController.isChanged());
        assertSame(Color.GREEN, model.getSelectedColor());
        changeController.reset();
        model.setSelectedColor(Color.GREEN);
        assertFalse(changeController.isChanged());
        model.setSelectedColor(new Color(0, 255, 0));
        assertFalse(changeController.isChanged());
    }

    public void testAddGetRemoveChangeListeners() {
        model.addChangeListener(changeController);
        assertTrue(Arrays.asList(model.getChangeListeners()).contains(
                changeController));
        assertEquals(model.getChangeListeners(), model
                .getChangeListeners());
        model.removeChangeListener(changeController);
        assertFalse(Arrays.asList(model.getChangeListeners()).contains(
                changeController));
        //assertEquals(model.listenerList.getListeners(ChangeListener.class), model
        //        .getChangeListeners());
        model.setListenerList(null);
        testExceptionalCase(new NullPointerCase() {
            @Override
            public void exceptionalAction() throws Exception {
                model.addChangeListener(changeController);
            }
        });
        testExceptionalCase(new NullPointerCase() {
            @Override
            public void exceptionalAction() throws Exception {
                model.getChangeListeners();
            }
        });
    }

    public void testFireStateChanged() {
        model.addChangeListener(changeController);
        model.fireStateChanged();
        assertNotNull(model.getChangeEvent());
        assertTrue(changeController.isChanged());
    }
}
