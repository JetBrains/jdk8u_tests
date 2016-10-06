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

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201p
.super java/lang/Object

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
   .limit stack 2
   .limit locals 2

; static initialization of test class must be occurs before getstatic instruction, and must be initiated by it.
   getstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201pTest/testField I
   sipush 105 ; if it is not equal to 105 fail
   if_icmpne Fail

; put 104 into the field
   sipush 104
   putstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201pTest/testField I

; second initialization may be occurs while new instance creation. If it will occurs, field will contain 105 again.
   new org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201pTest
   invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201pTest/<init>()V

; get value of field
   getstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201pTest/testField I
   ireturn

Fail:
   sipush 105
   ireturn
.end method

;
; main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization02/initialization0201/initialization0201p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
