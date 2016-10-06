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

package org.apache.harmony.test.func.api.java.awt.image.BufferedImage;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Hashtable;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class BufferedImageTest extends AutonomousTest {
    static File         testDir;
    protected Frame     frm;
    protected boolean   butActionListenerChanged = false;
    Button              btnClose;
    Button              btnDoNothing;
    protected final int WIN_WIDTH                = 320;
    protected final int WIN_HEIGHT               = 240;
    protected final int WIN_X                    = 150;
    protected final int WIN_Y                    = 150;
    protected Dimension winDimension             = new Dimension(WIN_WIDTH,
                                                         WIN_HEIGHT);
    protected Dimension smallWinDimension        = new Dimension(WIN_WIDTH / 4,
                                                         WIN_HEIGHT / 4);
    protected Point     winLocation              = new Point(WIN_X, WIN_Y);
    protected final int FILL_PIXELS              = 3;
    protected void setUp() {
        frm = new Frame("MyApp");
        btnClose = new Button("MyClose");
        btnDoNothing = new Button("MyLoafer");

        /* WM_DESTROY is posted when button is clicked */
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String action = ae.getActionCommand();
                if (action.equals("Close")) {
                    frm.dispose();
                    butActionListenerChanged = true;
                } else {
                    System.out.println(action);
                }
            }
        };

        btnClose.addActionListener(al);
        btnClose.setBounds(10, 10, 80, 22);
        btnDoNothing.setBounds(70, 20, 100, 22);

        Panel panel = new Panel();
        panel.setLayout(null);
        panel.setSize(winDimension);
        panel.add(btnClose);
        panel.add(btnDoNothing);

        frm.add(panel);

        /* Handler for WM_DESTROY message */
        frm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frm.dispose();
                // System.exit(0);
            }
        });

        frm.setSize(winDimension);

    }

    protected void tearDown() {
        frm.dispose();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) {

        // File f = new File("C:\\temp\\toDelete");
        // BufferedImageTest biTest = new BufferedImageTest();
        // biTest.setUp();
        // biTest.setTestDir(f);
        // biTest.test(args);
        // biTest.tearDown();
        // System.exit(0);
        System.exit(new BufferedImageTest().test(args));
    }

    public Result testBufferedImage_general() {

        /*
         * colorSpace - The ColorSpace associated with this color model.
         * hasAlpha - If true, this color model supports alpha.
         * isAlphaPremultiplied - If true, alpha is premultiplied. transparency -
         * Specifies what alpha values can be represented by this color model.
         * transferType - Specifies the type of primitive array used to
         * represent pixel values.
         */

        ComponentColorModel ccm = new ComponentColorModel(ColorSpace
                .getInstance(ColorSpace.CS_sRGB), true, false,
                ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_FLOAT);
        int[] bandOffsets = {0, 1, 2, 3};
        int w = 16;
        int h = 16;
        ComponentSampleModel csm = new ComponentSampleModel(
                DataBuffer.TYPE_FLOAT, w, h, bandOffsets.length,
                bandOffsets.length * w, bandOffsets);
        Point p = new Point(0, 0);
        Hashtable ht = new Hashtable();

        /* Create image 16x16 pixels */
        BufferedImage bufImage = new BufferedImage(ccm, Raster
                .createWritableRaster(csm, p), true, ht);

        frm.setIconImage(bufImage);

        if (bufImage.getColorModel().hasAlpha() == false) {
            return failed("\n BufferedImage#hasAlpha returned false instead"
                    + " of true");
        }
        if (bufImage.getColorModel().isAlphaPremultiplied() == false) {
            return failed("\n BufferedImage#isAlphaPremultiplied returned"
                    + "false instead of true");
        }

        bufImage = new BufferedImage(ccm, Raster.createWritableRaster(csm, p),
                false, ht);

        if (bufImage.getColorModel().isAlphaPremultiplied() == true) {
            return failed("\n BufferedImage#isAlphaPremultiplied returned"
                    + "true instead of false");
        }
        if (bufImage.getWidth() != w || bufImage.getHeight() != h) {
            return failed("\n BufferedImage has incorrect size. Size should be"
                    + "16x16, whereas it is: " + bufImage.getWidth() + "x"
                    + bufImage.getHeight());
        }

        if (bufImage.getWidth(frm) != w || bufImage.getHeight(frm) != h) {
            return failed("\n BufferedImage has incorrect size. Size should be"
                    + "16x16, whereas it is: " + bufImage.getWidth() + "x"
                    + bufImage.getHeight());
        }

        /* BufferedImage#createGraphics tests */
        Graphics2D sampleGr = bufImage.createGraphics();
        sampleGr.setColor(Color.YELLOW);

        sampleGr.fill(new Rectangle(FILL_PIXELS, FILL_PIXELS));

        frm.setIconImage(bufImage);
        BufferedImage retrievedImage = (BufferedImage) frm.getIconImage();
        /* Check retrieved image size */
        if (retrievedImage.getWidth() != bufImage.getWidth()
                || retrievedImage.getHeight() != bufImage.getHeight()
                || retrievedImage.getWidth() != 16
                || retrievedImage.getHeight() != 16) {
            return failed("The retieved image has incorrect");
        }

        for (int i = 0; i < FILL_PIXELS; ++i) {
            for (int j = 0; j < FILL_PIXELS; ++j) {
                if (bufImage.getRGB(i, j) != Color.YELLOW.getRGB()) {
                    return failed("\nPixel color is incorrect!" + "Expected: "
                            + Color.YELLOW.getRGB() + "\nRetrieved: "
                            + retrievedImage.getRGB(FILL_PIXELS, FILL_PIXELS));
                }
            }
        }
        /* Check the retrieved image type */
        if (retrievedImage.getType() != BufferedImage.TYPE_CUSTOM) {
            return failed("\nThe retieved image has incorrect type");
        }

        WritableRaster wr = bufImage.getRaster();
        double d = 3.0;
        boolean exceptionThrown = false;

        try {
            wr.setSample(20, 2, 2, d);
        } catch (ArrayIndexOutOfBoundsException e) {
            exceptionThrown = true;
        }

        if (exceptionThrown == false) {
            return failed("\nNo ArrayIndexOutOfBoundsException is thrown");
        }

        bufImage.coerceData(true);

        /* BufferedImage#getGraphics tests */
        Graphics2D sampleGr2 = bufImage.createGraphics();
        sampleGr2.setColor(Color.YELLOW);

        sampleGr2.fill(new Rectangle(FILL_PIXELS, FILL_PIXELS));

        frm.setIconImage(bufImage);
        BufferedImage retrievedImage2 = (BufferedImage) frm.getIconImage();
        /* Check retrieved image size */
        if (retrievedImage2.getWidth() != bufImage.getWidth()
                || retrievedImage2.getHeight() != bufImage.getHeight()
                || retrievedImage2.getWidth() != 16
                || retrievedImage2.getHeight() != 16) {
            return failed("The retieved image has incorrect");
        }

        for (int i = 0; i < FILL_PIXELS; ++i) {
            for (int j = 0; j < FILL_PIXELS; ++j) {
                if (bufImage.getRGB(i, j) != Color.YELLOW.getRGB()) {
                    return failed("\nPixel color is incorrect!" + "Expected: "
                            + Color.YELLOW.getRGB() + "\nRetrieved: "
                            + retrievedImage2.getRGB(FILL_PIXELS, FILL_PIXELS));
                }
            }
        }

        return passed();
    }
}