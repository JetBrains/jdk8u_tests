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

import java.io.IOException;
import java.io.InputStream;

public class LoadingClassLoaderJ extends ClassLoader {

		protected String[] names;
		
		public LoadingClassLoaderJ(String[] names) {
			this.names = names;
		}
		
		public Class loadClass(String s) throws ClassNotFoundException {
			for (int i = 0; i < names.length; i++) {
				if (s.equals(names[i])) {
					return loadClassFromResource(s, convertToFileName(s));
				}
			}
			return super.loadClass(s);
		}
		
		public static String convertToFileName(String s) {
			return "/" + s.replace('.', '/') + ".class";
		}

		protected Class loadClassFromResource(String className, String fileName) throws ClassNotFoundException {
			InputStream is = this.getClass().getResourceAsStream(fileName);
			try {
				byte[] data = new byte[is.available()];
				is.read(data);
				return defineClass(className, data, 0, data.length);
			} catch (Exception e) {
				throw new ClassNotFoundException("Error occurs while loading class: " + className + "\n" + "from " + fileName,  e);
			}
		}
		
}
