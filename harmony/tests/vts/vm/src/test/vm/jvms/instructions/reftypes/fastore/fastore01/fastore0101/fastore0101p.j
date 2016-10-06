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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/fastore/fastore01/fastore0101/fastore0101p
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
   .limit locals 3


; create new array float[1] and store arrayref to the local variable 2
   iconst_1
   newarray float
   astore_2

; store 2 to the array[0]
   aload_2
   iconst_0
   fconst_2
   fastore

; load value from array[0]
   aload_2
   iconst_0
   faload
   fconst_2 ; push 2
   fcmpg
   iconst_0
   if_icmpne Fail ; if not equivalent jump to Fail
   sipush 104
   ireturn
Fail:
   sipush 105
   ireturn


   return
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/fastore/fastore01/fastore0101/fastore0101p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/fastore/fastore01/fastore0101/fastore0101p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/fastore/fastore01/fastore0101/fastore0101p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
