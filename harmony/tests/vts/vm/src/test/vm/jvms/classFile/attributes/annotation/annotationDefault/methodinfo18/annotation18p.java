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

package org.apache.harmony.vts.test.vm.jvms.classFile.attributes.annotation.annotationDefault.methodinfo18;

import java.io.IOException;
import java.io.InputStream;

public class annotation18p {
   static final int arrayLen = 65534;
   int offset = 0;
   static final String testedClassName = "org.apache.harmony.vts.test.vm.jvms.classFile.attributes.annotation.annotationDefault.methodinfo18.annotation18";
   static final byte [] attributeTemplate = {
        0x49, //tag = I
        0x0, 0x8  //const_value_index = #8
   };

   public int test(String [] args) throws Exception {
      MyClassLoader mcl = new MyClassLoader();
      Class cl = Class.forName(testedClassName, true, mcl);
      Object o = cl.getMethod("value", new Class[]{}).getDefaultValue();
      if(((int [])o).length == (arrayLen + 1)) 
          return 104;
      return 105;
   }

   public static void main(String [] args) throws Exception {
      System.exit((new annotation18p()).test(args));
   }

   class MyClassLoader extends ClassLoader {
       public Class loadClass(String name) throws ClassNotFoundException {

           if(name.equals(testedClassName)) {
                byte [] orig = readClassFromFile(name);
                int length = orig.length +   (arrayLen  *  attributeTemplate.length);

                byte [] full = new byte[length];

                offset = orig.length - 12;

                System.arraycopy(orig, 0, full, 0, orig.length - 2);

                for(int i = 0; i < arrayLen; ++i) {
                 System.arraycopy(attributeTemplate, 0, full, 
                      (orig.length - 2) + (i * attributeTemplate.length), 
                      attributeTemplate.length);
                }

                full[offset] = 0;     //  attribute_length = 0x30000 
                full[offset + 1] = 3; //  = 3 + 65535 * attributeTemplate.length
                full[offset + 2] = 0; //
                full[offset + 3] = 0; //

                full[offset + 5] = (byte) 0xFF;  //
                full[offset + 6] = (byte) 0xFF;  // array_length = 65535

                //attributes_count = 0
                full[(orig.length - 2) + (arrayLen * attributeTemplate.length)] = 0;
                full[(orig.length - 1) + (arrayLen * attributeTemplate.length)] = 0;

                return defineClass(name, full, 0, full.length);
           }
           return super.loadClass(name);
       }

       private byte [] readClassFromFile(String classFileName) 
          throws ClassNotFoundException  {
            String classFileNameWithPath =   classFileName.replace(".", "/")
              + ".class";

                try {
                  InputStream is = getResourceAsStream(classFileNameWithPath);
                  int res = 0;
                  byte [] buf = new byte [1024];
                  byte [] fullBuf = new byte[0];
                  for(;;) {
                     res = is.read(buf);
                     if(res == -1) {
                        break;
                     }
                     byte [] tmpBuf = new byte[fullBuf.length + res];
                     System.arraycopy(fullBuf, 0, tmpBuf, 0, fullBuf.length);
                     System.arraycopy(buf, 0, tmpBuf, fullBuf.length, res);

                     fullBuf = tmpBuf;

                     if(res < buf.length) {
                        break;
                     }
                  }
                  is.close();
                  return fullBuf;

                } catch (IOException e) {
                    throw new ClassNotFoundException(e.getMessage(), e);
                }

        }
   }

}
