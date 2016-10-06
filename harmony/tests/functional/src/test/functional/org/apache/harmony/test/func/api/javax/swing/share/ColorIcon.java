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
/*
 * Created on 12.04.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.share;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 *  
 */
public class ColorIcon implements Icon {
    private Color color;

    public int getIconHeight() {
        return 32;
    }

    public int getIconWidth() {
        return 32;
    }

    public ColorIcon(Color color) {
        this.color = color == null ? Color.BLACK : color;
    }

    public void paintIcon(Component arg0, Graphics arg1, int arg2, int arg3) {
        arg1.setColor(color);
        arg1.drawRect(arg2, arg3, getIconWidth(),  getIconHeight());
    }
    
    public boolean equals(Object arg0) {
        if(!(arg0 instanceof ColorIcon)) {
            return false;
        }
        return ((ColorIcon) arg0).color.equals(color);
    }
    public int hashCode() {
        return color.hashCode();
    }
    
    public String toString() {
        return "ColorIcon: " + color.toString();
    }
}