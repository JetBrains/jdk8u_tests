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

package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1005;

import java.awt.image.RescaleOp;
import java.lang.reflect.InvocationTargetException;

import org.apache.harmony.share.Result;
import org.apache.harmony.vts.test.vm.jvms.classLLI.loading.share.LoadingClassLoaderJ;

public class loading1005n {
	final static String loading1005nAClassName = "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1005.loading1005nA";
	final static String loading1005nClassName = "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1005.loading1005n";

	static final int FAIL2 = 110;
	
	public static void main(String[] args) throws Exception {
		System.exit(new loading1005n().test(args));
	}
	
	public int test(String[] args) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		loading1005nLoader cl = new loading1005nLoader(new String[]{loading1005nAClassName, loading1005nClassName});
		Class c = Class.forName(loading1005nAClassName, true, cl);
		Integer i = (Integer) c.getMethod("test", null).invoke(c.newInstance(), null);
		if (i.intValue() != Result.PASS ) {
			return Result.FAIL;
		}
		
		cl.isCounting = true;
		
		i = (Integer) c.getMethod("test", null).invoke(c.newInstance(), null);
		if (i.intValue() != Result.PASS ) {
			return FAIL2;
		}

		// check that the class loader was not invoked during second call of the test method
		return ((cl.counter == 0) ? Result.PASS : Result.FAIL); 
	}
}



