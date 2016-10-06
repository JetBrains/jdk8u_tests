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
; Author: Maxim N. Kurzenev
; Version: $Revision: 1.1 $
;

.source threads11801p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads118/threads11801/threads11801p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest

.field  a I

.method public <init>()V
    .limit stack 2
    .limit locals 1

	aload_0
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest/<init>()V
	aload_0
	iconst_0
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads118/threads11801/threads11801p/a I
	return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 5

    .catch java/lang/ArithmeticException from L1 to L2 using Catcher
    .throws java/lang/InterruptedException
    ;        int i = 0;
	iconst_0
	istore_2
    ;        int status = 104; // passed
	bipush 104
	istore_3
    ;        new Interruptor(this, delay).start();
	new org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor
	dup
	aload_0
	iload_1
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest;I)V
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/start()V
LoopStart:
    ;        while (!interrupted()) {
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads118/threads11801/threads11801p/interrupted()Z
	ifne End
L1:
    ;            try {
    ;                synchronized (this) {
    ;                    a = ++i;
    ;                } // unlock a lock to trigger store operation
	aload_0
	monitorenter
	aload_0
	iinc 2 1
	iload_2
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads118/threads11801/threads11801p/a I
	aload_0
	monitorexit
    ;                int z = 1 / 0;
    ;                return 111; // should not reach this line
	iconst_1
	iconst_0
	idiv

	bipush 111
	ireturn
L2:
Catcher:
    ;            } catch (ArithmeticException e) {
    ;                if (a != i) {
    ;                    // local copy should already be equal to i
    ;                    status = 105;
    ;                    break;
    ;                }
    ;            }
    ;        }
    ;        return status;
	astore 4
	aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads118/threads11801/threads11801p/a I
	iload_2
	if_icmpeq LoopStart
	bipush 105
	istore_3
End:
	iload_3
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

	new org/apache/harmony/vts/test/vm/jvms/threads/threads118/threads11801/threads11801p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads118/threads11801/threads11801p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads118/threads11801/threads11801p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method
