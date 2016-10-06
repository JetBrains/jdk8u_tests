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
 * @version $Revision: 1.1 $
 */  
package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1003;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;


public class loading1003p {
        static final int BASE_FAIL = 110;

	public static void main(String[] args) throws Exception {
		System.exit(new loading1003p().test(args));
	}
	
	public int test(String[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException, NoSuchMethodException {
		if (!Modifier.isPublic(int[].class.getModifiers())) {
			return BASE_FAIL;
		}
		if (!Modifier.isPublic(byte[].class.getModifiers())) {
			return BASE_FAIL +1;
		}
		if (!Modifier.isPublic(short[].class.getModifiers())) {
			return BASE_FAIL + 2;
		}
		if (!Modifier.isPublic(boolean[].class.getModifiers())) {
			return BASE_FAIL + 3;
		}
		if (!Modifier.isPublic(char[].class.getModifiers())){
			return BASE_FAIL + 4;
		}
		if (!Modifier.isPublic(double[].class.getModifiers())) {
			return BASE_FAIL + 5;
		}
		if (!Modifier.isPublic(float[].class.getModifiers())) {
			return BASE_FAIL + 6;
		}
		if (!Modifier.isPublic(long[].class.getModifiers())) {
			return BASE_FAIL + 7;
		}
		
		return 104;
		
	}
		
}
