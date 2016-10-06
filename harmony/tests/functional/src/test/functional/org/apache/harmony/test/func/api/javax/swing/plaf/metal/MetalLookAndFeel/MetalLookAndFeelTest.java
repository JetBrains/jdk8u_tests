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
package org.apache.harmony.test.func.api.javax.swing.plaf.metal.MetalLookAndFeel;

import javax.swing.plaf.metal.MetalLookAndFeel;
import org.apache.harmony.test.func.api.share.AutonomousTest;
import org.apache.harmony.share.Result;

public class MetalLookAndFeelTest extends AutonomousTest {

    public static void main(String[] args) {
        System.exit(new MetalLookAndFeelTest().test(args));
    }

    public Result testMetalLookAndFeel_allStaticGetters() {
                
        if (MetalLookAndFeel.getControlTextFont() == null) {
            failed("MetalLookAndFeel.getControlTextFont() return null.");
        }
        
        if (MetalLookAndFeel.getSystemTextFont() == null) {
            failed("MetalLookAndFeel.getSystemTextFont() return null.");
        }
        
        if (MetalLookAndFeel.getUserTextFont() == null) {
            failed("MetalLookAndFeel.getUserTextFont() return null.");
        }
        
        if (MetalLookAndFeel.getMenuTextFont() == null) {
            failed("MetalLookAndFeel.getMenuTextFont() return null.");
        }
        
        if (MetalLookAndFeel.getWindowTitleFont() == null) {
            failed("MetalLookAndFeel.getWindowTitleFont() return null.");
        }
        
        if (MetalLookAndFeel.getSubTextFont() == null) {
            failed("MetalLookAndFeel.getSubTextFont() return null.");
        }

        if (MetalLookAndFeel.getDesktopColor() == null) {
            failed("MetalLookAndFeel.getDesktopColor() return null.");
        }
        
        if (MetalLookAndFeel.getFocusColor() == null) {
            failed("MetalLookAndFeel.getFocusColor() return null.");
        }
        /*
         * 
         * if (MetalLookAndFeel.getWhite() != new ColorUIResource(Color.WHITE)) { 
         *      failed("MetalLookAndFeel.getWhite()return not white."); 
         * } 
         * 
         * if (MetalLookAndFeel.getBlack() != new ColorUIResource(Color.BLACK)) {
         *      failed("MetalLookAndFeel.getBlack() return not black."); 
         * }
         */
        
        if (MetalLookAndFeel.getControl() == null) {
            failed("MetalLookAndFeel.getControl() return null.");
        }
        
        if (MetalLookAndFeel.getControlShadow() == null) {
            failed("MetalLookAndFeel.getControlShadow() return null.");
        }
        
        if (MetalLookAndFeel.getControlDarkShadow() == null) {
            failed("MetalLookAndFeel.getControlDarkShadow() return null.");
        }
        /*
         * 
         * if (MetalLookAndFeel.getControlInfo() == null) {
         *      failed("MetalLookAndFeel.getControlInfo() return null."); 
         * }
         */
        
        if (MetalLookAndFeel.getControlHighlight() == null) {
            failed("MetalLookAndFeel.getControlHighlight() return null.");
        }
        /*
         * 
         * if (MetalLookAndFeel.getControlDisabled() == null) {
         *      failed("MetalLookAndFeel.getControlDisabled() return null."); 
         * }
         */
        
        if (MetalLookAndFeel.getPrimaryControl() == null) {
            failed("MetalLookAndFeel.getPrimaryControl() return null.");
        }
        
        if (MetalLookAndFeel.getPrimaryControlShadow() == null) {
            failed("MetalLookAndFeel.getPrimaryControlShadow() return null.");
        }
        
        if (MetalLookAndFeel.getPrimaryControlDarkShadow() == null) {
          failed("MetalLookAndFeel.getPrimaryControlDarkShadow() return null.");
        }
        
        if (MetalLookAndFeel.getPrimaryControlInfo() == null) {
            failed("MetalLookAndFeel.getPrimaryControlInfo return null.");
        }
        
        if (MetalLookAndFeel.getPrimaryControlHighlight() == null) {
            failed("MetalLookAndFeel.getPrimaryControlHighlight return null.");
        }
        
        if (MetalLookAndFeel.getSystemTextColor() == null) {
            failed("MetalLookAndFeel.getSystemTextColor() return null.");
        }
        
        if (MetalLookAndFeel.getControlTextColor() == null) {
            failed("MetalLookAndFeel.getControlTextColor() return null.");
        }
        
        if (MetalLookAndFeel.getInactiveControlTextColor() == null) {
            failed("MetalLookAndFeel.getInactiveControlTextColor() return null.");
        }
        
        if (MetalLookAndFeel.getInactiveSystemTextColor() == null) {
            failed("MetalLookAndFeel.getInactiveSystemTextColor() return null.");
        }
        
        if (MetalLookAndFeel.getUserTextColor() == null) {
            failed("MetalLookAndFeel.getUserTextColor() return null.");
        }
        
        if (MetalLookAndFeel.getTextHighlightColor() == null) {
            failed("MetalLookAndFeel.getTextHighlightColor() return null.");
        }
        
        if (MetalLookAndFeel.getHighlightedTextColor() == null) {
            failed("MetalLookAndFeel.getHighlightedTextColor() return null.");
        }
        
        if (MetalLookAndFeel.getWindowBackground() == null) {
            failed("MetalLookAndFeel.getWindowBackground() return null.");
        }
        
        if (MetalLookAndFeel.getWindowTitleBackground() == null) {
            failed("MetalLookAndFeel.getWindowTitleBackground() return null.");
        }
        /*
         * 
         * if (MetalLookAndFeel.getWindowTitleForeground() == null) {
         *   failed("MetalLookAndFeel.getWindowTitleForeground() return null."); 
         * }
         */
        
        if (MetalLookAndFeel.getWindowTitleInactiveBackground() == null) {
            failed("MetalLookAndFeel.getWindowTitleInactiveBackground() "
                    + "return null.");
        }
        
        if (MetalLookAndFeel.getWindowTitleInactiveForeground() == null) {
            failed("MetalLookAndFeel.getWindowTitleInactiveForeground() "
                    + "return null.");
        }

        
        if (MetalLookAndFeel.getMenuBackground() == null) {
            failed("MetalLookAndFeel.getMenuBackground() return null.");
        }
        
        if (MetalLookAndFeel.getMenuForeground() == null) {
            failed("MetalLookAndFeel.getMenuForeground() return null.");
        }
        
        if (MetalLookAndFeel.getMenuSelectedBackground() == null) {
            failed("MetalLookAndFeel.getMenuSelectedBackground() return null.");
        }
        
        if (MetalLookAndFeel.getMenuSelectedForeground() == null) {
            failed("MetalLookAndFeel.getMenuSelectedForeground() return null.");
        }
        
        if (MetalLookAndFeel.getMenuDisabledForeground() == null) {
            failed("MetalLookAndFeel.getMenuDisabledForeground() return null.");
        }
        
        if (MetalLookAndFeel.getSeparatorBackground() == null) {
            failed("MetalLookAndFeel.getSeparatorBackground() return null.");
        }
        
        if (MetalLookAndFeel.getSeparatorForeground() == null) {
            failed("MetalLookAndFeel.getSeparatorForeground() return null.");
        }

        if (MetalLookAndFeel.getAcceleratorForeground() == null) {
            failed("MetalLookAndFeel.getAcceleratorForeground() return null.");
        }
        
        if (MetalLookAndFeel.getAcceleratorSelectedForeground() == null) {
            failed("MetalLookAndFeel.getAcceleratorSelectedForeground() "
                    + "return null.");
        }

        return passed();
    }
}
