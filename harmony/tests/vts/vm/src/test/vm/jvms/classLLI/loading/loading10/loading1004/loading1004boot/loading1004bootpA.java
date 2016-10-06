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
package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1004.loading1004boot;

import org.apache.harmony.share.Result;
import org.apache.harmony.vts.test.vm.jvms.classLLI.loading.share.LoadingClassLoaderJ;

public class loading1004bootpA {
        static final int FAIL_BASE = 110;
	public int test() {
		int[] ii = new int[1];
		byte[] bb = new byte[1];
		short[] ss = new short[1];
		boolean[] b = new boolean[1];
		char[] cc = new char[1]; 
		double[] dd = new double[1];
		float[] ff = new float[1];
		long[] ll = new long[1];
		
		Class[] classes = new Class[] { ii.getClass(), bb.getClass(),
				ss.getClass(), ll.getClass(), b.getClass(), cc.getClass(),
				dd.getClass(), ff.getClass() };
		
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].getClassLoader() != null && classes[i].getClassLoader().getClass() == LoadingClassLoaderJ.class) {
				return FAIL_BASE + i;
			}
		}
		return Result.PASS;
	}
}
