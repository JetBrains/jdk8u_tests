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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual05/invokevirtual0501/invokevirtual0501p
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
   .limit stack 5
   .limit locals 2

   
   aload_0 ; push this
   dconst_1 ; push double 1
   lconst_1 ; push long 1
   invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual05/invokevirtual0501/invokevirtual0501p/virtualTest(DJ)I
   ireturn ; return 104

.end method

;
; virtualTest method
.method public virtualTest(DJ)I
   .limit stack 4
   .limit locals 5

; load Double from local 1 and 2 and checking it (must be == 1.0)
   dload_1
   dconst_1
   dcmpg
   ifne Fail

; load int from local 3 and 4 and checking it (must be == 1)
   lload_3
   lconst_1
   lcmp
   ifne Fail

   sipush 104 ; push 104
   ireturn ; return 104

; local_(1<< + 2)!=1.0 or local_(3<< + 4)!=1
Fail:
   sipush 105
   ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 3
  .limit locals 2

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual05/invokevirtual0501/invokevirtual0501p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual05/invokevirtual0501/invokevirtual0501p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokevirtual/invokevirtual05/invokevirtual0501/invokevirtual0501p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
