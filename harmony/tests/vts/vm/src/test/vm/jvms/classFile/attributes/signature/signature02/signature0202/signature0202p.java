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
package org.apache.harmony.vts.test.vm.jvms.classFile.attributes.signature.signature02.signature0202;

import java.lang.reflect.TypeVariable;

public class signature0202p {

   public int test(String [] args) throws Exception {
      Class cl = Class.forName("org.apache.harmony.vts.test.vm.jvms.classFile.attributes.signature.signature02.signature0202.signature0202Test");
      TypeVariable [] types = cl.getTypeParameters();
      if(types.length != 1) return 105;
      //the element of the array represents TypeVariable object of type T.
      if(!types[0].getName().equals("T")) return 110;
      //instantiate the class
      signature0202Test<String> sigTest = new signature0202Test<String>();
      return sigTest.test("signature test");

   }

   public static void main(String [] args) throws Exception {
      System.exit((new signature0202p()).test(args));
   }
}
