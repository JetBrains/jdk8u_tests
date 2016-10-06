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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/branching/tableswitch/tableswitch03/tableswitch0301/tableswitch0301p
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


   sipush 555
   sipush 2222 ; push 2222
   tableswitch 2220
        lab1 ; if 2222==2220 jump to lab1, fail
        lab2 ; if 2222==2221 jump to lab2, fail
        lab3 ; if 2222==2222 jump to lab3, pass
        lab4 ; if 2222==2223 jump to lab4, fail
     default: def ; jump to def, fail

lab1:
   sipush 105
   ireturn
lab2:
   sipush 105
   ireturn
lab3:
   sipush 555
   if_icmpne Fail
   sipush 104
   ireturn
lab4:
   sipush 105
   ireturn
def:
   sipush 105
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

  new org/apache/harmony/vts/test/vm/jvms/instructions/branching/tableswitch/tableswitch03/tableswitch0301/tableswitch0301p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/branching/tableswitch/tableswitch03/tableswitch0301/tableswitch0301p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/branching/tableswitch/tableswitch03/tableswitch0301/tableswitch0301p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
