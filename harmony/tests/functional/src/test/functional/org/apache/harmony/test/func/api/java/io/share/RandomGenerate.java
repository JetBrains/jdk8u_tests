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
 * Created on 27.01.2005
 *
 */
package org.apache.harmony.test.func.api.java.io.share;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.harmony.test.func.api.java.io.share.PerformanceTestFramework;

/**
 *  
 */
abstract public class RandomGenerate extends PerformanceTestFramework {
    public static void main(String[] args) {
    }

    static final String SUFFIX = "xyz";

    static final String PREFIX = "temp";

    static final int LENGTH = 42;

    static Random r = new Random();

    public static short[] getArrayOfRandomShort(int number) {
        short[] b = new short[number];
        for (int i = 0; i < number; i++) {
            b[i] = (short) r.nextInt();
        }
        return b;
    }

    public static boolean[] getArrayOfRandomBoolean(int number) {
        boolean[] b = new boolean[number];
        for (int i = 0; i < number; i++) {
            b[i] = r.nextBoolean();
        }
        return b;
    }

    public static long[] getArrayOfRandomLong(int number) {
        long[] b = new long[number];
        for (int i = 0; i < number; i++) {
            b[i] = r.nextLong();
        }
        return b;
    }

    public static int[] getArrayOfRandomInt(int number) {
        int[] b = new int[number];
        for (int i = 0; i < number; i++) {
            b[i] = r.nextInt();
        }
        return b;
    }

    public static byte[] getArrayOfRandomByte(int number) {
        byte[] b = new byte[number];
        r.nextBytes(b);
        return b;
    }

    public static byte[] getArrayOfRandomPositiveByte(int number) {
        byte[] b = new byte[number];
        b = getArrayOfRandomByte(number);
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) Math.abs(b[i]);
            if (b[i] < 0) b[i] = 0; 
        }
        return b;
    }

    public static char[] getArrayOfRandomChar(int number) {
        String s = new String(getArrayOfRandomByte(number));
        return s.toCharArray();
    }

    public static String[] getArrayOfRandomString(int number, int length) {
        byte[] b;
        String[] as = new String[number];
        for (int i = 0; i < number; i++) {
            b = new byte[r.nextInt(length)];
            r.nextBytes(b);
            as[i] = new String(b, 0, b.length);
        }
        return as;
    }

    public static String[] getArrayOfRandomString(int number) {
        return getArrayOfRandomString(number, LENGTH);
    }

    public static String[] getArrayOfString(int number, String prefix,
            String suffix) {
        String[] ArrayOfString = new String[number];
        try {
            for (int i = 0; i < number; ++i) {
                ArrayOfString[i] = prefix + i + "." + suffix;
            }
        } catch (Throwable e) {
            log.info("error initalizing array of random string "
                    + e.getMessage());
        }
        return ArrayOfString;
    }

    public static String[] getArrayOfString(int number) {
        return getArrayOfString(number, PREFIX, SUFFIX);
    }

    public static File[] getArrayOfFile(int number, String prefix,
            String suffix, boolean create) {
        File[] ArrayOfFile = new File[number];
        String s = "";
        try {
            File f = File.createTempFile("File", "xxx");
            s = f.getParent();
            f.delete();
        } catch (IOException e) {
            log.info("can't create temporarily file " + e.getMessage());
        }
        try {
            for (int i = 0; i < number; ++i) {
                ArrayOfFile[i] = new File(s + File.separator + prefix + '_' + i
                        + '.' + suffix);
                if (create)
                    ArrayOfFile[i].createNewFile();
            }
        } catch (Throwable e) {
            log.info("can't create file " + e.getMessage());
        }
        return ArrayOfFile;
    }

    public static File[] getArrayOfFile(int number, boolean create) {
        return getArrayOfFile(number, PREFIX, SUFFIX, create);
    }

    public static File[] getArrayOfFile(int number) {
        return getArrayOfFile(number, true);
    }

    public static String[] getArrayOfFileString(File[] arrayOfFile) {
        String[] arrayOfString = new String[arrayOfFile.length];
        for (int i = 0; i < arrayOfFile.length; i++) {
            arrayOfString[i] = arrayOfFile[i].getAbsolutePath();
        }
        return arrayOfString;
    };

    public static String[] getArrayOfFileString(int number, String prefix,
            String suffix, boolean create) {
        File[] arrayOfFile = new File[number];
        String[] arrayOfString = new String[number];
        String s = "";
        try {
            File f = File.createTempFile("File", "xxx");
            s = f.getParent();
            f.delete();
        } catch (IOException e) {
            log.info("can't create temporarily file " + e.getMessage());
        }
        try {
            for (int i = 0; i < number; ++i) {
                arrayOfFile[i] = new File(s + File.separator + prefix + '_' + i
                        + '.' + suffix);
                arrayOfString[i] = arrayOfFile[i].getAbsolutePath();
                if (create)
                    arrayOfFile[i].createNewFile();
            }
        } catch (Throwable e) {
            log.info("can't create file " + e.getMessage());
        }
        return arrayOfString;
    }

    public static String[] getArrayOfFileString(int number, boolean create) {
        return getArrayOfFileString(number, PREFIX, SUFFIX, create);
    }

    public static String[] getArrayOfFileString(int number) {
        return getArrayOfFileString(number, true);
    }
}
