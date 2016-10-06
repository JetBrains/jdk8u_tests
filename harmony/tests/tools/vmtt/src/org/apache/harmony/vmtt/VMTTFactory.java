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
/*
 * Created on 20.12.2004
 */

package org.apache.harmony.vmtt;

import org.apache.harmony.vmtt.ir.ClassFileGenerator;
import org.apache.harmony.vmtt.ir.ClassFileParser;
import org.apache.harmony.vmtt.ir.DefaultClassFileGenerator;
import org.apache.harmony.vmtt.ir.DefaultClassFileParser;
import org.apache.harmony.vmtt.ccode.CodeFileParser;
import org.apache.harmony.vmtt.ccode.DefaultCodeFileParser;
import org.apache.harmony.vmtt.cdecode.CodeFileGenerator;
import org.apache.harmony.vmtt.cdecode.DefaultCodeFileGenerator;

/**
 * @author agolubit
 */

public class VMTTFactory {
    
    public static ClassFileParser getClassFileParser(String parser_name) {
        if (parser_name == null) {
            throw new NullPointerException();
        }
        if (parser_name == "Default") {
            return new DefaultClassFileParser();
        }
        return null;
    }

    public static CodeFileParser getCodeFileParser(String parser_name) {
        if (parser_name == null) {
            throw new NullPointerException();
        }
        if (parser_name == "Default") {
            return new DefaultCodeFileParser();
        }
        return null;
    }
    
    public static ClassFileGenerator getClassFileGenerator(String generator_name) {
        if (generator_name == null) {
            throw new NullPointerException();
        }
        if (generator_name == "Default") {
            return new DefaultClassFileGenerator();
        }
        return null;
    }
    
    public static CodeFileGenerator getCodeFileGenerator(String generator_name) {
        if (generator_name == null) {
            throw new NullPointerException();
        }
        if (generator_name == "Default") {
            return new DefaultCodeFileGenerator();
        }
        return null;
    }
}
