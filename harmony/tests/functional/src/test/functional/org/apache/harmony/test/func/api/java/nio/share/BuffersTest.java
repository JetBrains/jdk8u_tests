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
 * Created on 03.02.2005
 */
package org.apache.harmony.test.func.api.java.nio.share;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


/**
 */
public abstract class BuffersTest extends MultiCase {

    protected static final int BUF_SIZE = 1000;
    
    private Buffer testBuffer;
    
    protected abstract Buffer getValidBuffer();
    
    protected abstract boolean hasArray(Buffer buffer);
    
    public Result testClear() {
        testBuffer = getValidBuffer();
        testBuffer.position(testBuffer.capacity() / 2);
        testBuffer.limit(testBuffer.capacity() / 2 );
        testBuffer.clear();
        if (testBuffer.position() != 0) {
            return failed("position has not been reset to zero");
        }
        if (testBuffer.limit() != testBuffer.capacity() ) {
            return failed("limit has not been reset to capacity");
        }
        return passed();
    }

    public Result testRewing() {
        testBuffer = getValidBuffer();
        testBuffer.position(testBuffer.capacity() / 2);
        testBuffer.rewind();
        if (testBuffer.position() != 0) {
            return failed("position has not been reset to zero");
        }
        return passed();
    }

    public Result testFlip() {
        testBuffer = getValidBuffer();
        testBuffer.position(testBuffer.capacity() / 2);
        testBuffer.limit(testBuffer.capacity());
        testBuffer.flip();
        if (testBuffer.limit() != testBuffer.capacity() / 2) {
            return failed("limit has not been reset to last position");
        }
        if (testBuffer.position() != 0) {
            return failed("position has not been reset to zero");
        }
        return passed();
    }

    protected Result newBufferTest(Buffer buffer, int cap) {
        if (buffer == null) {
            return failed( "allocate() return null" );
        }
        
        if (buffer.capacity() != cap) {
            return failed( "unexpected capacity value" );
        }
        
        if (buffer.position() != 0) {
            return failed( "the position is not zero");
        }
        
        if (buffer.limit() != buffer.limit() ) {
            return failed( "limit != capacity" );
        }
        if (!hasArray(buffer)) {
            return failed( "It has not backing array" );
        }
        
        if (getArrayOffset( buffer ) != 0) {
            return failed( "arrayOffset != 0" );
        }
        
        return passed();
    }
    
    
    
    protected class TestArray {
        public TestArray() {};
        public Object data;
        public Object backingArray;
        public Buffer buffer;
    }
    
    protected abstract TestArray getValidTestBuffer(int cap);
    protected abstract Object changeObjectValue(Object o);
    protected abstract void setValue(Object o, int i, Buffer buffer);
    protected abstract Object getValue(int i, Buffer buffer);
    
    public Result testArray() {
        TestArray array = getValidTestBuffer( BUF_SIZE );
        if (array == null) {
            return failed("It has no backing array");
        }
        
        testBuffer = array.buffer;
        
        int arrayLength = Array.getLength(array.backingArray);
        
        if (arrayLength != Array.getLength( array.data) ) {
            return failed("wrong backing buffer length");
        }
        
        for (int i = 0; i < arrayLength; i++) {
            if (!Array.get( array.backingArray, i).equals( Array.get( array.data, i))) {
                return failed("wrong backing buffer content");
            }
        }
        
        Object el = Array.get( array.backingArray, arrayLength / 2 );
        el = changeObjectValue( el );
        
        Array.set( array.backingArray, arrayLength / 2, el);
        
        if (!getValue(arrayLength / 2, testBuffer ).equals( el )) {
            return failed("backing array not corresponds to buffer's contents");
        }
        
        el = changeObjectValue( el );
                
        setValue( el, arrayLength / 2, testBuffer);
        if (!Array.get(array.backingArray, arrayLength / 2).equals( getValue( arrayLength / 2, testBuffer))) {
            return failed("buffer's contents not corresponds to it's backing array");
        }
        return passed();
    }
    
    protected abstract int getArrayOffset(Buffer buf);
    
    public Result testArrayOffset() {
        TestArray array = getValidTestBuffer( BUF_SIZE );
        int offset = getArrayOffset(array.buffer );
        if (!Array.get( array.backingArray, offset + array.buffer.capacity() / 2).equals( getValue( array.buffer.capacity() / 2, array.buffer ))) {
            return failed("wrong offset of backing buffer");
        }
        return passed();
    }
    
}
