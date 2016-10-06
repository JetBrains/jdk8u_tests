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

import java.beans.XMLEncoder;

import org.apache.harmony.test.func.api.java.beans.persistence.beans.GeneratingBeans;

/**
 * Purpose: encode XML files with reference implementation Decoder.
 * <p>
 * First and the only argument is name of method from
 * <code>GeneratingBeans<code> classes. 
 * This method is invoked and get a bean. The bean is encoded. 
 * Result of encoding is XML. XML is sent to output stream.     
 * 
 */
public class EncoderReference {

    public static void main(String[] args) {
        try {
            String methodName = args[0];
            XMLEncoder encoder = new XMLEncoder(System.out);
            Object object = GeneratingBeans.class.getDeclaredMethod(methodName,
                null).invoke(null, null);
            encoder.writeObject(object);
            encoder.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}