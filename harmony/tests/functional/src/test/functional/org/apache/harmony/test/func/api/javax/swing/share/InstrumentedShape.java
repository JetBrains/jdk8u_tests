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
 
package org.apache.harmony.test.func.api.javax.swing.share;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class InstrumentedShape implements Shape {

    public boolean contains(double arg0, double arg1) {
        InstrumentedUILog.add(new Object[] {"Shape.contains", "" +  arg0, "" +  arg1} );
        return false;
    }

    public boolean contains(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] {"Shape.contains", "" +  arg0, "" +  arg1, "" +  arg2, "" +  arg3} );
        return false;
    }

    public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
        InstrumentedUILog.add(new Object[] {"Shape.intersects", "" +  arg0, "" +  arg1, "" +  arg2, "" +  arg3} );
        return false;
    }

    public Rectangle getBounds() {
        InstrumentedUILog.add(new Object[] {"Shape.getBounds"} );
        return new InstrumentedRectangle();
    }

    public boolean contains(Point2D arg0) {
        InstrumentedUILog.add(new Object[] {"Shape.contains",  arg0} );
        return false;
    }

    public Rectangle2D getBounds2D() {
        InstrumentedUILog.add(new Object[] {"Shape.getBounds2D"} );
        return null;
    }

    public boolean contains(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] {"Shape.contains",  arg0} );
        return false;
    }

    public boolean intersects(Rectangle2D arg0) {
        InstrumentedUILog.add(new Object[] {"Shape.intersects",  arg0} );
        return false;
    }

    public PathIterator getPathIterator(AffineTransform arg0) {
        InstrumentedUILog.add(new Object[] {"Shape.getPathIterator",  arg0} );
        return null;
    }

    public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
        InstrumentedUILog.add(new Object[] {"Shape.getPathIterator",  arg0, "" +  arg1} );
        return null;
    }

}
