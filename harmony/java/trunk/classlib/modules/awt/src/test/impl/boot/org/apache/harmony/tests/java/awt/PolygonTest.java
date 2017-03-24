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
package org.apache.harmony.tests.java.awt;

import junit.framework.Assert;

import java.awt.*;

import org.apache.harmony.tests.java.awt.geom.ShapeTestCase;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

public class PolygonTest extends ShapeTestCase {

    static { 
        SerializeTestCase.SERIALIZATION_TEST = true;
    }
    
    public PolygonTest(String name) {
        super(name);
//        filterImage = createFilter("^(polygon).*([.]ico)$", "(.*)((affine)|(flat)|(bounds))(.*)");
    }

    public void testCreate1() {
        Polygon pg = new Polygon();
        Assert.assertEquals(0, pg.npoints);
        Assert.assertNotNull(pg.xpoints);
        Assert.assertNotNull(pg.ypoints);
    }

    public void testCreate2() {
        int[] x = new int[]{1, 2, 3, 4, 5, 6};
        int[] y = new int[]{7, 8, 9, 10, 11, 12};
        
        Polygon pg = new Polygon(x, y, 0);
        Assert.assertEquals(0, pg.npoints);
        Assert.assertEquals(0, pg.xpoints.length);
        Assert.assertEquals(0, pg.ypoints.length);

        pg = new Polygon(x, y, 3);
        Assert.assertEquals(3, pg.npoints);
        assertEquals(new int[]{1, 2, 3}, pg.xpoints, 3);
        assertEquals(new int[]{7, 8, 9}, pg.ypoints, 3);
        Assert.assertEquals(3, pg.xpoints.length);
        Assert.assertEquals(3, pg.ypoints.length);
        
        // Regression for HARMONY-1445
        x[1] = 99;
        y[2] = 77;
        Assert.assertTrue(pg.xpoints[1] != 99);
        Assert.assertTrue(pg.ypoints[2] != 77);
    }

    public void testCreate3() {
        try {
            new Polygon(null, new int[]{1, 2}, 2);
            Assert.fail("Expected exception NullPointerException");
        } catch(NullPointerException e) {
        }

        try {
            new Polygon(new int[]{1, 2}, null, 2);
            Assert.fail("Expected exception NullPointerException");
        } catch(NullPointerException e) {
        }

        try {
            new Polygon(new int[]{1, 2}, new int[]{1, 2, 4, 5}, 3);
            Assert.fail("Expected exception IndexOutOfBoundsException");
        } catch(IndexOutOfBoundsException e) {
        }

        try {
            new Polygon(new int[]{1, 2}, new int[]{1, 2, 4, 5}, 5);
            Assert.fail("Expected exception IndexOutOfBoundsException");
        } catch(IndexOutOfBoundsException e) {
        }

        try {
            new Polygon(new int[]{1, 2}, new int[]{1, 2, 4, 5}, -1);
            Assert.fail("Expected exception NegativeArraySizeException");
        } catch(NegativeArraySizeException e) {
        }
    }

    public void testReset() {
        Polygon pg = new Polygon(new int[]{1, 2, 3, 4, 5, 6}, new int[]{7, 8, 9, 10, 11, 12}, 3);
        Assert.assertTrue(pg.npoints > 0);
        pg.reset();
        Assert.assertTrue(pg.npoints == 0);
    }

