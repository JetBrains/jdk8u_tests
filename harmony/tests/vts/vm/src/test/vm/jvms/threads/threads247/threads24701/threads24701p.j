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

.source threads24701p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest

.field threadCounter I
.field reenabledCounter I

;
; constructor
;
.method public <init>()V
    .limit stack 2
    .limit locals 1

    ; default initializer
    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest/<init>()V

    ; threadCounter = 0
    aload_0
    iconst_0
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p/threadCounter I

    ; reenabledCounter = 0
    aload_0
    iconst_0
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p/reenabledCounter I

    return
.end method

;
; test method
; starts two threads, waits each of them to call wait, 
; notifies, checks that only one was awaken 
;
.method public synchronized testTimed(I)I
    .limit stack 4
    .limit locals 5

    .throws java/lang/InterruptedException

Init:
    ; testLock = new Object();
    new java/lang/Object
    dup
    invokespecial java/lang/Object/<init>()V
    astore_2

    ; t1 = new Waiter(this, testLock)
    new org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/Waiter
    dup
    aload_0
    aload_2
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/Waiter/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p;Ljava/lang/Object;)V
    astore_3

    ; t2 = new Waiter(this, testLock)
    new org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/Waiter
    dup
    aload_0
    aload_2
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/Waiter/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p;Ljava/lang/Object;)V
    astore 4
    
Start:
    ; start both Waiter threads
    aload_3
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/Waiter/start()V
    aload 4
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/Waiter/start()V

ThreadsNotReady:
    ; wait(delay) 
    aload_0
    iload_1
    i2l
    invokevirtual java/lang/Object/wait(J)V

    ; until threadCounter == 2
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p/threadCounter I
    iconst_2
    if_icmpne ThreadsNotReady

ThreadsReady:
    ; by the moment both Waiter threads are waiting
    ; call testLock.notify() to wake up one of them
    aload_2
    monitorenter
    aload_2
    invokevirtual java/lang/Object/notify()V
    aload_2
    monitorexit

WaitForResults:
    ; repeat wait(delay) 2 times
    ; will not pass yet once reenabledCounter == 1. Should rather wait more
    ; to catch possible erroneous wakeup of second thread
    iconst_2
    istore_2 ; cycle counter
WaitMore:
    ; wait(delay)
    aload_0
    iload_1
    i2l
    invokevirtual java/lang/Object/wait(J)V

    ; check counter, if stayed here < 2 times then WaitMore
    iload_2
    dup
    iconst_1
    isub
    istore_2
    ifne WaitMore
Check:
    ; if no or both Waiters were awaken (i.e. reenabledCounter != 1)
    ; save failed status (105), passed (104) otherwise
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p/reenabledCounter I
    iconst_1 
    if_icmpne SaveFailed
SavePassed: 
    bipush 104
    istore_2
    goto Cleanup
SaveFailed:
    bipush 105
    istore_2
Cleanup:
    ; interrupt threads first
    aload_3
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/Waiter/interrupt()V
    aload 4
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/Waiter/interrupt()V

    iload_2 ; status
    ireturn
.end method

;
; main method
;
.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads247/threads24701/threads24701p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
