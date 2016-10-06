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

package org.apache.harmony.test.func.api.java.awt.FontMetrics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;

import org.apache.harmony.share.Test;

/*
 * May 27, 2005
 */
public class FontMetricsTest extends Test {

    private class MyFontMetrics extends FontMetrics {

        public MyFontMetrics(Font arg0) {
            super(arg0);
        }
    
    }
    public int test() {
        Font f = new Font("Arial", 0, 10);
        log.info("Reached first test line");

        log.info("Font family: " + f.getFamily());
        log.info("Font name: " + f.getFontName());
        log.info("Name: " + f.getName());
        log.info("Size: " + f.getSize());
        
        FontMetrics m = Toolkit.getDefaultToolkit().getFontMetrics(f);
        
        log.info("Metrics ascent: " + m.getAscent());
        log.info("Metrics descent: " + m.getDescent());
        log.info("Metrics leading: " + m.getLeading());
        log.info("Metrics height: " + m.getHeight());
        
        if (!f.equals(m.getFont()))
            return fail("FontMetric.getFont returned incorrect value");
        log.info("ok");
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new FontMetricsTest().test());
    }
}
