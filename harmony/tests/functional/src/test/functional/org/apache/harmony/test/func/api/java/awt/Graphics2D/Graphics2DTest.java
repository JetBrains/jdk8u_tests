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
package org.apache.harmony.test.func.api.java.awt.Graphics2D;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class Graphics2DTest extends AutonomousTest {

    public static void main(String[] args) throws IOException {
        Graphics2DTest test2D = new Graphics2DTest();
        System.exit(test2D.test(args));
    }

    public Result testGraphics2D_init() {
        BufferedImage bImage;
        Graphics2D graph2D;

        bImage = new BufferedImage(100, 100, 10);

        graph2D = bImage.createGraphics();

        if (!graph2D.getComposite().equals(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER))) {
            return failed("Graphics2D.init(): returned composite is not default."
                    + "returned: "
                    + graph2D.getComposite()
                    + ", expected: "
                    + AlphaComposite.getInstance(AlphaComposite.SRC_OVER) + ".");
        }

        if (!graph2D.getStroke().equals(new BasicStroke())) {
            return failed("Graphics2D.init(): returned storke is not default."
                    + "returned: " + graph2D.getStroke() + ", expected: "
                    + new BasicStroke() + ".");
        }

        graph2D.setBackground(Color.WHITE);
        if (graph2D.getBackground() != Color.WHITE) {
            return failed("Graphics2D.init(): setBackground() "
                    + "don't change the color to white. returned: "
                    + graph2D.getBackground()
                    + ", expected: java.awt.Color[r=255,g=255,b=255].");
        }

        return passed();
    }
}