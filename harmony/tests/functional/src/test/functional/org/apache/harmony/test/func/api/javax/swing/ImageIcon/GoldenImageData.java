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

package org.apache.harmony.test.func.api.javax.swing.ImageIcon;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 * 07.10.2005
 */
class GoldenImageData implements Serializable {

    private static final long serialVersionUID = -9134844048205383053L;

    public static GoldenImageData load(String filename) {
        System.err.println("loading from: " + filename);
        System.err.println("at: " + new File(filename).getAbsolutePath());
        try {
            ObjectInputStream input = new ObjectInputStream(
                    new FileInputStream(filename));
            GoldenImageData data = (GoldenImageData) input.readObject();
            input.close();
            return data;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }

    private int[][] colors;

    private int height;

    private Point location;

    private int width;

    public GoldenImageData() {

    }

    public void dump(String filename) {
        System.out.println("dumping to: " + filename);
        System.out.println("at: " + new File(filename).getAbsolutePath());
        Robot robo = null;
        try {
            robo = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        Rectangle img = findImage();
        double delta = 0.0;
        colors = new int[img.width + 1][img.height + 1];
        for (int x = 0; x <= img.width; x++) {
            for (int y = 0; y <= img.height; y++) {

                colors[x][y] = (robo.getPixelColor(x + img.x, y + img.y)
                        .getRGB());
                delta += Math.abs(colors[x][y]);
            }
        }

        try {
            ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream(filename));

            output.writeObject(this);

            System.err.println("ok, data dumped");
            System.err.println("summ = " + delta);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private Rectangle findImage() {
        Rectangle result = new Rectangle();
        result.height = this.height;
        result.width = this.width;
        result.x = location.x;
        result.y = location.y;
        return result;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean verify(Graphics g) {
        System.err.println("verify");
        Robot robo = null;
        try {
            robo = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        Rectangle img = findImage();
        double delta = 0;
        double summ = 0;
        g.setColor(Color.BLACK);
        System.out.println("Image - (" + img.x + ", " + img.y + ")-(" + (img.x + img.width) + ", " + (img.height + img.y) + ")");
        for (int x = 0; x <= img.width; x++) {
            for (int y = 0; y <= img.height; y++) {
                delta += Math.abs(colors[x][y]
                        - (robo.getPixelColor(x + img.x, y + img.y).getRGB()));
                summ += Math.abs(colors[x][y]);
                // g.drawLine(x-1+30, y+30, x+30,y+30);
            }
        }

        System.err.println("delta = " + delta);
        System.err.println("summ = " + summ);

        return true;
    }
}
