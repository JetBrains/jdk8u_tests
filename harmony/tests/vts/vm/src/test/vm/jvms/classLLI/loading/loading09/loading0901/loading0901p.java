/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/** 
 * @author Mikhail Bolotov
 * @version $Revision: 1.2 $
 */  

package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading09.loading0901;
        

import java.lang.reflect.InvocationTargetException;

import org.apache.harmony.share.Result;


public class loading0901p {
	
	static final String LOADING0901P_A = "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading09.loading0901.loading0901pA";
	
	static final int FAIL2 = 110;
	static final int FAIL3 = FAIL2+1;

	public static void main(String[] args) throws Exception {
		System.exit(new loading0901p().test(args));
	}
	
	public int test(String[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException, NoSuchMethodException {
		loading0901pA t;
		loading09pClassLoader cl = new loading09pClassLoader();
		Class c = Class.forName(LOADING0901P_A, true, cl);
		
		Integer i = (Integer) c.getMethod("test", null).invoke(c.newInstance(), null);
		if (i.intValue() != Result.PASS) {
			return FAIL2;
		}
		
		i = (Integer) c.getMethod("test", null).invoke(c.newInstance(), null);
		if (i.intValue() != Result.PASS) {
			return FAIL3;
		}

		// class loader must be queried for the loading0901pB class only once  
		if (cl.bLoadCounter != 1) {
			return Result.FAIL;
		}
		return Result.PASS;
	}
}



class loading09pClassLoader extends ClassLoader {

	public int bLoadCounter = 0;
	
	
	public Class loadClass(String s) throws ClassNotFoundException {
		
		if (s.equals(loading0901p.LOADING0901P_A)) {
				byte[] data = getBytesA();
				return defineClass(s, data, 0, data.length);
		}
		
		if (s.equals("org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading09.loading0901.loading0901pB")) {
			bLoadCounter++;
		}
		
		return super.loadClass(s);
	}


	private byte[] getBytesA() {
		// returns byte-representation of the loading0901pA class
		byte[] data = { -54, -2, -70, -66, 0, 0, 0, 46, 0, 21, 10, 0, 4, 0, 13, 9, 
				0, 14, 0, 15, 7, 0, 16, 7, 0, 17, 1, 0, 6, 60, 105, 110, 
				105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 
				1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 
				108, 101, 1, 0, 4, 116, 101, 115, 116, 1, 0, 3, 40, 41, 73, 1, 
				0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 18, 108, 
				111, 97, 100, 105, 110, 103, 48, 57, 48, 49, 112, 65, 46, 106, 97, 118, 
				97, 12, 0, 5, 0, 6, 7, 0, 18, 12, 0, 19, 0, 20, 1, 0, 
				88, 111, 114, 103, 47, 97, 112, 97, 99, 104, 101, 47, 104, 97, 114, 109, 
				111, 110, 121, 47, 118, 116, 115, 47, 116, 101, 115, 116, 47, 118, 109, 47, 
				106, 118, 109, 115, 47, 99, 108, 97, 115, 115, 76, 76, 73, 47, 108, 111, 
				97, 100, 105, 110, 103, 47, 108, 111, 97, 100, 105, 110, 103, 48, 57, 47, 
				108, 111, 97, 100, 105, 110, 103, 48, 57, 48, 49, 47, 108, 111, 97, 100, 
				105, 110, 103, 48, 57, 48, 49, 112, 65, 1, 0, 16, 106, 97, 118, 97, 
				47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 1, 0, 88, 111, 
				114, 103, 47, 97, 112, 97, 99, 104, 101, 47, 104, 97, 114, 109, 111, 110, 
				121, 47, 118, 116, 115, 47, 116, 101, 115, 116, 47, 118, 109, 47, 106, 118, 
				109, 115, 47, 99, 108, 97, 115, 115, 76, 76, 73, 47, 108, 111, 97, 100, 
				105, 110, 103, 47, 108, 111, 97, 100, 105, 110, 103, 48, 57, 47, 108, 111, 
				97, 100, 105, 110, 103, 48, 57, 48, 49, 47, 108, 111, 97, 100, 105, 110, 
				103, 48, 57, 48, 49, 112, 66, 1, 0, 1, 105, 1, 0, 1, 73, 0, 
				33, 0, 3, 0, 4, 0, 0, 0, 0, 0, 2, 0, 1, 0, 5, 0, 
				6, 0, 1, 0, 7, 0, 0, 0, 29, 0, 1, 0, 1, 0, 0, 0, 
				5, 42, -73, 0, 1, -79, 0, 0, 0, 1, 0, 8, 0, 0, 0, 6, 
				0, 1, 0, 0, 0, 7, 0, 1, 0, 9, 0, 10, 0, 1, 0, 7, 
				0, 0, 0, 28, 0, 1, 0, 1, 0, 0, 0, 4, -78, 0, 2, -84, 
				0, 0, 0, 1, 0, 8, 0, 0, 0, 6, 0, 1, 0, 0, 0, 9, 
				0, 1, 0, 11, 0, 0, 0, 2, 0, 12};
		return data;

	}
}


