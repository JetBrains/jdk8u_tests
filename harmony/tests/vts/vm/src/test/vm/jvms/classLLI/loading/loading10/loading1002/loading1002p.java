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
package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1002;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.harmony.share.Result;
import org.apache.harmony.vts.test.vm.jvms.classLLI.loading.share.LoadingClassLoaderJ;

public class loading1002p {

	static final String CLASS_HOLDER_NAME = "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1002.loading1002pClassHolder";
	static final String CLASS_HOLDER_NAME_PRIVATE = CLASS_HOLDER_NAME+"$loading1002pPrivate";
	static final String CLASS_HOLDER_NAME_PROTECTED = CLASS_HOLDER_NAME+"$loading1002pProtected";
	static final String PUBLIC_NAME = "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1002.loading1002pPublic";
	static final String DEFAULT_NAME = "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1002.loading1002pDefault";
	static final int BASE_FAIL = 110;

	public static void main(String[] args) throws Exception {
		System.exit(new loading1002p().test(args));
	}

	public int test(String[] args) throws IllegalArgumentException,
			SecurityException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException,
			InstantiationException, NoSuchMethodException {

		Class c = getClass("getPublic");
		if (!Modifier.isPublic(c.getModifiers()) ) {
			return BASE_FAIL;
		}
		
		c = getClass("getProtected");
		if (!Modifier.isProtected(c.getModifiers()) ) {
			return BASE_FAIL + 1;
		}
		
		c = getClass("getPrivate");
		if (!Modifier.isPrivate(c.getModifiers()) ) {
			return BASE_FAIL + 2;
		}
		
		c = getClass("getDefault");
		int i = c.getModifiers();
		if (Modifier.isPrivate(i) || Modifier.isProtected(i) || Modifier.isPublic(i) ) {
			return BASE_FAIL + 3;
		}

		return Result.PASS;
	}
	
	private Class getClass(String modifier) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		String[] classNames = {CLASS_HOLDER_NAME, DEFAULT_NAME, PUBLIC_NAME, CLASS_HOLDER_NAME, CLASS_HOLDER_NAME_PRIVATE, CLASS_HOLDER_NAME_PROTECTED};
		Class t = Class.forName(CLASS_HOLDER_NAME, true, new LoadingClassLoaderJ(classNames));
		Method m = t.getMethod(modifier, null);
		return  (Class) m.invoke(t.newInstance(), null);
	}
}

