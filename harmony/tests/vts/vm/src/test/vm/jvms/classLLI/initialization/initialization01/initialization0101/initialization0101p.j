;    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable
;
;    Licensed under the Apache License, Version 2.0 (the "License");
;    you may not use this file except in compliance with the License.
;    You may obtain a copy of the License at
;
;       http://www.apache.org/licenses/LICENSE-2.0
;
;    Unless required by applicable law or agreed to in writing, software
;    distributed under the License is distributed on an "AS IS" BASIS,
;    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;
;    See the License for the specific language governing permissions and
;    limitations under the License.

;
; Author: Alexander D. Shipilov
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization01/initialization0101/initialization0101p
.super org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization01/initialization0101/initialization0101pTest
.field public static testFieldThis I

;
; initializer
.method public <init>()V
   aload_0
   invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization01/initialization0101/initialization0101pTest/<init>()V
   return
.end method

; static initializer
.method public static <clinit>()V
; put 104 into the field. Must occurs after super initialization.
   sipush 104
   putstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization01/initialization0101/initialization0101p/testFieldThis I 
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 2

   getstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization01/initialization0101/initialization0101p/testFieldThis I
   ireturn

.end method

;
; main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization01/initialization0101/initialization0101p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization01/initialization0101/initialization0101p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization01/initialization0101/initialization0101p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
