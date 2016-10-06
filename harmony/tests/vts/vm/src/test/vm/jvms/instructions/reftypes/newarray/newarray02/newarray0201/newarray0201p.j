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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/newarray/newarray02/newarray0201/newarray0201p
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

; creating massive byte [3]
   iconst_3
   newarray byte
   astore_1 ; saving it (reference) in the local variable 1

   ; initialize [0] = 3
   aload_1
   iconst_0
   iconst_3
   bastore

   ; initialize [1] = 2
   aload_1
   iconst_1
   iconst_2
   bastore

   ; initialize [2] = 4
   aload_1
   iconst_2
   iconst_4
   bastore

   ; check [0] (must == 3)
   aload_1
   iconst_0
   baload
   iconst_3
   if_icmpne Fail ; if [0]!=3

   ; check [1] (must == 2)
   aload_1
   iconst_1
   baload
   iconst_2
   if_icmpne Fail ; if [1]!=2

   ; check [2] (must == 4)
   aload_1
   iconst_2
   baload
   iconst_4
   if_icmpne Fail ; if [2]!=4

   ; All values initialized correct
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

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/newarray/newarray02/newarray0201/newarray0201p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/newarray/newarray02/newarray0201/newarray0201p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/newarray/newarray02/newarray0201/newarray0201p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
