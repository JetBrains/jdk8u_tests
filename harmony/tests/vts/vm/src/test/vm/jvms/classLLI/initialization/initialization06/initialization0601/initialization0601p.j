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

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601p
.super java/lang/Object
.field static public testField I=105

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
   .limit locals 4

; create first thread
   new org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601pThread1
   dup
   invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601pThread1/<init>()V
   astore_2

; create second thread
   new org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601pThread2
   dup
   invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601pThread2/<init>()V
   astore_3

; start first thread
   aload_2
   invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601pThread1/start()V

; wait 3 seconds
   sipush 3000
   i2l
   invokestatic java/lang/Thread/sleep(J)V

; start second thread
   aload_3
   invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601pThread2/start()V

; do while second thread working
   aload_3
   invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601pThread2/join()V

   getstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601p/testField I
   ireturn

.end method

;
; main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization06/initialization0601/initialization0601p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
