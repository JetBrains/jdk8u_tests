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
 * @author Denis M. Kishenko
 */
package java.awt.tests.geom;

import junit.framework.Assert;

import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

public class Arc2DDoubleTest extends GeomTestCase {

    Arc2D.Double a;

    public Arc2DDoubleTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        a = new Arc2D.Double(1, 2, 3, 4, 5, 6, Arc2D.PIE);
    }

    @Override
    protected void tearDown() throws Exception {
        a = null;
        super.tearDown();
    }

    public void testCreate1() {
        assertEquals(new Arc2D.Double(0, 0, 0, 0, 0, 0, Arc2D.OPEN), new Arc2D.Double());
    }

    public void testCreate2() {
        assertEquals(new Arc2D.Double(0, 0, 0, 0, 0, 0, Arc2D.CHORD), new Arc2D.Double(Arc2D.CHORD));
        
        // Regression for HARMONY-1403
        try {
            // Invalid type
            new Arc2D.Double(7);
            Assert.fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testCreate3() {
        assertEquals(new Arc2D.Double(1, 2, 3, 4, 5, 6, Arc2D.CHORD), new Arc2D.Double(new Rectangle(1, 2, 3, 4), 5, 6, Arc2D.PIE));
        
        // Regression for HARMONY-1403
        try {
            // Invalid type
            new Arc2D.Double(new Rectangle(1, 2, 3, 4), 5, 6, 4);
            Assert.fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testGetX() {
        Assert.assertEquals(1.0, a.getX(), 0.0);
    }

    public void testGetY() {
        Assert.assertEquals(2.0, a.getY(), 0.0);
    }

    public void testGetWidth() {
        Assert.assertEquals(3.0, a.getWidth(), 0.0);
    }

    public void testGetHeight() {
        Assert.assertEquals(4.0, a.getHeight(), 0.0);
    }

    public void testGetAngleStart() {
        Assert.assertEquals(5.0, a.getAngleStart(), 0.0);
    }

    public void testGetAngleExtent() {
        Assert.assertEquals(6.0, a.getAngleExtent(), 0.0);
    }

    public void testIsEmpty() {
        Assert.assertFalse(a.isEmpty());
        Assert.assertFalse(new Arc2D.Double(0, 0, 1, 1, 2, 3, Arc2D.CHORD).isEmpty());
        Assert.assertTrue(new Arc2D.Double(0, 0, 0, 1, 2, 3, Arc2D.CHORD).isEmpty());
        Assert.assertTrue(new Arc2D.Double(0, 0, 1, 0, 2, 3, Arc2D.CHORD).isEmpty());
        Assert.assertTrue(new Arc2D.Double(0, 0, 0, 0, 2, 3, Arc2D.CHORD).isEmpty());
        Assert.assertTrue(new Arc2D.Double(0, 0, -1, 0, 2, 3, Arc2D.CHORD).isEmpty());
        Assert.assertTrue(new Arc2D.Double(0, 0, 0, -1, 2, 3, Arc2D.CHORD).isEmpty());
        Assert.assertTrue(new Arc2D.Double(0, 0, -1, -1, 2, 3, Arc2D.CHORD).isEmpty());
    }

    public void testSetArc() {
        a.setArc(7, 8, 9, 10, 11, 12, Arc2D.CHORD);
        assertEquals(new Arc2D.Double(7, 8, 9, 10, 11, 12, Arc2D.CHORD), a);
    }

    public void testAngleStart() {
        a.setAngleStart(10);
        Assert.assertEquals(10.0, a.getAngleStart(), 0.0);
    }

    public void testAngleExtent() {
        a.setAngleExtent(11);
        Assert.assertEquals(11.0, a.getAngleExtent(), 0.0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(Arc2DDoubleTest.class);
    }

}
