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

.source threads08001p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads080/threads08001/threads08001p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest
.implements org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty

.field  a I

.method public <init>()V
    .limit stack 1
    .limit locals 1

	aload_0
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest/<init>()V
	return
.end method

.method public anny()V
    .limit stack 2
    .limit locals 5

    ;        synchronized (this) {
    ;            synchronized (this) {
    ;                a = 1;
    ;            } // force store operation by unlocking a lock
    ;            a = 2;
    ;        } // force store operation by unlocking a lock

	aload_0
    dup
	monitorenter
	monitorenter

	aload_0
	iconst_1
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads080/threads08001/threads08001p/a I

	aload_0
	monitorexit

	aload_0
	iconst_2
	putfield org/apache/harmony/vts/test/vm/jvms/threads/threads080/threads08001/threads08001p/a I

	aload_0
	monitorexit
	return
.end method

; stub method to conform implemented interface
.method public betty()V
    .limit stack 0
    .limit locals 1

    return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 4

    ;        boolean passed = true;
	iconst_1
	istore_2

    ;        AnnyCaller t1 = new AnnyCaller(this);
	new org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller
	dup
	aload_0
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty;)V
	astore_3

    ;        new Interruptor(this, delay).start();
	new org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor
	dup
	aload_0
	iload_1
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest;I)V
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/start()V

    ;        t1.start();
	aload_3
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/start()V

LoopStart:
    ;        while (!interrupted()) {
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads080/threads08001/threads08001p/interrupted()Z
	ifne Cleanup
InnerLoopStart:
    ;            while (!t1.isReady()) {
    ;                Thread.yield();
    ;            }
	aload_3
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/isReady()Z
	ifne Check
	invokestatic java/lang/Thread/yield()V
	goto InnerLoopStart
Check:
    ;            synchronized (this) {
	aload_0
	monitorenter
    ;                if (a != 2) {
    ;                     passed = false;
    ;                     break;
    ;                }
	aload_0
	getfield org/apache/harmony/vts/test/vm/jvms/threads/threads080/threads08001/threads08001p/a I
	iconst_2
	if_icmpeq Check_OK
	iconst_0
	istore_2
	aload_0
	monitorexit
	goto Cleanup
Check_OK:
    ;                t1.setReady(false);
    ;                notifyAll();
	aload_3
	iconst_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/setReady(Z)V
	aload_0
	invokevirtual java/lang/Object/notifyAll()V
    ;            }
    ;        }
	aload_0
	monitorexit
	goto LoopStart
Cleanup:
    ;        t1.interrupt();
    ;        return passed ? 104 : 105;
	aload_3
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/interrupt()V
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

	new org/apache/harmony/vts/test/vm/jvms/threads/threads080/threads08001/threads08001p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads080/threads08001/threads08001p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads080/threads08001/threads08001p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method
