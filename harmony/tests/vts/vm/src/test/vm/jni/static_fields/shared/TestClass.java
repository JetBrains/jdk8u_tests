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
 * Created on 15.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.static_fields;

/**
 * @author Gregory Shimansky
 * Test class used in object field tests
 */
class TestClass {
    public int objInt;
    
    public static Object lpub;
    public static boolean zpub;
    public static byte bpub;
    public static char cpub;
    public static short spub;
    public static int ipub;
    public static long jpub;
    public static float fpub;
    public static double dpub;

    protected static Object lprot;
    protected static boolean zprot;
    protected static byte bprot;
    protected static char cprot;
    protected static short sprot;
    protected static int iprot;
    protected static long jprot;
    protected static float fprot;
    protected static double dprot;

    private static Object lpriv;
    private static boolean zpriv;
    private static byte bpriv;
    private static char cpriv;
    private static short spriv;
    private static int ipriv;
    private static long jpriv;
    private static float fpriv;
    private static double dpriv;
    /**
     * @return Returns the bpriv.
     */
    public static byte getBpriv() {
        return bpriv;
    }
    /**
     * @param bpriv The bpriv to set.
     */
    public static void setBpriv(byte bpriv) {
        TestClass.bpriv = bpriv;
    }
    /**
     * @return Returns the bprot.
     */
    public static byte getBprot() {
        return bprot;
    }
    /**
     * @param bprot The bprot to set.
     */
    public static void setBprot(byte bprot) {
        TestClass.bprot = bprot;
    }
    /**
     * @return Returns the bpub.
     */
    public static byte getBpub() {
        return bpub;
    }
    /**
     * @param bpub The bpub to set.
     */
    public static void setBpub(byte bpub) {
        TestClass.bpub = bpub;
    }
    /**
     * @return Returns the cpriv.
     */
    public static char getCpriv() {
        return cpriv;
    }
    /**
     * @param cpriv The cpriv to set.
     */
    public static void setCpriv(char cpriv) {
        TestClass.cpriv = cpriv;
    }
    /**
     * @return Returns the cprot.
     */
    public static char getCprot() {
        return cprot;
    }
    /**
     * @param cprot The cprot to set.
     */
    public static void setCprot(char cprot) {
        TestClass.cprot = cprot;
    }
    /**
     * @return Returns the cpub.
     */
    public static char getCpub() {
        return cpub;
    }
    /**
     * @param cpub The cpub to set.
     */
    public static void setCpub(char cpub) {
        TestClass.cpub = cpub;
    }
    /**
     * @return Returns the dpriv.
     */
    public static double getDpriv() {
        return dpriv;
    }
    /**
     * @param dpriv The dpriv to set.
     */
    public static void setDpriv(double dpriv) {
        TestClass.dpriv = dpriv;
    }
    /**
     * @return Returns the dprot.
     */
    public static double getDprot() {
        return dprot;
    }
    /**
     * @param dprot The dprot to set.
     */
    public static void setDprot(double dprot) {
        TestClass.dprot = dprot;
    }
    /**
     * @return Returns the dpub.
     */
    public static double getDpub() {
        return dpub;
    }
    /**
     * @param dpub The dpub to set.
     */
    public static void setDpub(double dpub) {
        TestClass.dpub = dpub;
    }
    /**
     * @return Returns the fpriv.
     */
    public static float getFpriv() {
        return fpriv;
    }
    /**
     * @param fpriv The fpriv to set.
     */
    public static void setFpriv(float fpriv) {
        TestClass.fpriv = fpriv;
    }
    /**
     * @return Returns the fprot.
     */
    public static float getFprot() {
        return fprot;
    }
    /**
     * @param fprot The fprot to set.
     */
    public static void setFprot(float fprot) {
        TestClass.fprot = fprot;
    }
    /**
     * @return Returns the fpub.
     */
    public static float getFpub() {
        return fpub;
    }
    /**
     * @param fpub The fpub to set.
     */
    public static void setFpub(float fpub) {
        TestClass.fpub = fpub;
    }
    /**
     * @return Returns the ipriv.
     */
    public static int getIpriv() {
        return ipriv;
    }
    /**
     * @param ipriv The ipriv to set.
     */
    public static void setIpriv(int ipriv) {
        TestClass.ipriv = ipriv;
    }
    /**
     * @return Returns the iprot.
     */
    public static int getIprot() {
        return iprot;
    }
    /**
     * @param iprot The iprot to set.
     */
    public static void setIprot(int iprot) {
        TestClass.iprot = iprot;
    }
    /**
     * @return Returns the ipub.
     */
    public static int getIpub() {
        return ipub;
    }
    /**
     * @param ipub The ipub to set.
     */
    public static void setIpub(int ipub) {
        TestClass.ipub = ipub;
    }
    /**
     * @return Returns the jpriv.
     */
    public static long getJpriv() {
        return jpriv;
    }
    /**
     * @param jpriv The jpriv to set.
     */
    public static void setJpriv(long jpriv) {
        TestClass.jpriv = jpriv;
    }
    /**
     * @return Returns the jprot.
     */
    public static long getJprot() {
        return jprot;
    }
    /**
     * @param jprot The jprot to set.
     */
    public static void setJprot(long jprot) {
        TestClass.jprot = jprot;
    }
    /**
     * @return Returns the jpub.
     */
    public static long getJpub() {
        return jpub;
    }
    /**
     * @param jpub The jpub to set.
     */
    public static void setJpub(long jpub) {
        TestClass.jpub = jpub;
    }
    /**
     * @return Returns the lpriv.
     */
    public static Object getLpriv() {
        return lpriv;
    }
    /**
     * @param lpriv The lpriv to set.
     */
    public static void setLpriv(Object lpriv) {
        TestClass.lpriv = lpriv;
    }
    /**
     * @return Returns the lprot.
     */
    public static Object getLprot() {
        return lprot;
    }
    /**
     * @param lprot The lprot to set.
     */
    public static void setLprot(Object lprot) {
        TestClass.lprot = lprot;
    }
    /**
     * @return Returns the lpub.
     */
    public static Object getLpub() {
        return lpub;
    }
    /**
     * @param lpub The lpub to set.
     */
    public static void setLpub(Object lpub) {
        TestClass.lpub = lpub;
    }
    /**
     * @return Returns the spriv.
     */
    public static short getSpriv() {
        return spriv;
    }
    /**
     * @param spriv The spriv to set.
     */
    public static void setSpriv(short spriv) {
        TestClass.spriv = spriv;
    }
    /**
     * @return Returns the sprot.
     */
    public static short getSprot() {
        return sprot;
    }
    /**
     * @param sprot The sprot to set.
     */
    public static void setSprot(short sprot) {
        TestClass.sprot = sprot;
    }
    /**
     * @return Returns the spub.
     */
    public static short getSpub() {
        return spub;
    }
    /**
     * @param spub The spub to set.
     */
    public static void setSpub(short spub) {
        TestClass.spub = spub;
    }
    /**
     * @return Returns the zpriv.
     */
    public static boolean isZpriv() {
        return zpriv;
    }
    /**
     * @param zpriv The zpriv to set.
     */
    public static void setZpriv(boolean zpriv) {
        TestClass.zpriv = zpriv;
    }
    /**
     * @return Returns the zprot.
     */
    public static boolean isZprot() {
        return zprot;
    }
    /**
     * @param zprot The zprot to set.
     */
    public static void setZprot(boolean zprot) {
        TestClass.zprot = zprot;
    }
    /**
     * @return Returns the zpub.
     */
    public static boolean isZpub() {
        return zpub;
    }
    /**
     * @param zpub The zpub to set.
     */
    public static void setZpub(boolean zpub) {
        TestClass.zpub = zpub;
    }
}
