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
 * Created on 21.04.2005
 */
package org.apache.harmony.test.func.api.javax.swing.LookAndFeel;

import java.io.File;

import javax.swing.ComponentInputMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;

import org.apache.harmony.test.func.api.javax.swing.share.LookAndFeels.LAFImpl;
import org.apache.harmony.test.func.api.share.InteractiveTest;
import org.apache.harmony.share.Result;

public class LookAndFeelTest extends InteractiveTest {
    private static final String GIF_ICON_NAME = "gifIcon.gif";
    LookAndFeel instance;
    static final int BINDINGS_NUMBER = 10;

    public Result testInit() {
        instance = new LAFImpl();
        return passed();
    }

    public Result testInitialize() {
        return passed();
    }

    
    private boolean checkIsSubset(Object[] a1, Object[] a2) {
        if (a1.length > a2.length ) {
            return false;
        }
        for (int i = 0; i < a1.length; i++) {
            boolean find = false;
            for (int j = 0; j < a2.length; j++) {
                if (a2[j].equals( a1[i])) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                return false;
            }
        }
        return true;
    }

    public Result testLoadKeyBindings() {
        try {
            LookAndFeel.loadKeyBindings( null, null);
        } catch (NullPointerException e) {
            return failed("Unexcepcted NPE");
        }
        InputMap map = new InputMap();
        
        Object[] bindings = new Object[]{KeyStroke.getKeyStroke('a'), new Object()};
        map.put( (KeyStroke) bindings[0], bindings[1]);
        LookAndFeel.loadKeyBindings( map, null );
        if (!checkIsSubset(bindings, buildBindings( map))) {
            return failed("InputMap's prevoius contents changed after loadKeyBinding call(1)");
        }
        
        Object[] newBindings = new Object[]{KeyStroke.getKeyStroke( 'b' ), new Object(), KeyStroke.getKeyStroke( 'c'), new Object()};
        
        LookAndFeel.loadKeyBindings( map, newBindings);
        if (!checkIsSubset(bindings, buildBindings( map))) {
            return failed("InputMap's prevoius contents changed after loadKeyBinding call(2)");
        }
        
        if (!checkIsSubset(newBindings, buildBindings( map))) {
            return failed("KeyBindings load fails");
        }
        
        return passed();
    }
    
    

    private Object[] buildBindings(InputMap map) {
        Object[] allKeys = map.allKeys(); 
        Object[] newBinds = new Object[allKeys.length * 2];
        for (int i = 0; i < newBinds.length / 2; i++) {
            newBinds[i*2] = allKeys[i];
            newBinds[i*2 + 1] = map.get( (KeyStroke) allKeys[i]);
        }
        return newBinds;
    }
    
    private Object[] createBindings() {
        Object[] bindings = new Object[BINDINGS_NUMBER * 2]; 
        for (int i = 0; i < BINDINGS_NUMBER ; i++) {
            bindings[i*2] = KeyStroke.getKeyStroke( (char) i);
            bindings[i*2+1] = new Object();
        }
        return bindings;
    }

    public Result testMakeComponentInputMap() {
        Object[] bindings = new Object[]{};
        JComponent component = new JComponent() { };
        try {
            ComponentInputMap map = LookAndFeel.makeComponentInputMap(component, bindings); 
            checkForNull( map, "map created from zero-length array is not empty");
            if (map.getComponent() != component) {
                return failed("wrong component in the map");
            }
            bindings = new Object[]{KeyStroke.getKeyStroke('a'), new Object(), KeyStroke.getKeyStroke( 'b')};
            try {
                LookAndFeel.makeComponentInputMap(component, bindings );
                return failed("Expect an RuntimeException");
            } catch (RuntimeException ee ) {}
            bindings = createBindings();
            if (!checkIsSubset( bindings, buildBindings( LookAndFeel.makeComponentInputMap(component, bindings )))) {
                return failed("Failed to make input map");
            }
            
        } catch (RuntimeException e) {
          //  e.printStackTrace();
            return failed("Unexpected exception of " + e.getClass());
        }
        
        return passed();
    }
    
    private void checkForNull(InputMap map, String mes) {
        if (map != null) {
            Object[] array = map.allKeys();
            if (array != null) {
                if (array.length != 0) {
                    throw new RuntimeException(mes);
                }
            }
        }
    }
    

    public Result testMakeInputMap() {
        Object[] bindings = new Object[]{};
        try {
            try {
                checkForNull(LookAndFeel.makeInputMap( null ), "map created from null array is not empty");
            } catch (NullPointerException e ) {              
            }
            checkForNull( LookAndFeel.makeInputMap(bindings), "map created from zero-length array is not empty");
            bindings = new Object[]{KeyStroke.getKeyStroke('a'), new Object(), KeyStroke.getKeyStroke( 'b')};
            try {
                LookAndFeel.makeInputMap( bindings );
                return failed("Expect an RuntimeException");
            } catch (RuntimeException ee ) {}
            bindings = createBindings();
            if (!checkIsSubset( bindings, buildBindings( LookAndFeel.makeInputMap( bindings )))) {
                return failed("Failed to make input map");
            }
            
        } catch (RuntimeException e) {
          //  e.printStackTrace();
            return failed("Unexpected exception of " + e.getClass());
        }
        return passed();
    }



    public Result testMakeIcon() {
        Object icon = LookAndFeel.makeIcon( ImageIcon.class, "auxilary" + File.separator + GIF_ICON_NAME);
        if (icon == null) {
            return failed("couldn't create an icon");
        } 
        return passed();
    }
    
    public Result testUninstalBorder() {
        //TODO visual test development
        return passed();
    }
    
    public Result testInstallBorder() {
//      TODO visual test development
//        System.out.println(UIManager.getSystemLookAndFeelClassName());
//        final JFrame frame = new JFrame();
//        final JButton button = new JButton();
//        final JList list = new JList();
//        LookAndFeel.installBorder(button, "com.sun.java.swing.border.LineBorder");
//        //LookAndFeel.
//        //LookAndFeel.uninstallBorder( button );
//
//        button.setVisible( true );
//        Container pane = frame.getContentPane();
//        pane.setVisible( true );
//        pane.setLayout(new FlowLayout());
//        pane.add(button);
//        //LookAndFeel.;
//        frame.show();
//        setDescription("A border has been installed on the button");
//        if (!Asserting()) {
//            return failed(getFailureReason());
//        }
        
//        frame.show();

        return passed();
    } 
    public Result testInstallColorsAndFont() {
//      TODO visual test development
        return passed();
    }

    public Result testInstallColors() {
//      TODO visual test development        
        return passed();
    }

    
    
    public static void main(String[] args) {
        System.exit(new LookAndFeelTest().test(args));
    }
}

