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
 * @author Sergey Burlak
 */
package javax.swing;

import java.awt.Dimension;

import static javax.swing.ScrollPaneConstants.*;

public class ScrollPaneLayoutTest extends SwingTestCase {
    private ScrollPaneLayout layout;

    private JScrollPane pane;

    private JLabel label;

    @Override
    protected void setUp() throws Exception {
        label = new JLabel();
        label.setPreferredSize(new Dimension(500, 500));
        pane = new JScrollPane(label);
        layout = (ScrollPaneLayout) pane.getLayout();
    }

    @Override
    protected void tearDown() throws Exception {
        layout = null;
        pane = null;
        label = null;
    }

    public void testGetPreferredLayoutSize() throws Exception {
        JViewport colHead = new JViewport();
        colHead.setPreferredSize(new Dimension(100, 30));
        layout.addLayoutComponent(COLUMN_HEADER, colHead);

        JViewport rowHead = new JViewport();
        rowHead.setPreferredSize(new Dimension(50, 20));
        layout.addLayoutComponent(ROW_HEADER, rowHead);

        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setBorder(BorderFactory.createEmptyBorder(51, 101, 151, 202));
        pane.setViewportBorder(BorderFactory.createEmptyBorder(51, 101, 151, 202));
        int width = layout.getViewport().getPreferredSize().width
                + rowHead.getPreferredSize().width
                + (layout.getVerticalScrollBar() == null ? 0 : layout.getVerticalScrollBar().getBounds().width)
                + pane.getInsets().right + pane.getInsets().left + 101 + 202;
        int height = layout.getViewport().getPreferredSize().height
                + colHead.getPreferredSize().height
                + (layout.getHorizontalScrollBar() == null ? 0 : layout.getHorizontalScrollBar().getBounds().height)
                + pane.getInsets().top + pane.getInsets().bottom + 51 + 151;
        assertEquals(width, layout.preferredLayoutSize(pane).width);
        assertEquals(height, layout.preferredLayoutSize(pane).height);
        try {
            layout.preferredLayoutSize(new JButton());
            fail("Class cast exception shall be thrown");
        } catch (ClassCastException e) {
        }
        //regression for HARMONY-1735
        try {
        	layout.preferredLayoutSize(null);
        	fail("No expected exception");
        }catch (NullPointerException e) {
        //expected
        }
      }

    public void testDefaultLayout() throws Exception {
        ScrollPaneLayout l = new ScrollPaneLayout();
        //assertNull(l.colHead);
        //assertNull(l.lowerLeft);
        //assertNull(l.lowerRight);
        //assertNull(l.upperLeft);
        //assertNull(l.upperRight);
        assertEquals(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, l.getVerticalScrollBarPolicy());
        assertEquals(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED, l.getHorizontalScrollBarPolicy());
    }

    public void testSetHorizontalPolicy() throws Exception {
        assertEquals(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED, layout
                .getHorizontalScrollBarPolicy());
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        layout.syncWithScrollPane(pane);
        assertEquals(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS, layout
                .getHorizontalScrollBarPolicy());
        layout.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        assertEquals(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER, layout
                .getHorizontalScrollBarPolicy());
        // regression 1 for HARMONY-1737
        try{
            layout.setHorizontalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            fail("No expected IllegalArgumentException");
        }catch(IllegalArgumentException e){
         //expected 
        }
    }

    public void testSetVerticalPolicy() throws Exception {
        assertEquals(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, layout
                .getVerticalScrollBarPolicy());
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        layout.syncWithScrollPane(pane);
        assertEquals(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, layout
                .getVerticalScrollBarPolicy());
        layout.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        assertEquals(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, layout
                .getVerticalScrollBarPolicy());
        // regression 2 for HARMONY-1737
        try{
            layout.setVerticalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            fail("No expected IllegalArgumentException");
        }catch(IllegalArgumentException e){
        //expected 
        } 
    }

    public void _testGetViewport() throws Exception {
        assertEquals(layout.viewport, layout.getViewport());
        layout.viewport = null;
        assertNull(layout.getViewport());
    }

    public void _testGetHorizontalScrollbar() throws Exception {
        assertEquals(layout.hsb, layout.getHorizontalScrollBar());
        layout.hsb = null;
        assertNull(layout.getHorizontalScrollBar());
    }

