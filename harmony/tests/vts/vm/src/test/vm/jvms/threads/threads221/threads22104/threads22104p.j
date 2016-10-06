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

.source threads22104p.j
.class public org/apache/harmony/vts/test/vm/jvms/threads/threads221/threads22104/threads22104p
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

    .catch java/lang/ArithmeticException from L0 to L1 using Catcher

L0:
    invokestatic org/apache/harmony/vts/test/vm/jvms/threads/threads221/threads22104/threads22104p/test1()V
L1:
    ; should not reach this line, return error status
    bipush 106
    ireturn
Catcher:
    ; return Thread.holdsLock(this.getClass()) ? 105 : 104;
    aload_0
    invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
    invokestatic java/lang/Thread/holdsLock(Ljava/lang/Object;)Z
    ifeq Passed
    bipush 105
    ireturn
Passed:
    bipush 104
    ireturn
.end method

.method public static synchronized test1()V
    .limit stack 2
    .limit locals 0

    ; force abrupt termination with division by zero
    iconst_1
    iconst_0
    idiv
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 1

    new org/apache/harmony/vts/test/vm/jvms/threads/threads221/threads22104/threads22104p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/threads/threads221/threads22104/threads22104p/<init>()V
    aload_0
    invokevirtual org/apache/harmony/vts/test/vm/jvms/threads/threads221/threads22104/threads22104p/test([Ljava/lang/String;)I
    invokestatic java/lang/System/exit(I)V
    return
.end method
