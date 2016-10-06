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
package org.apache.harmony.test.func.api.java.beans.persistence;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;

/**
 * Purpose: decode XML files with reference implementation Decoder.
 * <p>
 * First and the only argument is full file name, which has to be decoded.
 * Result of decoding is a object which is serialized and is sent to output
 * stream.
 * 
 */
public class DecoderReference {
    public static void main(String[] args) {
        try {
            String fileName = args[0];
            XMLDecoder d = new XMLDecoder(new FileInputStream(fileName));
            Object object = d.readObject();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                System.out);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}