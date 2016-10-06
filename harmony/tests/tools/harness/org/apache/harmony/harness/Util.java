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
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.10 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Util {

    public static final String[] FILE_EXT = { "", ".exe", ".bat", ".sh" };

    /**
     * Checks that the file with the given name exist and it is a directory.
     * 
     * @param value - directory name
     * @return - true if directory exist, otherwise false
     */
    public static synchronized boolean checkExistDir(String value) {
        try {
            if ((new File(value)).isDirectory()) {
                return true;
            }
        } catch (Exception e) {
            //return false;
        }
        return false;
    }

    /**
     * Checks that the file with the given name exist and it is a simple file.
     * 
     * @param value - file name
     * @return - true if file exist, otherwise false
     */
    public static synchronized boolean checkExistFile(String value) {
        try {
            if ((new File(value)).isFile()) {
                return true;
            }
        } catch (Exception e) {
            //return false;
        }
        return false;
    }

    /**
     * Checks that the file with the given name exist and it is a simple file.
     * Also checks for possible extensions: "exe", "bat", "sh"
     * 
     * @param value - file name
     * @return - true if file exist, otherwise false
     */
    public static synchronized boolean checkExistFileWithExt(String value) {
        for (int i = 0; i < FILE_EXT.length; i++) {
            try {
                if ((new File(value + FILE_EXT[i])).isFile()) {
                    return true;
                }
            } catch (Exception e) {
                //return false;
            }
        }
        return false;
    }

    /**
     * Convert time in ms to 'hh:mm:ss and ms' format
     * 
     * @param msTime - time in millisecond
     * @return - string for time or empty string for negative time
     */
    public static synchronized String getTime(long msTime) {
        if (msTime < 0) {
            return "";
        }
        long hh = msTime / (1000 * 60 * 60); //ms * s * min
        long tmp = msTime % (1000 * 60 * 60);
        long mm = tmp / (1000 * 60); //ms * s
        if (mm != 0) {
            tmp = tmp % (1000 * 60);
        } else { //less than 1 minut
            tmp = msTime % (1000 * 60);
        }
        long ss = tmp / 1000; //ms
        long ms = tmp % 1000;
        String retVal = "";
        if (hh != 0) {
            if (hh < 10) {
                retVal = retVal + "0" + hh + ":";
            } else {
                retVal = retVal + hh + ":";
            }
        } else {
            retVal = retVal + "00:";
        }
        if (mm != 0) {
            if (mm < 10) {
                retVal = retVal + "0" + mm + ":";
            } else {
                retVal = retVal + mm + ":";
            }
        } else {
            retVal = retVal + "00:";
        }
        if (ss != 0) {
            if (ss < 10) {
                retVal = retVal + "0" + ss;
            } else {
                retVal = retVal + ss;
            }
        } else {
            retVal = retVal + "00";
        }
        if (ms != 0) {
            retVal = retVal + " and " + ms + "ms";
        }
        return retVal;
    }

    /**
     * Convert time in ms to 'mm minutes ss seconds' format
     * 
     * @param msTime - time in millisecond
     * @return - string for time or empty string for negative time
     */
    public static synchronized String getMinutesTime(long msTime) {
        if (msTime < 0) {
            return "";
        }
        long mm = msTime / (1000 * 60); //ms * s
        long tmp = msTime % (1000 * 60);
        long ss = tmp / 1000; //ms
        long ms = tmp % 1000;
        if (ms > 500) {
            ss++;
        }
        return "Total time: " + mm + " minutes " + ss + " seconds";
    }

    /**
     * Return the current time in millisecond based on java.util.Calendar class
     * 
     * @return current time from epoch (in millisecond)
     */
    public static synchronized long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Return the current time. Used for add time to internal log
     * 
     * @return current time as string
     */
    public static String getCurrentTimeToLog() {
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.HOUR) + ":" + date.get(Calendar.MINUTE) + ":"
            + date.get(Calendar.SECOND);
    }

    /**
     * Convert the string "value1 value2 ..." to array of string {"value1",
     * "value2"}. Symbols-separators are: ',', ' ' (whitespace), tabulation '\n'
     * and '\r'
     * 
     * @param data - string to convert
     * @return array of string or null if data==null
     */
    public static synchronized String[] stringToArray(String data) {
        return stringToArray(data, ", \t\n\r");
    }

    /**
     * Convert the string "value1DELIMvalue2DELIM..." to array of string
     * {"value1", "value2"}. Symbols-separators are all chars from the string
     * 'delims'
     * 
     * @param data - string to convert
     * @param delims - string of delimiters
     * @return array of string or null if data==null
     */
    public static synchronized String[] stringToArray(String data, String delims) {
        if (data == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(data, delims);
        int iter = st.countTokens();
        String[] retVal = new String[iter];
        for (int cnt = 0; cnt < iter; cnt++) {
            retVal[cnt] = st.nextToken().trim();
        }
        return retVal;
    }

    /**
     * Merge 2 string's arrays data1 and data2 to one array of string 'data1 +
     * data2'.
     * 
     * @param data1 - first array of string
     * @param data2 - second array of string
     * @return - merged array or null if both arrays is null
     */
    public static synchronized String[] mergeArrays(String[] data1,
        String[] data2) {
        if (data1 == null && data2 == null) {
            return null;
        }
        if (data1 == null) {
            return data2;
        }
        if (data2 == null) {
            return data1;
        }
        int cnt = 0;
        String[] retVal = new String[data1.length + data2.length];
        for (int i = 0; i < data1.length; i++) {
            retVal[cnt++] = data1[i];
        }
        for (int i = 0; i < data2.length; i++) {
            retVal[cnt++] = data2[i];
        }
        return retVal;
    }

    /**
     * Read the pointed file as a text file into the memory and return the
     * ArrayList of strings as elements.
     * 
     * @param fileName - name of the file to read
     * @return list of string
     */
    public static synchronized ArrayList readStringsFromFile(String fileName) {
        ArrayList tmpStore = new ArrayList();
        try {
            FileInputStream in = new FileInputStream(fileName);
            int size = in.available();
            int index = 0;
            byte[] data = new byte[size];
            in.read(data);
            String tmp = new String(data);
            StringTokenizer st = new StringTokenizer(tmp, "\n");
            int iter = st.countTokens();
            for (int cnt = 0; cnt < iter; cnt++) {
                tmpStore.add(st.nextToken());
            }
        } catch (IOException e) {
            //return tmpStore;
        }
        return tmpStore;
    }

    /**
     * Remove first N elements from array.
     * 
     * @param arr - data array
     * @param n - numbers of elements to remove
     * @return zero-length array in the case of null or zero-length data array
     *         or N > arr.length or array with removed N first elements.
     */
    public static synchronized Object[] removeFirstElem(Object[] arr, int n) {
        if (arr == null || arr.length == 0 || arr.length <= n) {
            return new Object[0];
        }
        Object[] retArr = new Object[arr.length - n];
        for (int i = 0; i < (arr.length - n); i++) {
            retArr[i] = arr[n + i];
        }
        return retArr;
    }

    /**
     * Remove first N elements from string array.
     * 
     * @param arr - data array
     * @param n - numbers of elements to remove
     * @return zero-length array in the case of null or zero-length data array
     *         or N > arr.length or array with removed N first elements.
     */
    public static synchronized String[] removeFirstElem(String[] arr, int n) {
        if (arr == null || arr.length == 0 || arr.length <= n) {
            return new String[0];
        }
        String[] retArr = new String[arr.length - n];
        for (int i = 0; i < (arr.length - n); i++) {
            retArr[i] = arr[n + i];
        }
        return retArr;
    }

    /**
     * Add element to array.
     * 
     * @param arr array to add to
     * @param elem element to add to array
     * @param head add to head or tail of array
     * @return array with added element
     */
    public static synchronized Object[] addElemToArray(Object[] arr,
        Object elem, boolean head) {
        Object[] retArr;
        if (arr == null) {
            retArr = new Object[1];
            retArr[0] = elem;
            return retArr;
        }
        retArr = new Object[arr.length + 1];
        if (head) {
            retArr[0] = elem;
            for (int i = 1; i < arr.length; i++) {
                retArr[i] = arr[i - 1];
            }
        } else {
            for (int i = 0; i < arr.length; i++) {
                retArr[i] = arr[i];
            }
            retArr[arr.length] = elem;
        }
        return retArr;
    }

    /**
     * Add element to String array.
     * 
     * @param arr array to add to
     * @param elem element to add to array
     * @param head add to head or tail of array
     * @return array with added element
     */
    public static synchronized String[] addElemToArray(String[] arr,
        String elem, boolean head) {
        String[] retArr;
        if (arr == null) {
            retArr = new String[1];
            retArr[0] = elem;
            return retArr;
        }
        retArr = new String[arr.length + 1];
        if (head) {
            retArr[0] = elem;
            for (int i = 1; i < (arr.length + 1); i++) {
                retArr[i] = arr[i - 1];
            }
        } else {
            for (int i = 0; i < arr.length; i++) {
                retArr[i] = arr[i];
            }
            retArr[arr.length] = elem;
        }
        return retArr;
    }
}