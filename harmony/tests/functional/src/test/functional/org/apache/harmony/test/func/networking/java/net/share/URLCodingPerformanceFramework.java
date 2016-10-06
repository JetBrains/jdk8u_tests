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
 * Created on 28.01.2005
 */
package org.apache.harmony.test.func.networking.java.net.share;



/**
 */
public abstract class URLCodingPerformanceFramework extends
org.apache.harmony.test.func.networking.java.net.share.PerformanceTestFramework {
    
    public static final String[] AVAIL_CHARSETS = {
            "US-ASCII",
            "ISO-8859-1",
            "UTF-8",
            "UTF-16BE",
            "UTF-16",
            "UTF-16LE"
    };
    
    public static final String[] CODING_URLS = {
            "The+string+%C3%BC%40foo-bar",
            "The string ?@foo-bar",
            "dfsgdfg7324sdfasd%3B435tr%3B%3B%3B%3B%3B43tsd%40%23%25%5E%24%25%26%25%5E",
            ""
    };
    public static final String[] UNCODING_URLS = {
            "The string ?@foo-bar",
            "The+string+%C3%BC%40foo-bar",
            "dfsgdfg7324sdfasd;435tr;;;;;43tsd@#%^$%&%^",
            ""
    };
    
    public int div;

    protected void beforeTest(int tests) {
        div = tests / AVAIL_CHARSETS.length ;
        if  (tests % AVAIL_CHARSETS.length != 0) {
            div++; 
        }
        
    }
}
