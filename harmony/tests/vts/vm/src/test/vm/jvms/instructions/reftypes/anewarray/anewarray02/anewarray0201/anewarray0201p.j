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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/anewarray/anewarray02/anewarray0201/anewarray0201p
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
   .limit stack 3
   .limit locals 2

; creating massive String [3]
   iconst_3
   anewarray java/lang/String
   astore_1 ; saving it (reference) in the local variable 1

   ; initialize [0] = "first"
   aload_1
   iconst_0
   ldc "first"
   aastore

   ; initialize [1] = "second"
   aload_1
   iconst_1
   ldc "second"
   aastore

   ; initialize [2] = "third"
   aload_1
   iconst_2
   ldc "third"
   aastore

   ; check [0] (must == "first")
   aload_1
   iconst_0
   aaload
   ldc "first"
   if_acmpne Fail

   ; check [1] (must == "second")
   aload_1
   iconst_1
   aaload
   ldc "second"
   if_acmpne Fail

   ; check [2] (must == "third")
   aload_1
   iconst_2
   aaload
   ldc "third"
   if_acmpne Fail

   ; All items initialized correct
   sipush 104
   ireturn

Fail:
   ; Incorrect initialization
   sipush 105
   ireturn

   return
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/anewarray/anewarray02/anewarray0201/anewarray0201p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/anewarray/anewarray02/anewarray0201/anewarray0201p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/anewarray/anewarray02/anewarray0201/anewarray0201p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
