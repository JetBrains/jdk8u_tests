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
package org.apache.harmony.test.func.api.javax.management.monitor.share;

/**
 * MBean Used in ErrorNotification tests for all types of monitor
 * 
 */
public class ErrorProducer implements ErrorProducerMBean {

    private boolean isNull = false;

    private boolean isException = false;

    private Integer intValue = new Integer(0);

    private Double dValue = new Double(0);

    private String sValue = "match";

    public Object getObject() {
        return new Object();
    }

    public void setNullFlag(boolean offer) {
        this.isNull = offer;
    }

    public void setExceptionFlag(boolean offer) {
        this.isException = offer;
    }

    public Integer getInteger() {
        if (isNull) {
            return null;
        }
        if (isException) {
            throw new OutOfMemoryError("HAHAHAHAHAHAAA!");
        }
        return intValue;
    }

    public void setInteger(Integer offer) {
        this.intValue = offer;
    }

    public Double getDouble() {
        if (isNull) {
            return null;
        }
        if (isException) {
            throw new OutOfMemoryError("HAHAHAHAHAHAAA!");
        }
        return dValue;
    }

    public void setDouble(Double offer) {
        this.dValue = offer;
    }

    public String getString() {
        if (isException) {
            throw new OutOfMemoryError("HAHAHAHAHAHAAA!");
        }
        return sValue;
    }

    public void setString(String offer) {
        this.sValue = offer;
    }

}
