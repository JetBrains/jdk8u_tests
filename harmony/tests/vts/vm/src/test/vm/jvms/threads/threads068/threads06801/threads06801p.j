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

.source threads06801p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads068/threads06801/threads06801p
.super java/lang/Object

.field  V I

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

    ;    V = 1234567;
    ;    synchronized (this) { // triggers flush of thread's working memory
    ;        return V == 1234567 ? 104 : 105;
    ;    }

    aload_0
    ldc 1234567
    putfield org/apache/harmony/vts/test/vm/jvms/threads/threads068/threads06801/threads06801p/V I
    aload_0
    monitorenter

    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/threads/threads068/threads06801/threads06801p/V I
    ldc 1234567
    if_icmpne Failed
    bipush 104
    goto Cleanup
Failed:
    bipush 105
Cleanup:
    aload_0
    monitorexit
    ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    new org/apache/harmony/vts/test/vm/jvms/threads/threads068/threads06801/threads06801p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads068/threads06801/threads06801p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads068/threads06801/threads06801p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
