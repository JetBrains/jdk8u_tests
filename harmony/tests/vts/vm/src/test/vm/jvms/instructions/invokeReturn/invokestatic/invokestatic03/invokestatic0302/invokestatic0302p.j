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
; Version: $Revision: 1.1 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic03/invokestatic0302/invokestatic0302p
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 

; test  method
.method public static testMethod(FFFFFF)I
  .limit locals 7
  .limit stack 2


  fload_0
  ldc 1.4E-45

  fcmpl
  ifne Fail

  fload_1
  ldc 3.4028235E38

  fcmpl
  ifne Fail
  
  fload_2
  ldc -3.4028235E39 ; -Infinity  
  fcmpl
  sipush 1
  if_icmpeq Fail

  fload 3
  ldc 3.4028235E39 ; +Infinity  
  fcmpl
  sipush 1
  if_icmpeq Fail

  fload 4
  invokestatic java/lang/Float/isNaN(F)Z
  ifeq Fail

  fload 5
  ldc 3.14

  fcmpl
  ifne Fail
  ; test passed
  sipush 104
  ireturn

Fail:

  ; test fail
  sipush 105
  ireturn
.end method 

;
; test method
.method public test([Ljava/lang/String;)I
  .limit locals 7
  .limit stack 7

  ldc 1.4E-45 ; MIN_VALUE
  fstore_1	; store into local var 1

  ldc 3.4028235E38 ; MAX_VALUE
  fstore_2	; store into local var 2
  
  ldc -3.4028235E39 ; -Infinity  
  fstore_3	; store into local var 3

  ldc 3.4028235E39 ; +Infinity
  fstore 4	; store into local var 4

  ldc 0.0
  dup
  fdiv ; NaN
  fstore 5	; store into local var 5

  ldc 3.14 ; Pi
  fstore 6
 
  ; invoke the test method
  fload_1
  fload_2
  fload_3
  fload 4
  fload 5
  fload 6

  ; invoke the test method
  invokestatic org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic03/invokestatic0302/invokestatic0302p/testMethod(FFFFFF)I
  ; test passed
  ireturn
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic03/invokestatic0302/invokestatic0302p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic03/invokestatic0302/invokestatic0302p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic03/invokestatic0302/invokestatic0302p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
