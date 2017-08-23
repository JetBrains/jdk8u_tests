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
package javax.swing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InvocationEvent;

public class RepaintManagerTest extends BasicSwingTestCase {
    private Dimension dbMaxSize;

    public RepaintManagerTest(final String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        timeoutDelay = 10 * DEFAULT_TIMEOUT_DELAY;
        dbMaxSize = RepaintManager.currentManager((JComponent) null).getDoubleBufferMaximumSize();
    }

    @Override
    protected void tearDown() throws Exception {
        RepaintManager.currentManager((JComponent) null).setDoubleBufferMaximumSize(dbMaxSize);
        super.tearDown();
    }

    public void testCurrentManager() throws Exception {
        RepaintManager inst1 = RepaintManager.currentManager(new JButton());
        assertNotNull(inst1);
        RepaintManager inst2 = RepaintManager.currentManager(new Button());
        assertNotNull(inst2);
        RepaintManager inst3 = RepaintManager.currentManager((JComponent) null);
        assertNotNull(inst3);
        assertTrue(inst1 == inst2);
        assertTrue(inst2 == inst3);
    }

    public void testSetCurrentManager() throws Exception {
        RepaintManager newInst = new RepaintManager();
        RepaintManager.setCurrentManager(newInst);
        assertTrue(RepaintManager.currentManager((JComponent) null) == newInst);
        RepaintManager.setCurrentManager(null);
        assertFalse(RepaintManager.currentManager((JComponent) null) == newInst);
        assertNotNull(RepaintManager.currentManager((JComponent) null));
    }

