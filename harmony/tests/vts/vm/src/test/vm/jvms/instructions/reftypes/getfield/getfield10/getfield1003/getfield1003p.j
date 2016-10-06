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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield10/getfield1003/getfield1003p
.super org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield10/getfield1003/getfield1003pTest/getfield1003pTest

;
; initializer
.method public <init>()V
   aload_0
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield10/getfield1003/getfield1003pTest/getfield1003pTest/<init>()V
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 2

   .catch java/lang/IllegalAccessError from first to second using catcher   
first:

   aload_0
      ; must not throw java/lang/IllegalAccessError
   getfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield10/getfield1003/getfield1003pTest/getfield1003pTest/testField I
   istore_1
   sipush 104
   ireturn ; return pass

second:  
catcher:
   sipush 105
   ireturn
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield10/getfield1003/getfield1003p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield10/getfield1003/getfield1003p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield10/getfield1003/getfield1003p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