    public void _testGetVerticalScrollbar() throws Exception {
        assertEquals(layout.vsb, layout.getVerticalScrollBar());
        layout.vsb = null;
        assertNull(layout.getVerticalScrollBar());
    }

    public void testGetCorner() throws Exception {
        JButton lowerLeftButton = new JButton();
        JButton upperLeftButton = new JButton();
        JButton lowerRightButton = new JButton();
        JButton upperRightButton = new JButton();
        layout.addLayoutComponent(LOWER_LEFT_CORNER, lowerLeftButton);
        layout.addLayoutComponent(UPPER_LEFT_CORNER, upperLeftButton);
        layout.addLayoutComponent(LOWER_RIGHT_CORNER, lowerRightButton);
        layout.addLayoutComponent(UPPER_RIGHT_CORNER, upperRightButton);
        assertEquals(lowerLeftButton, layout.getCorner(LOWER_LEFT_CORNER));
        assertEquals(upperLeftButton, layout.getCorner(UPPER_LEFT_CORNER));
        assertEquals(lowerRightButton, layout.getCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER));
        assertEquals(upperRightButton, layout.getCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER));
        assertNull(layout.getCorner("something"));
    }

    public void testGetRowHeader() throws Exception {
        assertNull(layout.getRowHeader());
        JViewport rowHead = new JViewport();
        layout.addLayoutComponent(ROW_HEADER, rowHead);
        assertEquals(rowHead, layout.getRowHeader());
    }

    public void testGetColumnHeader() throws Exception {
        assertNull(layout.getColumnHeader());
        JViewport colHead = new JViewport();
        layout.addLayoutComponent(COLUMN_HEADER, colHead);
        assertEquals(colHead, layout.getColumnHeader());
    }

    public void testSyncWithScrollPane() throws Exception {
        ScrollPaneLayout l = new ScrollPaneLayout();
        assertNull(l.getViewport());
        assertNull(l.getRowHeader());
        assertNull(l.getColumnHeader());
        assertNull(l.getCorner(LOWER_LEFT_CORNER));
        assertNull(l.getCorner(LOWER_RIGHT_CORNER));
        assertNull(l.getCorner(UPPER_LEFT_CORNER));
        assertNull(l.getCorner(UPPER_RIGHT_CORNER));
        assertNull(l.getHorizontalScrollBar());
        assertNull(l.getVerticalScrollBar());
        l.syncWithScrollPane(pane);
        assertEquals(pane.getViewport(), l.getViewport());
        assertEquals(pane.getRowHeader(), l.getRowHeader());
        assertEquals(pane.getColumnHeader(), l.getColumnHeader());
        assertEquals(pane.getHorizontalScrollBar(), l.getHorizontalScrollBar());
        assertEquals(pane.getVerticalScrollBar(), l.getVerticalScrollBar());
        assertEquals(pane.getCorner(LOWER_LEFT_CORNER), l.getCorner(LOWER_LEFT_CORNER));
        assertEquals(pane.getCorner(LOWER_RIGHT_CORNER), l.getCorner(LOWER_RIGHT_CORNER));
        assertEquals(pane.getCorner(UPPER_LEFT_CORNER), l.getCorner(UPPER_LEFT_CORNER));
        assertEquals(pane.getCorner(UPPER_RIGHT_CORNER), l.getCorner(UPPER_RIGHT_CORNER));
        assertEquals(pane.getHorizontalScrollBarPolicy(), l.getHorizontalScrollBarPolicy());
        assertEquals(pane.getVerticalScrollBarPolicy(), l.getVerticalScrollBarPolicy());
        try {
            layout.syncWithScrollPane(null);
            fail("NPE shall be thrown");
        } catch (NullPointerException e) {
        }
    }

    public void testGetViewportBorderBounds() throws Exception {
        assertEquals(pane.getViewportBorderBounds(), layout.getViewportBorderBounds(pane));
    }

    public void testRemoveLayoutComponent() throws Exception {
        assertNotNull(layout.getViewport());
        layout.removeLayoutComponent(layout.getViewport());
        assertNull(layout.getViewport());
        assertNotNull(layout.getVerticalScrollBar());
        layout.removeLayoutComponent(layout.getVerticalScrollBar());
        assertNull(layout.getVerticalScrollBar());
        assertNotNull(layout.getHorizontalScrollBar());
        layout.removeLayoutComponent(layout.getHorizontalScrollBar());
        assertNull(layout.getHorizontalScrollBar());

        JViewport rowHead = new JViewport();
        layout.addLayoutComponent(ROW_HEADER, rowHead);
        assertNotNull(layout.getRowHeader());
        layout.removeLayoutComponent(layout.getRowHeader());
        assertNull(layout.getRowHeader());

        JViewport colHead = new JViewport();
        layout.addLayoutComponent(COLUMN_HEADER, colHead);
        assertNotNull(layout.getColumnHeader());
        layout.removeLayoutComponent(layout.getColumnHeader());
        assertNull(layout.getColumnHeader());

        JButton lowerLeftButton = new JButton();
        layout.addLayoutComponent(LOWER_LEFT_CORNER, lowerLeftButton);
        assertNotNull(layout.getCorner(LOWER_LEFT_CORNER));
        layout.removeLayoutComponent(layout.getCorner(LOWER_LEFT_CORNER));
        assertNull(layout.getCorner(LOWER_LEFT_CORNER));

        JButton lowerRightButton = new JButton();
        layout.addLayoutComponent(LOWER_RIGHT_CORNER, lowerRightButton);
        assertNotNull(layout.getCorner(LOWER_RIGHT_CORNER));
        layout.removeLayoutComponent(layout.getCorner(LOWER_RIGHT_CORNER));
        assertNull(layout.getCorner(LOWER_RIGHT_CORNER));

        JButton upperLeftButton = new JButton();
        layout.addLayoutComponent(UPPER_LEFT_CORNER, upperLeftButton);
        assertNotNull(layout.getCorner(UPPER_LEFT_CORNER));
        layout.removeLayoutComponent(layout.getCorner(UPPER_LEFT_CORNER));
        assertNull(layout.getCorner(UPPER_LEFT_CORNER));

        JButton upperRightButton = new JButton();
        layout.addLayoutComponent(UPPER_RIGHT_CORNER, upperRightButton);
        assertNotNull(layout.getCorner(UPPER_RIGHT_CORNER));
        layout.removeLayoutComponent(layout.getCorner(UPPER_RIGHT_CORNER));
        assertNull(layout.getCorner(UPPER_RIGHT_CORNER));
    }

    public void _testAddSingletonLayoutComponent() throws Exception {
        JButton newButton = new JButton();
        JButton button = new JButton();
        pane.setCorner(LOWER_LEFT_CORNER, button);
        int componentCount = pane.getComponentCount();
        assertEquals(newButton, layout.addSingletonComponent(button, newButton));
        assertEquals(componentCount - 1, pane.getComponentCount());
        pane.setCorner(LOWER_LEFT_CORNER, button);
        componentCount = pane.getComponentCount();
        assertNull(layout.addSingletonComponent(button, null));
        assertEquals(componentCount - 1, pane.getComponentCount());
    }

    public void testMinimumLayoutSize() throws Exception {
        JViewport colHead = new JViewport();
        colHead.setPreferredSize(new Dimension(100, 30));
        layout.addLayoutComponent(COLUMN_HEADER, colHead);

        JViewport rowHead = new JViewport();
        rowHead.setPreferredSize(new Dimension(50, 20));
        layout.addLayoutComponent(ROW_HEADER, rowHead);

        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setBorder(BorderFactory.createEmptyBorder(51, 101, 151, 202));
        pane.setViewportBorder(BorderFactory.createEmptyBorder(51, 101, 151, 202));
        int width = layout.getViewport().getMinimumSize().width
                + layout.getRowHeader().getMinimumSize().width
                + pane.getVerticalScrollBar().getMinimumSize().width + pane.getInsets().right
                + pane.getInsets().left + 101 + 202;
        int height = layout.getViewport().getMinimumSize().height
                + layout.getColumnHeader().getMinimumSize().height
                + pane.getHorizontalScrollBar().getMinimumSize().height + pane.getInsets().top
                + pane.getInsets().bottom + 51 + 151;
        assertEquals(new Dimension(width, height), layout.minimumLayoutSize(pane));
    }
}
