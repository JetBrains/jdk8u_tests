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
 * @author Gregory Shimansky, Petr Ivanov
 * @version $Revision: 1.3 $
 */

/*
 * Created on 01.12.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_methods;

/**
 * Class used in object methods tests.
 */

class NativeTestClass {
    boolean retz;
    byte retb;
    char retc;
    short rets;
    int reti;
    long retj;
    float retf;
    double retd;

    native void method(boolean z1, boolean z2, boolean z3, boolean z4);

    native void method(byte b1, byte b2, byte b3, byte b4);

    native void method(char c1, char c2, char c3, char c4);

    native void method(short s1, short s2, short s3, short s4);

    native void method(int i1, int i2, int i3, int i4);

    native void method(long j1, long j2, long j3, long j4);

    native void method(float f1, float f2, float f3, float f4);

    native void method(double d1, double d2, double d3, double d4);

    public native boolean method(boolean z1);

    public native boolean method(boolean z1, boolean z2);

    public native byte method(byte b1);

    public native byte method(byte b1, byte b2);

    public native char method(char c1);

    public native char method(char c1, char c2);

    public native short method(short s1);

    public native short method(short s1, short s2);

    public native int method(int i1);

    public native int method(int i1, int i2);

    public native long method(long j1);

    public native long method(long j1, long j2);

    public native float method(float f1);

    public native float method(float f1, float f2);

    public native double method(double d1);

    public native double method(double d1, double d2);

    protected native boolean method(boolean[] z1);

    protected native boolean method(boolean[] z1, boolean[] z2);

    protected native byte method(byte[] z1);

    protected native byte method(byte[] z1, byte[] z2);

    protected native char method(char[] z1);

    protected native char method(char[] z1, char[] z2);

    protected native short method(short[] z1);

    protected native short method(short[] z1, short[] z2);

    protected native int method(int[] z1);

    protected native int method(int[] z1, int[] z2);

    protected native long method(long[] z1);

    protected native long method(long[] z1, long[] z2);

    protected native float method(float[] z1);

    protected native float method(float[] z1, float[] z2);

    protected native double method(double[] z1);

    protected native double method(double[] z1, double[] z2);

    private native boolean method(Boolean z1);

    private native boolean method(Boolean z1, Boolean z2);

    private native byte method(Byte b1);

    private native byte method(Byte b1, Byte b2);

    private native char method(Character c1);

    private native char method(Character c1, Character c2);

    private native short method(Short s1);

    private native short method(Short s1, Short s2);

    private native int method(Integer i1);

    private native int method(Integer i1, Integer i2);

    private native long method(Long j1);

    private native long method(Long j1, Long j2);

    private native float method(Float f1);

    private native float method(Float f1, Float f2);

    private native double method(Double d1);

    private native double method(Double d1, Double d2);

    private native Boolean method(boolean z1, boolean z2, boolean z3);

    private native Byte method(byte b1, byte b2, byte b3);

    private native Character method(char c1, char c2, char c3);

    private native Short method(short s1, short s2, short s3);

    private native Integer method(int i1, int i2, int i3);

    private native Long method(long j1, long j2, long j3);

    private native Float method(float f1, float f2, float f3);

    private native Double method(double d1, double d2, double d3);

    private native long method(byte b, char c, short s, int i, long j,
            Byte ob, Character oc, Short os, Integer oi, Long oj,
            String sb, String ss, String si, String sj);
}
