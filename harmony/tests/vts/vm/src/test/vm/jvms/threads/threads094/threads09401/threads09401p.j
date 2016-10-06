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

.source threads09401p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09401/threads09401p
.super java/lang/Object

.method public <init>()V
    .limit stack 1
    .limit locals 1

    aload_0
    invokespecial java/lang/Object/<init>()V
    return
.end method

.method public test([Ljava/lang/String;)I
    .limit stack 2
    .limit locals 2

    ;        synchronized (this) {
    ;            synchronized (this) {
    aload_0
    dup
    monitorenter
    monitorenter

    ;                if (!Thread.holdsLock(this)) {
    ;                    return 110;
    ;                }
    aload_0
    invokestatic java/lang/Thread/holdsLock(Ljava/lang/Object;)Z
    ifne FirstCheck_OK
    aload_0
    dup
    monitorexit
    monitorexit

    bipush 110
    ireturn
FirstCheck_OK:

    ;            } // close inner synchronized block
    aload_0
    monitorexit

    ;            if (!Thread.holdsLock(this)) {
    ;                return 111;
    ;            }
    aload_0
    invokestatic java/lang/Thread/holdsLock(Ljava/lang/Object;)Z
    ifne SecondCheck_OK

    aload_0
    monitorexit
    bipush 111
    ireturn
SecondCheck_OK:
    ;        } // close outer synchronized block
    aload_0
    monitorexit

    ;        return Thread.holdsLock(this) ? 105 : 104;
    aload_0
    invokestatic java/lang/Thread/holdsLock(Ljava/lang/Object;)Z
    ifeq Passed
    bipush 105
    ireturn
Passed:
    bipush 104
    ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    new org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09401/threads09401p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09401/threads09401p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads094/threads09401/threads09401p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
