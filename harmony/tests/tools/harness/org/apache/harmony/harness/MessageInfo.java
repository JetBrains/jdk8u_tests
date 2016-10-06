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
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.4 $
 */
package org.apache.harmony.harness;

/*
 * This is a class that provide some info for log message to other classes.
 * Expected format of all log messages: "product_name + '\t' + class_name + '\t' +
 * method_name + ':'"
 */
public class MessageInfo {
    public static final String MSG_PREFIX     = "TestHarness:";
    public static final String UNEX_EXCEPTION = "unexpected exception ";
}