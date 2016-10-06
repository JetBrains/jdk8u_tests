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
 
package org.apache.harmony.test.func.api.javax.swing.share;

import javax.swing.text.Segment;



public class InstrumentedSegment extends Segment {

    public Object clone() {
        InstrumentedUILog.add(new Object[] {"Segment.clone"} );
        return super.clone();
    }
    public char current() {
        InstrumentedUILog.add(new Object[] {"Segment.current"} );
        return super.current();
    }
    public char first() {
        InstrumentedUILog.add(new Object[] {"Segment.first"} );
        return super.first();
    }
    public int getBeginIndex() {
        InstrumentedUILog.add(new Object[] {"Segment.getBeginIndex"} );
        return super.getBeginIndex();
    }
    public int getEndIndex() {
        InstrumentedUILog.add(new Object[] {"Segment.getEndIndex"} );
        return super.getEndIndex();
    }
    public int getIndex() {
        InstrumentedUILog.add(new Object[] {"Segment.getIndex"} );
        return super.getIndex();
    }
    public boolean isPartialReturn() {
        InstrumentedUILog.add(new Object[] {"Segment.isPartialReturn"} );
        return super.isPartialReturn();
    }
    public char last() {
        InstrumentedUILog.add(new Object[] {"Segment.last"} );
        return super.last();
    }
    public char next() {
        InstrumentedUILog.add(new Object[] {"Segment.next"} );
        return super.next();
    }
    public char previous() {
        InstrumentedUILog.add(new Object[] {"Segment.previous"} );
        return super.previous();
    }
    public char setIndex(int arg0) {
        InstrumentedUILog.add(new Object[] {"Segment.setIndex", "" +  arg0} );
        return super.setIndex(arg0);
    }
    public void setPartialReturn(boolean arg0) {
        InstrumentedUILog.add(new Object[] {"Segment.setPartialReturn", "" +  arg0} );
        super.setPartialReturn(arg0);
    }
    public String toString() {
        InstrumentedUILog.add(new Object[] {"Segment.toString"} );
        return super.toString();
    }
    public int hashCode() {
        InstrumentedUILog.add(new Object[] {"Segment.hashCode"} );
        return super.hashCode();
    }
    protected void finalize() throws Throwable {
        super.finalize();
    }
    public boolean equals(Object arg0) {
        InstrumentedUILog.add(new Object[] {"Segment.equals",  arg0} );
        return super.equals(arg0);
    }
}
