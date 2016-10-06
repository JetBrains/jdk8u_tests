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
package org.apache.harmony.test.func.jit.HLO.inline.ReturnValue.CheckCast4;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 14.11.2005
 * 
 * Jitrino HLO test
 * Tests narrowing primitive conversions during inlining
 */

public class CheckCast4 extends MultiCase {

    public static void main(String[] args) {
        System.exit(new CheckCast4().test(args));
    }

    public Result testByteToChar() {
        char check1 = (char) inlineByte();
        if (check1 >= Character.MIN_VALUE && check1 <= Character.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: char check1==" + (int) check1);
    }
            
////////////////////////////////////////////////////////
    
    public Result testShortToByte() {        
        byte check2 = (byte) inlineShort();
        if (check2 >= Byte.MIN_VALUE && check2 <= Byte.MAX_VALUE) 
            return passed();    
        else return failed("TEST FAILED: byte check2==" + check2);
    }
    
    public Result testShortToChar() {
        char check3 = (char) inlineShort();
        if (check3 >= Character.MIN_VALUE && check3 <= Character.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: char check3==" + (int) check3);
    }
            
/////////////////////////////////////////////////////////
        
    public Result testCharToByte() {
        byte check4 = (byte) inlineChar();
        if (check4 >= Byte.MIN_VALUE && check4 <= Byte.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: byte check4==" + check4);
    }
    
    public Result testCharToShort() {
        short check5 = (short) inlineChar();
        if (check5 >= Short.MIN_VALUE && check5 <= Short.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: short check5==" + check5);
    }
            
/////////////////////////////////////////////////////////
    
    public Result testIntToByte() {
        byte check6 = (byte) inlineInt();
        if (check6 >= Byte.MIN_VALUE && check6 <= Byte.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: byte check6==" + check6);
    }
    
    public Result testIntToShort() {
        short check7 = (short) inlineInt();
        if (check7 >= Short.MIN_VALUE && check7 <= Short.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: short check7==" + check7);
    }
    
    public Result testIntToChar() {
        char check8 = (char) inlineInt();
        if (check8 >= Character.MIN_VALUE && check8 <= Character.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: char check8==" + (int) check8);
    }

/////////////////////////////////////////////////////////
        
    public Result testLongToByte() {
        byte check9 = (byte) inlineLong();
        if (check9 >= Byte.MIN_VALUE && check9 <= Byte.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: byte check9==" + check9);
    }
    
    public Result testLongToShort() {
        short check10 = (short) inlineLong();
        if (check10 >= Short.MIN_VALUE && check10 <= Short.MAX_VALUE)
            return passed();
        else return failed("TEST FAILED: short check10==" + check10);
    }
    
    public Result testLongToInt() {
        int check11 = (int) inlineLong();
        if (check11 >= Integer.MIN_VALUE && check11 <= Integer.MAX_VALUE)
            return passed();
        else return failed("TEST FAILED: int check11==" + check11);
    }
    
    public Result testLongToChar() {
        char check12 = (char) inlineLong();
        if (check12 >= Character.MIN_VALUE && check12 <= Character.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: char check12==" + (long) check12);
    }
            
/////////////////////////////////////////////////////////
        
    public Result testFloatToByte() {
        byte check13 = (byte) inlineFloat();
        if (check13 >= Byte.MIN_VALUE && check13 <= Byte.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: byte check13==" + check13);
    }
    
    public Result testFloatToShort() {
        short check14 = (short) inlineFloat();
        if (check14 >= Short.MIN_VALUE && check14 <= Short.MAX_VALUE)
            return passed();
        else return failed("TEST FAILED: short check14==" + check14);
    }
    
    public Result testFloatToInt() {
        int check15 = (int) inlineFloat();
        if (check15 >= Integer.MIN_VALUE && check15 <= Integer.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: int check15==" + check15);
    }
    
    public Result testFloatToChar() {
        char check16 = (char) inlineFloat();
        if (check16 >= Character.MIN_VALUE && check16 <= Character.MAX_VALUE)
            return passed();
        else return failed("TEST FAILED: char check16==" + (float) check16);
    }
    
    public Result testFloatToLong() {
        long check17 = (long) inlineFloat();
        if (check17 >= Long.MIN_VALUE && check17 <= Long.MAX_VALUE)
            return passed();
        else return failed("TEST FAILED: long check17==" + check17);
    }

/////////////////////////////////////////////////////////
    
    public Result testDoubleToByte() {
        byte check18 = (byte) inlineDouble();
        if (check18 >= Byte.MIN_VALUE && check18 <= Byte.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: byte check18==" + check18);
    }
    
    public Result testDoubleToShort() {
        short check19 = (short) inlineDouble();
        if (check19 >= Short.MIN_VALUE && check19 <= Short.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: short check19==" + check19);
    }
    
    public Result testDoubleToInt() {
        int check20 = (int) inlineDouble();
        if (check20 >= Integer.MIN_VALUE && check20 <= Integer.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: int check20==" + check20);
    }
    
    public Result testDoubleToChar() {
        char check21 = (char) inlineDouble();
        if (check21 >= Character.MIN_VALUE && check21 <= Character.MAX_VALUE)
            return passed();
        else return failed("TEST FAILED: char check21==" + (double) check21);
    }
    
    public Result testDoubleToLong() {
        long check22 = (long) inlineDouble();
        if (check22 >= Long.MIN_VALUE && check22 <= Long.MAX_VALUE) 
            return passed();
        else return failed("TEST FAILED: long check22==" + check22);
    }
    
    public Result testDoubleToFloat() {
        float check23 = (float) inlineDouble();
        if (check23 == Float.POSITIVE_INFINITY) 
            return passed();
        else return failed("TEST FAILED: float check23==" + check23);
    }

    final byte inlineByte() {
        return -30; 
    }
    
    final short inlineShort() {
        return -2000;  
    }
    
    final char inlineChar() {
        return 65000;  
    }
    
    final int inlineInt() {
        return -65000; 
    }
    
    final long inlineLong() {
        return 30000000000L; 
    }
    
    final float inlineFloat() {
        return Float.MAX_VALUE; 
    }
    
    final double inlineDouble() {
        return Double.MAX_VALUE; 
    }
    
}
