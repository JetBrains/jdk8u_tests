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

package org.apache.harmony.test.func.api.java.io.ObjectStreamClass;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamClass;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public final class ObjectStreamClassTest extends IOMultiCase {
    static final Object[][] map = new Object[][] {
            { C1.class, new Long(12345L) },
            { C2.class, new Long(54321L) },
            { java.io.File.class, new Long(301077366599181567L) },
            { java.io.FilePermission.class, new Long(7930732926638008763L) },
            { java.io.ObjectStreamClass.class, new Long(-6120832682080437368L) },
            { java.io.SerializablePermission.class,
                    new Long(8537212141160296410L) },
            { java.util.ArrayList.class, new Long(8683452581122892189L) },
            { java.util.BitSet.class, new Long(7997698588986878753L) },
            { java.util.Calendar.class, new Long(-1807547505821590642L) },
            { java.util.Currency.class, new Long(-158308464356906721L) },
            { java.util.Date.class, new Long(7523967970034938905L) },
            { java.util.EventObject.class, new Long(5516075349620653480L) },
            { java.util.GregorianCalendar.class,
                    new Long(-8125100834729963327L) },
            { java.util.HashMap.class, new Long(362498820763181265L) },
            { java.util.HashSet.class, new Long(-5024744406713321676L) },
            { java.util.Hashtable.class, new Long(1421746759512286392L) },
            { java.util.IdentityHashMap.class, new Long(8188218128353913216L) },
            { java.util.LinkedHashMap.class, new Long(3801124242820219131L) },
            { java.util.LinkedHashSet.class, new Long(-2851667679971038690L) },
            { java.util.LinkedList.class, new Long(876323262645176354L) },
            { java.util.Locale.class, new Long(9149081749638150636L) },
            { java.util.Properties.class, new Long(4112578634029874840L) },
            { java.util.PropertyPermission.class, new Long(885438825399942851L) },
            { java.util.Random.class, new Long(3905348978240129619L) },
            { java.util.SimpleTimeZone.class, new Long(-403250971215465050L) },
            { java.util.Stack.class, new Long(1224463164541339165L) },
            { java.util.TimeZone.class, new Long(3581463369166924961L) },
            { java.util.TreeMap.class, new Long(919286545866124006L) },
            { java.util.TreeSet.class, new Long(-2479143000061671589L) },
            { java.util.Vector.class, new Long(-2767605614048989439L) },
            { java.util.ConcurrentModificationException.class,
                    new Long(-3666751008965953603L) },
            { java.util.EmptyStackException.class,
                    new Long(5084686378493302095L) },
            { java.util.MissingResourceException.class,
                    new Long(-4876345176062000401L) },
            { java.util.NoSuchElementException.class,
                    new Long(6769829250639411880L) },
            { java.util.TooManyListenersException.class,
                    new Long(5074640544770687831L) }, };

    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new ObjectStreamClassTest(), args));
    }

    public Result testGetSerialVersionUID() {
        if (ObjectStreamClass.lookup(C.class) != null) {
            return failed("expected null in lookup non-serializable class");
        }
        for (int i = 0; i < map.length; ++i) {
            if (ObjectStreamClass.lookup((Class) map[i][0])
                    .getSerialVersionUID() != ((Number) map[i][1]).longValue()) {
                return failed("wrong getSerialVersionUID after lookup "
                        + map[i][0]
                        + " : got "
                        + ObjectStreamClass.lookup((Class) map[i][0])
                                .getSerialVersionUID() + "expected "
                        + map[i][1]);
            }
        }
        return passed();
    }

}

class C {
    private static final long serialVersionUID = 12345L;
}

class C1 implements Serializable {
    private static final long serialVersionUID = 12345L;
}

class C2 implements Externalizable {
    private static final long serialVersionUID = 54321L;

    public void readExternal(ObjectInput arg0) throws IOException,
            ClassNotFoundException {
    }

    public void writeExternal(ObjectOutput arg0) throws IOException {
    }
}