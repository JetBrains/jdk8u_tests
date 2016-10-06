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

package org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading10.loading1001.loading1001userdef;

import org.apache.harmony.share.Result;

public class loading1001userdefpA {
	public int test() {
		loading1001userdefpB[] bb = new loading1001userdefpB[]{new loading1001userdefpB()};
		return ((bb[0].test() == Result.PASS && bb.getClass().getComponentType() == loading1001userdefpB.class) ? (Result.PASS) : (Result.FAIL)) ; 		
	}
}
