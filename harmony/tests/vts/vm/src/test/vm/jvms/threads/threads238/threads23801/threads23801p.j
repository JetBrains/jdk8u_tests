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

.source threads23801p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads238/threads23801/threads23801p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest
.implements org/apache/harmony/vts/test/vm/jvms/threads/share/FlaggedTest

.field  flag Z

.method public <init>()V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest/<init>()V
    aload_0
    iconst_0
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads238/threads23801/threads23801p/flag Z
    return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 9

    .throws java/lang/InterruptedException

    ; Object lock1 = new Object();
    new java/lang/Object
    dup
    invokespecial java/lang/Object/<init>()V
    astore_2

    ; Object lock2 = new Object();
    new java/lang/Object
    dup
    invokespecial java/lang/Object/<init>()V
    astore_3

    ; enter monitors of both locks
    aload_2
    monitorenter
    aload_3
    monitorenter

    ; Waiter waiter = new Waiter((FlaggedTest) this, lock2);
    new org/apache/harmony/vts/test/vm/jvms/threads/share/Waiter
    dup
    aload_0
    aload_3
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Waiter/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/FlaggedTest;Ljava/lang/Object;)V

    ; waiter.start();
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/Waiter/start()V

    ; lock1.wait(delay);
    aload_2
    iload_1
    i2l
    ; lock1.wait() should not relinquish the lock to lock2 
    ; thus not letting waiter to set flag to true
    invokevirtual java/lang/Object/wait(J)V
    
    ; return flag ? 105 : 104;
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads238/threads23801/threads23801p/flag Z
    ifeq Passed
    bipush 105
    goto Cleanup
Passed:
    bipush 104
Cleanup:
    aload_3
    monitorexit
    aload_2
    monitorexit
    ireturn
.end method

.method public setFlag(Z)V
    .limit stack 2
    .limit locals 2

    aload_0
    iload_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads238/threads23801/threads23801p/flag Z
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads238/threads23801/threads23801p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads238/threads23801/threads23801p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads238/threads23801/threads23801p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
