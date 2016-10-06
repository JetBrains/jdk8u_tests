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
package org.apache.harmony.test.func.api.java.nio.charBuffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Random;

import org.apache.harmony.test.func.api.java.nio.share.BuffersTest;
import org.apache.harmony.share.Result;

/**
 */
public class CharBufferTest extends BuffersTest {
    
    private CharBuffer testCharBuffer;
    
    public Result testAllocate() {
        try {
            testCharBuffer = CharBuffer.allocate( -1 );
            return failed( " expected IllegalArgumentExeption trying to create a buffer with negative capacity " );
        } catch (IllegalArgumentException e) {
        }
        testCharBuffer = CharBuffer.allocate( BUF_SIZE );
        return newBufferTest( testCharBuffer, BUF_SIZE  );
    }

    protected Buffer getValidBuffer() {
        CharBuffer buffer = CharBuffer.allocate( BUF_SIZE );
        fillBuffer( buffer );
        return buffer; 
    }
    
    private char[] fillBuffer(CharBuffer buffer ) {
        char[] data = new char[buffer.capacity()]; 
        Random rand = new Random();
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (char)rand.nextInt(0xff);
            buffer.put(i, data[i]);
        }
        return data;
    }


    protected TestArray getValidTestBuffer(int cap) {
        Buffer buffer = CharBuffer.allocate( BUF_SIZE );
        TestArray array = new TestArray();
        array.data = fillBuffer( (CharBuffer) buffer );
        if (!((CharBuffer)buffer).hasArray()) {
            return null;
        }
        array.backingArray = ((CharBuffer) buffer).array() ;
        array.buffer = buffer;
        return array;
    }
    
    protected Object changeObjectValue(Object o) {
        char c = ((Character)o).charValue();
        c++;
        return new Character(c);
    }
    
    protected int getArrayOffset(Buffer buf) {
        return ((CharBuffer)buf).arrayOffset();
    }

    protected boolean hasArray(Buffer buffer) {
        return ((CharBuffer)buffer).hasArray();
    }
    
    protected Object getValue(int i, Buffer buffer) {
        return new Character(((CharBuffer)buffer).get(i));
    }

    protected void setValue(Object o, int i, Buffer buffer) {
        ((CharBuffer)buffer).put(i, ((Character)o).charValue());
    }
    
    public Result testClear() {
        return super.testClear();
    }

    public Result testFlip() {
        return super.testFlip();
    }

    public Result testRewing() {
        return super.testRewing();
    }

    public Result testArray() {
        return super.testArray();
    }
    
    public Result testArrayOffset() {
        return super.testArrayOffset();
    }
    
    public static void main(String[] args) {
        System.exit( new CharBufferTest().test(args));
    }

}
