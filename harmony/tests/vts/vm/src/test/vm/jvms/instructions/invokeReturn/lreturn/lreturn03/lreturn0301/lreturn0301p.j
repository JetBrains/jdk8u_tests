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
; Author: Maxim V. Makarov
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/lreturn/lreturn03/lreturn0301/lreturn0301p
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 

; 
; this synchronized method contains monitorexit instruction but no monitorenter
.method public synchronized testMethod()J
   .limit stack 4
   .limit locals 4

    aload_0		; load this from local var
    monitorexit ; exit monitor this object

	lconst_1 ; push int 1

	lreturn     ; must throw java/lang/IllegalMonitorStateException

.end method


;
; test method
.method public test([Ljava/lang/String;)I
    .limit locals 4
    .limit stack 3

   .catch java/lang/IllegalMonitorStateException from L1 to L2 using Handler

L1:
    ; invocation of synchronized testMethod
    ;
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/lreturn/lreturn03/lreturn0301/lreturn0301p/testMethod()J
	pop2
    ; test failed
	sipush 105
	ireturn
L2:
Handler:
	; test passed
	sipush 104
	ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/lreturn/lreturn03/lreturn0301/lreturn0301p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/lreturn/lreturn03/lreturn0301/lreturn0301p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/lreturn/lreturn03/lreturn0301/lreturn0301p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
