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

package org.apache.harmony.test.func.api.java.awt.Component;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.apache.harmony.test.func.api.java.awt.share.SimpleComponent;
import org.apache.harmony.share.Test;

public class LocationTest extends Test {

    Point inside = new Point(50, 15);

    Point outside = new Point(14, 95);

    Point smallShift = new Point(3, 3);

    Dimension[] size = { new Dimension(70, 25), new Dimension(75, 22),
            new Dimension(77, 27), new Dimension(79, 30), };

    Point[] location = { new Point(50, 25), new Point(55, 24),
            new Point(45, 27), new Point(48, 29), };

    private boolean checkSize(Component c, int sz, int loc) {
        Point rv = new Point(0, 0);
        Rectangle bounds = new Rectangle(location[loc], size[sz]);
        Rectangle rr = new Rectangle(0, 0);
        Dimension rs = new Dimension(0, 0);
        //return
        return c.contains(inside) && c.contains(inside.x, inside.y)
                && !c.contains(outside) && !c.contains(outside.x, outside.y)
                && c.getHeight() == size[sz].height
                && c.getWidth() == size[sz].width
                && c.getX() == location[loc].x && c.getY() == location[loc].y
                && c.getSize().equals(size[sz])
                && c.getSize(rs).equals(size[sz])
                && rs.equals(size[sz])
                && c.getLocation().equals(location[loc])
                && c.getLocation(rv).equals(location[loc])
                && rv.equals(location[loc])
                && c.getBounds().equals(bounds)
                && c.getBounds(rr).equals(bounds)
                && rr.equals(bounds)
                && true;

    }

    public static void main(String[] args) {
        System.exit(new LocationTest().test());
    }

    public int test() {

        Component c = new SimpleComponent();

        c.setBounds(new Rectangle(location[3], size[3]));
        if (!(checkSize(c, 3, 3))) return fail("Component.setBounds(Rectangle)");

        c.setSize(new Dimension(size[0].width, size[0].height));
        if (!(checkSize(c, 0, 3))) return fail("Component.setSize(Dimension)");

        c.setSize(size[1].width, size[1].height);
        if (!(checkSize(c, 1, 3))) return fail("Component.setSize(int, int)");

        c.setLocation(location[0].x, location[0].y);
        if (!(checkSize(c, 1, 0))) return fail("Component.setLocation(int, int)");

        c.setLocation(location[1]);
        if (!(checkSize(c, 1, 1))) return fail("Component.setLocation(Point)");

        c.setBounds(location[2].x, location[2].y, size[2].width,
                        size[2].height);
        if (!(checkSize(c, 2, 2))) return fail("Component.setBounds(int, int, int, int)");

        return pass();
    }
}