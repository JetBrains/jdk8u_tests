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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic02/invokestatic0201/invokestatic0201p
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
   .limit stack 2
   .limit locals 2

; invoke static method staticTest("Hello!", 2);
   ldc "Hello!" ; push "Hello!"
   iconst_2 ; push int 2
   invokestatic org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic02/invokestatic0201/invokestatic0201p/staticTest(Ljava/lang/String;I)I
   ireturn

.end method

;
; staticTest method
.method public static staticTest(Ljava/lang/String;I)I
   .limit stack 2
   .limit locals 2

; load String from local 0 and checking it (must be == "Hello!")
   aload_0
   ldc "Hello!"
   if_acmpne Fail

; load int from local 1 and checking it (must be == 2)
   iload_1
   iconst_2
   if_icmpne Fail

   sipush 104 ; push 104
   ireturn ; return 104

; local_0!="Hello!" or local_1!=2
Fail:
   sipush 105
   ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic02/invokestatic0201/invokestatic0201p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic02/invokestatic0201/invokestatic0201p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic02/invokestatic0201/invokestatic0201p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
