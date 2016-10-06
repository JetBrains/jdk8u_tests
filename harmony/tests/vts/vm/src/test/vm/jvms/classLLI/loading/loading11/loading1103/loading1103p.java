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

package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading11.loading1103;

import java.lang.reflect.InvocationTargetException;

import org.apache.harmony.share.Result;

public class loading1103p {
	final static String interfaceName = "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading11.loading1103.loading1103pInterface";
	public static void main(String[] args) throws Exception {
		System.exit(new loading1103p().test(args));
	}
	
	public int test(String[] args) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		loading1103pLoader cl = new loading1103pLoader(interfaceName);
		Class c = Class.forName(interfaceName, true, cl);
		if (cl.isObjectFirst) {
			return Result.PASS;
		} else {
			return Result.FAIL;
		}
	}
}
