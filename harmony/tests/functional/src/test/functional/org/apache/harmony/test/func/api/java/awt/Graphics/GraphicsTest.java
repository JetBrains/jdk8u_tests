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
package org.apache.harmony.test.func.api.java.awt.Graphics;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class GraphicsTest extends AutonomousTest {

    public static void main(String[] args) {
        GraphicsTest gTest = new GraphicsTest();
        System.exit(gTest.test(args));
    }

    public Result testGraphics_init() {

        BufferedImage bImage = new BufferedImage(100, 100, 10);
        Graphics q = bImage.getGraphics();

        int x = 40;
        int y = 2;
        int w = 45;
        int h = 87;

        q.setClip(x, y, w, h);

        if (q.getClipRect().x != x) {
            return failed("Graphics.init(): wrong x coordinate. returned: "
                    + q.getClipRect().x + ", expected: " + x + ".");
        }
        if (q.getClipRect().y != y) {
            return failed("Graphics.init(): wrong y coordinate. returned: "
                    + q.getClipRect().x + ", expected: " + x + ".");
        }
        if (q.getClipRect().width != w) {
            return failed("Graphics.init(): wrong width. returned: "
                    + q.getClipRect().width + ", expected: " + w + ".");
        }
        if (q.getClipRect().height != h) {
            return failed("Graphics.init(): wrong height. returned: "
                    + q.getClipRect().height + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testGraphics_create() {
        BufferedImage bImage = new BufferedImage(100, 100, 10);
        Graphics q = bImage.createGraphics();

        int x = 0;
        int y = 0;
        int w = 95;
        int h = 70;

        Graphics q1 = q.create(x, y, w, h);

        Rectangle rect = q1.getClipRect();

        if (rect.x != x) {
            return failed("Graphics.create(int,int,int,int): wrong x coordinate."
                    + " returned: " + rect.x + ", expected: " + x + ".");
        }
        if (rect.y != y) {
            return failed("Graphics.create(int,int,int,int): wrong y coordinate."
                    + " returned: " + rect.y + ", expected: " + y + ".");
        }
        if (rect.width != w) {
            return failed("Graphics.create(int,int,int,int): wrong width. "
                    + "returned: " + rect.width + ", expected: " + w + ".");
        }
        if (rect.height != h) {
            return failed("Graphics.create(int,int,int,int): wrong height. "
                    + "returned: " + rect.height + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testGraphics_getFontMetrics() {

        BufferedImage bImage = new BufferedImage(100, 100, 10);
        Graphics q = bImage.createGraphics();

        if (!q.getFontMetrics().getFont().equals(q.getFont())) {
            return failed("Graphics.getFontMetrics(): unexpected font. "
                    + "returned: " + q.getFontMetrics().getFont()
                    + ", expected: " + q.getFont() + ".");
        }

        return passed();
    }

    public Result testGraphics_finalize() {
        BufferedImage bImage = new BufferedImage(100, 100, 10);
        Graphics q = bImage.createGraphics();

        try {
            q.finalize();
        } catch (Exception e) {
            failed("Graphics.finalize(): crashed with exception: '"
                    + e.getMessage() + "' .");
        }

        return passed();
    }
}