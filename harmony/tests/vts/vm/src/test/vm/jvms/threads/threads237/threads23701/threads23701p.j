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

.source threads23701p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads237/threads23701/threads23701p
.super org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest
.implements org/apache/harmony/vts/test/vm/jvms/threads/share/FlaggedTest

.field  flag Z

.method public <init>()V
    .limit stack 2
    .limit locals 1

    aload_0
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/timedTest/<init>()V
    aload_0
    iconst_0
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads237/threads23701/threads23701p/flag Z
    return
.end method

.method public testTimed(I)I
    .limit stack 4
    .limit locals 4

    .throws java/lang/InterruptedException

    ; Object lock = new Object();
    new java/lang/Object
    dup
    invokespecial java/lang/Object/<init>()V
    astore_2
        
    ; synchronized (lock) {
    ;     synchronized (lock) {
    aload_2
    monitorenter
    aload_2
    monitorenter

    ; (new Waiter(this, lock)).start();
    new org/apache/harmony/vts/test/vm/jvms/threads/share/Waiter
    dup
    aload_0
    aload_2
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/share/Waiter/<init>(Lorg/apache/harmony/vts/test/vm/jvms/threads/share/FlaggedTest;Ljava/lang/Object;)V
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/share/Waiter/start()V

    ; lock.wait(delay);
    aload_2
    iload_1 ; delay parameter
    i2l
    invokevirtual java/lang/Object/wait(J)V

    ; }} //close synchronized blocks
    aload_2
    monitorexit
    aload_2
    monitorexit

    ; return flag ? 104 : 105;
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads237/threads23701/threads23701p/flag Z
    ifeq Failed
    bipush 104
    ireturn
Failed:
    bipush 105
    ireturn
.end method

.method public setFlag(Z)V
    .limit stack 2
    .limit locals 2

    ; this.flag = flag;
    aload_0
    iload_1
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads237/threads23701/threads23701p/flag Z
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    .throws java/lang/InterruptedException

    new org/apache/harmony/vts/test/vm/jvms/threads/threads237/threads23701/threads23701p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads237/threads23701/threads23701p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads237/threads23701/threads23701p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
