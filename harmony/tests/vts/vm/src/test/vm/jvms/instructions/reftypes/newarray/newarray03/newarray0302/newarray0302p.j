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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/newarray/newarray03/newarray0302/newarray0302p
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

   .catch java/lang/ArrayIndexOutOfBoundsException from first to second using catcher
first:
   iconst_3
   newarray int ; creating massive int [3]
   iconst_3
   iconst_1
   iastore ; trying to store 1 in the [3]. Must throw ArrayIndexOutOfBoundsException.
second:
   goto Fail
catcher:
   pop ; removing the top of the stack
   ; the test passed: ArrayIndexOutOfBoundsException has been thrown
   sipush 104
   ireturn

Fail:
   ; the test failed due to: ArrayIndexOutOfBoundsException has not been thrown
   sipush 105
   ireturn

   return
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/newarray/newarray03/newarray0302/newarray0302p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/newarray/newarray03/newarray0302/newarray0302p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/newarray/newarray03/newarray0302/newarray0302p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
