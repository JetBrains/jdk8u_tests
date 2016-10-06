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
 
package org.apache.harmony.test.func.api.java.util.regex.share;


public class CharSeq implements CharSequence {
    private CharSequence delegate;
    
    public CharSeq(CharSequence delegate) {
        this.delegate = delegate;
    }

    public int length() {
        return delegate.length();
    }

    public char charAt(int arg0) {
        return delegate.charAt(arg0);
    }

    public CharSequence subSequence(int arg0, int arg1) {
        return delegate.subSequence(arg0, arg1);
    }
    
    public boolean equals(Object arg0) {
        return delegate.equals(arg0);
    }
    public int hashCode() {
        return delegate.hashCode();
    }
    public String toString() {
        return delegate.toString();
    }
}
