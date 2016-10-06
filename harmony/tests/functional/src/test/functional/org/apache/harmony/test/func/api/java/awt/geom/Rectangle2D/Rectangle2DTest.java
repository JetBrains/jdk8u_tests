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
package org.apache.harmony.test.func.api.java.awt.geom.Rectangle2D;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class Rectangle2DTest extends AutonomousTest {

    public static void main(String[] args) throws IOException {
        Rectangle2DTest rect2DTest = new Rectangle2DTest();
        System.exit(rect2DTest.test(args));
    }

    public Result testRectangle2D_Float_init() {
        Rectangle2D.Float rect2DF = new Rectangle2D.Float();

        if (rect2DF.x != 0.0) {
            return failed("Rectangle2D.Float.init(): wrong x coordinate. "
                    + "returned: " + rect2DF.x + ", expected: 0.0 .");
        }
        if (rect2DF.y != 0.0) {
            return failed("Rectangle2D.Float.init(): wrong y coordinate. "
                    + "returned: " + rect2DF.y + ", expected: 0.0 .");
        }
        if (rect2DF.width != 0.0) {
            return failed("Rectangle2D.Float.init(): wrong width. "
                    + "returned: " + rect2DF.width + ", expected: 0.0 .");
        }
        if (rect2DF.height != 0.0) {
            return failed("Rectangle2D.Float.init(): wrong height "
                    + "returned: " + rect2DF.height + ", expected: 0.0 .");
        }

        return passed();
    }

    public Result testRectangle2D_Float_init_ffff() {
        float x = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float y = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float w = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float h = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);

        Rectangle2D.Float rect2DF = new Rectangle2D.Float(x, y, w, h);

        if (rect2DF.x != x) {
            return failed("Rectangle2D.Float.init(float, float, float, float): "
                    + "wrong x coordinate. "
                    + "returned: "
                    + rect2DF.x
                    + ", expected: " + x + ".");
        }
        if (rect2DF.y != y) {
            return failed("Rectangle2D.Float.init(float, float, float, float):"
                    + " wrong y coordinate. " + "returned: " + rect2DF.y
                    + ", expected: " + y + ".");
        }
        if (rect2DF.width != w) {
            return failed("Rectangle2D.Float.init(float, float, float, float): "
                    + "wrong width. "
                    + "returned: "
                    + rect2DF.width
                    + ", expected: " + w + ".");
        }
        if (rect2DF.height != h) {
            return failed("Rectangle2D.Float.init(float, float, float, float):"
                    + " wrong height. " + "returned: " + rect2DF.height
                    + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testRectangle2D_Float_getXYWH() {
        float x = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float y = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float w = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float h = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);

        Rectangle2D.Float rect2DF = new Rectangle2D.Float(x, y, w, h);

        if (rect2DF.getX() != (double) x) {
            return failed("Rectangle2D.Float.getX(): wrong x coordinate. "
                    + "returned: " + rect2DF.getX() + ", expected: "
                    + (double) x + ".");
        }
        if (rect2DF.getY() != (double) y) {
            return failed("Rectangle2D.Float.getY(): wrong Y coordinate. "
                    + "returned: " + rect2DF.getY() + ", expected: "
                    + (double) y + ".");
        }
        if (rect2DF.getWidth() != (double) w) {
            return failed("Rectangle2D.Float.getWidth(): wrong width. "
                    + "returned: " + rect2DF.getWidth() + ", expected: "
                    + (double) w + ".");

        }

        if (rect2DF.getHeight() != (double) h) {
            return failed("Rectangle2D.Float.getHeight(): wrong height. "
                    + "returned: " + rect2DF.getHeight() + ", expected: "
                    + (double) h + ".");

        }

        return passed();
    }

    public Result testRectangle2D_Float_setRect_ffff() {
        Rectangle2D.Float rect2DF = new Rectangle2D.Float();

        float x = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float y = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float w = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float h = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);

        rect2DF.setRect(x, y, w, h);

        if (rect2DF.x != x) {
            return failed("Rectangle2D.Float.setRect(float, float, float, float): "
                    + "wrong x coordinate. "
                    + "returned: "
                    + rect2DF.x
                    + ", expected: " + x + ".");
        }
        if (rect2DF.y != y) {
            return failed("Rectangle2D.Float.setRect(float, float, float, float):"
                    + " wrong y coordinate. "
                    + "returned: "
                    + rect2DF.y
                    + ", expected: " + y + ".");
        }
        if (rect2DF.width != w) {
            return failed("Rectangle2D.Float.setRect(float, float, float, float): "
                    + "wrong width. "
                    + "returned: "
                    + rect2DF.width
                    + ", expected: " + w + ".");
        }
        if (rect2DF.height != h) {
            return failed("Rectangle2D.Float.setRect(float, float, float, float):"
                    + " wrong height. "
                    + "returned: "
                    + rect2DF.height
                    + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testRectangle2D_Float_setRect_rectangle2D() {
        float x = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float y = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float w = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);
        float h = (float) (Math.random() * (Float.MAX_VALUE - 1) + 1);

        Rectangle2D.Float rect2DF = new Rectangle2D.Float(x, y, w, h);
        Rectangle2D.Float rect2DF_def = new Rectangle2D.Float();

        rect2DF_def.setRect(rect2DF);

        if (!rect2DF_def.equals(rect2DF)) {

            return failed("Rectangle2D.Float.setRect(Rectangle2D): wrong the"
                    + "Rectangle2D is not the same as the specified Rectangle2D.");
        }

        return passed();
    }

    //public Result testRectangle2D_init() {return passed();}
    //public Result testRectangularShape_init(){}

}
