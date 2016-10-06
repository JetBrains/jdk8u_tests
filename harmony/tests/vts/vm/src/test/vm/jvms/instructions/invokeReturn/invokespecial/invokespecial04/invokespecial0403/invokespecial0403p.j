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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial04/invokespecial0403/invokespecial0403p
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 

; test method
.method public testMethod(DDDDDD)I
  .limit locals 13
  .limit stack 4

  dload_1
  ldc2_w 4.9E-324 ; MIN_VALUE
  dcmpl
  ifne Fail

  dload_3
  ldc2_w 1.7976931348623157E308 ; MAX_VALUE
  dcmpl
  ifne Fail

  dload 5
  ldc2_w -1.7976931348623157E309 ; -Infinity  
  dcmpl
  sipush 1
  if_icmpeq Fail

  dload 7
  ldc2_w 1.7976931348623157E309 ; +Infinity
  dcmpl
  sipush 1
  if_icmpeq Fail

  dload 9
  invokestatic java/lang/Double/isNaN(D)Z
  ifeq Fail

  dload 11
  ldc2_w 3.14
  dcmpl
  ifne Fail
  ; test passed
  sipush 104
  ireturn
Fail:
  ; test failed
  sipush 105
  ireturn
.end method 

;
; test method
.method public test([Ljava/lang/String;)I
  .limit locals 13
  .limit stack 14

  ldc2_w 4.9E-324 ; MIN_VALUE
  dstore 1	; store into local var 1

  ldc2_w 1.7976931348623157E308 ; MAX_VALUE
  dstore 3	; store into local var 3
  
  ldc2_w -1.7976931348623157E309 ; -Infinity  
  dstore 5	; store into local var 5

  ldc2_w 1.7976931348623157E309 ; +Infinity
  dstore 7	; store into local var 7

  ldc2_w 0.0
  dup2
  ddiv ; NaN
  dstore 9	; store into local var 9

  ldc2_w 3.14 ; Pi
  dstore 11

  aload_0

  ; invoke the test method

  dload 1
  dload 3
  dload 5
  dload 7
  dload 9
  dload 11
	
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial04/invokespecial0403/invokespecial0403p/testMethod(DDDDDD)I
  ; must be 104 on the operand stack
  ; test passed
  ireturn


.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial04/invokespecial0403/invokespecial0403p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial04/invokespecial0403/invokespecial0403p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial04/invokespecial0403/invokespecial0403p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
