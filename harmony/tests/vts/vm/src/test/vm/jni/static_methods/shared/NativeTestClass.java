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
/** 
 * @author Petr Ivanov
 * @version $Revision: 1.2 $
 */  
/*
 * Created on 03.11.2005
 */
package org.apache.harmony.vts.test.vm.jni.static_methods;

/**
 * @author Petr Ivanov
 *
 * Class used in static methods tests.
 */

class NativeTestClass {
    static boolean retz;
    static byte retb;
    static char retc;
    static short rets;
    static int reti;
    static long retj;
    static float retf;
    static double retd;

    static native void method(boolean z1, boolean z2, boolean z3, boolean z4);

    static native void method(byte b1, byte b2, byte b3, byte b4);

    static native void method(char c1, char c2, char c3, char c4);

    static native void method(short s1, short s2, short s3, short s4);

    static native void method(int i1, int i2, int i3, int i4);

    static native void method(long j1, long j2, long j3, long j4);

    static native void method(float f1, float f2, float f3, float f4);

    static native void method(double d1, double d2, double d3, double d4);

    static public native boolean method(boolean z1);

    static public native boolean method(boolean z1, boolean z2);

    static public native byte method(byte b1);

    static public native byte method(byte b1, byte b2);

    static public native char method(char c1);

    static public native char method(char c1, char c2);

    static public native short method(short s1);

    static public native short method(short s1, short s2);

    static public native int method(int i1);

    static public native int method(int i1, int i2);

    static public native long method(long j1);

    static public native long method(long j1, long j2);

    static public native float method(float f1);

    static public native float method(float f1, float f2);

    static public native double method(double d1);

    static public native double method(double d1, double d2);

    static protected native boolean method(boolean[] z1);

    static protected native boolean method(boolean[] z1, boolean[] z2);

    static protected native byte method(byte[] z1);

    static protected native byte method(byte[] z1, byte[] z2);

    static protected native char method(char[] z1);

    static protected native char method(char[] z1, char[] z2);

    static protected native short method(short[] z1);

    static protected native short method(short[] z1, short[] z2);

    static protected native int method(int[] z1);

    static protected native int method(int[] z1, int[] z2);

    static protected native long method(long[] z1);

    static protected native long method(long[] z1, long[] z2);

    static protected native float method(float[] z1);

    static protected native float method(float[] z1, float[] z2);

    static protected native double method(double[] z1);

    static protected native double method(double[] z1, double[] z2);

    static private native boolean method(Boolean z1);

    static private native boolean method(Boolean z1, Boolean z2);

    static private native byte method(Byte b1);

    static private native byte method(Byte b1, Byte b2);

    static private native char method(Character c1);

    static private native char method(Character c1, Character c2);

    static private native short method(Short s1);

    static private native short method(Short s1, Short s2);

    static private native int method(Integer i1);

    static private native int method(Integer i1, Integer i2);

    static private native long method(Long j1);

    static private native long method(Long j1, Long j2);

    static private native float method(Float f1);

    static private native float method(Float f1, Float f2);

    static private native double method(Double d1);

    static private native double method(Double d1, Double d2);

    static private native Boolean method(boolean z1, boolean z2, boolean z3);

    static private native Byte method(byte b1, byte b2, byte b3);

    static private native Character method(char c1, char c2, char c3);

    static private native Short method(short s1, short s2, short s3);

    static private native Integer method(int i1, int i2, int i3);

    static private native Long method(long j1, long j2, long j3);

    static private native Float method(float f1, float f2, float f3);

    static private native Double method(double d1, double d2, double d3);

    static private native long method(byte b, char c, short s, int i, long j,
            Byte ob, Character oc, Short os, Integer oi, Long oj,
            String sb, String ss, String si, String sj);
}
