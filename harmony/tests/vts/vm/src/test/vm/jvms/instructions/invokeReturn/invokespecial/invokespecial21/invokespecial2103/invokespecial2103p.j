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
; Version: $Revision: 1.4 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2103/invokespecial2103p
.super java/lang/Object

.field public static testField I = 105

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 

; native method
.method public native nativeMethod(ZBCSIJLjava/lang/Object;[I)V
.end method 

;
; test method
.method public test([Ljava/lang/String;)I
  .limit locals 10
  .limit stack 15

  ; System.loadLibrary("jnitests")
  ldc "jnitests"
  invokestatic java/lang/System/loadLibrary(Ljava/lang/String;)V

  aload_0

  iconst_1   ; 'true' on the stack
  bipush -127  ; push -127 on the stack
  ldc 65535    ; '\uFFFF' value on the stack
  sipush 104   ; short on the stack
  iconst_2     ; int on the stack
  lconst_1     ; long on the stack
  aload_0      ; this on the stack

  iconst_3
  newarray int ; int[3] on the stack
  ; invoke the native method
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2103/invokespecial2103p/nativeMethod(ZBCSIJLjava/lang/Object;[I)V

  getstatic org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2103/invokespecial2103p/testField I
  ; test passed
  ireturn
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2103/invokespecial2103p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2103/invokespecial2103p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial21/invokespecial2103/invokespecial2103p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
