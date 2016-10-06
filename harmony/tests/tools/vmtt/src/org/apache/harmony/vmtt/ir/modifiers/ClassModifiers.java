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

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.1 $
 */

/**
 * This class describes class access and property modifiers 
 */

public class ClassModifiers implements Modifiers {
    
    public final short ACC_PUBLIC       = 0x0001;
    public final short ACC_FINAL        = 0x0010;
    public final short ACC_SUPER        = 0x0020;
    public final short ACC_INTERFACE    = 0x0200;
    public final short ACC_ABSTRACT     = 0x0400;
    public final short ACC_SYNTHETIC    = 0x1000;
    public final short ACC_ANNOTATION   = 0x2000;
    public final short ACC_ENUM         = 0x4000;
    
    protected final short[] modifiers = {ACC_PUBLIC, ACC_FINAL, ACC_SUPER,
                                         ACC_INTERFACE, ACC_ABSTRACT, ACC_SYNTHETIC,
                                         ACC_ANNOTATION, ACC_ENUM};
    
    protected final String[] modifierNames = {"PUBLIC", "FINAL", "SUPER",
                                              "INTERFACE", "ABSTRACT", "SYNTHETIC",
                                              "ANNOTATION", "ENUM"};
    
    public static ClassModifiers classModifiers = new ClassModifiers();
    
    public short[] getModifiers() {
        return modifiers;
    }
    
    public String[] getModifierNames() {
        return modifierNames;
    }
}
