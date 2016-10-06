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
 * Created on 18.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.object_methods;

/**
 * @author Gregory Shimansky
 *
 * Class used in object methods tests.
 */
class TestClass {
    boolean retz;
    byte retb;
    char retc;
    short rets;
    int reti;
    long retj;
    float retf;
    double retd;

    void method(boolean z1, boolean z2, boolean z3, boolean z4) {
        retz = z1 | z2 | z3 | z4;
    }

    void method(byte b1, byte b2, byte b3, byte b4) {
        retb = (byte)(b1 + b2 + b3 + b4);
    }

    void method(char c1, char c2, char c3, char c4) {
        retc = (char)(c1 + c2 + c3 + c4);
    }

    void method(short s1, short s2, short s3, short s4) {
        rets = (short)(s1 + s2 + s3 + s4);
    }

    void method(int i1, int i2, int i3, int i4) {
        reti = (int)(i1 + i2 + i3 + i4);
    }

    void method(long j1, long j2, long j3, long j4) {
        retj = (long)(j1 + j2 + j3 + j4);
    }

    void method(float f1, float f2, float f3, float f4) {
        retf = (float)(f1 + f2 + f3 + f4);
    }

    void method(double d1, double d2, double d3, double d4) {
        retd = (double)(d1 + d2 + d3 + d4);
    }
    
    public boolean method(boolean z1) {
        return z1;
    }

    public boolean method(boolean z1, boolean z2) {
        return z1 || z2;
    }

    public byte method(byte b1) {
        return b1;
    }

    public byte method(byte b1, byte b2) {
        return (byte)(b1 + b2);
    }

    public char method(char c1) {
        return c1;
    }

    public char method(char c1, char c2) {
        return (char)(c1 + c2);
    }

    public short method(short s1) {
        return s1;
    }

    public short method(short s1, short s2) {
        return (short)(s1 + s2);
    }

    public int method(int i1) {
        return i1;
    }

    public int method(int i1, int i2) {
        return i1 + i2;
    }

    public long method(long j1) {
        return j1;
    }

    public long method(long j1, long j2) {
        return j1 + j2;
    }

    public float method(float f1) {
        return f1;
    }

    public float method(float f1, float f2) {
        return f1 + f2;
    }

    public double method(double d1) {
        return d1;
    }

    public double method(double d1, double d2) {
        return d1 + d2;
    }

    protected boolean method(boolean[] z1) {
        boolean result = false;
        for (int iii = 0; iii < z1.length; iii++)
            result = result || z1[iii];
        return result;
    }

    protected boolean method(boolean[] z1, boolean[] z2) {
        boolean result = false;
        for (int iii = 0; iii < z1.length; iii++)
            result = result || z1[iii];
        for (int iii = 0; iii < z2.length; iii++)
            result = result || z2[iii];
        return result;
    }

    protected byte method(byte[] z1) {
        byte result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        return result;
    }

    protected byte method(byte[] z1, byte[] z2) {
        byte result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        for (int iii = 0; iii < z2.length; iii++)
            result += z2[iii];
        return result;
    }

    protected char method(char[] z1) {
        char result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        return result;
    }

    protected char method(char[] z1, char[] z2) {
        char result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        for (int iii = 0; iii < z2.length; iii++)
            result += z2[iii];
        return result;
    }

    protected short method(short[] z1) {
        short result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        return result;
    }

    protected short method(short[] z1, short[] z2) {
        short result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        for (int iii = 0; iii < z2.length; iii++)
            result += z2[iii];
        return result;
    }

    protected int method(int[] z1) {
        int result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        return result;
    }

    protected int method(int[] z1, int[] z2) {
        int result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        for (int iii = 0; iii < z2.length; iii++)
            result += z2[iii];
        return result;
    }

    protected long method(long[] z1) {
        long result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        return result;
    }

    protected long method(long[] z1, long[] z2) {
        long result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        for (int iii = 0; iii < z2.length; iii++)
            result += z2[iii];
        return result;
    }

    protected float method(float[] z1) {
        float result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        return result;
    }

    protected float method(float[] z1, float[] z2) {
        float result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        for (int iii = 0; iii < z2.length; iii++)
            result += z2[iii];
        return result;
    }

    protected double method(double[] z1) {
        double result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        return result;
    }

    protected double method(double[] z1, double[] z2) {
        double result = 0;
        for (int iii = 0; iii < z1.length; iii++)
            result += z1[iii];
        for (int iii = 0; iii < z2.length; iii++)
            result += z2[iii];
        return result;
    }

    private boolean method(Boolean z1) {
        return z1.booleanValue();
    }

    private boolean method(Boolean z1, Boolean z2) {
        return z1.booleanValue() || z2.booleanValue();
    }

    private byte method(Byte b1) {
        return b1.byteValue();
    }

    private byte method(Byte b1, Byte b2) {
        return (byte)(b1.byteValue() + b2.byteValue());
    }

    private char method(Character c1) {
        return c1.charValue();
    }

    private char method(Character c1, Character c2) {
        return (char)(c1.charValue() + c2.charValue());
    }

    private short method(Short s1) {
        return s1.shortValue();
    }

    private short method(Short s1, Short s2) {
        return (short)(s1.shortValue() + s2.shortValue());
    }

    private int method(Integer i1) {
        return i1.intValue();
    }

    private int method(Integer i1, Integer i2) {
        return i1.intValue() + i2.intValue();
    }

    private long method(Long j1) {
        return j1.longValue();
    }

    private long method(Long j1, Long j2) {
        return j1.longValue() + j2.longValue();
    }

    private float method(Float f1) {
        return f1.floatValue();
    }

    private float method(Float f1, Float f2) {
        return f1.floatValue() + f2.floatValue();
    }

    private double method(Double d1) {
        return d1.doubleValue();
    }

    private double method(Double d1, Double d2) {
        return d1.doubleValue() + d2.doubleValue();
    }

    private Boolean method(boolean z1, boolean z2, boolean z3) {
        return new Boolean(z1 || z2 || z3);
    }

    private Byte method(byte b1, byte b2, byte b3) {
        return new Byte((byte)(b1 + b2 + b3));
    }

    private Character method(char c1, char c2, char c3) {
        return new Character((char)(c1 + c2 + c3));
    }

    private Short method(short s1, short s2, short s3) {
        return new Short((short)(s1 + s2 + s3));
    }

    private Integer method(int i1, int i2, int i3) {
        return new Integer(i1 + i2 + i3);
    }

    private Long method(long j1, long j2, long j3) {
        return new Long(j1 + j2 + j3);
    }

    private Float method(float f1, float f2, float f3) {
        return new Float(f1 + f2 + f3);
    }

    private Double method(double d1, double d2, double d3) {
        return new Double(d1 + d2 + d3);
    }

    private long method(byte b, char c, short s, int i, long j,
            Byte ob, Character oc, Short os, Integer oi, Long oj,
            String sb, String ss, String si, String sj) {
        long d1 = b + c + s + i + j;
        long d2 = ob.byteValue() + oc.charValue() + os.shortValue() +
            oi.intValue() + oj.longValue();
        long d3;
        try {
            d3 = (new Byte(sb)).byteValue() +
                (new Short(ss)).shortValue() +
                (new Integer(si)).intValue() +
                (new Long(sj)).longValue();
        } catch (NumberFormatException e) {
            return 0;
        }
        return d1 + d2 + d3;
    }
}
