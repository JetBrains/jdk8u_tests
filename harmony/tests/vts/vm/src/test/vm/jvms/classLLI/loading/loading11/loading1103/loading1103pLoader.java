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

import org.apache.harmony.vts.test.vm.jvms.classLLI.loading.share.LoadingClassLoaderJ;

public class loading1103pLoader extends LoadingClassLoaderJ {
	private boolean isObjectFound = false;
	private boolean isTestFirst = false;
	public boolean isObjectFirst = false;
	private String interfaceName;

	public loading1103pLoader(String interfaceName) {
		super(new String[]{interfaceName});
		this.interfaceName = interfaceName;
	}

	public Class loadClass(String s) throws ClassNotFoundException {
		Class res = super.loadClass(s); // loading...

		if (!isObjectFound && s.equals("java.lang.Object")) {
			isObjectFound = true;
		}
		if (!isTestFirst && s.equals(interfaceName)) {
			if (isObjectFound) {
				// Object was loaded first, all is ok
				isObjectFirst = true; 
			} else {
				// Found interface loading first!
				isTestFirst = true;
			}
		}
			
		return res;
	}

}
