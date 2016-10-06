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
; Author: Ilia A. Leviev
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield17/getfield1701/getfield1701p
.super java/lang/Object
.implements org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield17/getfield1701/getfield1701pTest/getfield1701pTest


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
   .catch java/lang/IncompatibleClassChangeError from L1 to L2 using L2 
L1:
   new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield17/getfield1701/getfield1701p
   dup
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield17/getfield1701/getfield1701p/<init>()V
   getfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield17/getfield1701/getfield1701p/testField I
   sipush 105
   ireturn 
L2:
   sipush 104
   ireturn
   
.end method




;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield17/getfield1701/getfield1701p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield17/getfield1701/getfield1701p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/getfield/getfield17/getfield1701/getfield1701p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
