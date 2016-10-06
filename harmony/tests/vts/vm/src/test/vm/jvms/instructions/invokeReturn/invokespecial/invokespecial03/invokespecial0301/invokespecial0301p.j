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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial03/invokespecial0301/invokespecial0301p
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

   
   aload_0 ; push this
   ldc "Hello!" ; push "Hello!"
   iconst_3 ; push int 3
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial03/invokespecial0301/invokespecial0301p/virtualTest(Ljava/lang/String;I)I
          ; must return 104
   ireturn ; return 104

.end method

;
; virtualTest method
.method private virtualTest(Ljava/lang/String;I)I
   .limit stack 2
   .limit locals 3

; load String from local 1 and checking it (must be == "Hello!")
   aload_1
   ldc "Hello!"
   if_acmpne Fail

; load int from local 2 and checking it (must be == 3)
   iload_2
   iconst_3
   if_icmpne Fail

   sipush 104 ; push 104
   ireturn ; return 104

; local_1!="Hello!" or local_2!=3
Fail:
   sipush 105
   ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial03/invokespecial0301/invokespecial0301p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial03/invokespecial0301/invokespecial0301p/<init>()V
  aload_0
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial03/invokespecial0301/invokespecial0301p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
