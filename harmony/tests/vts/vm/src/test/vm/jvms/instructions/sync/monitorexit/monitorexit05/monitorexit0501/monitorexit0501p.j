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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/sync/monitorexit/monitorexit05/monitorexit0501/monitorexit0501p
.super java/lang/Thread

;
; initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Thread/<init>()V  
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 3

 first:
      ; must not throw any exception or error.
   aload_1 ; push local variable 1
   monitorenter ; enter monitor
   sipush 555
   aload_1
   monitorexit
   sipush 555
   if_icmpne Fail
   sipush 104
   ireturn

Fail:
   sipush 105
   ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 3
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/sync/monitorexit/monitorexit05/monitorexit0501/monitorexit0501p
  dup
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/sync/monitorexit/monitorexit05/monitorexit0501/monitorexit0501p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/sync/monitorexit/monitorexit05/monitorexit0501/monitorexit0501p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  
  return
.end method
