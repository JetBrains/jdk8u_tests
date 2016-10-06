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
 * @version $Revision: 1.2 $
 */
package org.apache.harmony.vts.test.vm.jvms.classFile.attributes.annotation.runtimeVisibleAnnotations.classinfo02;

import java.lang.annotation.*;

public class annotation02p {

   public int test(String [] args) throws Exception {
      Class cl = Class.forName("org.apache.harmony.vts.test.vm.jvms.classFile.attributes.annotation.runtimeVisibleAnnotations.share.classinfo_annotation");
      if(!cl.isAnnotation()) return 105;
      Annotation [] annotations = cl.getAnnotations();
      if(annotations.length != 1) return 110;
      //the element of the array represents Retention annotation.
      if(annotations[0].annotationType().getName()
         .equals("java.lang.annotation.Retention")) 
         return 104;
      return 111;
   }

   public static void main(String [] args) throws Exception {
      System.exit((new annotation02p()).test(args));
   }
}
