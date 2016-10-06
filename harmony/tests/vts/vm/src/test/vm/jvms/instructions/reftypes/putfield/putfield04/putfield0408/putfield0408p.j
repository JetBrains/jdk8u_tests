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
; Author: Maxim V. Makarov
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield04/putfield0408/putfield0408p
.super java/lang/Object

.field public testField F

.field public testField1 D

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 7
   .limit locals 2

   aload_0 ; push this

   ldc 0.0
   dup
   fdiv ; NaN
   putfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield04/putfield0408/putfield0408p/testField F 

   aload_0 ; push this
   getfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield04/putfield0408/putfield0408p/testField F 

   ldc 1.0
   fcmpg
   sipush 1
   if_icmpne Fail

   aload_0 ; push this

   ldc2_w 0.0
   dup2
   ddiv ; NaN
   putfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield04/putfield0408/putfield0408p/testField1 D 

   aload_0 ; push this
   getfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield04/putfield0408/putfield0408p/testField1 D 

   ldc2_w 1.0d
   dcmpg
   sipush 1
   if_icmpne Fail

   ; test passed
   sipush 104
   ireturn

Fail:

   ;test failed
   sipush 105
   ireturn
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield04/putfield0408/putfield0408p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield04/putfield0408/putfield0408p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield04/putfield0408/putfield0408p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
