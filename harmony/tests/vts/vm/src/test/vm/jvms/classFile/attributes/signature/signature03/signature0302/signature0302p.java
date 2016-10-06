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
 * @author Alexander V. Esin
 * @version $Revision: 1.1 $
 */
package org.apache.harmony.vts.test.vm.jvms.classFile.attributes.signature.signature03.signature0302;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class signature0302p {

   public int test(String [] args) throws Exception {
      Class c =  Class.forName("org.apache.harmony.vts.test.vm.jvms.classFile.attributes.signature.signature03.signature0302.signature0302Test");
      Field field= c.getField("testField");
      Type type = field.getGenericType();
      TypeVariable typeVar = (TypeVariable) type;
      if(!(typeVar.getName().equals("T")))
         return 105;
      return 104;

   }

   public static void main(String [] args) throws Exception {
      System.exit((new signature0302p()).test(args));
   }
}
