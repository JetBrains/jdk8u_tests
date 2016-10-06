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
 * Created on 02.02.2005
 */
package org.apache.harmony.test.func.api.java.nio.byteBuffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Random;

import org.apache.harmony.test.func.api.java.nio.share.BuffersTest;
import org.apache.harmony.share.Result;

/**
 */
public class ByteBufferTest extends BuffersTest  {
    
    private ByteBuffer testByteBuffer;
    
    public static void main(String[] args) {
        System.exit( new ByteBufferTest().test(args));
    }
    
    protected Buffer getValidBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate( BUF_SIZE );
        fillBuffer( buffer );
        return buffer; 
    }
    
    private byte[] fillBuffer(ByteBuffer buffer ) {
        byte[] data = new byte[buffer.capacity()]; 
        Random rand = new Random();
        for (int i = 0; i < buffer.capacity(); i++) {
            data[i] = (byte)rand.nextInt(0xff);
            buffer.put(i, data[i]);
        }
        return data;
    }
    
    public Result testAllocate() {
        try {
            testByteBuffer = ByteBuffer.allocate( -1 );
            return failed( " expected IllegalArgumentExeption trying to create a buffer with negative capacity " );
        } catch (IllegalArgumentException e) {
        }
        testByteBuffer = ByteBuffer.allocate( BUF_SIZE ) ;
        return newBufferTest( testByteBuffer, BUF_SIZE  );
    }

    protected TestArray getValidTestBuffer(int cap) {
        Buffer buffer = ByteBuffer.allocate( BUF_SIZE );
        TestArray array = new TestArray();
        array.data = fillBuffer( (ByteBuffer) buffer );
        if (!((ByteBuffer)buffer).hasArray()) {
            return null;
        }
        array.backingArray = ((ByteBuffer) buffer).array() ;
        array.buffer = buffer;
        return array;
    }

    protected int getArrayOffset(Buffer buf) {
        return ((ByteBuffer)buf).arrayOffset();
    }
    

    protected boolean hasArray(Buffer buffer) {
        return ((ByteBuffer)buffer).hasArray();
    }
    
    protected Object changeObjectValue(Object o) {
        byte b = ((Byte)o).byteValue();
        b++;
        return new Byte(b);
    }

    protected Object getValue(int i, Buffer buffer) {
        return new Byte(((ByteBuffer)buffer).get(i));
    }

    protected void setValue(Object o, int i, Buffer buffer) {
        ((ByteBuffer)buffer).put(i, ((Byte)o).byteValue());
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
    
}
    
    
 
