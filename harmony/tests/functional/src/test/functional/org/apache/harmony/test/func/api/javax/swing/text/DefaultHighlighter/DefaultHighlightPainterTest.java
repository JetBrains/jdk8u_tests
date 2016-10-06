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

package org.apache.harmony.test.func.api.javax.swing.text.DefaultHighlighter;

import java.awt.Color;
import java.awt.Shape;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.Position;

import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedDefaultHighlightPainter;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedGraphics;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedJTextComponent;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedLookAndFeel;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedShape;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedUILog;
import org.apache.harmony.test.func.api.javax.swing.share.InstrumentedView;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class DefaultHighlightPainterTest extends MultiCase {
    public static void main(String[] args)
            throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new InstrumentedLookAndFeel());
        System.exit(new DefaultHighlightPainterTest().test(args));
    }

    public Result testDefaultHighlightPainterConstructor() {
        InstrumentedUILog.clear();

        new InstrumentedDefaultHighlightPainter(Color.YELLOW);

        if (!InstrumentedUILog.equals(new Object[][] {})) {
            InstrumentedUILog.printLog();
            return failed("extected DefaultHighlightPainter(Color) not to call anything");
        }

        return passed();
    }

    public Result testDefaultHighlightPainterPaintLayer() {
        InstrumentedDefaultHighlightPainter idhp = new InstrumentedDefaultHighlightPainter();
        InstrumentedGraphics ig = new InstrumentedGraphics();
        InstrumentedShape is = new InstrumentedShape();
        InstrumentedJTextComponent ijtc = new InstrumentedJTextComponent();
        InstrumentedView iv = new InstrumentedView();
        Color c = idhp.getColor();

        InstrumentedUILog.clear();

        Shape s = idhp.paintLayer(ig, 0, 1, is, ijtc, iv);

        if (s == is) {
            return failed("expected paintLayer to return its parameter");
        }

        if (!InstrumentedUILog
                .equals(new Object[][] {
                        /* 1 */{ "DefaultHighlightPainter.paintLayer", ig,
                                "0", "1", is, ijtc, iv },
                        /* 2 */{ "DefaultHighlightPainter.getColor" },
                        /* 3 */{ "Graphics.setColor", c },
                        /* 4 */{ "View.getStartOffset" },
                        /* 5 */{ "Element.getStartOffset" },
                        /* 6 */{ "View.getEndOffset" },
                        /* 7 */{ "Element.getEndOffset" },
                        /* 8 */{ "View.modelToView", "0",
                                Position.Bias.Forward, "1",
                                Position.Bias.Backward, is },
                        /* 9 */{ "View.modelToView", "0", is,
                                Position.Bias.Forward },
                        /* 10 */{ "View.getEndOffset" },
                        /* 11 */{ "Element.getEndOffset" },
                        /* 12 */{ "View.modelToView", "1", is,
                                Position.Bias.Backward },
                        /* 13 */{ "Shape.getBounds" },
                        /* 14 */{ "Shape.getBounds" },
                        /* 15 */{ "Rectangle.add",
                                InstrumentedUILog.ANY_NON_NULL_VALUE },
                        /* 16 */{ "Graphics.fillRect", "0", "0", "0", "0" }, })) {
            InstrumentedUILog.printLog();
            return failed("expected paintLayer() to call another sequence of events");
        }

        return passed();
    }
    

}