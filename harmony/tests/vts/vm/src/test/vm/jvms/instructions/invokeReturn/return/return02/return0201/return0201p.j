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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201p
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
   .limit stack 4
   .limit locals 6
   
; create locker array int[1]. array[0] = 105
   iconst_1
   newarray int
   astore_2
   aload_2
   iconst_0
   sipush 105
   iastore

; create first thread
   new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201pFirst
   dup
   aload_2
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201pFirst/<init>([I)V
   astore_3

; start first thread
   aload_3
   invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201pFirst/start()V

; do while first thread working
   aload_3
   invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201pFirst/join()V

; create checker array
   iconst_1
   newarray int
   astore 4
   aload 4
   iconst_0
   iconst_0
   iastore

; create second thread
   new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201pSecond
   dup
   aload_2
   aload 4
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201pSecond/<init>([I[I)V
   astore 5

; start second thread
   aload 5
   invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201pSecond/start()V

; hold this thread while checker[0] == 0
label:
   aload 4
   iconst_0
   iaload
   ifeq label

; time to work to second thread
   sipush 10000
   i2l
   invokestatic java/lang/Thread/sleep(J)V

; stop second thread
   aload 5
   invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201pSecond/stop()V

; returning result
   aload_2
   iconst_0
   iaload
   ireturn
 
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/return/return02/return0201/return0201p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
