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
 * This class describes method access and property flags
 */

public class MethodModifiers implements Modifiers {

    public final short ACC_PUBLIC       = 0x0001;
    public final short ACC_PRIVATE      = 0x0002;      
    public final short ACC_PROTECTED    = 0x0004;
    public final short ACC_STATIC       = 0x0008;
    public final short ACC_FINAL        = 0x0010;
    public final short ACC_SYNCHRONIZED = 0x0020;
    public final short ACC_BRIDGE       = 0x0040;
    public final short ACC_VARARGS      = 0x0080;
    public final short ACC_NATIVE       = 0x0100;
    public final short ACC_ABSTRACT     = 0x0400;
    public final short ACC_STRICT       = 0x0800;
    public final short ACC_SYNTHETIC    = 0x1000;
    
    protected final short[] modifiers = {ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED,
                                         ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED,
                                         ACC_BRIDGE, ACC_VARARGS, ACC_NATIVE,
                                         ACC_ABSTRACT, ACC_STRICT, ACC_SYNTHETIC};
    
    protected final String[] modifierNames = {"PUBLIC", "PRIVATE", "PROTECTED",
                                              "STATIC", "FINAL", "SYNCHRONIZED",
                                              "BRIDGE", "VARARGS", "NATIVE",
                                              "ABSTRACT", "STRICT", "SYNTHETIC"};
    
    public static MethodModifiers methodModifiers = new MethodModifiers();
    
    public short[] getModifiers() {
        return modifiers;
    }
    
    public String[] getModifierNames() {
        return modifierNames;
    }
}
