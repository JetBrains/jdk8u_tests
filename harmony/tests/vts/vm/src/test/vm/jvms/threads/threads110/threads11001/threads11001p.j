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
; Version: $Revision: 1.2 $
;

.source threads11001p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest

.field  hi J
.field  lo J
.field volatile v J

.method public <init>()V
    .limit stack 3
    .limit locals 1

	aload_0
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest/<init>()V

    ; hi = 0xFFFFFFFF00000000L;
	aload_0
    ldc2_w -4294967296
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/hi J

    ; lo = 0x00000000FFFFFFFFL;
	aload_0
	ldc2_w 4294967295
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/lo J

	; v = hi
    aload_0
	aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/hi J
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/v J
	return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 8

    ; boolean passed = true;
    iconst_1
	istore_2

    ; Thread t1 = new threads11001p$1();
    new org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p$1
	dup
	aload_0
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p$1/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p;)V
	astore_3

    ; Thread t2 = new threads11001p$2();
	new org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p$2
	dup
	aload_0
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p$2/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p;)V
	astore 4

    ; Interruptor killer = new Interruptor(this, delay);
	new org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor
	dup
	aload_0
	iload_1
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest;I)V
	astore 5

    ;    t1.start();
    ;    t2.start();
    ;    killer.start();
	aload_3
	invokevirtual java/lang/Thread/start()V
	aload 4
	invokevirtual java/lang/Thread/start()V
	aload 5
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/start()V

LoopStart:
    ;     while (!interrupted()) {
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/interrupted()Z
	ifne Cleanup

    ;         Thread.yield(); // improves failure rate for wrong implementations
    invokestatic java/lang/Thread/yield()V

    ;         long v1 = v; // important: invariable copy to use twice during the check
	aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/v J
	lstore 6

    ;         if (v1 != hi && v1 != lo) {
	lload 6
	aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/hi J
	lcmp
	ifeq LoopStart
	lload 6
	aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/lo J
	lcmp
	ifeq LoopStart

    ;            passed = false;
    ;            break;
    ;        } // if
    ;    } // while
	iconst_0
	istore_2
Cleanup:
    ;    t1.interrupt();
    ;    t2.interrupt();
	aload_3
	invokevirtual java/lang/Thread/interrupt()V
	aload 4
	invokevirtual java/lang/Thread/interrupt()V

    ;    return passed ? 104 : 105;
	iload_2
	ifeq Failed
	bipush 104
	ireturn
Failed:
	bipush 105
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

	new org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads110/threads11001/threads11001p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method
