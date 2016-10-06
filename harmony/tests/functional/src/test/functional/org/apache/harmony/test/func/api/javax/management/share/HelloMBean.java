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
package org.apache.harmony.test.func.api.javax.management.share;

import java.math.BigDecimal;

/**
 * Simple MBean interface.
 * 
 */
public interface HelloMBean {

    /**
     * Returns name attribute value.
     * 
     * @return name attribute value.
     */
    public String getName();

    /**
     * Set name attribute value.
     * 
     * @param name new value.
     */
    public void setName(String name);

    /**
     * Returns attribute1 attribute value.
     * 
     * @return attribute1 attribute value.
     */
    public String getAttribute1();

    /**
     * Set attribute1 attribute value.
     * 
     * @param attribute1 new value.
     */
    public void setAttribute1(String attribute1);

    /**
     * Returns attribute2 attribute value.
     * 
     * @return attribute2 attribute value.
     */
    public String getAttribute2();

    /**
     * Set attribute2 attribute value.
     * 
     * @param attribute2 new value.
     */
    public void setAttribute2(String attribute2);

    /**
     * Returns attribute3 attribute value.
     * 
     * @return attribute3 attribute value.
     */
    public String getAttribute3();

    /**
     * Returns attribute4 attribute value.
     * 
     * @return attribute4 attribute value.
     */
    public boolean isAttribute4();

    /**
     * Print "Hello" to the output.
     */
    public void sayHello();

    /**
     * Read/write operation.
     * 
     * @param operationParam
     * @return
     */
    public Object operation(Object operationParam);

    public Integer getNumber();

    public void setNumber(Integer number);

    public BigDecimal getNumber1();

    public void setNumber1(BigDecimal number1);

    public BigDecimal getNumber2();

    public void setNumber2(BigDecimal number1);

    public int getIntNumber1();

    public void setIntNumber1(int intNumber1);

    public int getIntNumber2();

    public void setIntNumber2(int intNumber2);

    public long getLongNumber1();

    public void setLongNumber1(long longNumber1);

    public long getLongNumber2();

    public void setLongNumber2(long longNumber2);

    public double getDoubleNumber1();

    public void setDoubleNumber1(double doubleNumber1);

    public double getDoubleNumber2();

    public void setDoubleNumber2(double doubleNumber2);

    public float getFloatNumber1();

    public void setFloatNumber1(float floatNumber1);

    public float getFloatNumber2();

    public void setFloatNumber2(float floatNumber2);

    public boolean isBoolean1();

    public void setBoolean1(boolean boolean1);

    public boolean isBoolean2();

    public void setBoolean2(boolean boolean2);

    public Number getNumberSubcl();
}
