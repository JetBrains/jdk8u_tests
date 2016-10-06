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

package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading11.loading1102;

import org.apache.harmony.share.Result;
import org.apache.harmony.vts.test.vm.jvms.classLLI.loading.share.LoadingClassLoaderJ;

public class loading1102p {
	final static String realName = "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading11.loading1102.loading1102p";
	final static String testedName = realName + "testedName";
	public static void main(String[] s) throws Exception {
		System.exit(new loading1102p().test(s));
	}
	
	public int test(String[] s) throws ClassNotFoundException {
		Loader loader = new Loader(new String[]{realName});
		try {
			loader.loadClass(testedName);
		} catch (NoClassDefFoundError e) {
			return Result.PASS;
		}
		return Result.FAIL;
	}
	
}
class Loader extends LoadingClassLoaderJ {
	public Loader(String[] names) {
		super(names);
	}

	public Class loadClass(String s) throws ClassNotFoundException {
		if (s.equals(loading1102p.testedName)) {
			return super.loadClassFromResource(s, convertToFileName(loading1102p.realName));
		}
		return super.loadClass(s);
	}
	
}