    public void testInvalidate() {
        Polygon pg = new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3);
        Assert.assertEquals(new Rectangle(1, 4, 2, 2), pg.getBounds());
        pg.xpoints[0] = 0;
        pg.ypoints[0] = -1;
        Assert.assertEquals(new Rectangle(1, 4, 2, 2), pg.getBounds());
        pg.invalidate();
        Assert.assertEquals(new Rectangle(0, -1, 3, 7), pg.getBounds());
    }

    public void testAddPoint() {
        Polygon pg = new Polygon(new int[]{1, 2, 3, 4, 5, 6}, new int[]{7, 8, 9, 10, 11, 12}, 3);
        Assert.assertEquals(3, pg.npoints);
        pg.addPoint(13, 14);
        Assert.assertEquals(4, pg.npoints);
        Assert.assertEquals(13, pg.xpoints[pg.npoints - 1]);
        Assert.assertEquals(14, pg.ypoints[pg.npoints - 1]);
    }

    public void testGetBounds() {
        Polygon pg = new Polygon();
        Assert.assertEquals(new Rectangle(), pg.getBounds());
        pg.addPoint(1, 2);
        Assert.assertEquals(new Rectangle(1, 2, 0, 0), pg.getBounds());
        pg.addPoint(3, 5);
        Assert.assertEquals(new Rectangle(1, 2, 2, 3), pg.getBounds());
        pg.addPoint(4, 1);
        Assert.assertEquals(new Rectangle(1, 1, 3, 4), pg.getBounds());
        pg.translate(0, 0);
        Assert.assertEquals(new Rectangle(1, 1, 3, 4), pg.getBounds());
        pg.translate(2, 3);
        Assert.assertEquals(new Rectangle(3, 4, 3, 4), pg.getBounds());
    }

    @SuppressWarnings("deprecation")
    public void testGetBoundingBox() {
        Polygon pg = new Polygon();
        Assert.assertEquals(new Rectangle(), pg.getBoundingBox());
        pg.addPoint(1, 2);
        Assert.assertEquals(new Rectangle(1, 2, 0, 0), pg.getBoundingBox());
        pg.addPoint(3, 5);
        Assert.assertEquals(new Rectangle(1, 2, 2, 3), pg.getBoundingBox());
        pg.addPoint(4, 1);
        Assert.assertEquals(new Rectangle(1, 1, 3, 4), pg.getBoundingBox());
        pg.translate(0, 0);
        Assert.assertEquals(new Rectangle(1, 1, 3, 4), pg.getBoundingBox());
        pg.translate(2, 3);
        Assert.assertEquals(new Rectangle(3, 4, 3, 4), pg.getBoundingBox());
    }

    public void testGetBounds2D() {
        Polygon pg = new Polygon();
        Assert.assertEquals(new Rectangle2D.Double(), pg.getBounds2D());
        pg.addPoint(1, 2);
        Assert.assertEquals(new Rectangle2D.Double(1, 2, 0, 0), pg.getBounds2D());
        pg.addPoint(3, 5);
        Assert.assertEquals(new Rectangle2D.Double(1, 2, 2, 3), pg.getBounds2D());
        pg.addPoint(4, 1);
        Assert.assertEquals(new Rectangle2D.Double(1, 1, 3, 4), pg.getBounds2D());
        pg.translate(0, 0);
        Assert.assertEquals(new Rectangle(1, 1, 3, 4), pg.getBounds2D());
        pg.translate(2, 3);
        Assert.assertEquals(new Rectangle(3, 4, 3, 4), pg.getBounds2D());
    }

    public void testTranslate() {
        Polygon pg = new Polygon(new int[]{1, 2, 3}, new int[]{7, 8, 9}, 3);
        pg.translate(0, 0);
        Assert.assertEquals(3, pg.npoints);
        assertEquals(new int[]{1, 2, 3}, pg.xpoints, 3);
        assertEquals(new int[]{7, 8, 9}, pg.ypoints, 3);

        pg.translate(2, 3);
        Assert.assertEquals(3, pg.npoints);
        assertEquals(new int[]{3, 4, 5}, pg.xpoints, 3);
        assertEquals(new int[]{10, 11, 12}, pg.ypoints, 3);
    }

    @SuppressWarnings("deprecation")
    public void testInside() {
        Polygon pg = new Polygon();
        pg.addPoint(1, 2);
        pg.addPoint(3, 5);
        pg.addPoint(4, 1);
        Assert.assertTrue(pg.inside(2, 2));
        Assert.assertFalse(pg.inside(5, 5));
    }

    public void testContains1() {
        Polygon pg = new Polygon();
        pg.addPoint(1, 2);
        pg.addPoint(3, 5);
        pg.addPoint(4, 1);
        Assert.assertTrue(pg.contains(2, 2));
        Assert.assertFalse(pg.contains(5, 5));
    }

    public void testContains2() {
        Polygon pg = new Polygon();
        pg.addPoint(1, 2);
        pg.addPoint(3, 5);
        pg.addPoint(4, 1);
        Assert.assertTrue(pg.contains(new Point(2, 2)));
        Assert.assertFalse(pg.contains(new Point(5, 5)));
    }

    void checkPathIteratorDouble(PathIterator p, double[] values) {
        checkPathRule(p, PathIterator.WIND_EVEN_ODD);
        checkPathMove(p, false, values[0], values[1], 0.0);
        for(int i = 2; i < values.length;) {
            checkPathLine(p, false, values[i++], values[i++], 0.0);
        }
        checkPathClose(p, true);
    }

    void checkPathIteratorFloat(PathIterator p, float[] values) {
        checkPathRule(p, PathIterator.WIND_EVEN_ODD);
        checkPathMove(p, false, values[0], values[1], 0.0f);
        for(int i = 2; i < values.length;) {
            checkPathLine(p, false, values[i++], values[i++], 0.0f);
        }
        checkPathClose(p, true);
    }

    public void testGetPathIteratorDouble() {
        Polygon pg = new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3);
        checkPathIteratorDouble(
                pg.getPathIterator(null),
                new double[]{1, 4, 2, 5, 3, 6});
    }

    public void testGetPathIteratorFloat() {
        Polygon pg = new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3);
        checkPathIteratorFloat(
                pg.getPathIterator(null),
                new float[]{1, 4, 2, 5, 3, 6});
    }

    public void testGetPathIteratorDoubleFlat() {
        Polygon pg = new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3);
        checkPathIteratorDouble(
                pg.getPathIterator(null, 2),
                new double[]{1, 4, 2, 5, 3, 6});
    }

    public void testGetPathIteratorFloatFlat() {
        Polygon pg = new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3);
        checkPathIteratorFloat(
                pg.getPathIterator(null, 5),
                new float[]{1, 4, 2, 5, 3, 6});
    }

    public void testGetPathIteratorDoubleAffine() {
        Polygon pg = new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3);
        checkPathIteratorDouble(
                pg.getPathIterator(AffineTransform.getTranslateInstance(3, 1)),
                new double[]{4, 5, 5, 6, 6, 7});
    }

    public void testGetPathIteratorFloatAffine() {
        Polygon pg = new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3);
        checkPathIteratorFloat(
                pg.getPathIterator(AffineTransform.getTranslateInstance(3, 1)),
                new float[]{4, 5, 5, 6, 6, 7});
    }

    public void testGetPathIteratorEmpty() {
        // Regression for HARMONY-1572
        Polygon pg = new Polygon(new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 },
                0);
        PathIterator p = pg.getPathIterator(null);
        checkPathRule(p, PathIterator.WIND_EVEN_ODD);
        checkPathDone(p, true);
    }

    @Override
    public String objToStr(Object obj) {
        Polygon p = (Polygon)obj;
        String data = p.npoints + " [";
        Rectangle bounds = p.getBounds();
        if (bounds == null) {
            data += "null";
        } else {
            data += bounds.x + "," + bounds.y + "," + bounds.width + "," + bounds.height;
        }
        data +=  "] (";
        for(int i = 0; i < p.npoints; i++) {
            data += p.xpoints[i];
            if (i < p.npoints - 1) {
                data += ",";
            }
        }
        data += ") (";
        for(int i = 0; i < p.npoints; i++) {
            data += p.ypoints[i];
            if (i < p.npoints - 1) {
                data += ",";
            }
        }
        data += ")";
        return obj.getClass().getName() + "[" + data + "]";
    }

    public void testSerializeRead1() {
        checkRead(new Polygon());
    }

    public void testSerializeRead2() {
        checkRead(new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3));
    }
    
    public void testSerializeWrite1() {
        checkWrite(new Polygon());
    }

    public void testSerializeWrite2() {
        checkWrite(new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3));
    }
    
    public void createSerializeTemplates() {
        saveSerialized(new Polygon());
        saveSerialized(new Polygon(new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3));
    }

    public static void main(String[] args) {
//        new PolygonTest("").createSerializeTemplates();
        junit.textui.TestRunner.run(PolygonTest.class);
    }

}
