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
 * @author Kirill Rodionov
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.harness.plugins.variationtests;

/**
 * This class defines property name prefixes used to store certain data in
 * TestIR (which is passed to TResIR after test execution)
 */
public interface VConstants {
    public static final String CFG_PREFIX          = "cfg_";
    public static final String FILTER_PREFIX       = "filter_";
    public static final String VARIANT_PREFIX      = "variant_";
    public static final String RESOLVED_CMD_PREFIX = "resolvedCmd";
}