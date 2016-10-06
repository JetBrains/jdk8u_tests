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
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2107/invokespecial2107p
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 

; native method
.method public native nativeMethod(FFFFFF)I
.end method 

;
; test method
.method public test([Ljava/lang/String;)I
  .limit locals 7
  .limit stack 7

  ; System.loadLibrary("jnitests")
  ldc "jnitests"
  invokestatic java/lang/System/loadLibrary(Ljava/lang/String;)V

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
 
  aload_0
  ; invoke the native method
  fload_1
  fload_2
  fload_3
  fload 4
  fload 5
  fload 6
	
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2107/invokespecial2107p/nativeMethod(FFFFFF)I
  ; must be 104 on the operand stack
  ; test passed
  ireturn


.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2107/invokespecial2107p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2107/invokespecial2107p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2107/invokespecial2107p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
