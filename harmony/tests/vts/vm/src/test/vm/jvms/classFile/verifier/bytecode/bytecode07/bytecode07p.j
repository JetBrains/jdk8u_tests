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
; Author: Khen G. Kim
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode07/bytecode07p
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
   .limit locals 2
   .limit stack 2

   aload_0
   ifnonnull label
   aload_0
   goto finish 
label:
   new org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode07/bytecode07pTest
   dup
   invokespecial org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode07/bytecode07pTest/<init>()V
finish:
   ; Tested point - org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode07/bytecode07p
   ; should be on the operand stack 
   invokevirtual org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode07/bytecode07p/isSuper()I
   ireturn
.end method

;
; support method
.method public isSuper()I
   .limit locals 2
   .limit stack 2
   sipush 104
   ireturn
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode07/bytecode07p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode07/bytecode07p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode07/bytecode07p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method

