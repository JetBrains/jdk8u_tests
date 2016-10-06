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
package org.apache.harmony.test.func.api.java.util.Vector.functional;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.apache.harmony.test.func.share.synctest.SyncTestCaller;

/**
 */
public class VectorTest extends SyncTestCaller {

    Vector v;     
    static public Object       oTestedArr[] = new Integer[100];
    static public Object       oTestedArrLarge[] = new Integer[1000];
    
    
    public VectorTest() {
    }

    public VectorTest(int id){
        super(id);
        v = new Vector();
        v.add(new Integer(0));        
        lock = v;
        
    }

    public SyncTestCaller getInstance(int id) {
        return new VectorTest(id);
    }

    public void run() {
        switch (getCallerId()) {            
            case 0:
                v.add(0, new Integer(0));
                setCallPassed();
                break;
            case 1:                
                v.add(new Integer(0));
                setCallPassed();
                break;
            case 2:                
                v.addAll(new ArrayList());
                setCallPassed();
                break;
            case 3:                
                v.addAll(0, new ArrayList());
                setCallPassed();
                break;
            case 4:                
                v.addElement(new Integer(0));
                setCallPassed();
                break;
            case 5:                
                v.capacity();
                setCallPassed();
                break;
            case 6:                
                v.clear();
                setCallPassed();
                break;            
            case 7:                
                v.clone();
                setCallPassed();
                break;
            case 8:                
                v.contains(new Integer(0));
                setCallPassed();
                break;
            case 9:                
                v.containsAll(new ArrayList());
                setCallPassed();
                break;
            case 10:                
                v.copyInto(oTestedArrLarge);
                setCallPassed();
                break;
            case 11:
                try {
                    v.elementAt(0);                    
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }                
                setCallPassed();
                break;
            case 12:
                /* Method elements() does not reflect the inner state of the object,
                 * so we do not include into the test
                 */
                // v.elements();
                // setCallPassed();
                break;
            case 13:                
                v.ensureCapacity(1);
                setCallPassed();
                break;
            case 14:                
                v.equals(new Integer(0));
                setCallPassed();
                break;
            case 15:    
                try {
                    v.firstElement();                    
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }                
                setCallPassed();
                break;
            case 16:
                try {
                    v.get(0);                    
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                setCallPassed();
                break;
            case 17:                
                v.hashCode();
                setCallPassed();
                break;
            case 18:               
                v.indexOf(new Integer(0));
                setCallPassed();
                break;
            case 19:
                try {
                    v.indexOf(new Integer(0), 0);                    
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                setCallPassed();
                break;
            case 20:                
                v.insertElementAt(new Integer(0), 0);
                setCallPassed();
                break;
            case 21:                
                v.isEmpty();
                setCallPassed();
                break;
            case 22:                
                v.lastElement();
                setCallPassed();
                break;
            case 23:                
                v.lastIndexOf(new Integer(0));
                setCallPassed();
                break;
            case 24:
                try {                   
                    v.lastIndexOf(new Integer(0), 0);                    
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                setCallPassed();
                break;
            case 25:
                v.remove(0);
                setCallPassed();
                break; 
            case 26:
                v.remove(new Integer(0));
                setCallPassed();
                break;
            case 27:                
                v.removeAll(new ArrayList());
                setCallPassed();
                break;
            case 28:                
                v.removeAllElements();
                setCallPassed();
                break;
            case 29:
                v.removeElement(new Integer(0));
                setCallPassed();
                break;
            case 30:
                try {
                    v.removeElementAt(0);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }                
                setCallPassed();
                break;
            case 31:
                v.retainAll(new ArrayList());
                setCallPassed();
                break;
            case 32:
                try {
                    v.set(0, new Integer(0));                    
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                setCallPassed();
                break;
            case 33:
                try {
                    v.setElementAt(new Integer(0), 0);                    
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                setCallPassed();
                break;
            case 34:
                v.setSize(100);
                setCallPassed();
                break;
            case 35:
                v.size();
                setCallPassed();
                break;
            case 36:
                v.subList(0, 1);
                setCallPassed();
                break;
            case 37:
                v.toArray();
                setCallPassed();
                break;
            case 38:
                v.toArray(oTestedArrLarge);
                setCallPassed();
                break;
            case 39:
                v.toString();
                setCallPassed();
                break;
            case 40:
                v.trimToSize();
                setCallPassed();
                break;            
        }
    }

    protected void fillTestedMethods() {
        registerMethodName("add_int_Object");
        registerMethodName("add_Object");
        registerMethodName("addAll_Collection");
        registerMethodName("addAll_int_Collection");
        registerMethodName("addElement_Object");
        registerMethodName("capacity");        
        registerMethodName("clear");
        registerMethodName("clone");
        registerMethodName("contains_Object");
        registerMethodName("containsAll_Collection");
        registerMethodName("copyInto_ArrayOfObject");
        registerMethodName("elementAt_int");
        registerMethodName("elements");
        registerMethodName("ensureCapacity_int");
        registerMethodName("equals_Object");
        registerMethodName("firstElement");
        registerMethodName("get_int");
        registerMethodName("hashCode");
        registerMethodName("indexOf_Object");
        registerMethodName("indexOf_Object_int");
        registerMethodName("insertElementAt_Object_int");
        registerMethodName("isEmpty");
        registerMethodName("lastElement");
        registerMethodName("lastIndexOf_Object");
        registerMethodName("lastIndexOf_Object_int");
        registerMethodName("remove_int");
        registerMethodName("remove_Object");
        registerMethodName("removeAll_Collection");
        registerMethodName("removeAllElements");
        registerMethodName("removeElement_Object");       
        registerMethodName("removeElementAt_int");
        registerMethodName("retainAll_Collection");
        registerMethodName("set_int_Object");
        registerMethodName("setElementAt_Object_int");
        registerMethodName("setSize_int");
        registerMethodName("size");
        registerMethodName("subList_int_int");
        registerMethodName("toArray");
        registerMethodName("toArray_ArrayOfObject");        
        registerMethodName("toString");
        registerMethodName("trimToSize");
    }

    public static void main(String[] args){
        VectorTest t = new VectorTest();
        System.exit(t.test());
    }
}