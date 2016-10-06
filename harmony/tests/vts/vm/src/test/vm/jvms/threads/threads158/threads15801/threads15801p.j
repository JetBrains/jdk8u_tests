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

.source threads15801p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest
.implements org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty

.field  a I
.field  b I

.method public <init>()V
    .limit stack 2
    .limit locals 1

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest/<init>()V
    ; a = 1;
    aload_0
    iconst_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/a I
    ; b = 2;
    aload_0
    iconst_2
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/b I
    return
.end method

.method public synchronized reset()V
    .limit stack 2
    .limit locals 1

    ; a = 1;
    aload_0
    iconst_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/a I
    ; b = 2;
    aload_0
    iconst_2
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/b I
    return
.end method

.method public anny()V
    .limit stack 2
    .limit locals 1

    ; a = b;
    aload_0
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/b I
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/a I
    return
.end method

.method public betty()V
    .limit stack 2
    .limit locals 1

    ; b = a;
    aload_0
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/a I
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/b I
    return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 6

    ; boolean passed = true;
    iconst_1
    istore_2

    ; AnnyCaller t1 = new AnnyCaller(this);
    new org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller
    dup
    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty;)V
    astore_3

    ; BettyCaller t2 = new BettyCaller(this);
    new org/apache/harmony/vts/test/vm/jvms/threads/share/BettyCaller
    dup
    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/BettyCaller/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/AnnyBetty;)V
    astore 4

    ; Interruptor killer = new Interruptor(this, delay);
    new org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor
    dup
    aload_0
    iload_1
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/InterruptibleTest;I)V
    astore 5

    ; t1.start();
    ; t2.start();
    ; killer.start();
    aload_3
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/start()V
    aload 4
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/BettyCaller/start()V
    aload 5
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/Interruptor/start()V

LoopStart:
    ; while (!interrupted()) {
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/interrupted()Z
    ifne Cleanup

WaitForReadyLoopStart:
    ;     while (!t1.isReady() || !t2.isReady()) {
    ;         Thread.yield();
    ;     }
    aload_3
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/isReady()Z
    ifeq ThreadsNotReady
    aload 4
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/BettyCaller/isReady()Z
    ifne ThreadsReady
ThreadsNotReady:
    invokestatic java/lang/Thread/yield()V
    goto WaitForReadyLoopStart
ThreadsReady:
    ;     synchronized (this) {
    aload_0
    monitorenter

    ;          if (a != 1 && a != 2)
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/a I
    iconst_1
    if_icmpeq SecondCheck
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/a I
    iconst_2
    if_icmpne SetFailed
SecondCheck:
    ;          if (b != 1 && b != 2)
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/b I
    iconst_1
    if_icmpeq ThirdCheck
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/b I
    iconst_2
    if_icmpne SetFailed
ThirdCheck:
    ;          || (a == 1 && b == 2)) {
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/a I
    iconst_1
    if_icmpne Continue
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/b I
    iconst_2
    if_icmpne Continue

SetFailed:
    ;              passed = false;
    ;              break;
    ;         }
    iconst_0
    istore_2
    aload_0
    monitorexit
    goto Cleanup
Continue:
    ;         reset();
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/reset()V

    ;         t1.setReady(false);
    aload_3
    iconst_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/setReady(Z)V

    ;         t2.setReady(false);
    aload 4
    iconst_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/BettyCaller/setReady(Z)V

    ;         notifyAll();
    ;     }
    ; }
    aload_0
    invokevirtual java/lang/Object/notifyAll()V
    aload_0
    monitorexit
    goto LoopStart
Cleanup:
    ; t1.interrupt();
    aload_3
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/AnnyCaller/interrupt()V

    ; t2.interrupt();
    aload 4
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/BettyCaller/interrupt()V

    ; return passed ? 104 : 105;
    iload_2
    ifeq Fail
    bipush 104
    ireturn
Fail:
    bipush 105
    ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads158/threads15801/threads15801p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
