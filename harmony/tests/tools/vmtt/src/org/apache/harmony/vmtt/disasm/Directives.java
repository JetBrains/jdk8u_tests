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
package org.apache.harmony.vmtt.disasm;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.2 $
 */

public class Directives {


	public static final int D_CATCH			= 0;
	public static final int D_CLASS			= 1;
	public static final int D_END			= 2;
	public static final int D_FIELD			= 3;
	public static final int D_IMPLEMENTS	= 4;
	public static final int D_INTERFACE		= 5;
	public static final int D_LIMIT			= 6;
	public static final int D_LINE			= 7;
	public static final int D_METHOD		= 8;
	public static final int D_SOURCE		= 9;
	public static final int D_SUPER			= 10;
	public static final int D_THROWS		= 11;
	public static final int D_VAR			= 12;

	public static String defaultDirectivePrefix = ".";

	private static String[] directives = {"catch", "class",
										  "end", "field",
										  "implements", "interface",
										  "limit", "line",
										  "method", "source",
										  "super", "throws",
										  "var"};
	
	public static String getDirective(int id) {
		return defaultDirectivePrefix + directives[id];
	}
	
	public static boolean isDirective(String d) {
		for (int i = 0; i < directives.length; i++) {
			if (d.equals(directives[i])) {
				return true;
			}
		}
		return false;
	}
}
