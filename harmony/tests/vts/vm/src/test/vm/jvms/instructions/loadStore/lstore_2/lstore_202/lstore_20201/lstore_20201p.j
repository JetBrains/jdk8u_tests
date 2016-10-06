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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/lstore_2/lstore_202/lstore_20201/lstore_20201p
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
   .limit stack 4
   .limit locals 4

   ; lstore_2 must not throw any exception.

   lconst_1
   lstore_2 ; store long 1 to the local variables 2 and 3
   lload_2 ; load from local variables 2 and 3
   lconst_1
   lcmp
   ifne Fail ; if not equals jump to Fail
   sipush 104
   ireturn

Fail:
   sipush 105
   ireturn
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/lstore_2/lstore_202/lstore_20201/lstore_20201p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/lstore_2/lstore_202/lstore_20201/lstore_20201p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/lstore_2/lstore_202/lstore_20201/lstore_20201p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
