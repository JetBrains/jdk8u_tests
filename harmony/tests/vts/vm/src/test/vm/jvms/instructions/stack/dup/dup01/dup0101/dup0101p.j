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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/stack/dup/dup01/dup0101/dup0101p
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

   
   sipush 343 ; push short 343
   dup ; must lead the stack to: ...343,343
   sipush 343 ; push short 343
   if_icmpne lab1 ; if not equivalent jump to lab1
   sipush 343 ; push short 343
   if_icmpne lab2 ; if not equivalent jump to lab2
   sipush 104 ; push 201
   ireturn ; return 104
lab1:
   pop ; remove top of the stack
lab2:
   sipush 105
   ireturn ; return 105

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/stack/dup/dup01/dup0101/dup0101p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/stack/dup/dup01/dup0101/dup0101p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/stack/dup/dup01/dup0101/dup0101p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
