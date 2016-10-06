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
package org.apache.harmony.vts.test.vm.jvms.verifier.classFile.attributes.signature.signature04.signature0402;

import java.lang.reflect.TypeVariable;

public class signature0402p {

   public int test(String [] args) throws Exception {
      signature0402Test<String, Class> test = 
            new signature0402Test<String, Class>();
      TypeVariable [] types = test.getClass().getTypeParameters();
      if(types.length != 2) return 105;
      //the element of the array represents TypeVariable object of type T.
      if(!types[0].getName().equals("T")) return 110;
      if(!types[1].getName().equals("T2")) return 111;
      //assign to a generic filed a string
      test.testField = "signature test";
      test.testField2 = this.getClass();
      test.test("signature test");
      test.test2(this.getClass());
      return 104;

   }

   public static void main(String [] args) throws Exception {
      System.exit((new signature0402p()).test(args));
   }
}
