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

package org.apache.harmony.test.func.api.share;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

class ImageCanvas extends Canvas {
    private Image   img;
    private long    waitingTime = 150;
    private boolean pause       = false;
    public ImageCanvas() {
        img = null;
    }

    public boolean getPause() {
        return pause;
    }

    public void setPause(boolean val) {
        pause = val;
    }

    public void decrementWaitingTime() {
        --waitingTime;
        repaint((int) (DialogConfirm.msgBoxDimension.getWidth() - 200), 0, 200,
                20);
    }

    public ImageCanvas(Image newImg, long time) {
        if (newImg == null) {
            throw new NullPointerException("Image can not be null");
        }
        waitingTime = time;
        img = newImg;
    }

    public void paint(Graphics g) {
        int w = (int) (getPreferredSize().getWidth());
        int h = (int) (getPreferredSize().getHeight());
        Long hours = new Long(waitingTime / 3600);
        Long minutes = new Long((waitingTime - hours.longValue() * 3600) / 60);
        Long seconds = new Long(
                (waitingTime - (hours.longValue() * 3600 + minutes.longValue() * 60)));
        String timeLeft = "Script will timeout in: "
                + hours.toString()
                + "h:"
                + (minutes.longValue() < 10
                        ? "0" + minutes.toString()
                        : minutes.toString())
                + "m:"
                + (seconds.longValue() < 10
                        ? "0" + seconds.toString()
                        : seconds.toString()) + "s";

        if (img != null) {
            /* Image painting starts from top left corner of client area */
            g.drawImage(img, 0, 0, this);
            g.setColor(Color.BLACK);

            /* Draw rectangle around the image */
            g.drawRect(2, 2, img.getWidth(null) - 3, img.getHeight(null) - 3);

            /* Draw white rectangle for the warning */
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(2, h - 22, w - 3, 21);
            g.setColor(Color.BLACK);
            g.drawRect(2, h - 22, w - 3, 21);

            g.setFont(new Font("Arial", Font.PLAIN, 10));

            g.drawString("Given screenshot (if present) provides the "
                    + "general conception of how the result "
                    + "should appear, real colors, fonts, etc. may "
                    + "slightly vary.", 3, h - 12);

            /* Draw white rectangle for the timeout time */
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(w - 215, 2, 215 - 1, 19);
            g.setColor(Color.BLACK);
            g.drawRect(w - 215, 2, 215 - 1, 19);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(timeLeft, w - 212, 15);
        }
    }

    public void loadImage(Image newImg) {
        if (newImg == null) {
            throw new NullPointerException("Image can not be null");
        }
        img = newImg;
    }
}