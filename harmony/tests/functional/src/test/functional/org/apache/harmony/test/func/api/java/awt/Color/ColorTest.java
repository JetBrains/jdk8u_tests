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

package org.apache.harmony.test.func.api.java.awt.Color;

import java.awt.Color;
import java.io.IOException;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class ColorTest extends AutonomousTest {

    public static void main(String[] args) throws IOException {
        ColorTest colorTest = new ColorTest();
        System.exit(colorTest.test(args));
    }

    public Result testColor_init_iii() {
        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);

        Color color = new Color(r, g, b);

        if (color.getRed() != r) {
            return failed("Color.init(int, int, int): wrong red value. "
                    + "returned: " + color.getRed() + ", expected: " + r + ".");
        }
        if (color.getGreen() != g) {
            return failed("Color.init(int, int, int): wrong green value. "
                    + "returned: " + color.getGreen() + ", expected: " + g
                    + ".");
        }
        if (color.getBlue() != b) {
            return failed("Color.init(int, int, int): wrong blue value. "
                    + "returned: " + color.getBlue() + ", expected: " + b + ".");
        }
        if (color.getAlpha() != 255) {
            return failed("Color.init(int, int, int): wrong alpha. "
                    + "returned: " + color.getAlpha() + ", expected: 255.");

        }

        return passed();
    }

    public Result testColor_init_iiii() {
        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);
        int a = (int) (Math.random() * 255);

        Color color = new Color(r, g, b, a);

        if (color.getRed() != r) {
            return failed("Color.init(int, int, int, int): wrong red value. "
                    + "returned: " + color.getRed() + ", expected: " + r + ".");
        }
        if (color.getGreen() != g) {
            return failed("Color.init(int, int, int, int): wrong green value. "
                    + "returned: " + color.getGreen() + ", expected: " + g
                    + ".");
        }
        if (color.getBlue() != b) {
            return failed("Color.init(int, int, int, int): wrong blue value. "
                    + "returned: " + color.getBlue() + ", expected: " + b + ".");
        }
        if (color.getAlpha() != a) {
            return failed("Color.init(int, int, int, int): wrong alpha. "
                    + "returned: " + color.getAlpha() + ", expected: " + a
                    + ".");

        }

        return passed();
    }

    public Result testColor_equals() {
        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);
        int a = 1 + (int) (Math.random() * 254);

        Color color = new Color(r, g, b, a);
        Color color3 = new Color(r, g, b, a);
        Color color2 = new Color(r, g, b, a - 1); 
        
        if (color.equals(null)) {
            return failed("Color.equals(Object): equals return true "
                    + "for null pointer object.");
        }
        if (color.equals("Text")) {
            return failed("Color.equals(Object): equals return true "
                    + "for String object.");
        }

        if (color.equals(color2)) {
            return failed("Color,equals(Object): not equal objects are equal.");
        }
        if (!color.equals(color)) {
            return failed("Color,equals(Object): object doesn't equal itself.");
        }
        if (!color.equals(color3)) {
            return failed("Color,equals(Object): same objects do not equal.");
        }

        return passed();
    }

    public Result testColor_getARGB() {
        int r = (int) (Math.random() * 255);
        int g = (int) (Math.random() * 255);
        int b = (int) (Math.random() * 255);
        int a = (int) (Math.random() * 255);

        Color color = new Color(r, g, b, a);

        if (color.getRed() != r) {
            return failed("Color.getRed(): wrong red value. " + "returned: "
                    + color.getRed() + ", expected: " + r + ".");
        }
        if (color.getGreen() != g) {
            return failed("Color.getGreen(): wrong green value. "
                    + "returned: " + color.getGreen() + ", expected: " + g
                    + ".");
        }
        if (color.getBlue() != b) {
            return failed("Color.getBlue(): wrong blue value. " + "returned: "
                    + color.getBlue() + ", expected: " + b + ".");
        }
        if (color.getAlpha() != a) {
            return failed("Color.getAlpha(): wrong alpha. " + "returned: "
                    + color.getAlpha() + ", expected: " + a + ".");

        }

        return passed();
    }

    public Result testColor_getRGB() {

        Color black = new Color(0, 0, 0);
        Color white = new Color(255, 255, 255);
        Color red = new Color(255, 0, 0);
        Color green = new Color(0, 255, 0);
        Color blue = new Color(0, 0, 255);

        if (black.getRGB() != Color.BLACK.getRGB()
                || white.getRGB() != Color.WHITE.getRGB()
                || red.getRGB() != Color.RED.getRGB()
                || green.getRGB() != Color.GREEN.getRGB()
                || blue.getRGB() != Color.BLUE.getRGB()) {
            return failed("Color.getRGB(): wrong RGB.");
        }

        return passed();
    }

    public Result testColor_brighter() {
        Color black = new Color(0, 0, 0);
        Color white = new Color(255, 255, 255);
        Color red = new Color(255, 0, 0);
        Color green = new Color(0, 255, 0);
        Color blue = new Color(0, 0, 255);

        if (!black.brighter().equals(Color.BLACK.brighter())
                | !white.brighter().equals(Color.WHITE.brighter())
                | !red.brighter().equals(Color.RED.brighter())
                | !green.brighter().equals(Color.GREEN.brighter())
                | !blue.brighter().equals(Color.BLUE.brighter())) {
            return failed("Color.brighter(): wrong color.");
        }

        return passed();

    }

    public Result testColor_darker() {
        Color black = new Color(0, 0, 0);
        Color white = new Color(255, 255, 255);
        Color red = new Color(255, 0, 0);
        Color green = new Color(0, 255, 0);
        Color blue = new Color(0, 0, 255);

        if (!black.darker().equals(Color.BLACK.darker())
                | !white.darker().equals(Color.WHITE.darker())
                | !red.darker().equals(Color.RED.darker())
                | !green.darker().equals(Color.GREEN.darker())
                | !blue.darker().equals(Color.BLUE.darker())) {
            return failed("Color.darker(): wrong color.");
        }

        return passed();

    }
}
