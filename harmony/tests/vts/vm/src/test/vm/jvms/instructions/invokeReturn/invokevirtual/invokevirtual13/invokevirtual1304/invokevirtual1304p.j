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
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304p
.super org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304pTest/invokevirtual1304pTest

;
; initializer
.method public <init>()V
   aload_0
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304pTest/invokevirtual1304pTest/<init>()V
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 2

   .catch java/lang/LinkageError from first to second using catcher

first:
   ; invoke protected accessTest from superclass. Must throw IllegalAccessError.

   ; create and initialize invokevirtual1302pTest
   new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304pSecond
   dup
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304pSecond/<init>()V
 
   invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304pSecond/test()V
   sipush 105
   ireturn ; return fail
second:
catcher:
   ; LinkageError has been thrown
   sipush 104
   ireturn


.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual13/invokevirtual1304/invokevirtual1304p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
