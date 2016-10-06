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
package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.share;

import java.lang.reflect.InvocationTargetException;


/**
 *
 *  Takes an array of class names from the arguments array.
 *  Invokes method "test()" of the first class and returns the result value.  
 * 
 **/
public class ClassLoaderRunner {
	String[] getNames(String[] s) {
		String[] res;
		int size = Integer.parseInt(s[0]);
		res = new String[size];
		for (int i = 0; i < size; i++) {
			res[i] = s[i+1];
		}
		
		return res;
	}
	
	public int test(String[] s) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		String[] names = getNames(s);

		LoadingClassLoaderJ loader = new LoadingClassLoaderJ(names);
		Class c = Class.forName(names[0], true, loader);
		Integer b = (Integer) c.getMethod("test", (Class[])null).invoke(c.newInstance(), (Object[])null);
		return b.intValue();
	}
	
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		System.exit(new ClassLoaderRunner().test(args));
	}
}
