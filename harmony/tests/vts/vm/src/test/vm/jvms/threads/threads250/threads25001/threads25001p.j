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

.source threads25001p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p
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
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p/threadCounter I

    ; reenabledCounter = 0
    aload_0
    iconst_0
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p/reenabledCounter I

    return
.end method

;
; test method
; starts two threads, waits each of them to call wait, 
; notifiesAll, checks that both were awaken 
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
    new org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/Waiter
    dup
    aload_0
    aload_2
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/Waiter/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p;Ljava/lang/Object;)V
    astore_3

    ; t2 = new Waiter(this, testLock)
    new org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/Waiter
    dup
    aload_0
    aload_2
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/Waiter/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p;Ljava/lang/Object;)V
    astore 4
    
Start:
    ; start both Waiter threads
    aload_3
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/Waiter/start()V
    aload 4
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/Waiter/start()V

ThreadsNotReady:
    ; wait(delay) 
    aload_0
    iload_1
    i2l
    invokevirtual java/lang/Object/wait(J)V

    ; until threadCounter == 2
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p/threadCounter I
    iconst_2
    if_icmpne ThreadsNotReady

ThreadsReady:
    ; by the moment both Waiter threads are waiting
    ; call testLock.notifyAll() to wake up them
    aload_2
    monitorenter
    aload_2
    invokevirtual java/lang/Object/notifyAll()V
    aload_2
    monitorexit

WaitForResults:
    ; repeat wait(delay) until reenabledCounter == 2 but no more than 2 times
    iconst_2
    istore_2 ; cycle counter
WaitMore:
    ; wait(delay)
    aload_0
    iload_1
    i2l
    invokevirtual java/lang/Object/wait(J)V

    ; if both Waiters were awaken (i.e. reenabledCounter == 2) jump to Passed
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p/reenabledCounter I
    iconst_2 
    if_icmpeq Passed

    ; check counter, if stayed here < 2 times then WaitMore, fail otherwise
    iload_2
    dup
    iconst_1
    isub
    istore_2
    ifne WaitMore
Failed:
    ; interrupt threads first
    aload_3
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/Waiter/interrupt()V
    aload 4
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/Waiter/interrupt()V
    
    ; return failed status
    bipush 105
    ireturn

Passed: 
    ; return passed status
    bipush 104
    ireturn
.end method

;
; main method
;
.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads250/threads25001/threads25001p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
