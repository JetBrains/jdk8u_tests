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
package org.apache.harmony.test.func.api.java.awt.Font;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.font.TransformAttribute;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.Map;

import org.apache.harmony.share.Test;

public class FontTest extends Test{

    public int test() {
        
        java.awt.Font f = new Font("Arial", Font.BOLD, 12);

        Object peer = f.getPeer();
        if (peer == null || !(peer instanceof java.awt.peer.FontPeer))
            return -1;

        {
            String name = f.getName();
            if (!name.equals("Arial"))
                return -2;

            int size = f.getSize();
            if (size != 12)
                return -3;

            int style = f.getStyle();
            if (style != Font.BOLD)
                return -4;

            AffineTransform trans = f.getTransform();
            if (trans.getType() != AffineTransform.TYPE_IDENTITY)
                return -5;
        }

        Font larger = f.deriveFont(14.0f);
        
        {
            String name = larger.getName();
            if (!name.equals("Arial"))
                return -6;

            int size = larger.getSize();
            if (size != 14)
                return -7;

            int style = larger.getStyle();
            if (style != Font.BOLD)
                return -8;

            AffineTransform trans = larger.getTransform();
            if (trans.getType() != AffineTransform.TYPE_IDENTITY)
                return -9;
        }

        Font italic = f.deriveFont(Font.ITALIC);
        
        {
            String name = italic.getName();
            if (!name.equals("Arial"))
                return -11;

            int size = italic.getSize();
            if (size != 12)
                return -11;

            int style = italic.getStyle();
            if (style != Font.ITALIC)
                return -12;

            AffineTransform trans = italic.getTransform();
            if (trans.getType() != AffineTransform.TYPE_IDENTITY)
                return -13;
        }

        Font same = f.deriveFont(Font.BOLD);

        {
            String name = same.getName();
            if (!name.equals("Arial"))
                return -14;

            int size = same.getSize();
            if (size != 12)
                return -15;

            int style = same.getStyle();
            if (style != Font.BOLD)
                return -16;

            AffineTransform trans = same.getTransform();
            if (trans.getType() != AffineTransform.TYPE_IDENTITY)
                return -17;
        }
        
        if (!same.equals(f))
            return -18;
        
        if (italic.equals(f))
            return -19;
        
        if (larger.equals(f))
            return -20;
        
        Map attr = new Hashtable();
        
        attr.put(TextAttribute.FAMILY, "Arial");
        attr.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        attr.put(TextAttribute.SIZE, new Float(12));
        attr.put(TextAttribute.TRANSFORM, 
            new TransformAttribute(new AffineTransform()));
                        
        Font mapsrc = new Font(attr);
        
        {
            String name = mapsrc.getName();
            if (!name.equals("Arial"))
                return -21;

            int size = mapsrc.getSize();
            if (size != 12)
                return -22;

            int style = mapsrc.getStyle();
            if (style != Font.BOLD)
                return -23;

            AffineTransform trans = mapsrc.getTransform();
            if (trans.getType() != AffineTransform.TYPE_IDENTITY)
                return -24;
        }
        
        if (!mapsrc.equals(f))
            return -25;
        
        return 0;
    }

    public static void main(String[] args) {
        FontTest test = new FontTest();
        int result = test.test();
        if (result < 0) {
            System.exit(test.fail("Result is " + result + ", see source code for details"));
        }
        else
        {
            System.exit(test.pass());
        }
    }
}