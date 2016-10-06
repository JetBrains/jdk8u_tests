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

package org.apache.harmony.test.func.api.java.awt.Rectangle;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Random;

import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class RectangleTest extends AutonomousTest {

    public static void main(String[] args) throws IOException {
        RectangleTest rTest = new RectangleTest();
        System.exit(rTest.test(args));
    }

    public Result testRectangle_init() {

        Rectangle rect = new Rectangle();

        if (rect.x != 0) {
            return failed("Rectangle(): x coordinate nonzero.");
        }
        if (rect.y != 0) {
            return failed("Rectangle(): y coordinate nonzero.");
        }
        if (rect.height != 0) {
            return failed("Rectangle(): height nonzero.");
        }
        if (rect.width != 0) {
            return failed("Rectangle(): width nonzero.");
        }

        return passed();
    }

    public Result testRectangle_init_ii() {

        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(w, h);

        if (rect.x != 0) {
            return failed("Rectangle(int, int): x coordinate nonzero.");
        }
        if (rect.y != 0) {
            return failed("Rectangle(int, int): y coordinate nonzero.");
        }
        if (rect.width != w) {
            return failed("Rectangle(int, int): return wrong width. returned: "
                    + rect.width + ", expected: " + w + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle(int, int): return wrong height. returned: "
                    + rect.height + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testRectangle_init_iiii() {

        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(x, y, w, h);

        if (rect.x != x) {
            return failed("Rectangle(int, int, int, int): set wrong x coordinate."
                    + " returned: " + rect.x + ", expected value: " + x + ".");
        }

        if (rect.y != y) {
            return failed("Rectangle(int, int, int, int): set wrong y coordinate. returned: "
                    + rect.y + ", expected value: " + y + ".");
        }
        if (rect.width != w) {
            return failed("Rectangle(int, int, int, int): set wrong width. "
                    + "returned: " + rect.width + ", expected value: " + w
                    + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle(int, int, int, int): set wrong height. "
                    + "returned: " + rect.height + ", expected value: " + h
                    + ".");
        }

        return passed();
    }

    public Result testRectangle_init_rectangle() {

        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(x, y, w, h);
        Rectangle rect_tested = new Rectangle(rect);

        if (rect.x != x) {
            return failed("Rectangle(Rectangle): x coordinate wrong. returned: "
                    + rect.x + ", expected: " + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle(Rectangle): y coordinate wrong. returned: "
                    + rect.y + ", expected: " + y + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle(Rectangle): height wrong. returned: "
                    + rect.height + ", expected: " + h + ".");
        }
        if (rect.width != w) {
            return failed("Rectangle(Rectangle): width wrong. returned: "
                    + rect.width + ", expected: " + w + ".");
        }

        return passed();
    }

    public Result testRectangle_equal() {

        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(x, y, w, h);
        Rectangle rect1 = new Rectangle(x, y, w, h);

        if (rect.equals(null)) {
            return failed("Rectangle.equals(Object;): "
                    + "successful for null pointer object. returned: true, "
                    + "expected: false.");
        }
        if (rect.equals("test")) {
            return failed("Rectangle.equals(Object;): "
                    + "successful for String object. returned: true, "
                    + "expected: false.");
        }
        if (!rect.equals(rect1)) {
            return failed("Rectangle.equals(Object;): "
                    + "the rectangle is not equal itself. returned: false, "
                    + "expected: true.");
        }

        rect1.setRect(x + 10.0, y + 10.0, w + 100, h + 100);
        if (rect.equals(rect1)) {
            return failed("Rectangle.equals(Object;): "
                    + "the different rectangles are equal. returned: true, "
                    + "expected: false.");
        }

        return passed();
    }

    public Result testRectangle_setBounds_iiii() {

        Rectangle rect = new Rectangle();

        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        rect.setBounds(x, y, w, h);

        if (rect.x != x) {
            return failed("Rectangle.setBounds(int, int, int, int): "
                    + "set wrong x coordinate. returned: " + rect.x
                    + ", expected: " + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle.setBounds(int, int, int, int):"
                    + " set wrong y coordinate. returned: " + rect.y
                    + ", expected: " + y + ".");
        }
        if (rect.width != w) {
            return failed("Rectangle.setBounds(int, int, int, int): "
                    + "set wrong width. returned: " + rect.width
                    + ", expected: " + w + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle.setBounds(int, int, int, int): "
                    + "set wrong height. returned: " + rect.height
                    + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testRectangle_setBounds_rectangle() {

        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect1 = new Rectangle(x, y, w, h);
        Rectangle rect = new Rectangle();

        rect.setBounds(rect1);

        if (rect.x != x) {
            return failed("Rectangle.setBounds(Rectangle): "
                    + "set wrong x coordinate. returned: " + rect.x
                    + ", expected: " + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle.setBounds(Rectangle): "
                    + "set wrong y coordinate. returned: " + rect.y
                    + ", expected: " + y + ".");
        }
        if (rect.width != w) {
            return failed("Rectangle.setBounds(Rectangle): set wrong width. "
                    + "returned: " + rect.width + ", expected: " + w + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle.setBounds(Rectangle): set wrong height. "
                    + "returned: " + rect.height + ", expected: " + h + ".");
        }

        return passed();

    }

    public Result testRectangle_getBounds() {

        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(x, y, w, h);
        Rectangle rect_bounds;

        rect_bounds = rect.getBounds();

        if (rect_bounds.x != x) {
            return failed("Rectangle.getBounds(): wrong x coordinate. "
                    + "returned: " + rect_bounds.x + ", expected: " + x + ".");
        }
        if (rect_bounds.y != y) {
            return failed("Rectangle.getBounds(): wrong y coordinate. "
                    + "returned: " + rect_bounds.y + ", expected: " + y + ".");
        }
        if (rect_bounds.height != h) {
            return failed("Rectangle.getBounds(): wrong height. returned: "
                    + rect_bounds.height + ", expected: " + h + ".");
        }
        if (rect_bounds.width != w) {
            return failed("Rectangle.getBounds(): wrong width. returned: "
                    + rect_bounds.width + ", expected: " + w + ".");
        }

        return passed();
    }

    public Result testRectangle_getYXWH() {

        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(x, y, w, h);

        if ((int) rect.getX() != x) {
            return failed("Rectangle.getX(): wrong x coordinate. returned: "
                    + (int) rect.getX() + ", expected: " + x + ".");
        }
        if ((int) rect.getY() != y) {
            return failed("Rectangle.getY(): wrong y coordinate. returned: "
                    + (int) rect.getY() + ", expected: " + y + ".");
        }
        if ((int) rect.getWidth() != w) {
            return failed("Rectangle.getWidth(): wrong width. returned: "
                    + (int) rect.getWidth() + ", expected: " + w + ".");
        }
        if ((int) rect.getHeight() != h) {
            return failed("Rectangle.getHeight(): wrong height. returned: "
                    + (int) rect.getHeight() + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testRectangle_isEmpty() {

        Rectangle rect = new Rectangle(0, 0, 0, 0);
        Rectangle rect1 = new Rectangle(0, 0, 120, 10);

        if (!rect.isEmpty()) {
            return failed("Rectangle.isEmpty(): The empty rectangle like "
                    + "not empty. returned: true, expected: false.");
        }
        if (rect1.isEmpty()) {
            return failed("Rectangle.isEmpty(): The nonempty rectangle like "
                    + "empty. returned: true, expected: false.");
        }

        return passed();
    }

    public Result testRectangle_move() {

        Rectangle rect = new Rectangle(0, 0, 100, 100);
        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        rect.move(x, y);

        if (rect.x != x) {
            return failed("Rectangle.move(int, int): didn't change x coordinate."
                    + " returned: " + rect.x + ", expected: " + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle.move(int, int): didn't change y coordinate."
                    + " returned: " + rect.y + ", expected: " + y + ".");
        }

        return passed();
    }

    public Result testRectangle_getSize() {

        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(w, h);
        Dimension dem = rect.getSize();

        if (dem.height != h) {
            return failed("Rectangle.getSize(): wrong height. returned: "
                    + dem.height + ", expected: " + h + ".");
        }
        if (dem.width != w) {
            return failed("Rectangle.getSize(): wrong width. returned: "
                    + dem.width + ", expected: " + w + ".");
        }

        return passed();
    }

    public Result testRectangle_setLocation_ii() {

        Rectangle rect = new Rectangle();
        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        rect.setLocation(x, y);

        if (rect.x != x) {
            return failed("Rectangle.setLocation(int, int): "
                    + "didn't set x coordinate. returned: " + rect.x
                    + ", expected:" + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle.setLocation(int, int): "
                    + "didn't set y coordinate. returned: " + rect.y
                    + ", expected:" + y + ".");
        }

        return passed();
    }

    public Result testRectangle_reshape() {
        Rectangle rect = new Rectangle();

        int x = (int) (Math.random() * Integer.MAX_VALUE);
        int y = (int) (Math.random() * Integer.MAX_VALUE);
        int h = (int) (Math.random() * Integer.MAX_VALUE);
        int w = (int) (Math.random() * Integer.MAX_VALUE);

        rect.reshape(x, y, w, h);

        if (rect.x != x) {
            return failed("Rectangle.reshape(int, int, int, int): "
                    + "wrong x coordinate. returned: " + rect.x
                    + ", expected: " + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle.reshape(int, int, int, int): "
                    + "wrong y coordinate. returned: " + rect.y
                    + ", expected: " + y + ".");
        }
        if (rect.width != w) {
            return failed("Rectangle.reshape(int, int, int, int): "
                    + "wrong width. returned: " + rect.width + ", expected: "
                    + w + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle.reshape(int, int, int, int): "
                    + "wrong height. returned: " + rect.height + ", expected: "
                    + h + ".");
        }

        return passed();
    }

    public Result testRectangle_translate() {

        Rectangle rect = new Rectangle(0, 0, 100, 100);
        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        rect.translate(x, y);

        if (rect.x != x) {
            return failed("Rectangle.move(int, int): "
                    + "didn't change x coordinate. returned: " + rect.x
                    + ", expected: " + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle.move(int, int): "
                    + "didn't change y coordinate. returned: " + rect.y
                    + ", expected: " + y + ".");
        }

        return passed();
    }

    public Result testRectangle_union() {

        int offset = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 2);

        Rectangle rect = new Rectangle(0, 0, 100, 100);
        Rectangle rect1 = new Rectangle(0, 0, 100, 100);
        Rectangle union;

        union = rect.union(rect1);

        if (rect.union(rect1).x != 0) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong x coordinate (union of same rectangles). "
                    + "returned: " + rect.union(rect1).x + ", expected: 0.");
        }
        if (rect.union(rect1).y != 0) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong y coordinate (union of same rectangles). "
                    + "returned: " + rect.union(rect1).y + ", expected: 0.");
        }
        if (rect.union(rect1).width != 100) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong width (union of same rectangles). returned: "
                    + rect.union(rect1).width + ", expected: 100.");
        }
        if (rect.union(rect1).height != 100) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong height (union of rectangles). returned: "
                    + rect.union(rect1).height + ", expected: 100.");
        }

        rect1.setBounds(100, 100, 100, 100);

        if (rect.union(rect1).x != 0) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong x coordinate (common line). returned: "
                    + rect.union(rect1).x + ", expected: 0.");
        }
        if (rect.union(rect1).y != 0) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong y coordinate (common line). returned: "
                    + rect.union(rect1).y + ", expected: 0.");
        }
        if (rect.union(rect1).width != 200) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong width (common line). returned: "
                    + rect.union(rect1).width + ", expected: 200.");
        }
        if (rect.union(rect1).height != 200) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong height (common line). returned: "
                    + rect.union(rect1).height + ", expected: 200.");
        }

        rect1.setBounds(0, 100, 100, 100);

        if (rect.union(rect1).x != 0) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong x coordinate (common point). returned: "
                    + rect.union(rect1).x + ", expected: 0.");
        }
        if (rect.union(rect1).y != 0) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong y coordinate (common point). returned: "
                    + rect.union(rect1).y + ", expected: 0.");
        }
        if (rect.union(rect1).width != 100) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong width (common point). returned: "
                    + rect.union(rect1).width + ", expected: 100.");
        }
        if (rect.union(rect1).height != 200) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong height (common point). returned: "
                    + rect.union(rect1).height + ", expected: 200.");
        }

        rect1.setBounds(100 + offset, 100 + offset, 100, 100);

        if (rect.union(rect1).x != 0) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong x coordinate (do not intersect). returned: "
                    + rect.union(rect1).x + ", expected: 0.");
        }
        if (rect.union(rect1).y != 0) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong y coordinate (do not intersect). returned: "
                    + rect.union(rect1).y + ", expected: 0.");
        }
        if (rect.union(rect1).width != 200 + offset) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong width (do not intersect). returned: "
                    + rect.union(rect1).width + ", expected: " + (200 + offset)
                    + ".");
        }
        if (rect.union(rect1).height != (200 + offset)) {
            return failed("Rectangle.union(Rectangle): "
                    + "wrong height (do not intersect). returned: "
                    + rect.union(rect1).height + ", expected: "
                    + (200 + offset) + ".");
        }

        return passed();
    }

    public Result testRectangle_intersects() {

        Rectangle rect = new Rectangle(0, 0, 100, 100);
        Rectangle rect1 = new Rectangle(0, 0, 100, 100);

        if (!rect.intersects(rect1)) {
            return failed("Rectangle.intersects(Rectangle): "
                    + "rectangle does not intersect itself. "
                    + "returned: false, expected: true.");
        }

        rect1.setBounds(101, 101, 100, 100);
        if (rect.intersects(rect1)) {
            return failed("Rectangle.intersects(Rectangle):  "
                    + "wrong result for two nonintersecting rectangles. "
                    + "returned: true, expected: false.");
        }

        rect1.setBounds(99, 0, 100, 100);
        if (!rect.intersects(rect1)) {
            return failed("Rectangle.intersects(Rectangle): "
                    + "wrong result for linear intersection. "
                    + "returned: false, expected: true.");
        }

        rect1.setBounds(50, 50, 100, 100);
        if (!rect.intersects(rect1)) {
            return failed("Rectangle.intersects(Rectangle): "
                    + "wrong result for rectangular intersection. "
                    + " returned: false, expected: true.");
        }

        rect1.setBounds(99, 99, 100, 100);
        if (!rect.intersects(rect1)) {
            return failed("Rectangle.intersects(Rectangle): "
                    + "wrong result for point intersection. "
                    + "returned: false, expected: true.");
        }

        return passed();
    }

    public Result testRectangle_intersection() {

        Rectangle rect = new Rectangle(0, 0, 100, 100);
        Rectangle rect1 = new Rectangle(0, 0, 100, 100);

        if (rect.intersection(rect1).height == 0
                && rect.intersection(rect1).width == 0
                && rect.intersection(rect1).x == 0
                && rect.intersection(rect1).y == 0) {
            return failed("Rectangle.intersection(Rectangle): "
                    + "rectangle has empty intersection with itself. "
                    + "returned: empty intersection, expected: not empty");
        }

        rect1.setBounds(100, 0, 100, 100);
        if (rect.intersection(rect1).height == 0
                && rect.intersection(rect1).width == 0
                && rect.intersection(rect1).x == 0
                && rect.intersection(rect1).y == 0) {
            return failed("Rectangle.intersection(Rectangle): "
                    + "wrong result for linear intersection. "
                    + "returned: empty intersection, " + "expected: not empty");
        }

        rect1.setBounds(100, 100, 100, 100);
        if (rect.intersection(rect1).height == 0
                && rect.intersection(rect1).width == 0
                && rect.intersection(rect1).x == 0
                && rect.intersection(rect1).y == 0) {
            return failed("Rectangle.intersection(Rectangle): "
                    + "lose common point. returned: empty intersection, "
                    + "expected: not empty");
        }

        rect1.setBounds(200, 200, 10, 10);
        if (rect.intersection(rect1).height == 0
                && rect.intersection(rect1).width == 0
                && rect.intersection(rect1).x == 0
                && rect.intersection(rect1).y == 0) {
            return failed("Rectangle.intersection(Rectangle):"
                    + " nonempty intersection for"
                    + " two nonintersecting rectangles. "
                    + "returned: nonempty intersection," + " expected: empty");
        }

        return passed();
    }

    public Result testRectangle_init_dim() {
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Dimension dim = new Dimension(w, h);

        Rectangle rect = new Rectangle(dim);

        if (rect.x != 0 || rect.y != 0) {
            return failed("Rectangle.init(Dimension): x or y coordinate "
                    + "not zero.");
        }
        if (rect.width != w) {
            return failed("Rectangle.init(Dimension): wrong width. "
                    + "returned: " + rect.width + ", expected: " + w + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle.init(Dimension): wrong height. "
                    + "returned: " + rect.height + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testRectangle_init_point_dim() {
        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Point p = new Point(x, y);
        Dimension d = new Dimension(w, h);

        Rectangle rect = new Rectangle(p, d);

        if (rect.x != x) {
            return failed("Rectangle.init(Poin, Dimension): wrong x coordinate. "
                    + "returned: " + rect.x + ", expected: " + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle.init(Poin, Dimension): wrong y coordinate. "
                    + "returned: " + rect.y + ", expected: " + y + ".");
        }
        if (rect.width != w) {
            return failed("Rectangle.init(Poin, Dimension): wrong width. "
                    + "returned: " + rect.width + ", expected: " + w + ".");

        }
        if (rect.height != h) {
            return failed("Rectangle.init(Poin, Dimension): wrong height. "
                    + "returned: " + rect.height + ", expected: " + h + ".");

        }
        return passed();
    }

    public Result testRectangle_add() {

        Rectangle rect = new Rectangle(50, 50, 50, 50);

        rect.add(49, 49);

        if (!rect.inside(49, 49)) {
            return failed("Rectangle.add(int,int): point is out of Rectangle.");
        }

        Point p = new Point(45, 45);
        rect.add(p);

        if (!rect.inside(45, 45)) {
            return failed("Rectangle.add(Point): point is out of Rectangle.");
        }

        Rectangle rect1 = new Rectangle(0, 0, 12, 12);
        rect.add(rect1);

        if (!rect.contains(rect1)) {
            return failed("Rectangle.add(Rectangle): Rectangle is out of Rectangle.");
        }
        return passed();
    }

    public Result testRectangle_contains_ii() {
        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(x, y, w, h);

        if (rect.contains(x - 2, y - 2)) {
            return failed("Rectangle.contains(int, int): Rectangle contains outside point.");
        }
        if (!rect.contains(x + 2, y + 2)) {
            return failed("Rectangle.contains(int, int): Rectangle don't contain inside point.");
        }

        return passed();
    }

    public Result testRectangle_contains_point() {
        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int w = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle(x, y, w, h);
        Point p = new Point(x, y);
        Point p1 = new Point(x - 2, y - 2);
        Point p2 = new Point(x + 2, y + 2);

        if (!rect.contains(p)) {
            return failed("Rectangle.contains(Point): Rectangle don't contein"
                    + "left-top corner point.");
        }
        if (rect.contains(p1)) {
            return failed("Rectangle.contains(Point): Rectangle contains "
                    + "outside point.");
        }
        if (!rect.contains(p2)) {
            return failed("Rectangle.contains(Point): Rectangle don't contain "
                    + "inside point.");
        }

        return passed();
    }

    public Result testRectangle_setLocation_point() {

        Rectangle rect = new Rectangle();
        int x = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        int y = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);
        Point p = new Point(x, y);

        rect.setLocation(p);

        if (rect.x != x) {
            return failed("Rectangle.setLocation(Point): "
                    + "didn't set x coordinate. returned: " + rect.x
                    + ", expected:" + x + ".");
        }
        if (rect.y != y) {
            return failed("Rectangle.setLocation(Point): "
                    + "didn't set y coordinate. returned: " + rect.y
                    + ", expected:" + y + ".");
        }

        return passed();
    }

    public Result testRectamgle_setSize_ii() {
        int w = (int) (Math.random() * (Double.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Double.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle();

        rect.setSize(w, h);

        if (rect.width != w) {
            return failed("Rectangle.setSize(int,int): set wrong width. "
                    + "returned: " + rect.width + ", expected: " + w + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle.setSize(int,int): set wrong height. "
                    + "returned: " + rect.height + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testRectamgle_setSize_dim() {
        int w = (int) (Math.random() * (Double.MAX_VALUE - 1) + 1);
        int h = (int) (Math.random() * (Double.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle();
        Dimension d = new Dimension(w, h);

        rect.setSize(d);

        if (rect.width != w) {
            return failed("Rectangle.setSize(int,int): set wrong width. "
                    + "returned: " + rect.width + ", expected: " + w + ".");
        }
        if (rect.height != h) {
            return failed("Rectangle.setSize(int,int): set wrong height. "
                    + "returned: " + rect.height + ", expected: " + h + ".");
        }

        return passed();
    }

    public Result testRectamgle_setRect_dddd() {
        Random r = new Random();
        Double x = new Double(r.nextDouble() * 100);
        Double y = new Double(r.nextDouble() * 100);
        Double w = new Double(r.nextDouble() * 100);
        Double h = new Double(r.nextDouble() * 100);
        int offset = (int) (Math.random() * (Integer.MAX_VALUE - 1) + 1);

        Rectangle rect = new Rectangle();
        rect.setRect(x.doubleValue(), y.doubleValue(), w.doubleValue(), h
                .doubleValue());

        if (rect.outcode(x.doubleValue(), y.doubleValue()) != 0) {
            return failed("Rectangle.setRect(double,double,double,double): "
                    + "lose top-left corner point.");
        }
        if (rect.outcode(x.doubleValue() + w.doubleValue(), y.doubleValue()
                + h.doubleValue()) != 0) {
            return failed("Rectangle.setRect(double,double,double,double): "
                    + "lose right-bottom corner point.");
        }
        if (rect.outcode(x.doubleValue() + w.doubleValue() + offset, y
                .doubleValue()
                + h.doubleValue() + offset) == 0) {
            return failed("Rectangle.setRect(double,double,double,double): "
                    + "include some outside point.");
        }

        return passed();
    }
}