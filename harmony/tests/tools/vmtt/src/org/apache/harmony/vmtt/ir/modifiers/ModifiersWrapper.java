/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.apache.harmony.vmtt.ir.modifiers;

import java.util.regex.PatternSyntaxException;

public class ModifiersWrapper {

    public static String toString(short modifier, Modifiers m) {
        StringBuffer value = new StringBuffer();
        for (int i = 0; i < m.getModifiers().length; i++) {
            if ((modifier & m.getModifiers()[i]) == m.getModifiers()[i]) {
                value.append(m.getModifierNames()[i] + " ");
            }
        }
        return value.toString();
    }
    
    public static short getModifiers(String str, Modifiers m) { 
        short value = 0x0000;
        try {
            String[] arrModifiers = str.split("\\s");
            for (int i = 0; i < arrModifiers.length; i++) {
                for (int j = 0; j < m.getModifierNames().length; j++) {
                    if (arrModifiers[i].equals(m.getModifierNames()[j])) {
                        value |= m.getModifiers()[j];
                        continue;
                    }
                }
            }
        } catch (PatternSyntaxException e) {
        }
        return value;
    } 

    public static boolean isModifier(String str, Modifiers m) {
        String uStr = str.toUpperCase();
        for (int i = 0; i < m.getModifierNames().length; i++) {
            if (uStr.equals(m.getModifierNames()[i])) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isModifier(short value, Modifiers m) {
        for (int i = 0; i < m.getModifiers().length; i++) {
            if (m.getModifiers()[i] == value) {
                return true;
            }
        }
        return false;
    }
}
