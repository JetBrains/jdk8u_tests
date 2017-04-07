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
 * @author Alexey A. Ivanov
 */
package javax.swing.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

public class StyleConstantsTest extends TestCase {
    protected StyleConstants sc;

    protected SimpleAttributeSet attr = new SimpleAttributeSet();

    String message = "Test for StyleConstants";

    private Component component;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sc = new StyleConstants(message);
    }

    public void _testToString() {
        assertEquals(message, sc.toString());
    }

    protected void putAttribute(final Object key, final Object value) {
        attr.removeAttributes(attr);
        attr.addAttribute(key, value);
    }

    public void _testFirstLineIndent() {
        assertEquals(StyleConstants.ParagraphConstants.class, StyleConstants.FirstLineIndent
                .getClass());
        assertEquals("FirstLineIndent", StyleConstants.FirstLineIndent.toString());
    }

    public void _testGetFirstLineIndent() {
        assertTrue(0.0f == StyleConstants.getFirstLineIndent(SimpleAttributeSet.EMPTY));
        float value = 1.23f;
        putAttribute(StyleConstants.FirstLineIndent, new Float(value));
        assertTrue(value == StyleConstants.getFirstLineIndent(attr));
    }

    public void _testLeftIndent() {
        assertEquals(StyleConstants.ParagraphConstants.class, StyleConstants.LeftIndent
                .getClass());
        assertEquals("LeftIndent", StyleConstants.LeftIndent.toString());
    }

    public void _testGetLeftIndent() {
        assertTrue(0.0f == StyleConstants.getLeftIndent(SimpleAttributeSet.EMPTY));
        float value = 1.234f;
        putAttribute(StyleConstants.LeftIndent, new Float(value));
        assertTrue(value == StyleConstants.getLeftIndent(attr));
    }

    public void _testLineSpacing() {
        assertEquals(StyleConstants.ParagraphConstants.class, StyleConstants.LineSpacing
                .getClass());
        assertEquals("LineSpacing", StyleConstants.LineSpacing.toString());
    }

    public void _testGetLineSpacing() {
        assertTrue(0.0f == StyleConstants.getLineSpacing(SimpleAttributeSet.EMPTY));
        float value = 1.2345f;
        putAttribute(StyleConstants.LineSpacing, new Float(value));
        assertTrue(value == StyleConstants.getLineSpacing(attr));
    }

    public void _testRightIndent() {
        assertEquals(StyleConstants.ParagraphConstants.class, StyleConstants.RightIndent
                .getClass());
        assertEquals("RightIndent", StyleConstants.RightIndent.toString());
    }

    public void _testGetRightIndent() {
        assertTrue(0.0f == StyleConstants.getRightIndent(SimpleAttributeSet.EMPTY));
        float value = 1.23456f;
        putAttribute(StyleConstants.RightIndent, new Float(value));
        assertTrue(value == StyleConstants.getRightIndent(attr));
    }

    public void _testSpaceAbove() {
        assertEquals(StyleConstants.ParagraphConstants.class, StyleConstants.SpaceAbove
                .getClass());
        assertEquals("SpaceAbove", StyleConstants.SpaceAbove.toString());
    }

    public void _testGetSpaceAbove() {
        assertTrue(0.0f == StyleConstants.getSpaceAbove(SimpleAttributeSet.EMPTY));
        float value = 1.234567f;
        putAttribute(StyleConstants.SpaceAbove, new Float(value));
        assertTrue(value == StyleConstants.getSpaceAbove(attr));
    }

    public void _testSpaceBelow() {
        assertEquals(StyleConstants.ParagraphConstants.class, StyleConstants.SpaceBelow
                .getClass());
        assertEquals("SpaceBelow", StyleConstants.SpaceBelow.toString());
    }

    public void _testGetSpaceBelow() {
        assertTrue(0.0f == StyleConstants.getSpaceBelow(SimpleAttributeSet.EMPTY));
        float value = 1.2345678f;
        putAttribute(StyleConstants.SpaceBelow, new Float(value));
        assertTrue(value == StyleConstants.getSpaceBelow(attr));
    }

    public void _testALIGN_CENTER() {
        assertEquals(1, StyleConstants.ALIGN_CENTER);
    }

    public void _testALIGN_JUSTIFIED() {
        assertEquals(3, StyleConstants.ALIGN_JUSTIFIED);
    }

    public void _testALIGN_LEFT() {
        assertEquals(0, StyleConstants.ALIGN_LEFT);
    }

    public void _testALIGN_RIGHT() {
        assertEquals(2, StyleConstants.ALIGN_RIGHT);
    }

    public void _testComponentElementName() {
        assertEquals("component", StyleConstants.ComponentElementName);
    }

    public void _testIconElementName() {
        assertEquals("icon", StyleConstants.IconElementName);
    }

    public void _testAlignment() {
        assertEquals(StyleConstants.ParagraphConstants.class, StyleConstants.Alignment
                .getClass());
        assertEquals("Alignment", StyleConstants.Alignment.toString());
    }

    public void _testGetAlignment() {
        assertTrue(StyleConstants.ALIGN_LEFT == StyleConstants
                .getAlignment(SimpleAttributeSet.EMPTY));
        putAttribute(StyleConstants.Alignment, new Integer(StyleConstants.ALIGN_JUSTIFIED));
        assertTrue(StyleConstants.ALIGN_JUSTIFIED == StyleConstants.getAlignment(attr));
    }

    public void _testBidiLevel() {
        assertEquals(StyleConstants.CharacterConstants.class, StyleConstants.BidiLevel
                .getClass());
        assertEquals("bidiLevel", StyleConstants.BidiLevel.toString());
    }

    public void _testGetBidiLevel() {
        assertTrue(0 == StyleConstants.getBidiLevel(SimpleAttributeSet.EMPTY));
        int val = 1;
        putAttribute(StyleConstants.BidiLevel, new Integer(val));
        assertTrue(val == StyleConstants.getBidiLevel(attr));
    }

    public void _testFontSize() {
        assertEquals(StyleConstants.FontConstants.class, StyleConstants.FontSize.getClass());
        assertEquals("size", StyleConstants.FontSize.toString());
    }

    public void _testGetFontSize() {
        assertTrue(12 == StyleConstants.getFontSize(SimpleAttributeSet.EMPTY));
        int val = 2;
        putAttribute(StyleConstants.FontSize, new Integer(val));
        assertTrue(val == StyleConstants.getFontSize(attr));
    }

    public void _testBold() {
        assertEquals(StyleConstants.FontConstants.class, StyleConstants.Bold.getClass());
        assertEquals("bold", StyleConstants.Bold.toString());
    }

    public void _testIsBold() {
        assertFalse(StyleConstants.isBold(SimpleAttributeSet.EMPTY));
        putAttribute(StyleConstants.Bold, Boolean.TRUE);
        assertTrue(StyleConstants.isBold(attr));
        putAttribute(StyleConstants.Bold, Boolean.FALSE);
        assertFalse(StyleConstants.isBold(attr));
    }

    public void _testItalic() {
        assertEquals(StyleConstants.FontConstants.class, StyleConstants.Italic.getClass());
        assertEquals("italic", StyleConstants.Italic.toString());
    }

    public void _testIsItalic() {
        assertFalse(StyleConstants.isItalic(SimpleAttributeSet.EMPTY));
        putAttribute(StyleConstants.Italic, Boolean.TRUE);
        assertTrue(StyleConstants.isItalic(attr));
        putAttribute(StyleConstants.Italic, Boolean.FALSE);
        assertFalse(StyleConstants.isItalic(attr));
    }

    public void _testStrikeThrough() {
        assertEquals(StyleConstants.CharacterConstants.class, StyleConstants.StrikeThrough
                .getClass());
        assertEquals("strikethrough", StyleConstants.StrikeThrough.toString());
    }

    public void _testIsStrikeThrough() {
        assertFalse(StyleConstants.isStrikeThrough(SimpleAttributeSet.EMPTY));
        putAttribute(StyleConstants.StrikeThrough, Boolean.TRUE);
        assertTrue(StyleConstants.isStrikeThrough(attr));
        putAttribute(StyleConstants.StrikeThrough, Boolean.FALSE);
        assertFalse(StyleConstants.isStrikeThrough(attr));
    }

    public void _testSubscript() {
        assertEquals(StyleConstants.CharacterConstants.class, StyleConstants.Subscript
                .getClass());
        assertEquals("subscript", StyleConstants.Subscript.toString());
    }

    public void _testIsSubscript() {
        assertFalse(StyleConstants.isSubscript(SimpleAttributeSet.EMPTY));
        putAttribute(StyleConstants.Subscript, Boolean.TRUE);
        assertTrue(StyleConstants.isSubscript(attr));
        putAttribute(StyleConstants.Subscript, Boolean.FALSE);
        assertFalse(StyleConstants.isSubscript(attr));
    }

    public void _testSuperscript() {
        assertEquals(StyleConstants.CharacterConstants.class, StyleConstants.Superscript
                .getClass());
        assertEquals("superscript", StyleConstants.Superscript.toString());
    }

    public void _testIsSuperscript() {
        assertFalse(StyleConstants.isSuperscript(SimpleAttributeSet.EMPTY));
        putAttribute(StyleConstants.Superscript, Boolean.TRUE);
        assertTrue(StyleConstants.isSuperscript(attr));
        putAttribute(StyleConstants.Superscript, Boolean.FALSE);
        assertFalse(StyleConstants.isSuperscript(attr));
    }

    public void _testUnderline() {
        assertEquals(StyleConstants.CharacterConstants.class, StyleConstants.Underline
                .getClass());
        assertEquals("underline", StyleConstants.Underline.toString());
    }

    public void _testIsUnderline() {
        assertFalse(StyleConstants.isUnderline(SimpleAttributeSet.EMPTY));
        putAttribute(StyleConstants.Underline, Boolean.TRUE);
        assertTrue(StyleConstants.isUnderline(attr));
        putAttribute(StyleConstants.Underline, Boolean.FALSE);
        assertFalse(StyleConstants.isUnderline(attr));
    }

    public void _testSetFirstLineIndent() {
        attr.removeAttributes(attr);
        float val = 1.2f;
        StyleConstants.setFirstLineIndent(attr, val);
        assertTrue(val == ((Float) attr.getAttribute(StyleConstants.FirstLineIndent))
                .floatValue());
    }

    public void _testSetLeftIndent() {
        attr.removeAttributes(attr);
        float val = 1.23f;
        StyleConstants.setLeftIndent(attr, val);
        assertTrue(val == ((Float) attr.getAttribute(StyleConstants.LeftIndent)).floatValue());
    }

    public void _testSetLineSpacing() {
        attr.removeAttributes(attr);
        float val = 1.234f;
        StyleConstants.setLineSpacing(attr, val);
        assertTrue(val == ((Float) attr.getAttribute(StyleConstants.LineSpacing)).floatValue());
    }

    public void _testSetRightIndent() {
        attr.removeAttributes(attr);
        float val = 1.2345f;
        StyleConstants.setRightIndent(attr, val);
        assertTrue(val == ((Float) attr.getAttribute(StyleConstants.RightIndent)).floatValue());
    }

    public void _testSetSpaceAbove() {
        attr.removeAttributes(attr);
        float val = 1.23456f;
        StyleConstants.setSpaceAbove(attr, val);
        assertTrue(val == ((Float) attr.getAttribute(StyleConstants.SpaceAbove)).floatValue());
    }

    public void _testSetSpaceBelow() {
        attr.removeAttributes(attr);
        float val = 1.234567f;
        StyleConstants.setSpaceBelow(attr, val);
        assertTrue(val == ((Float) attr.getAttribute(StyleConstants.SpaceBelow)).floatValue());
    }

    public void _testSetAlignment() {
        attr.removeAttributes(attr);
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_JUSTIFIED);
        assertTrue(StyleConstants.ALIGN_JUSTIFIED == ((Integer) attr
                .getAttribute(StyleConstants.Alignment)).intValue());
    }

    public void _testSetBidiLevel() {
        attr.removeAttributes(attr);
        int val = 2;
        StyleConstants.setBidiLevel(attr, val);
        assertTrue(val == ((Integer) attr.getAttribute(StyleConstants.BidiLevel)).intValue());
    }

    public void _testSetFontSize() {
        attr.removeAttributes(attr);
        int val = 10;
        StyleConstants.setFontSize(attr, val);
        assertTrue(val == ((Integer) attr.getAttribute(StyleConstants.FontSize)).intValue());
    }

    public void _testSetBold() {
        attr.removeAttributes(attr);
        StyleConstants.setBold(attr, true);
        assertTrue(((Boolean) attr.getAttribute(StyleConstants.Bold)).booleanValue());
        StyleConstants.setBold(attr, false);
        assertFalse(((Boolean) attr.getAttribute(StyleConstants.Bold)).booleanValue());
    }

    public void _testSetItalic() {
        attr.removeAttributes(attr);
        StyleConstants.setItalic(attr, true);
        assertTrue(((Boolean) attr.getAttribute(StyleConstants.Italic)).booleanValue());
        StyleConstants.setItalic(attr, false);
        assertFalse(((Boolean) attr.getAttribute(StyleConstants.Italic)).booleanValue());
    }

    public void _testSetStrikeThrough() {
        attr.removeAttributes(attr);
        StyleConstants.setStrikeThrough(attr, true);
        assertTrue(((Boolean) attr.getAttribute(StyleConstants.StrikeThrough)).booleanValue());
        StyleConstants.setStrikeThrough(attr, false);
        assertFalse(((Boolean) attr.getAttribute(StyleConstants.StrikeThrough)).booleanValue());
    }

    public void _testSetSubscript() {
        attr.removeAttributes(attr);
        StyleConstants.setSubscript(attr, true);
        assertTrue(((Boolean) attr.getAttribute(StyleConstants.Subscript)).booleanValue());
        StyleConstants.setSubscript(attr, false);
        assertFalse(((Boolean) attr.getAttribute(StyleConstants.Subscript)).booleanValue());
    }

    public void _testSetSuperscript() {
        attr.removeAttributes(attr);
        StyleConstants.setSuperscript(attr, true);
        assertTrue(((Boolean) attr.getAttribute(StyleConstants.Superscript)).booleanValue());
        StyleConstants.setSuperscript(attr, false);
        assertFalse(((Boolean) attr.getAttribute(StyleConstants.Superscript)).booleanValue());
    }

    public void _testSetUnderline() {
        attr.removeAttributes(attr);
        StyleConstants.setUnderline(attr, true);
        assertTrue(((Boolean) attr.getAttribute(StyleConstants.Underline)).booleanValue());
        StyleConstants.setUnderline(attr, false);
        assertFalse(((Boolean) attr.getAttribute(StyleConstants.Underline)).booleanValue());
    }

    public void _testBackground() {
        assertEquals(StyleConstants.ColorConstants.class, StyleConstants.Background.getClass());
        assertEquals("background", StyleConstants.Background.toString());
    }

    public void _testGetBackground() {
        assertTrue(Color.black == StyleConstants.getBackground(SimpleAttributeSet.EMPTY));
        Color val = new Color(10, 11, 12);
        putAttribute(StyleConstants.Background, val);
        assertEquals(val, StyleConstants.getBackground(attr));
    }

    public void _testForeground() {
        assertEquals(StyleConstants.ColorConstants.class, StyleConstants.Foreground.getClass());
        assertEquals("foreground", StyleConstants.Foreground.toString());
    }

    public void _testGetForeground() {
        assertTrue(Color.black == StyleConstants.getForeground(SimpleAttributeSet.EMPTY));
        Color val = new Color(11, 12, 13);
        putAttribute(StyleConstants.Foreground, val);
        assertEquals(val, StyleConstants.getForeground(attr));
    }

    public void _testSetBackground() {
        attr.removeAttributes(attr);
        Color val = new Color(13, 14, 15);
        StyleConstants.setBackground(attr, val);
        assertEquals(val, attr.getAttribute(StyleConstants.Background));
    }

    public void _testSetForeground() {
        attr.removeAttributes(attr);
        Color val = new Color(15, 16, 17);
        StyleConstants.setForeground(attr, val);
        assertEquals(val, attr.getAttribute(StyleConstants.Foreground));
    }

    public void _testComponentAttribute() {
        assertEquals(StyleConstants.CharacterConstants.class, StyleConstants.ComponentAttribute
                .getClass());
        assertEquals("component", StyleConstants.ComponentAttribute.toString());
    }

    public void _testGetComponent() throws Exception {
        assertNull(StyleConstants.getComponent(SimpleAttributeSet.EMPTY));
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    component = new JLabel("test component");
                }
            });
        putAttribute(StyleConstants.ComponentAttribute, component);
        assertEquals(component, StyleConstants.getComponent(attr));
    }

    public void _testGetComponent_Null() {
        // Regression test for HARMONY-1767
        try {
            StyleConstants.getComponent(null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    public void _testSetComponent() throws Exception {
        attr.removeAttributes(attr);
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    component = new JLabel("test component");
                }
            });
        StyleConstants.setComponent(attr, component);
        assertEquals(2, attr.getAttributeCount());
        assertEquals(component,
                     attr.getAttribute(StyleConstants.ComponentAttribute));
        assertEquals(StyleConstants.ComponentElementName,
                     attr.getAttribute(AbstractDocument.ElementNameAttribute));
    }

    public void _testFontFamily() {
        assertEquals(StyleConstants.FontConstants.class, StyleConstants.FontFamily.getClass());
        assertEquals("family", StyleConstants.FontFamily.toString());
    }

    public void _testGetFontFamily() {
        assertEquals("Monospaced", StyleConstants.getFontFamily(SimpleAttributeSet.EMPTY));
        String val = "arial";
        putAttribute(StyleConstants.FontFamily, val);
        assertEquals(val, StyleConstants.getFontFamily(attr));
    }

    public void _testSetFontFamily() {
        attr.removeAttributes(attr);
        String val = "arial";
        StyleConstants.setFontFamily(attr, val);
        assertEquals(val, attr.getAttribute(StyleConstants.FontFamily));
    }

    public void _testGetIcon() {
        assertNull(StyleConstants.getIcon(SimpleAttributeSet.EMPTY));
        Icon val = new Icon() {
            public int getIconHeight() {
                return 0;
            }

            public int getIconWidth() {
                return 0;
            }

            public void paintIcon(final Component arg0, final Graphics arg1, final int arg2,
                    final int arg3) {
            }
        };
        putAttribute(StyleConstants.IconAttribute, val);
        assertEquals(val, StyleConstants.getIcon(attr));
    }

    public void _testSetIcon() {
        attr.removeAttributes(attr);
        Icon val = new Icon() {
            public int getIconHeight() {
                return 0;
            }

            public int getIconWidth() {
                return 0;
            }

            public void paintIcon(final Component arg0, final Graphics arg1, final int arg2,
                    final int arg3) {
            }
        };
        StyleConstants.setIcon(attr, val);
        assertEquals(2, attr.getAttributeCount());
        assertEquals(val, attr.getAttribute(StyleConstants.IconAttribute));
        assertEquals(StyleConstants.IconElementName, attr
                .getAttribute(AbstractDocument.ElementNameAttribute));
    }

    public void _testTabSet() {
        assertEquals(StyleConstants.ParagraphConstants.class, StyleConstants.TabSet.getClass());
        assertEquals("TabSet", StyleConstants.TabSet.toString());
    }

    public void _testGetTabSet() {
        assertNull(StyleConstants.getTabSet(SimpleAttributeSet.EMPTY));
        TabSet val = new TabSet(new TabStop[] { new TabStop(0.1f) });
        putAttribute(StyleConstants.TabSet, val);
        assertEquals(val, StyleConstants.getTabSet(attr));
    }

    public void _testSetTabSet() {
        attr.removeAttributes(attr);
        TabSet val = new TabSet(new TabStop[] { new TabStop(0.2f) });
        StyleConstants.setTabSet(attr, val);
        assertEquals(val, attr.getAttribute(StyleConstants.TabSet));
    }

    public void _testNameAttribute() {
        assertEquals(StyleConstants.class, StyleConstants.NameAttribute.getClass());
        assertEquals("name", StyleConstants.NameAttribute.toString());
    }

    public void _testResolveAttribute() {
        assertEquals(StyleConstants.class, StyleConstants.ResolveAttribute.getClass());
        assertEquals("resolver", StyleConstants.ResolveAttribute.toString());
    }

    public void _testModelAttribute() {
        assertEquals(StyleConstants.class, StyleConstants.ModelAttribute.getClass());
        assertEquals("model", StyleConstants.ModelAttribute.toString());
    }

    public void _testCharacterConstants() {
        assertEquals(StyleConstants.ColorConstants.class, StyleConstants.Background.getClass());
        assertEquals("background", StyleConstants.Background.toString());
        assertEquals(StyleConstants.ColorConstants.class, StyleConstants.Foreground.getClass());
        assertEquals("foreground", StyleConstants.Foreground.toString());
        assertEquals(StyleConstants.FontConstants.class, StyleConstants.Family.getClass());
        assertEquals("family", StyleConstants.Family.toString());
        assertEquals(StyleConstants.FontConstants.class, StyleConstants.Size.getClass());
        assertEquals("size", StyleConstants.Size.toString());
        assertEquals(StyleConstants.FontConstants.class, StyleConstants.Bold.getClass());
        assertEquals("bold", StyleConstants.Bold.toString());
        assertEquals(StyleConstants.FontConstants.class, StyleConstants.Italic.getClass());
        assertEquals("italic", StyleConstants.Italic.toString());
    }

    public void _testComposedTextAttribute() {
        assertEquals(StyleConstants.class, StyleConstants.ComposedTextAttribute.getClass());
        assertEquals("composed text", StyleConstants.ComposedTextAttribute.toString());
    }
}
