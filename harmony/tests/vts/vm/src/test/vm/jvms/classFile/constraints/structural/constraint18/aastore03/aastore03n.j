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
; Version: $Revision: 1.4 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03n
.super java/lang/Object
.implements org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03nInterface

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
   .limit stack 3
   .limit locals 3

.catch java/lang/VerifyError from first to second using catcher
.catch java/lang/ArrayStoreException from first to second using catcher1

first:
   new org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03nVer
   astore_2
   aload_2
   invokespecial org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03nVer/<init>()V

   aload_2
   sipush 1
   anewarray org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03n
   putfield org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03nVer/testField [Lorg/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03nInterface;

   aload_2
   invokevirtual org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03nVer/test2()V
second:
   sipush 105
   ireturn

catcher:
   sipush 104
   ireturn

catcher1:
   sipush 104
   ireturn


.end method

.method public myMethod()V
  .limit stack 2
  .limit locals 1

   getstatic java/lang/System/out Ljava/io/PrintStream;
   ldc "Hello"
   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V

   return
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03n
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03n/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/classFile/constraints/structural/constraint18/aastore03/aastore03n/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
