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


; Author: Alexander V. Esin
; Version: $Revision: 1.1 $
.bytecode 49.0
.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w08/ldc_w0805/ldc_w0805p
.super java/lang/Object

.field private static final testField Ljava/lang/String; = "org.apache.harmony.vts.test.vm.jvms.instructions.loadStore.ldc_w.ldc_w08.ldc_w0805.ldc_w0805pTest"

.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method


; Test method

.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 2

   ; Pushes class org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w08/ldc_w0805/ldc_w0805pTest
   ; onto the stack 
   ldc_w org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w08/ldc_w0805/ldc_w0805pTest

   ; Pushes string "org.apache.harmony.vts.test.vm.jvms.instructions.loadStore.ldc_w.ldc_w08.ldc_w0805.ldc_w0805pTest" 
   ; onto the stack
   getstatic org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w08/ldc_w0805/ldc_w0805p/testField Ljava/lang/String;
   
   ; Pushes class org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w08/ldc_w0805/ldc_w0805pTest
   ; onto the stack 
   invokestatic java/lang/Class/forName(Ljava/lang/String;)Ljava/lang/Class; 
   
   ; Compares values on the top of the stack
   ; If they are equal return 104 (PASS) else goto fail: and return 105 (FAIL)
   if_acmpne fail 
   sipush 104
   ireturn
   
fail:
   sipush 105
   ireturn
.end method

.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w08/ldc_w0805/ldc_w0805p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w08/ldc_w0805/ldc_w0805p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/ldc_w/ldc_w08/ldc_w0805/ldc_w0805p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
