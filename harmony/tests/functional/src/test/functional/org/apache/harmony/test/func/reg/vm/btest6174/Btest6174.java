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
package org.apache.harmony.test.func.reg.vm.btest6174;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Reflection does not allow widening convertion
 * of primitive parameters for Methods and Constructors
 *
 */
public class Btest6174 extends MultiCase {

    static Logger log = Logger.global;
    
    private Result goTestValid(String name, Class type, Object valid){
        try {
            log.info("test "+name+" with "+valid.getClass());
            Method m = PrimitiveWorks.class.getMethod(name, new Class[]{type});
            m.invoke(null, new Object[]{valid});
            return passed();
        } catch (Exception e) {
            return failed("Failed with exception: " + e);
        }
    }
    
    private Result goTestInvalid(String name, Class type, Object invalid){
        try {
            log.info("test "+name+" with "+invalid.getClass());
            Method m = PrimitiveWorks.class.getMethod(name, new Class[]{type});
            m.invoke(null, new Object[]{invalid});
            return failed("Accepted invalid parameter");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return passed();
        } catch (Throwable t) {
            t.printStackTrace();
            return new Result(Result.ERROR, "Unexpected error: " + t);
        }
    }
    
    public Result testB2S() { 
        return goTestValid("setShort", Short.TYPE, new Byte((byte)123));
    }

    public Result testB2I() { 
        return goTestValid("setInt", Integer.TYPE, new Byte((byte)123));
    }

    public Result testB2J() { 
        return goTestValid("setLong", Long.TYPE, new Byte((byte)123));
    }

    public Result testB2F() { 
        return goTestValid("setFloat", Float.TYPE, new Byte((byte)123));
    }

    public Result testB2D() { 
        return goTestValid("setDouble", Double.TYPE, new Byte((byte)123));
    }

    public Result testS2I() { 
        return goTestValid("setInt", Integer.TYPE, new Short((short)1234));
    }

    public Result testS2L() { 
        return goTestValid("setLong", Long.TYPE, new Short((short)1234));
    }
    
    public Result testS2F() { 
        return goTestValid("setFloat", Float.TYPE, new Short((short)1234));
    }

    public Result testS2D() { 
        return goTestValid("setDouble", Double.TYPE, new Short((short)1234));
    }

    public Result testC2I() { 
        return goTestValid("setInt", Integer.TYPE, new Character('s'));
    }

    public Result testC2L() { 
        return goTestValid("setLong", Long.TYPE, new Character('s'));
    }

    public Result testC2F() { 
        return goTestValid("setFloat", Float.TYPE, new Character('s'));
    }

    public Result testC2D() { 
        return goTestValid("setDouble", Double.TYPE, new Character('s'));
    }

    public Result testI2L() { 
        return goTestValid("setLong", Long.TYPE, new Integer(38783756));
    }

    public Result testI2F() { 
        return goTestValid("setFloat", Float.TYPE, new Integer(38783756));
    }

    public Result testI2D() { 
        return goTestValid("setDouble", Double.TYPE, new Integer(38783756));
    }
    
    public Result testL2F() { 
        return goTestValid("setFloat", Float.TYPE, new Long(343387837456756L));
    }

    public Result testL2D() { 
        return goTestValid("setDouble", Double.TYPE, new Long(343538783756343L));
    }

    public Result testF2D() { 
        return goTestValid("setDouble", Double.TYPE, new Float(343.56565438783756));
    }
    
    public Result testF2L() { 
        return goTestInvalid("setLong", Long.TYPE, new Float(343543878));
    }

    public Result testI2B() { 
        return goTestInvalid("setByte", Byte.TYPE, new Integer(123));
    }

    public Result testC2Z() { 
        return goTestInvalid("setBoolean", Boolean.TYPE, new Character('n'));
    }

    public Result testZ2I() { 
        return goTestInvalid("setInt", Integer.TYPE, new Boolean(true));
    }

    public static void main(String[] args) {
        System.exit(new Btest6174().test(args));
    }
}

class PrimitiveWorks {
    public static void setChar(char v){System.out.println(""+v);}
    public static void setByte(byte v){System.out.println(""+v);}
    public static void setShort(short v){System.out.println(""+v);}
    public static void setInt(int v){System.out.println(""+v);}
    public static void setLong(long v){System.out.println(""+v);}
    public static void setFloat(float v){System.out.println(""+v);}
    public static void setDouble(double v){System.out.println(""+v);}
    public static void setBoolean(boolean v){System.out.println(""+v);}   
}