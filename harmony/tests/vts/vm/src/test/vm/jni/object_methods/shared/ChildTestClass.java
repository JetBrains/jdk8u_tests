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
 * @version $Revision: 1.1.1.1 $
 */  
/*
 * Created on 01.12.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_methods;

/**
 * @author Gregory Shimansky
 *
 * Class used in object methods tests.
 */
class ChildTestClass extends TestClass {
/*    boolean retz;
    byte retb;
    char retc;
    short rets;
    int reti;
    long retj;
    float retf;
    double retd;
*/
    void method(boolean z1, boolean z2, boolean z3, boolean z4) {
        retz = false;
    }

    void method(byte b1, byte b2, byte b3, byte b4) {
        retb = 1;
    }

    void method(char c1, char c2, char c3, char c4) {
        retc = 0;
    }

    void method(short s1, short s2, short s3, short s4) {
        rets = 0;
    }

    void method(int i1, int i2, int i3, int i4) {
        reti = 0;
    }

    void method(long j1, long j2, long j3, long j4) {
        retj = 0;
    }

    void method(float f1, float f2, float f3, float f4) {
        retf = 0;
    }

    void method(double d1, double d2, double d3, double d4) {
        retd = 0;
    }

    public boolean method(boolean z1) {
        return false;
    }

    public boolean method(boolean z1, boolean z2) {
        return false;
    }

    public byte method(byte b1) {
        return 0;
    }

    public byte method(byte b1, byte b2) {
        return 0;
    }

    public char method(char c1) {
        return 0;
    }

    public char method(char c1, char c2) {
        return 0;
    }

    public short method(short s1) {
        return 0;
    }

    public short method(short s1, short s2) {
        return 0;
    }

    public int method(int i1) {
        return 0;
    }

    public int method(int i1, int i2) {
        return 0;
    }

    public long method(long j1) {
        return 0;
    }

    public long method(long j1, long j2) {
        return 0;
    }

    public float method(float f1) {
        return 0;
    }

    public float method(float f1, float f2) {
        return 0;
    }

    public double method(double d1) {
        return 0;
    }

    public double method(double d1, double d2) {
        return 0;
    }

    protected boolean method(boolean[] z1) {
        return false;
    }

    protected boolean method(boolean[] z1, boolean[] z2) {
        return false;
    }

    protected byte method(byte[] z1) {
        return 0;
    }

    protected byte method(byte[] z1, byte[] z2) {
        return 0;
    }

    protected char method(char[] z1) {
        return 0;
    }

    protected char method(char[] z1, char[] z2) {
        return 0;
    }

    protected short method(short[] z1) {
        return 0;
    }

    protected short method(short[] z1, short[] z2) {
        return 0;
    }

    protected int method(int[] z1) {
        return 0;
    }

    protected int method(int[] z1, int[] z2) {
        return 0;
    }

    protected long method(long[] z1) {
        return 0;
    }

    protected long method(long[] z1, long[] z2) {
        return 0;
    }

    protected float method(float[] z1) {
        return 0;
    }

    protected float method(float[] z1, float[] z2) {
        return 0;
    }

    protected double method(double[] z1) {
        return 0;
    }

    protected double method(double[] z1, double[] z2) {
        return 0;
    }

    private boolean method(Boolean z1) {
        return false;
    }

    private boolean method(Boolean z1, Boolean z2) {
        return false;
    }

    private byte method(Byte b1) {
        return 0;
    }

    private byte method(Byte b1, Byte b2) {
        return 0;
    }

    private char method(Character c1) {
        return 0;
    }

    private char method(Character c1, Character c2) {
        return 0;
    }

    private short method(Short s1) {
        return 0;
    }

    private short method(Short s1, Short s2) {
        return 0;
    }

    private int method(Integer i1) {
        return 0;
    }

    private int method(Integer i1, Integer i2) {
        return 0;
    }

    private long method(Long j1) {
        return 0;
    }

    private long method(Long j1, Long j2) {
        return 0;
    }

    private float method(Float f1) {
        return 0;
    }

    private float method(Float f1, Float f2) {
        return 0;
    }

    private double method(Double d1) {
        return 0;
    }

    private double method(Double d1, Double d2) {
        return 0;
    }

    private Boolean method(boolean z1, boolean z2, boolean z3) {
        return null;
    }

    private Byte method(byte b1, byte b2, byte b3) {
        return null;
    }

    private Character method(char c1, char c2, char c3) {
        return null;
    }

    private Short method(short s1, short s2, short s3) {
        return null;
    }

    private Integer method(int i1, int i2, int i3) {
        return null;
    }

    private Long method(long j1, long j2, long j3) {
        return null;
    }

    private Float method(float f1, float f2, float f3) {
        return null;
    }

    private Double method(double d1, double d2, double d3) {
        return null;
    }

    private long method(byte b, char c, short s, int i, long j,
            Byte ob, Character oc, Short os, Integer oi, Long oj,
            String sb, String ss, String si, String sj) {
        return 0;
    }
}