    public void _testAddRemoveInvalidComponent() throws Exception {
        Frame f = new Frame();
        final JPanel rootPanel = new JPanel(new BorderLayout()) {
                private static final long serialVersionUID = 1L;
                
                @Override
                    public boolean isValidateRoot() {
                    return true;
                }
            };
        final JPanel controlled = new JPanel();
        f.add(rootPanel);
        rootPanel.add(controlled);
        
        assertFalse(controlled.isValid());
        assertFalse(rootPanel.isValid());
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    RepaintManager.currentManager((JComponent) null).addInvalidComponent(controlled);
                }
            });
        
        final Marker isValid = new Marker();
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    isValid.setOccurred(controlled.isValid());
                }
            });
        assertFalse(isValid.isOccurred());
        
        f.setVisible(true);
        waitForIdle();
        assertTrue(controlled.isValid());
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    RepaintManager.currentManager((JComponent) null).addInvalidComponent(controlled);
                }
            });
        assertTrue(controlled.isValid());
        assertTrue(rootPanel.isValid());
        
        isValid.reset();
        controlled.invalidate();
        waitForIdle();
        assertFalse(controlled.isValid());
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    RepaintManager.currentManager((JComponent) null).addInvalidComponent(controlled);
                }
            });
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    isValid.setOccurred(controlled.isValid());
                }
            });
        assertTrue(isValid.isOccurred());
        
        isValid.reset();
        controlled.invalidate();
        assertFalse(controlled.isValid());
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    RepaintManager.currentManager((JComponent) null).addInvalidComponent(controlled);
                    RepaintManager.currentManager((JComponent) null).removeInvalidComponent(controlled);
                }
            });
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    isValid.setOccurred(controlled.isValid());
                }
            });
        assertTrue(isValid.isOccurred());
        
        isValid.reset();
        controlled.invalidate();
        assertFalse(controlled.isValid());
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    RepaintManager.currentManager((JComponent) null).addInvalidComponent(controlled);
                    RepaintManager.currentManager((JComponent) null).removeInvalidComponent(rootPanel);
                }
            });
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    isValid.setOccurred(controlled.isValid());
                }
            });
        assertFalse(isValid.isOccurred());
        
        try { // Regression test for HARMONY-1725
            RepaintManager.currentManager((JComponent) null).addInvalidComponent(null);
        } catch (NullPointerException e) {
            fail("Unexpected NullPointerException is thrown");
        }
    }

    public void testValidateInvalidComponents() throws Exception {
        Frame f = new Frame();
        final JPanel rootPanel = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isValidateRoot() {
                return true;
            }
        };
        final JPanel controlled = new JPanel();
        f.add(rootPanel);
        rootPanel.add(controlled);

        f.setVisible(true);
        waitForIdle();
        assertTrue(controlled.isValid());
        controlled.invalidate();
        assertFalse(controlled.isValid());
        final Marker isValid = new Marker();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                RepaintManager.currentManager((JComponent) null).addInvalidComponent(controlled);
                RepaintManager.currentManager((JComponent) null).validateInvalidComponents();
                isValid.setOccurred(controlled.isValid());
            }
        });
        assertTrue(isValid.isOccurred());

        f.dispose();
        isValid.reset();
        controlled.invalidate();
        assertFalse(controlled.isValid());
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                RepaintManager.currentManager((JComponent) null).addInvalidComponent(controlled);
                RepaintManager.currentManager((JComponent) null).removeInvalidComponent(rootPanel);
                RepaintManager.currentManager((JComponent) null).validateInvalidComponents();
                isValid.setOccurred(controlled.isValid());
            }
        });
        assertFalse(isValid.isOccurred());
    }

    public void testIsDoubleBufferingEnabled() throws Exception {
        assertTrue(RepaintManager.currentManager((JComponent) null).isDoubleBufferingEnabled());
        RepaintManager.currentManager((JComponent) null).setDoubleBufferingEnabled(false);
        assertFalse(RepaintManager.currentManager((JComponent) null).isDoubleBufferingEnabled());
    }

    public void testGetDoubleBufferMaximumSize() throws Exception {
        assertEquals(Toolkit.getDefaultToolkit().getScreenSize(), RepaintManager
                .currentManager((JComponent) null).getDoubleBufferMaximumSize());
        Dimension bufferSize = new Dimension(100, 100);
        RepaintManager.currentManager((JComponent) null).setDoubleBufferMaximumSize(bufferSize);
        assertEquals(bufferSize, RepaintManager.currentManager((JComponent) null)
                .getDoubleBufferMaximumSize());
    }

    public void _testGetOffscreenBuffer() throws Exception {
        JPanel root = new JPanel();
        JFrame f = new JFrame();
        f.getContentPane().add(root);
        assertNull(RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10, 10));
        f.pack();
        Image offscreenImage = RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10,
                10);
        assertNotNull(offscreenImage);
        assertEquals(10, offscreenImage.getWidth(f));
        assertEquals(10, offscreenImage.getHeight(f));
        assertEquals(RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10, 10),
                RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10, 10));
        assertEquals(RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(f.getRootPane(),
                10, 10), RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10, 10));
        assertEquals(RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(f, 10, 10),
                RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10, 10));
        Image im10x10 = RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10, 10);
        Image im10x20 = RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10, 20);
        Image im20x10 = RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 20, 10);
        Image im20x20 = RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 20, 20);
        assertNotSame(im10x10, im10x20);
        assertNotSame(im10x20, im20x10);
        assertNotSame(im10x10, im20x10);
        assertNotSame(im10x20, im20x20);
        assertNotSame(im10x10, RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10,
                10));
        assertSame(im20x20, RepaintManager.currentManager((JComponent) null)
                .getOffscreenBuffer(root, 10, 10));
        assertSame(im20x20, RepaintManager.currentManager((JComponent) null)
                .getOffscreenBuffer(root, 20, 20));
        assertSame(im20x20, RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(f, 20, 20));
        assertSame(im20x20, RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(
                new JButton(), 20, 20));
        Image im30x20 = RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 30, 20);
        assertNotSame(im20x20, im30x20);
        assertSame(im30x20, RepaintManager.currentManager((JComponent) null)
                .getOffscreenBuffer(root, 20, 20));
        assertNull(RepaintManager.currentManager((JComponent) null)
                .getOffscreenBuffer(new JButton(), 50, 20));
        assertNotSame(im30x20, RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 20,
                20));
        offscreenImage = RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10000,
                10000);
        assertNotNull(offscreenImage);
        assertEquals(RepaintManager.currentManager((JComponent) null).getDoubleBufferMaximumSize().width,
                offscreenImage.getWidth(f));
        assertEquals(RepaintManager.currentManager((JComponent) null).getDoubleBufferMaximumSize().height,
                offscreenImage.getHeight(f));
        offscreenImage = RepaintManager.currentManager((JComponent) null).getOffscreenBuffer(root, 10000,
                10000);
        assertNotNull(offscreenImage);
        assertEquals(RepaintManager.currentManager((JComponent) null).getDoubleBufferMaximumSize().width,
                offscreenImage.getWidth(f));
        assertEquals(RepaintManager.currentManager((JComponent) null).getDoubleBufferMaximumSize().height,
                offscreenImage.getHeight(f));
        f.dispose();
    }

    public void _testGetVolatileOffscreenBuffer() throws Exception {
        JPanel root = new JPanel();
        JFrame f = new JFrame();
        f.getContentPane().add(root);
        f.pack();
        Image offscreenImage = RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(
                root, 400, 400);
        assertNotNull(offscreenImage);
        assertEquals(400, offscreenImage.getWidth(f));
        assertEquals(400, offscreenImage.getHeight(f));
        assertEquals(RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(root, 400,
                400), RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(root, 400,
                400));
        assertEquals(RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(
                f.getRootPane(), 400, 400), RepaintManager.currentManager((JComponent) null)
                .getVolatileOffscreenBuffer(root, 400, 400));
        assertEquals(RepaintManager.currentManager((JComponent) null)
                .getVolatileOffscreenBuffer(f, 400, 400), RepaintManager.currentManager((JComponent) null)
                .getVolatileOffscreenBuffer(root, 400, 400));
        Image im400x400 = RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(root,
                400, 400);
        Image im400x420 = RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(root,
                400, 420);
        Image im420x400 = RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(root,
                420, 400);
        Image im420x420 = RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(root,
                420, 420);
        assertNotSame(im400x400, im400x420);
        assertNotSame(im400x420, im420x400);
        assertNotSame(im400x420, im420x400);
        assertNotSame(im400x420, im420x420);
        assertNotSame(im400x400, RepaintManager.currentManager((JComponent) null)
                .getVolatileOffscreenBuffer(root, 400, 400));
        assertSame(im420x420, RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(
                root, 400, 400));
        assertSame(im420x420, RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(
                root, 420, 420));
        assertSame(im420x420, RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(f,
                420, 420));
        assertSame(im420x420, RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(
                new JButton(), 420, 420));
        Image im430x420 = RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(root,
                430, 420);
        assertNotSame(im420x420, im430x420);
        assertSame(im430x420, RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(
                root, 420, 420));
        assertSame(im430x420, RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(
                root, 420, 420));
        offscreenImage = RepaintManager.currentManager((JComponent) null).getVolatileOffscreenBuffer(root,
                10000, 10000);
        assertNotNull(offscreenImage);
        assertEquals(RepaintManager.currentManager((JComponent) null).getDoubleBufferMaximumSize().width,
                offscreenImage.getWidth(f));
        assertEquals(RepaintManager.currentManager((JComponent) null).getDoubleBufferMaximumSize().height,
                offscreenImage.getHeight(f));
        f.dispose();
    }

    private boolean checkRepaintEvent() {
        return EventQueue.getCurrentEvent() instanceof InvocationEvent;
    }

}
