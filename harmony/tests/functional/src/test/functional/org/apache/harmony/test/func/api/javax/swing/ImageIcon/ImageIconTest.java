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

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.apache.harmony.share.Test;

/*
 * 06.06.2005
 */
public class ImageIconTest extends Test {

    private final boolean learnMode = false;

    private static String testDir;

    private final String dataFile = "auxiliary/img.dat";

    private final String imageFile = "auxiliary/img.PNG";

    private Object formReady = new Object();

    public int test() {
        JFrame frame = null;
        try {
            final ImageIcon stringIcon = new ImageIcon(testDir + imageFile);
            Image img = stringIcon.getImage();
            final ImageIcon icon = new ImageIcon(img);

            if (!img.equals(icon.getImage())) {
                return fail("ImageIcon.getImage() returns incorrect value");
            }

            final Point frameLoc = new Point(100, 100);
            final Point imgLoc = (Point) frameLoc.clone();
            imgLoc.translate(30, 30);

            frame = new JFrame("Test App") {
                public void paint(Graphics g) {
                    icon.paintIcon(this, g, 30, 30);
                    synchronized (formReady) {
                        formReady.notifyAll();
                    }
                }
            };
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocation(frameLoc);
            frame.setSize(icon.getIconWidth() + 60, icon.getIconHeight() + 60);

            frame.setVisible(true);
            frame.repaint();

            synchronized (formReady) {
                try {
                    Thread.sleep(5000);
                    formReady.wait();
                } catch (InterruptedException e) {
                }
            }
            
            System.out.println("ok, verify");

            // icon.paintIcon(frame, frame.getGraphics(), 30, 30);

            if (learnMode) {
                GoldenImageData data = new GoldenImageData();

                data.setLocation(imgLoc);

                data.setHeight(icon.getIconHeight());
                data.setWidth(icon.getIconWidth());
                data.dump(testDir + dataFile);
                return pass();
            } else {
                GoldenImageData data = GoldenImageData.load(testDir + dataFile);
                data.setLocation(imgLoc);

                if (data.getWidth() != icon.getIconWidth()) {
                    return fail(icon.getIconWidth()
                            + " == icon.getIconWidth() != icon width == "
                            + data.getWidth());
                }
                if (data.getHeight() != icon.getIconHeight()) {
                    return fail(icon.getIconHeight()
                            + " == icon.getIconHeight() != icon Height == "
                            + data.getHeight());
                }
                if (!data.verify(frame.getGraphics())) {
                    return fail("Painted image do not match original image");
                }
                return pass();
            }
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < args.length; i++) {
            if ("-testDir".equals(args[i])) {
                testDir = args[i + 1];
                if (testDir.lastIndexOf(File.separatorChar) != testDir.length() - 1) {
                    testDir += File.separatorChar;
                }
                break;
            }
        }
        //testDir = "src/org/apache/harmony/test/func/api/javax/swing/ImageIcon/";
        System.err.println(new File(".").getAbsolutePath());
        System.err.println(testDir);
        int result = new ImageIconTest().test();
        long finish = System.currentTimeMillis();
        
        System.out.println("Ok :" + (finish - start) + "ms");
        
        System.exit(result);
    }
}
