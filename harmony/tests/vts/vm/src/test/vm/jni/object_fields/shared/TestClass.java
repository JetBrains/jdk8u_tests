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
package org.apache.harmony.vts.test.vm.jni.object_fields;

/**
 * @author Gregory Shimansky
 * Test class used in object field tests
 */
class TestClass {
    public static int stInt;
    private static boolean stBool;
    
    public Object lpub;
    public boolean zpub;
    public byte bpub;
    public char cpub;
    public short spub;
    public int ipub;
    public long jpub;
    public float fpub;
    public double dpub;

    protected Object lprot;
    protected boolean zprot;
    protected byte bprot;
    protected char cprot;
    protected short sprot;
    protected int iprot;
    protected long jprot;
    /**
     * @return Returns the bprot.
     */
    public byte getBprot() {
        return bprot;
    }
    /**
     * @param bprot The bprot to set.
     */
    public void setBprot(byte bprot) {
        this.bprot = bprot;
    }
    /**
     * @return Returns the cprot.
     */
    public char getCprot() {
        return cprot;
    }
    /**
     * @param cprot The cprot to set.
     */
    public void setCprot(char cprot) {
        this.cprot = cprot;
    }
    /**
     * @return Returns the dprot.
     */
    public double getDprot() {
        return dprot;
    }
    /**
     * @param dprot The dprot to set.
     */
    public void setDprot(double dprot) {
        this.dprot = dprot;
    }
    /**
     * @return Returns the fprot.
     */
    public float getFprot() {
        return fprot;
    }
    /**
     * @param fprot The fprot to set.
     */
    public void setFprot(float fprot) {
        this.fprot = fprot;
    }
    /**
     * @return Returns the iprot.
     */
    public int getIprot() {
        return iprot;
    }
    /**
     * @param iprot The iprot to set.
     */
    public void setIprot(int iprot) {
        this.iprot = iprot;
    }
    /**
     * @return Returns the jprot.
     */
    public long getJprot() {
        return jprot;
    }
    /**
     * @param jprot The jprot to set.
     */
    public void setJprot(long jprot) {
        this.jprot = jprot;
    }
    /**
     * @return Returns the lprot.
     */
    public Object getLprot() {
        return lprot;
    }
    /**
     * @param lprot The lprot to set.
     */
    public void setLprot(Object lprot) {
        this.lprot = lprot;
    }
    /**
     * @return Returns the sprot.
     */
    public short getSprot() {
        return sprot;
    }
    /**
     * @param sprot The sprot to set.
     */
    public void setSprot(short sprot) {
        this.sprot = sprot;
    }
    /**
     * @return Returns the zprot.
     */
    public boolean isZprot() {
        return zprot;
    }
    /**
     * @param zprot The zprot to set.
     */
    public void setZprot(boolean zprot) {
        this.zprot = zprot;
    }
    protected float fprot;
    protected double dprot;

    private Object lpriv;
    private boolean zpriv;
    private byte bpriv;
    private char cpriv;
    private short spriv;
    private int ipriv;
    private long jpriv;
    private float fpriv;
    private double dpriv;
    /**
     * @return Returns the bpriv.
     */
    public byte getBpriv() {
        return bpriv;
    }
    /**
     * @param bpriv The bpriv to set.
     */
    public void setBpriv(byte bpriv) {
        this.bpriv = bpriv;
    }
    /**
     * @return Returns the bpub.
     */
    public byte getBpub() {
        return bpub;
    }
    /**
     * @param bpub The bpub to set.
     */
    public void setBpub(byte bpub) {
        this.bpub = bpub;
    }
    /**
     * @return Returns the cpriv.
     */
    public char getCpriv() {
        return cpriv;
    }
    /**
     * @param cpriv The cpriv to set.
     */
    public void setCpriv(char cpriv) {
        this.cpriv = cpriv;
    }
    /**
     * @return Returns the cpub.
     */
    public char getCpub() {
        return cpub;
    }
    /**
     * @param cpub The cpub to set.
     */
    public void setCpub(char cpub) {
        this.cpub = cpub;
    }
    /**
     * @return Returns the dpriv.
     */
    public double getDpriv() {
        return dpriv;
    }
    /**
     * @param dpriv The dpriv to set.
     */
    public void setDpriv(double dpriv) {
        this.dpriv = dpriv;
    }
    /**
     * @return Returns the dpub.
     */
    public double getDpub() {
        return dpub;
    }
    /**
     * @param dpub The dpub to set.
     */
    public void setDpub(double dpub) {
        this.dpub = dpub;
    }
    /**
     * @return Returns the fpriv.
     */
    public float getFpriv() {
        return fpriv;
    }
    /**
     * @param fpriv The fpriv to set.
     */
    public void setFpriv(float fpriv) {
        this.fpriv = fpriv;
    }
    /**
     * @return Returns the fpub.
     */
    public float getFpub() {
        return fpub;
    }
    /**
     * @param fpub The fpub to set.
     */
    public void setFpub(float fpub) {
        this.fpub = fpub;
    }
    /**
     * @return Returns the ipriv.
     */
    public int getIpriv() {
        return ipriv;
    }
    /**
     * @param ipriv The ipriv to set.
     */
    public void setIpriv(int ipriv) {
        this.ipriv = ipriv;
    }
    /**
     * @return Returns the ipub.
     */
    public int getIpub() {
        return ipub;
    }
    /**
     * @param ipub The ipub to set.
     */
    public void setIpub(int ipub) {
        this.ipub = ipub;
    }
    /**
     * @return Returns the jpriv.
     */
    public long getJpriv() {
        return jpriv;
    }
    /**
     * @param jpriv The jpriv to set.
     */
    public void setJpriv(long jpriv) {
        this.jpriv = jpriv;
    }
    /**
     * @return Returns the jpub.
     */
    public long getJpub() {
        return jpub;
    }
    /**
     * @param jpub The jpub to set.
     */
    public void setJpub(long jpub) {
        this.jpub = jpub;
    }
    /**
     * @return Returns the lpriv.
     */
    public Object getLpriv() {
        return lpriv;
    }
    /**
     * @param lpriv The lpriv to set.
     */
    public void setLpriv(Object lpriv) {
        this.lpriv = lpriv;
    }
    /**
     * @return Returns the lpub.
     */
    public Object getLpub() {
        return lpub;
    }
    /**
     * @param lpub The lpub to set.
     */
    public void setLpub(Object lpub) {
        this.lpub = lpub;
    }
    /**
     * @return Returns the spriv.
     */
    public short getSpriv() {
        return spriv;
    }
    /**
     * @param spriv The spriv to set.
     */
    public void setSpriv(short spriv) {
        this.spriv = spriv;
    }
    /**
     * @return Returns the spub.
     */
    public short getSpub() {
        return spub;
    }
    /**
     * @param spub The spub to set.
     */
    public void setSpub(short spub) {
        this.spub = spub;
    }
    /**
     * @return Returns the zpriv.
     */
    public boolean isZpriv() {
        return zpriv;
    }
    /**
     * @param zpriv The zpriv to set.
     */
    public void setZpriv(boolean zpriv) {
        this.zpriv = zpriv;
    }
    /**
     * @return Returns the zpub.
     */
    public boolean isZpub() {
        return zpub;
    }
    /**
     * @param zpub The zpub to set.
     */
    public void setZpub(boolean zpub) {
        this.zpub = zpub;
    }
}
